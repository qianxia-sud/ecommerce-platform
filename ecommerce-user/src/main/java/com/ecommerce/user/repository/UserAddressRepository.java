package com.ecommerce.user.repository;

import com.ecommerce.common.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户地址数据访问接口
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    
    /**
     * 查找用户的所有地址
     */
    List<UserAddress> findByUserIdOrderByIsDefaultDesc(Long userId);
    
    /**
     * 查找用户的默认地址
     */
    Optional<UserAddress> findByUserIdAndIsDefault(Long userId, Integer isDefault);
    
    /**
     * 重置用户的所有地址为非默认
     */
    @Modifying
    @Query("UPDATE UserAddress ua SET ua.isDefault = 0 WHERE ua.userId = ?1")
    void resetDefaultAddress(Long userId);
}

