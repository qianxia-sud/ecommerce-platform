package com.ecommerce.common.structure;

import com.ecommerce.common.enums.InventoryOperationType;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 库存操作队列
 * 使用队列数据结构记录库存操作，支持异步处理
 */
public class InventoryOperationQueue {
    
    /** 操作队列 */
    private final ConcurrentLinkedQueue<InventoryOperation> queue;
    
    /** 队列大小计数器 */
    private final AtomicInteger size;
    
    /** 最大队列长度 */
    private final int maxSize;
    
    public InventoryOperationQueue() {
        this(10000);
    }
    
    public InventoryOperationQueue(int maxSize) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.size = new AtomicInteger(0);
        this.maxSize = maxSize;
    }
    
    /**
     * 库存操作记录
     */
    @Data
    @AllArgsConstructor
    public static class InventoryOperation {
        private Long productId;
        private Long orderId;
        private InventoryOperationType operationType;
        private Integer quantity;
        private LocalDateTime operationTime;
        private String remark;
    }
    
    /**
     * 入队操作
     * @param operation 操作记录
     * @return 是否成功
     */
    public boolean enqueue(InventoryOperation operation) {
        if (size.get() >= maxSize) {
            return false;
        }
        boolean added = queue.offer(operation);
        if (added) {
            size.incrementAndGet();
        }
        return added;
    }
    
    /**
     * 出队操作
     * @return 操作记录，队列为空返回null
     */
    public InventoryOperation dequeue() {
        InventoryOperation operation = queue.poll();
        if (operation != null) {
            size.decrementAndGet();
        }
        return operation;
    }
    
    /**
     * 查看队首元素（不移除）
     * @return 操作记录
     */
    public InventoryOperation peek() {
        return queue.peek();
    }
    
    /**
     * 获取队列大小
     * @return 队列当前大小
     */
    public int size() {
        return size.get();
    }
    
    /**
     * 检查队列是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * 清空队列
     */
    public void clear() {
        queue.clear();
        size.set(0);
    }
    
    /**
     * 创建锁定库存操作
     */
    public static InventoryOperation createLockOperation(Long productId, Long orderId, Integer quantity) {
        return new InventoryOperation(
            productId, orderId, InventoryOperationType.LOCK, 
            quantity, LocalDateTime.now(), "订单锁定库存"
        );
    }
    
    /**
     * 创建扣减库存操作
     */
    public static InventoryOperation createDeductOperation(Long productId, Long orderId, Integer quantity) {
        return new InventoryOperation(
            productId, orderId, InventoryOperationType.DEDUCT, 
            quantity, LocalDateTime.now(), "订单扣减库存"
        );
    }
    
    /**
     * 创建释放库存操作
     */
    public static InventoryOperation createReleaseOperation(Long productId, Long orderId, Integer quantity) {
        return new InventoryOperation(
            productId, orderId, InventoryOperationType.RELEASE, 
            quantity, LocalDateTime.now(), "订单取消释放库存"
        );
    }
}


