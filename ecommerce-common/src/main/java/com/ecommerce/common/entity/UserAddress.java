package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户收货地址实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_user_address")
public class UserAddress implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(nullable = false)
    private Long userId;
    
    /** 收货人姓名 */
    @Column(nullable = false, length = 50)
    private String receiverName;
    
    /** 收货人电话 */
    @Column(nullable = false, length = 20)
    private String receiverPhone;
    
    /** 省份 */
    @Column(nullable = false, length = 50)
    private String province;
    
    /** 城市 */
    @Column(nullable = false, length = 50)
    private String city;
    
    /** 区/县 */
    @Column(nullable = false, length = 50)
    private String district;
    
    /** 详细地址 */
    @Column(nullable = false, length = 255)
    private String detailAddress;
    
    /** 是否默认地址：0-否，1-是 */
    @Column(nullable = false)
    private Integer isDefault = 0;
    
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


