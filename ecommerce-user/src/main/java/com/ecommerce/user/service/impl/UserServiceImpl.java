package com.ecommerce.user.service.impl;

import com.ecommerce.common.dto.LoginDTO;
import com.ecommerce.common.dto.UserDTO;
import com.ecommerce.common.entity.User;
import com.ecommerce.common.entity.UserAddress;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.util.JwtUtil;
import com.ecommerce.common.vo.LoginVO;
import com.ecommerce.common.vo.UserVO;
import com.ecommerce.user.repository.UserAddressRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final UserAddressRepository addressRepository;
    
    @Override
    @Transactional
    public UserVO register(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }
        
        // 创建用户
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(encryptPassword(userDTO.getPassword()))
                .nickname(userDTO.getNickname() != null ? userDTO.getNickname() : userDTO.getUsername())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .avatar(userDTO.getAvatar())
                .status(1)
                .role(userDTO.getRole() != null ? userDTO.getRole() : "USER")
                .build();
        
        user = userRepository.save(user);
        return convertToVO(user);
    }
    
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查找用户
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        
        // 验证密码
        if (!user.getPassword().equals(encryptPassword(loginDTO.getPassword()))) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 生成Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        return LoginVO.builder()
                .token(token)
                .expiresIn(JwtUtil.getExpirationSeconds())
                .user(convertToVO(user))
                .build();
    }
    
    @Override
    public UserVO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        return convertToVO(user);
    }
    
    @Override
    public UserVO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        return convertToVO(user);
    }
    
    @Override
    @Transactional
    public UserVO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        
        if (userDTO.getNickname() != null) {
            user.setNickname(userDTO.getNickname());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(encryptPassword(userDTO.getPassword()));
        }
        
        user = userRepository.save(user);
        return convertToVO(user);
    }
    
    @Override
    public List<UserVO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public UserAddress addAddress(Long userId, UserAddress address) {
        // 验证用户存在
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        address.setUserId(userId);
        
        // 如果是默认地址，先重置其他地址
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            addressRepository.resetDefaultAddress(userId);
        }
        
        return addressRepository.save(address);
    }
    
    @Override
    public List<UserAddress> getUserAddresses(Long userId) {
        return addressRepository.findByUserIdOrderByIsDefaultDesc(userId);
    }
    
    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("地址不存在"));
        
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此地址");
        }
        
        // 重置所有地址为非默认
        addressRepository.resetDefaultAddress(userId);
        
        // 设置当前地址为默认
        address.setIsDefault(1);
        addressRepository.save(address);
    }
    
    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("地址不存在"));
        
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此地址");
        }
        
        addressRepository.deleteById(addressId);
    }
    
    @Override
    public UserAddress getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("地址不存在"));
    }
    
    /**
     * 密码加密（MD5）
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    public Long getUserCount() {
        return userRepository.count();
    }
    
    /**
     * 转换为VO对象
     */
    private UserVO convertToVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .role(user.getRole())
                .createTime(user.getCreateTime())
                .build();
    }
}

