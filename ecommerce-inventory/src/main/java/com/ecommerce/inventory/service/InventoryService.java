package com.ecommerce.inventory.service;

import com.ecommerce.common.entity.Inventory;
import com.ecommerce.common.entity.InventoryLog;

import java.util.List;

/**
 * 库存服务接口
 */
public interface InventoryService {
    
    /**
     * 初始化商品库存
     */
    Inventory initInventory(Long productId, Integer stock);
    
    /**
     * 获取商品库存
     */
    Inventory getInventory(Long productId);
    
    /**
     * 获取可用库存
     */
    Integer getAvailableStock(Long productId);
    
    /**
     * 锁定库存
     */
    boolean lockStock(Long productId, Integer quantity, Long orderId);
    
    /**
     * 扣减库存
     */
    boolean deductStock(Long productId, Integer quantity, Long orderId);
    
    /**
     * 释放库存
     */
    boolean releaseStock(Long productId, Integer quantity, Long orderId);
    
    /**
     * 增加库存
     */
    boolean addStock(Long productId, Integer quantity);
    
    /**
     * 设置库存
     */
    Inventory setStock(Long productId, Integer stock);
    
    /**
     * 获取库存预警列表
     */
    List<Inventory> getWarningInventories();
    
    /**
     * 获取库存日志
     */
    List<InventoryLog> getInventoryLogs(Long productId);
}

