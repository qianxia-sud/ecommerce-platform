package com.ecommerce.common.result;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "没有权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    
    // 服务端错误 5xx
    ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),
    
    // 业务错误 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    TOKEN_INVALID(1004, "Token无效或已过期"),
    TOKEN_EXPIRED(1005, "Token已过期"),
    
    // 商品错误 2xxx
    PRODUCT_NOT_FOUND(2001, "商品不存在"),
    PRODUCT_OFF_SHELF(2002, "商品已下架"),
    CATEGORY_NOT_FOUND(2003, "分类不存在"),
    
    // 库存错误 3xxx
    INVENTORY_NOT_ENOUGH(3001, "库存不足"),
    INVENTORY_LOCK_FAILED(3002, "库存锁定失败"),
    INVENTORY_NOT_FOUND(3003, "库存记录不存在"),
    
    // 订单错误 4xxx
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态异常"),
    ORDER_CREATE_FAILED(4003, "订单创建失败"),
    ORDER_CANCEL_FAILED(4004, "订单取消失败"),
    
    // 支付错误 5xxx
    PAYMENT_FAILED(5001, "支付失败"),
    PAYMENT_NOT_FOUND(5002, "支付记录不存在"),
    REFUND_FAILED(5003, "退款失败");
    
    /** 状态码 */
    private final Integer code;
    
    /** 消息 */
    private final String message;
}


