package com.ecommerce.payment.service;

import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.PaymentChannel;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付渠道服务接口
 * 预留接口，可扩展实现实际的微信/支付宝支付
 */
public interface PaymentChannelService {
    
    /**
     * 获取支付渠道
     */
    PaymentChannel getChannel();
    
    /**
     * 创建支付请求
     * @param payment 支付记录
     * @return 支付请求参数（如支付链接、二维码等）
     */
    Map<String, Object> createPayRequest(Payment payment);
    
    /**
     * 查询支付结果
     * @param payment 支付记录
     * @return 支付结果
     */
    Map<String, Object> queryPayResult(Payment payment);
    
    /**
     * 验证回调签名
     * @param params 回调参数
     * @return 是否验证通过
     */
    boolean verifyCallback(Map<String, String> params);
    
    /**
     * 解析回调结果
     * @param params 回调参数
     * @return 解析后的结果
     */
    Map<String, Object> parseCallback(Map<String, String> params);
    
    /**
     * 申请退款
     * @param payment 支付记录
     * @param amount 退款金额
     * @param reason 退款原因
     * @return 退款结果
     */
    Map<String, Object> refund(Payment payment, BigDecimal amount, String reason);
}

