package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_inventory")
public class Inventory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 商品ID */
    @Column(nullable = false, unique = true)
    private Long productId;
    
    /** 总库存 */
    @Column(nullable = false)
    private Integer totalStock = 0;
    
    /** 可用库存 */
    @Column(nullable = false)
    private Integer availableStock = 0;
    
    /** 锁定库存（已下单未支付） */
    @Column(nullable = false)
    private Integer lockedStock = 0;
    
    /** 预警库存阈值 */
    @Column(nullable = false)
    private Integer warningThreshold = 10;
    
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
    
    /**
     * 检查是否需要预警
     */
    public boolean needWarning() {
        return this.availableStock <= this.warningThreshold;
    }
}



