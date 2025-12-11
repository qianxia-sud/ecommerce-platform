package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存操作日志实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_inventory_log")
public class InventoryLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 商品ID */
    @Column(nullable = false)
    private Long productId;
    
    /** 订单ID（如果有） */
    private Long orderId;
    
    /** 操作类型：LOCK-锁定, DEDUCT-扣减, RELEASE-释放, ADD-增加 */
    @Column(nullable = false, length = 20)
    private String operationType;
    
    /** 变动数量 */
    @Column(nullable = false)
    private Integer quantity;
    
    /** 操作前库存 */
    @Column(nullable = false)
    private Integer beforeStock;
    
    /** 操作后库存 */
    @Column(nullable = false)
    private Integer afterStock;
    
    /** 备注 */
    @Column(length = 255)
    private String remark;
    
    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}


