package com.ecommerce.inventory.service.impl;

import com.ecommerce.common.entity.Inventory;
import com.ecommerce.common.entity.InventoryLog;
import com.ecommerce.common.enums.InventoryOperationType;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.structure.InventoryOperationQueue;
import com.ecommerce.inventory.repository.InventoryLogRepository;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 库存服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final InventoryLogRepository inventoryLogRepository;
    
    // 库存操作队列（用于记录操作日志）
    private final InventoryOperationQueue operationQueue = new InventoryOperationQueue();
    
    @Override
    @Transactional
    public Inventory initInventory(Long productId, Integer stock) {
        // 检查是否已存在
        if (inventoryRepository.findByProductId(productId).isPresent()) {
            throw new BusinessException("该商品库存已存在");
        }
        
        Inventory inventory = Inventory.builder()
                .productId(productId)
                .totalStock(stock)
                .availableStock(stock)
                .lockedStock(0)
                .warningThreshold(10)
                .build();
        
        return inventoryRepository.save(inventory);
    }
    
    @Override
    public Inventory getInventory(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ResultCode.INVENTORY_NOT_FOUND));
    }
    
    @Override
    public Integer getAvailableStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(Inventory::getAvailableStock)
                .orElse(0);
    }
    
    @Override
    @Transactional
    public boolean lockStock(Long productId, Integer quantity, Long orderId) {
        // 使用悲观锁获取库存
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(ResultCode.INVENTORY_NOT_FOUND));
        
        // 检查库存是否充足
        if (inventory.getAvailableStock() < quantity) {
            throw new BusinessException(ResultCode.INVENTORY_NOT_ENOUGH);
        }
        
        // 锁定库存
        int result = inventoryRepository.lockStock(productId, quantity);
        if (result > 0) {
            // 记录日志
            saveLog(productId, orderId, InventoryOperationType.LOCK, quantity, 
                    inventory.getAvailableStock(), inventory.getAvailableStock() - quantity);
            
            // 加入操作队列
            operationQueue.enqueue(InventoryOperationQueue.createLockOperation(productId, orderId, quantity));
            
            log.info("库存锁定成功: productId={}, quantity={}, orderId={}", productId, quantity, orderId);
            return true;
        }
        
        throw new BusinessException(ResultCode.INVENTORY_LOCK_FAILED);
    }
    
    @Override
    @Transactional
    public boolean deductStock(Long productId, Integer quantity, Long orderId) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(ResultCode.INVENTORY_NOT_FOUND));
        
        // 检查锁定库存是否充足
        if (inventory.getLockedStock() < quantity) {
            throw new BusinessException("锁定库存不足，无法扣减");
        }
        
        int result = inventoryRepository.deductStock(productId, quantity);
        if (result > 0) {
            saveLog(productId, orderId, InventoryOperationType.DEDUCT, quantity,
                    inventory.getLockedStock(), inventory.getLockedStock() - quantity);
            
            operationQueue.enqueue(InventoryOperationQueue.createDeductOperation(productId, orderId, quantity));
            
            log.info("库存扣减成功: productId={}, quantity={}, orderId={}", productId, quantity, orderId);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public boolean releaseStock(Long productId, Integer quantity, Long orderId) {
        Inventory inventory = inventoryRepository.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new BusinessException(ResultCode.INVENTORY_NOT_FOUND));
        
        // 检查锁定库存是否充足
        if (inventory.getLockedStock() < quantity) {
            throw new BusinessException("锁定库存不足，无法释放");
        }
        
        int result = inventoryRepository.releaseStock(productId, quantity);
        if (result > 0) {
            saveLog(productId, orderId, InventoryOperationType.RELEASE, quantity,
                    inventory.getAvailableStock(), inventory.getAvailableStock() + quantity);
            
            operationQueue.enqueue(InventoryOperationQueue.createReleaseOperation(productId, orderId, quantity));
            
            log.info("库存释放成功: productId={}, quantity={}, orderId={}", productId, quantity, orderId);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public boolean addStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElse(null);
        
        if (inventory == null) {
            // 不存在则初始化
            initInventory(productId, quantity);
            return true;
        }
        
        int result = inventoryRepository.addStock(productId, quantity);
        if (result > 0) {
            saveLog(productId, null, InventoryOperationType.ADD, quantity,
                    inventory.getAvailableStock(), inventory.getAvailableStock() + quantity);
            
            log.info("库存增加成功: productId={}, quantity={}", productId, quantity);
            return true;
        }
        
        return false;
    }
    
    @Override
    @Transactional
    public Inventory setStock(Long productId, Integer stock) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElse(null);
        
        if (inventory == null) {
            return initInventory(productId, stock);
        }
        
        int diff = stock - inventory.getTotalStock();
        inventory.setTotalStock(stock);
        inventory.setAvailableStock(inventory.getAvailableStock() + diff);
        
        saveLog(productId, null, InventoryOperationType.SET, diff,
                inventory.getAvailableStock() - diff, inventory.getAvailableStock());
        
        return inventoryRepository.save(inventory);
    }
    
    @Override
    public List<Inventory> getWarningInventories() {
        return inventoryRepository.findWarningInventories();
    }
    
    @Override
    public List<InventoryLog> getInventoryLogs(Long productId) {
        return inventoryLogRepository.findByProductIdOrderByCreateTimeDesc(productId);
    }
    
    /**
     * 保存库存操作日志
     */
    private void saveLog(Long productId, Long orderId, InventoryOperationType operationType,
                         Integer quantity, Integer beforeStock, Integer afterStock) {
        InventoryLog log = InventoryLog.builder()
                .productId(productId)
                .orderId(orderId)
                .operationType(operationType.name())
                .quantity(quantity)
                .beforeStock(beforeStock)
                .afterStock(afterStock)
                .remark(operationType.getDescription())
                .build();
        
        inventoryLogRepository.save(log);
    }
}

