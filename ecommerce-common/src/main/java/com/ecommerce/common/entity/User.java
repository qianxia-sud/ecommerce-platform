package com.ecommerce.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户名 */
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /** 密码 */
    @Column(nullable = false, length = 100)
    private String password;
    
    /** 昵称 */
    @Column(length = 50)
    private String nickname;
    
    /** 邮箱 */
    @Column(length = 100)
    private String email;
    
    /** 手机号 */
    @Column(length = 20)
    private String phone;
    
    /** 头像URL */
    @Column(length = 255)
    private String avatar;
    
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


