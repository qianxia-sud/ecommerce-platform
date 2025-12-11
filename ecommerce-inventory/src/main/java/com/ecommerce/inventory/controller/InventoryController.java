package com.ecommerce.inventory.controller;

import com.ecommerce.common.dto.InventoryDTO;
import com.ecommerce.common.entity.Inventory;
import com.ecommerce.common.entity.InventoryLog;
import com.ecommerce.common.result.Result;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存控制器
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    /**
     * 初始化商品库存
     */
    @PostMapping("/init")
    public Result<Inventory> initInventory(@RequestParam Long productId, @RequestParam Integer stock) {
        return Result.success(inventoryService.initInventory(productId, stock));
    }
    
    /**
     * 获取商品库存信息
     */
    @GetMapping("/product/{productId}")
    public Result<Inventory> getInventory(@PathVariable Long productId) {
        return Result.success(inventoryService.getInventory(productId));
    }
    
    /**
     * 获取可用库存
     */
    @GetMapping("/product/{productId}/stock")
    public Result<Integer> getAvailableStock(@PathVariable Long productId) {
        return Result.success(inventoryService.getAvailableStock(productId));
    }
    
    /**
     * 锁定库存
     */
    @PostMapping("/lock")
    public Result<Boolean> lockStock(@RequestBody @Validated InventoryDTO dto) {
        return Result.success(inventoryService.lockStock(dto.getProductId(), dto.getQuantity(), dto.getOrderId()));
    }
    
    /**
     * 扣减库存
     */
    @PostMapping("/deduct")
    public Result<Boolean> deductStock(@RequestBody @Validated InventoryDTO dto) {
        return Result.success(inventoryService.deductStock(dto.getProductId(), dto.getQuantity(), dto.getOrderId()));
    }
    
    /**
     * 释放库存
     */
    @PostMapping("/release")
    public Result<Boolean> releaseStock(@RequestBody @Validated InventoryDTO dto) {
        return Result.success(inventoryService.releaseStock(dto.getProductId(), dto.getQuantity(), dto.getOrderId()));
    }
    
    /**
     * 增加库存
     */
    @PostMapping("/add")
    public Result<Boolean> addStock(@RequestParam Long productId, @RequestParam Integer quantity) {
        return Result.success(inventoryService.addStock(productId, quantity));
    }
    
    /**
     * 设置库存
     */
    @PutMapping("/set")
    public Result<Inventory> setStock(@RequestParam Long productId, @RequestParam Integer stock) {
        return Result.success(inventoryService.setStock(productId, stock));
    }
    
    /**
     * 获取库存预警列表
     */
    @GetMapping("/warning")
    public Result<List<Inventory>> getWarningInventories() {
        return Result.success(inventoryService.getWarningInventories());
    }
    
    /**
     * 获取库存日志
     */
    @GetMapping("/product/{productId}/logs")
    public Result<List<InventoryLog>> getInventoryLogs(@PathVariable Long productId) {
        return Result.success(inventoryService.getInventoryLogs(productId));
    }
}

