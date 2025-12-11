package com.ecommerce.payment.service.impl;

import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.PaymentChannel;
import com.ecommerce.payment.service.PaymentChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 模拟支付渠道服务实现
 * 用于测试，实际项目中可替换为微信/支付宝实现
 */
@Slf4j
@Service
public class MockPaymentChannelService implements PaymentChannelService {
    
    @Override
    public PaymentChannel getChannel() {
        return PaymentChannel.MOCK;
    }
    
    @Override
    public Map<String, Object> createPayRequest(Payment payment) {
        log.info("创建模拟支付请求: paymentNo={}, amount={}", 
                payment.getPaymentNo(), payment.getAmount());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("paymentNo", payment.getPaymentNo());
        result.put("payUrl", "http://mock-payment/pay?no=" + payment.getPaymentNo());
        result.put("qrCode", "mock://pay/" + payment.getPaymentNo());
        result.put("message", "模拟支付，请调用mockPay接口完成支付");
        
        return result;
    }
    
    @Override
    public Map<String, Object> queryPayResult(Payment payment) {
        log.info("查询模拟支付结果: paymentNo={}", payment.getPaymentNo());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("paymentNo", payment.getPaymentNo());
        result.put("status", payment.getStatus().name());
        result.put("tradeNo", payment.getTradeNo());
        
        return result;
    }
    
    @Override
    public boolean verifyCallback(Map<String, String> params) {
        // 模拟验签，总是返回true
        log.info("模拟验证回调签名: params={}", params);
        return true;
    }
    
    @Override
    public Map<String, Object> parseCallback(Map<String, String> params) {
        log.info("解析模拟回调参数: params={}", params);
        
        Map<String, Object> result = new HashMap<>();
        result.put("paymentNo", params.get("paymentNo"));
        result.put("tradeNo", params.getOrDefault("tradeNo", "MOCK_" + UUID.randomUUID().toString().replace("-", "")));
        result.put("success", "SUCCESS".equals(params.getOrDefault("status", "SUCCESS")));
        
        return result;
    }
    
    @Override
    public Map<String, Object> refund(Payment payment, BigDecimal amount, String reason) {
        log.info("模拟退款: paymentNo={}, amount={}, reason={}", 
                payment.getPaymentNo(), amount, reason);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("paymentNo", payment.getPaymentNo());
        result.put("refundNo", "REF_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        result.put("refundAmount", amount);
        result.put("message", "模拟退款成功");
        
        return result;
    }
}

