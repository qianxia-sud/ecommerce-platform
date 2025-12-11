package com.ecommerce.payment.service.impl;

import com.ecommerce.common.dto.PaymentDTO;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.OrderStatus;
import com.ecommerce.common.enums.PaymentChannel;
import com.ecommerce.common.enums.PaymentStatus;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.util.OrderNoGenerator;
import com.ecommerce.payment.feign.OrderFeignClient;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.service.PaymentChannelService;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 支付服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final List<PaymentChannelService> channelServices;
    
    /** 支付渠道服务映射 */
    private Map<PaymentChannel, PaymentChannelService> channelServiceMap;
    
    @PostConstruct
    public void init() {
        channelServiceMap = new HashMap<>();
        for (PaymentChannelService service : channelServices) {
            channelServiceMap.put(service.getChannel(), service);
        }
    }
    
    @Override
    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO) {
        // 1. 获取订单信息
        Result<Order> orderResult = orderFeignClient.getOrderEntityById(paymentDTO.getOrderId());
        if (!orderResult.isSuccess() || orderResult.getData() == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        Order order = orderResult.getData();
        
        // 2. 检查订单状态
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("订单状态不允许支付");
        }
        
        // 3. 检查是否已有支付记录
        Payment existingPayment = paymentRepository.findByOrderId(order.getId()).orElse(null);
        if (existingPayment != null) {
            if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                throw new BusinessException("订单已支付");
            }
            if (existingPayment.getStatus() == PaymentStatus.PENDING) {
                // 返回已有的待支付记录
                return existingPayment;
            }
        }
        
        // 4. 创建支付记录
        Payment payment = Payment.builder()
                .paymentNo(OrderNoGenerator.generatePaymentNo())
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .userId(order.getUserId())
                .amount(order.getPayAmount())
                .channel(paymentDTO.getChannel())
                .status(PaymentStatus.PENDING)
                .build();
        
        payment = paymentRepository.save(payment);
        
        log.info("支付记录创建成功: paymentNo={}, orderId={}, amount={}", 
                payment.getPaymentNo(), order.getId(), payment.getAmount());
        
        return payment;
    }
    
    @Override
    @Transactional
    public Payment mockPay(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ResultCode.PAYMENT_NOT_FOUND));
        
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("支付状态不正确");
        }
        
        // 模拟支付成功
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPayTime(LocalDateTime.now());
        payment.setTradeNo("MOCK_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        
        payment = paymentRepository.save(payment);
        
        // 通知订单服务更新订单状态
        orderFeignClient.payOrder(payment.getOrderId());
        
        log.info("模拟支付成功: paymentNo={}, tradeNo={}", payment.getPaymentNo(), payment.getTradeNo());
        
        return payment;
    }
    
    @Override
    @Transactional
    public void handleCallback(PaymentChannel channel, Map<String, String> params) {
        PaymentChannelService channelService = channelServiceMap.get(channel);
        if (channelService == null) {
            throw new BusinessException("不支持的支付渠道: " + channel);
        }
        
        // 1. 验证签名
        if (!channelService.verifyCallback(params)) {
            throw new BusinessException("回调签名验证失败");
        }
        
        // 2. 解析回调结果
        Map<String, Object> result = channelService.parseCallback(params);
        String paymentNo = (String) result.get("paymentNo");
        boolean success = (boolean) result.getOrDefault("success", false);
        String tradeNo = (String) result.get("tradeNo");
        
        // 3. 更新支付记录
        Payment payment = paymentRepository.findByPaymentNo(paymentNo)
                .orElseThrow(() -> new BusinessException("支付记录不存在"));
        
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.info("支付已成功，忽略重复回调: paymentNo={}", paymentNo);
            return;
        }
        
        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPayTime(LocalDateTime.now());
            payment.setTradeNo(tradeNo);
            paymentRepository.save(payment);
            
            // 通知订单服务
            orderFeignClient.payOrder(payment.getOrderId());
            
            log.info("支付回调处理成功: paymentNo={}, tradeNo={}", paymentNo, tradeNo);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            
            log.warn("支付失败: paymentNo={}", paymentNo);
        }
    }
    
    @Override
    public Payment queryPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ResultCode.PAYMENT_NOT_FOUND));
    }
    
    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ResultCode.PAYMENT_NOT_FOUND));
    }
    
    @Override
    @Transactional
    public Payment refund(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = queryPayment(paymentId);
        
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BusinessException("只有支付成功的订单才能退款");
        }
        
        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new BusinessException("退款金额不能超过支付金额");
        }
        
        // 调用支付渠道退款
        PaymentChannelService channelService = channelServiceMap.get(payment.getChannel());
        if (channelService != null) {
            Map<String, Object> result = channelService.refund(payment, amount, reason);
            if ((boolean) result.getOrDefault("success", false)) {
                payment.setStatus(PaymentStatus.REFUNDED);
                payment.setRefundAmount(amount);
                payment.setRefundTime(LocalDateTime.now());
                paymentRepository.save(payment);
                
                // 通知订单服务更新状态
                orderFeignClient.updateOrderStatus(payment.getOrderId(), OrderStatus.REFUNDED.name());
                
                log.info("退款成功: paymentNo={}, amount={}", payment.getPaymentNo(), amount);
            }
        }
        
        return payment;
    }
    
    @Override
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }
    
    @Override
    @Transactional
    public void closePayment(Long paymentId) {
        Payment payment = queryPayment(paymentId);
        
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("只有待支付的记录才能关闭");
        }
        
        payment.setStatus(PaymentStatus.CLOSED);
        paymentRepository.save(payment);
        
        log.info("支付关闭: paymentId={}", paymentId);
    }
}

