package com.ecommerce.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息VO（不包含敏感信息）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String username;
    
    private String nickname;
    
    private String email;
    
    private String phone;
    
    private String avatar;
    
    private Integer status;
    
    /** 角色：ADMIN-管理员，USER-普通用户 */
    private String role;
    
    private LocalDateTime createTime;
}


