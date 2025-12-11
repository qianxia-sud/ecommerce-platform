package com.ecommerce.common.entity;

import com.ecommerce.common.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_order")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 订单号 */
    @Column(nullable = false, unique = true, length = 32)
    private String orderNo;
    
    /** 用户ID */
    @Column(nullable = false)
    private Long userId;
    
    /** 订单总金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    /** 实付金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payAmount;
    
    /** 运费 */
    @Column(precision = 10, scale = 2)
    private BigDecimal freight = BigDecimal.ZERO;
    
    /** 订单状态 */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;
    
    /** 收货人姓名 */
    @Column(nullable = false, length = 50)
    private String receiverName;
    
    /** 收货人电话 */
    @Column(nullable = false, length = 20)
    private String receiverPhone;
    
    /** 收货地址 */
    @Column(nullable = false, length = 500)
    private String receiverAddress;
    
    /** 订单备注 */
    @Column(length = 500)
    private String remark;
    
    /** 支付时间 */
    private LocalDateTime payTime;
    
    /** 发货时间 */
    private LocalDateTime shipTime;
    
    /** 收货时间 */
    private LocalDateTime receiveTime;
    
    /** 完成时间 */
    private LocalDateTime completeTime;
    
    /** 取消时间 */
    private LocalDateTime cancelTime;
    
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


