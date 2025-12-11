package com.ecommerce.common.entity;

import com.ecommerce.common.enums.PaymentChannel;
import com.ecommerce.common.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_payment")
public class Payment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 支付流水号 */
    @Column(nullable = false, unique = true, length = 32)
    private String paymentNo;
    
    /** 订单ID */
    @Column(nullable = false)
    private Long orderId;
    
    /** 订单号 */
    @Column(nullable = false, length = 32)
    private String orderNo;
    
    /** 用户ID */
    @Column(nullable = false)
    private Long userId;
    
    /** 支付金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    /** 支付渠道 */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentChannel channel;
    
    /** 支付状态 */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;
    
    /** 第三方支付交易号 */
    @Column(length = 64)
    private String tradeNo;
    
    /** 支付时间 */
    private LocalDateTime payTime;
    
    /** 退款金额 */
    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;
    
    /** 退款时间 */
    private LocalDateTime refundTime;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private LocalDateTime updateTime;
    
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}


