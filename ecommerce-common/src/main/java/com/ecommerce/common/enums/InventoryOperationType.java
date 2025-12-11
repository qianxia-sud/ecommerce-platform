package com.ecommerce.common.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 库存操作类型枚举
 */
@Getter
@AllArgsConstructor
public enum InventoryOperationType {
    
    LOCK("锁定库存"),
    DEDUCT("扣减库存"),
    RELEASE("释放库存"),
    ADD("增加库存"),
    SET("设置库存");
    
    private final String description;
}


