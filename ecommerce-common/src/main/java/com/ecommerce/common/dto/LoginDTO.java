package com.ecommerce.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
}


