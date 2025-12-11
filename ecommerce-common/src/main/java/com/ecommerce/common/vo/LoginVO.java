package com.ecommerce.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

/**
 * 登录返回VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 访问令牌 */
    private String token;
    
    /** 过期时间（秒） */
    private Long expiresIn;
    
    /** 用户信息 */
    private UserVO user;
}


