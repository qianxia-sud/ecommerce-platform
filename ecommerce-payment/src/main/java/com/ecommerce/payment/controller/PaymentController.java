package com.ecommerce.payment.controller;

import com.ecommerce.common.dto.PaymentDTO;
import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.PaymentChannel;
import com.ecommerce.common.result.Result;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 支付控制器
 */
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    /**
     * 创建支付
     */
    @PostMapping("/create")
    public Result<Payment> createPayment(@RequestBody @Validated PaymentDTO paymentDTO) {
        return Result.success(paymentService.createPayment(paymentDTO));
    }
    
    /**
     * 模拟支付（测试用）
     */
    @PostMapping("/{id}/mock-pay")
    public Result<Payment> mockPay(@PathVariable Long id) {
        return Result.success(paymentService.mockPay(id));
    }
    
    /**
     * 查询支付状态
     */
    @GetMapping("/{id}")
    public Result<Payment> queryPayment(@PathVariable Long id) {
        return Result.success(paymentService.queryPayment(id));
    }
    
    /**
     * 根据订单ID查询支付
     */
    @GetMapping("/order/{orderId}")
    public Result<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        return Result.success(paymentService.getPaymentByOrderId(orderId));
    }
    
    /**
     * 获取用户支付记录
     */
    @GetMapping("/user/{userId}")
    public Result<List<Payment>> getUserPayments(@PathVariable Long userId) {
        return Result.success(paymentService.getUserPayments(userId));
    }
    
    /**
     * 申请退款
     */
    @PostMapping("/{id}/refund")
    public Result<Payment> refund(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false, defaultValue = "用户申请退款") String reason) {
        return Result.success(paymentService.refund(id, amount, reason));
    }
    
    /**
     * 关闭支付
     */
    @PostMapping("/{id}/close")
    public Result<Void> closePayment(@PathVariable Long id) {
        paymentService.closePayment(id);
        return Result.success();
    }
    
    /**
     * 微信支付回调（预留）
     */
    @PostMapping("/callback/wechat")
    public String wechatCallback(@RequestBody Map<String, String> params) {
        try {
            paymentService.handleCallback(PaymentChannel.WECHAT, params);
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
        } catch (Exception e) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
    }
    
    /**
     * 支付宝支付回调（预留）
     */
    @PostMapping("/callback/alipay")
    public String alipayCallback(@RequestParam Map<String, String> params) {
        try {
            paymentService.handleCallback(PaymentChannel.ALIPAY, params);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }
}

