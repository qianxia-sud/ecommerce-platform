package com.ecommerce.inventory.repository;

import com.ecommerce.common.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * 库存数据访问接口
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    /**
     * 根据商品ID查找库存
     */
    Optional<Inventory> findByProductId(Long productId);
    
    /**
     * 根据商品ID查找库存（带悲观锁）
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productId = ?1")
    Optional<Inventory> findByProductIdForUpdate(Long productId);
    
    /**
     * 查找需要预警的库存
     */
    @Query("SELECT i FROM Inventory i WHERE i.availableStock <= i.warningThreshold")
    List<Inventory> findWarningInventories();
    
    /**
     * 锁定库存
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.availableStock = i.availableStock - ?2, i.lockedStock = i.lockedStock + ?2 WHERE i.productId = ?1 AND i.availableStock >= ?2")
    int lockStock(Long productId, Integer quantity);
    
    /**
     * 扣减库存（从锁定库存扣减）
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.lockedStock = i.lockedStock - ?2, i.totalStock = i.totalStock - ?2 WHERE i.productId = ?1 AND i.lockedStock >= ?2")
    int deductStock(Long productId, Integer quantity);
    
    /**
     * 释放库存（从锁定库存释放回可用库存）
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.availableStock = i.availableStock + ?2, i.lockedStock = i.lockedStock - ?2 WHERE i.productId = ?1 AND i.lockedStock >= ?2")
    int releaseStock(Long productId, Integer quantity);
    
    /**
     * 增加库存
     */
    @Modifying
    @Query("UPDATE Inventory i SET i.totalStock = i.totalStock + ?2, i.availableStock = i.availableStock + ?2 WHERE i.productId = ?1")
    int addStock(Long productId, Integer quantity);
}

