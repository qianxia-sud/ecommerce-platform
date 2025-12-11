package com.ecommerce.inventory.repository;

import com.ecommerce.common.entity.InventoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存日志数据访问接口
 */
@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    
    /**
     * 根据商品ID查找库存日志
     */
    List<InventoryLog> findByProductIdOrderByCreateTimeDesc(Long productId);
    
    /**
     * 根据订单ID查找库存日志
     */
    List<InventoryLog> findByOrderId(Long orderId);
    
    /**
     * 分页查询库存日志
     */
    Page<InventoryLog> findByProductId(Long productId, Pageable pageable);
}

