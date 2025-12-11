package com.ecommerce.user.controller;

import com.ecommerce.common.dto.LoginDTO;
import com.ecommerce.common.dto.UserDTO;
import com.ecommerce.common.entity.UserAddress;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.vo.LoginVO;
import com.ecommerce.common.vo.UserVO;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody @Validated UserDTO userDTO) {
        return Result.success(userService.register(userDTO));
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Validated LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
    
    /**
     * 获取所有用户
     */
    @GetMapping("/list")
    public Result<List<UserVO>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<UserVO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return Result.success(userService.updateUser(id, userDTO));
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    /**
     * 添加用户地址
     */
    @PostMapping("/{userId}/address")
    public Result<UserAddress> addAddress(@PathVariable Long userId, @RequestBody UserAddress address) {
        return Result.success(userService.addAddress(userId, address));
    }
    
    /**
     * 获取用户地址列表
     */
    @GetMapping("/{userId}/address")
    public Result<List<UserAddress>> getUserAddresses(@PathVariable Long userId) {
        return Result.success(userService.getUserAddresses(userId));
    }
    
    /**
     * 设置默认地址
     */
    @PutMapping("/{userId}/address/{addressId}/default")
    public Result<Void> setDefaultAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        userService.setDefaultAddress(userId, addressId);
        return Result.success();
    }
    
    /**
     * 删除地址
     */
    @DeleteMapping("/{userId}/address/{addressId}")
    public Result<Void> deleteAddress(@PathVariable Long userId, @PathVariable Long addressId) {
        userService.deleteAddress(userId, addressId);
        return Result.success();
    }
    
    /**
     * 根据ID获取地址（内部调用）
     */
    @GetMapping("/address/{addressId}")
    public Result<UserAddress> getAddressById(@PathVariable Long addressId) {
        return Result.success(userService.getAddressById(addressId));
    }
    
    /**
     * 获取用户总数
     */
    @GetMapping("/count")
    public Result<Long> getUserCount() {
        return Result.success(userService.getUserCount());
    }
}

