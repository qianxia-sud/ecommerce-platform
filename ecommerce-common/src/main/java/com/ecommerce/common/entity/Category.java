package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_category")
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 分类名称 */
    @Column(nullable = false, length = 50)
    private String name;
    
    /** 父分类ID，0表示顶级分类 */
    @Column(nullable = false)
    private Long parentId = 0L;
    
    /** 分类层级：1-一级，2-二级，3-三级 */
    @Column(nullable = false)
    private Integer level = 1;
    
    /** 排序 */
    @Column(nullable = false)
    private Integer sort = 0;
    
    /** 分类图标 */
    @Column(length = 255)
    private String icon;
    
    /** 状态：0-禁用，1-正常 */
    @Column(nullable = false)
    private Integer status = 1;
    
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


