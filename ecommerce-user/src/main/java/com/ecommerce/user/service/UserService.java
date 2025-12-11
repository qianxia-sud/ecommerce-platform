package com.ecommerce.user.service;

import com.ecommerce.common.dto.LoginDTO;
import com.ecommerce.common.dto.UserDTO;
import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserAddress;
import com.ecommerce.common.vo.LoginVO;
import com.ecommerce.common.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserVO register(UserDTO userDTO);
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 根据ID获取用户信息
     */
    UserVO getUserById(Long id);
    
    /**
     * 根据用户名获取用户信息
     */
    UserVO getUserByUsername(String username);
    
    /**
     * 更新用户信息
     */
    UserVO updateUser(Long id, UserDTO userDTO);
    
    /**
     * 获取所有用户列表
     */
    List<UserVO> getAllUsers();
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 添加用户地址
     */
    UserAddress addAddress(Long userId, UserAddress address);
    
    /**
     * 获取用户的所有地址
     */
    List<UserAddress> getUserAddresses(Long userId);
    
    /**
     * 设置默认地址
     */
    void setDefaultAddress(Long userId, Long addressId);
    
    /**
     * 删除地址
     */
    void deleteAddress(Long userId, Long addressId);
    
    /**
     * 根据ID获取地址
     */
    UserAddress getAddressById(Long addressId);
    
    /**
     * 获取用户总数
     */
    Long getUserCount();
}

