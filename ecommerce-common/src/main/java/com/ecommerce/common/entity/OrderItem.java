package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品明细实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_order_item")
public class OrderItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 订单ID */
    @Column(nullable = false)
    private Long orderId;
    
    /** 订单号 */
    @Column(nullable = false, length = 32)
    private String orderNo;
    
    /** 商品ID */
    @Column(nullable = false)
    private Long productId;
    
    /** 商品名称（快照） */
    @Column(nullable = false, length = 200)
    private String productName;
    
    /** 商品图片（快照） */
    @Column(length = 255)
    private String productImage;
    
    /** 商品单价（快照） */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /** 购买数量 */
    @Column(nullable = false)
    private Integer quantity;
    
    /** 小计金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        // 计算小计
        if (this.price != null && this.quantity != null) {
            this.subtotal = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}


