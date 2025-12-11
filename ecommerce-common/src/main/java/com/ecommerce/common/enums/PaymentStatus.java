package com.ecommerce.common.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 支付状态枚举
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {
    
    PENDING("待支付"),
    SUCCESS("支付成功"),
    FAILED("支付失败"),
    CLOSED("已关闭"),
    REFUNDING("退款中"),
    REFUNDED("已退款"),
    PARTIAL_REFUND("部分退款");
    
    private final String description;
}


