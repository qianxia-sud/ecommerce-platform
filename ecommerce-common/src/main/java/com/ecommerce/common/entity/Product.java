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
 * 商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_product")
public class Product implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 商品名称 */
    @Column(nullable = false, length = 200)
    private String name;
    
    /** 商品描述 */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /** 分类ID */
    @Column(nullable = false)
    private Long categoryId;
    
    /** 商品价格 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /** 市场价 */
    @Column(precision = 10, scale = 2)
    private BigDecimal marketPrice;
    
    /** 主图URL */
    @Column(length = 255)
    private String mainImage;
    
    /** 商品图片（多张，用逗号分隔） */
    @Column(columnDefinition = "TEXT")
    private String images;
    
    /** 商品状态：0-下架，1-上架 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 销量 */
    @Column(nullable = false)
    private Integer sales = 0;
    
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


