package com.ecommerce.common.structure;

import com.ecommerce.common.enums.OrderStatus;

import java.util.*;

/**
 * 订单状态机
 * 使用图结构实现订单状态流转
 */
public class OrderStateMachine {
    
    /** 状态转换图：当前状态 -> 可转换的目标状态集合 */
    private static final Map<OrderStatus, Set<OrderStatus>> TRANSITIONS = new HashMap<>();
    
    static {
        // 初始化状态转换规则
        // 待支付 -> 已支付, 已取消
        TRANSITIONS.put(OrderStatus.PENDING_PAYMENT, 
            new HashSet<>(Arrays.asList(OrderStatus.PAID, OrderStatus.CANCELLED)));
        
        // 已支付 -> 已发货, 退款中
        TRANSITIONS.put(OrderStatus.PAID, 
            new HashSet<>(Arrays.asList(OrderStatus.SHIPPED, OrderStatus.REFUNDING)));
        
        // 已发货 -> 已收货, 退款中
        TRANSITIONS.put(OrderStatus.SHIPPED, 
            new HashSet<>(Arrays.asList(OrderStatus.RECEIVED, OrderStatus.REFUNDING)));
        
        // 已收货 -> 已完成, 退款中
        TRANSITIONS.put(OrderStatus.RECEIVED, 
            new HashSet<>(Arrays.asList(OrderStatus.COMPLETED, OrderStatus.REFUNDING)));
        
        // 已完成 -> 无后续状态
        TRANSITIONS.put(OrderStatus.COMPLETED, new HashSet<>());
        
        // 已取消 -> 无后续状态
        TRANSITIONS.put(OrderStatus.CANCELLED, new HashSet<>());
        
        // 退款中 -> 已退款
        TRANSITIONS.put(OrderStatus.REFUNDING, 
            new HashSet<>(Collections.singletonList(OrderStatus.REFUNDED)));
        
        // 已退款 -> 无后续状态
        TRANSITIONS.put(OrderStatus.REFUNDED, new HashSet<>());
    }
    
    /**
     * 检查状态转换是否合法
     * @param currentStatus 当前状态
     * @param targetStatus 目标状态
     * @return 是否可以转换
     */
    public static boolean canTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
        if (currentStatus == null || targetStatus == null) {
            return false;
        }
        Set<OrderStatus> allowedTargets = TRANSITIONS.get(currentStatus);
        return allowedTargets != null && allowedTargets.contains(targetStatus);
    }
    
    /**
     * 获取当前状态可转换的所有目标状态
     * @param currentStatus 当前状态
     * @return 可转换的目标状态集合
     */
    public static Set<OrderStatus> getNextStates(OrderStatus currentStatus) {
        if (currentStatus == null) {
            return Collections.emptySet();
        }
        return TRANSITIONS.getOrDefault(currentStatus, Collections.emptySet());
    }
    
    /**
     * 判断是否为终态
     * @param status 状态
     * @return 是否为终态
     */
    public static boolean isFinalState(OrderStatus status) {
        if (status == null) {
            return false;
        }
        Set<OrderStatus> nextStates = TRANSITIONS.get(status);
        return nextStates == null || nextStates.isEmpty();
    }
    
    /**
     * 获取所有可能的状态路径（使用BFS）
     * @param start 起始状态
     * @param end 目标状态
     * @return 状态路径列表
     */
    public static List<OrderStatus> findPath(OrderStatus start, OrderStatus end) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        
        if (start == end) {
            return Collections.singletonList(start);
        }
        
        Queue<List<OrderStatus>> queue = new LinkedList<>();
        Set<OrderStatus> visited = new HashSet<>();
        
        queue.offer(new ArrayList<>(Collections.singletonList(start)));
        visited.add(start);
        
        while (!queue.isEmpty()) {
            List<OrderStatus> path = queue.poll();
            OrderStatus current = path.get(path.size() - 1);
            
            for (OrderStatus next : getNextStates(current)) {
                if (next == end) {
                    List<OrderStatus> result = new ArrayList<>(path);
                    result.add(end);
                    return result;
                }
                
                if (!visited.contains(next)) {
                    visited.add(next);
                    List<OrderStatus> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    queue.offer(newPath);
                }
            }
        }
        
        return Collections.emptyList();
    }
}


