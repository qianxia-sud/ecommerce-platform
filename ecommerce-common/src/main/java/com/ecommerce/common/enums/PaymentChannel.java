package com.ecommerce.common.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 支付渠道枚举
 */
@Getter
@AllArgsConstructor
public enum PaymentChannel {
    
    WECHAT("微信支付"),
    ALIPAY("支付宝"),
    BANK_CARD("银行卡"),
    BALANCE("余额支付"),
    MOCK("模拟支付");  // 用于测试
    
    private final String description;
}


