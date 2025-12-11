package com.ecommerce.common.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    
    PENDING_PAYMENT("待支付"),
    PAID("已支付"),
    SHIPPED("已发货"),
    RECEIVED("已收货"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    REFUNDING("退款中"),
    REFUNDED("已退款");
    
    private final String description;
    
    /**
     * 检查是否可以取消订单
     */
    public boolean canCancel() {
        return this == PENDING_PAYMENT;
    }
    
    /**
     * 检查是否可以支付
     */
    public boolean canPay() {
        return this == PENDING_PAYMENT;
    }
    
    /**
     * 检查是否可以发货
     */
    public boolean canShip() {
        return this == PAID;
    }
    
    /**
     * 检查是否可以确认收货
     */
    public boolean canReceive() {
        return this == SHIPPED;
    }
    
    /**
     * 检查是否可以申请退款
     */
    public boolean canRefund() {
        return this == PAID || this == SHIPPED || this == RECEIVED;
    }
}


