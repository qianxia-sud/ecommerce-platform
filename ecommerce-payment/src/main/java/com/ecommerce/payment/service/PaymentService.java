package com.ecommerce.payment.service;

import com.ecommerce.common.dto.PaymentDTO;
import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.PaymentChannel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 创建支付
     */
    Payment createPayment(PaymentDTO paymentDTO);
    
    /**
     * 模拟支付（测试用）
     */
    Payment mockPay(Long paymentId);
    
    /**
     * 处理支付回调
     */
    void handleCallback(PaymentChannel channel, Map<String, String> params);
    
    /**
     * 查询支付状态
     */
    Payment queryPayment(Long paymentId);
    
    /**
     * 根据订单ID查询支付
     */
    Payment getPaymentByOrderId(Long orderId);
    
    /**
     * 申请退款
     */
    Payment refund(Long paymentId, BigDecimal amount, String reason);
    
    /**
     * 获取用户支付记录
     */
    List<Payment> getUserPayments(Long userId);
    
    /**
     * 关闭支付
     */
    void closePayment(Long paymentId);
}

