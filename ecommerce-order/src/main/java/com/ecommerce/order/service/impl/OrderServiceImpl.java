package com.ecommerce.order.service.impl;

import com.ecommerce.common.dto.InventoryDTO;
import com.ecommerce.common.dto.OrderCreateDTO;
import com.ecommerce.common.dto.OrderItemDTO;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderItem;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.entity.UserAddress;
import com.ecommerce.common.enums.OrderStatus;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.structure.OrderStateMachine;
import com.ecommerce.common.util.OrderNoGenerator;
import com.ecommerce.common.vo.OrderItemVO;
import com.ecommerce.common.vo.OrderVO;
import com.ecommerce.order.feign.InventoryFeignClient;
import com.ecommerce.order.feign.ProductFeignClient;
import com.ecommerce.order.feign.UserFeignClient;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserFeignClient userFeignClient;
    private final ProductFeignClient productFeignClient;
    private final InventoryFeignClient inventoryFeignClient;
    
    @Override
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderDTO) {
        // 1. 获取收货地址
        Result<UserAddress> addressResult = userFeignClient.getAddressById(orderDTO.getAddressId());
        if (!addressResult.isSuccess() || addressResult.getData() == null) {
            throw new BusinessException("收货地址不存在");
        }
        UserAddress address = addressResult.getData();
        
        // 2. 生成订单号
        String orderNo = OrderNoGenerator.generate();
        
        // 3. 计算订单金额并创建订单项
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            // 获取商品信息
            Result<Product> productResult = productFeignClient.getProductEntityById(itemDTO.getProductId());
            if (!productResult.isSuccess() || productResult.getData() == null) {
                throw new BusinessException("商品不存在: " + itemDTO.getProductId());
            }
            Product product = productResult.getData();
            
            // 检查商品状态
            if (product.getStatus() != 1) {
                throw new BusinessException(ResultCode.PRODUCT_OFF_SHELF, "商品已下架: " + product.getName());
            }
            
            // 计算小计
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            // 创建订单项
            OrderItem orderItem = OrderItem.builder()
                    .orderNo(orderNo)
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(product.getMainImage())
                    .price(product.getPrice())
                    .quantity(itemDTO.getQuantity())
                    .subtotal(subtotal)
                    .build();
            orderItems.add(orderItem);
        }
        
        // 4. 创建订单
        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(orderDTO.getUserId())
                .totalAmount(totalAmount)
                .payAmount(totalAmount)  // 暂不计算优惠
                .freight(BigDecimal.ZERO)
                .status(OrderStatus.PENDING_PAYMENT)
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .receiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress())
                .remark(orderDTO.getRemark())
                .build();
        
        order = orderRepository.save(order);
        
        // 5. 保存订单项并锁定库存
        final Long orderId = order.getId();
        for (OrderItem item : orderItems) {
            item.setOrderId(orderId);
            orderItemRepository.save(item);
            
            // 锁定库存
            InventoryDTO inventoryDTO = InventoryDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .orderId(orderId)
                    .build();
            
            Result<Boolean> lockResult = inventoryFeignClient.lockStock(inventoryDTO);
            if (!lockResult.isSuccess() || !Boolean.TRUE.equals(lockResult.getData())) {
                throw new BusinessException(ResultCode.INVENTORY_LOCK_FAILED, "库存锁定失败: " + item.getProductName());
            }
        }
        
        log.info("订单创建成功: orderNo={}, userId={}, totalAmount={}", orderNo, orderDTO.getUserId(), totalAmount);
        
        return convertToVO(order, orderItems);
    }
    
    @Override
    public OrderVO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        return convertToVO(order, items);
    }
    
    @Override
    public OrderVO getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));
        List<OrderItem> items = orderItemRepository.findByOrderNo(orderNo);
        return convertToVO(order, items);
    }
    
    @Override
    public Order getOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));
    }
    
    @Override
    public List<OrderVO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreateTimeDesc(userId);
        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToVO(order, items);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public PageResult<OrderVO> getAllOrders(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Order> page = orderRepository.findAllByOrderByCreateTimeDesc(pageRequest);
        
        List<OrderVO> list = page.getContent().stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    return convertToVO(order, items);
                })
                .collect(Collectors.toList());
        
        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }
    
    @Override
    @Transactional
    public void payOrder(Long orderId) {
        Order order = getOrderEntityById(orderId);
        
        // 使用状态机检查状态转换
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.PAID)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "当前订单状态不允许支付");
        }
        
        order.setStatus(OrderStatus.PAID);
        order.setPayTime(LocalDateTime.now());
        orderRepository.save(order);
        
        // 扣减库存并增加销量
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            // 扣减库存
            InventoryDTO inventoryDTO = InventoryDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .orderId(orderId)
                    .build();
            inventoryFeignClient.deductStock(inventoryDTO);
            
            // 增加销量
            productFeignClient.increaseSales(item.getProductId(), item.getQuantity());
        }
        
        log.info("订单支付成功: orderId={}", orderId);
    }
    
    @Override
    @Transactional
    public void shipOrder(Long orderId) {
        Order order = getOrderEntityById(orderId);
        
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.SHIPPED)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "当前订单状态不允许发货");
        }
        
        order.setStatus(OrderStatus.SHIPPED);
        order.setShipTime(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("订单发货成功: orderId={}", orderId);
    }
    
    @Override
    @Transactional
    public void receiveOrder(Long orderId) {
        Order order = getOrderEntityById(orderId);
        
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.RECEIVED)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "当前订单状态不允许确认收货");
        }
        
        order.setStatus(OrderStatus.RECEIVED);
        order.setReceiveTime(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("订单确认收货: orderId={}", orderId);
    }
    
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrderEntityById(orderId);
        
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.CANCELLED)) {
            throw new BusinessException(ResultCode.ORDER_CANCEL_FAILED, "当前订单状态不允许取消");
        }
        
        // 释放库存
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : items) {
            InventoryDTO inventoryDTO = InventoryDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .orderId(orderId)
                    .build();
            inventoryFeignClient.releaseStock(inventoryDTO);
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("订单取消成功: orderId={}", orderId);
    }
    
    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        Order order = getOrderEntityById(orderId);
        
        if (!OrderStateMachine.canTransition(order.getStatus(), OrderStatus.COMPLETED)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR, "当前订单状态不允许完成");
        }
        
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompleteTime(LocalDateTime.now());
        orderRepository.save(order);
        
        log.info("订单完成: orderId={}", orderId);
    }
    
    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = getOrderEntityById(orderId);
        OrderStatus newStatus = OrderStatus.valueOf(status);
        
        if (!OrderStateMachine.canTransition(order.getStatus(), newStatus)) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }
        
        orderRepository.updateStatus(orderId, newStatus, LocalDateTime.now());
    }
    
    @Override
    public Long getOrderCount() {
        return orderRepository.count();
    }
    
    @Override
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    /**
     * 转换为VO对象
     */
    private OrderVO convertToVO(Order order, List<OrderItem> items) {
        List<OrderItemVO> itemVOs = items.stream()
                .map(item -> OrderItemVO.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .productImage(item.getProductImage())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());
        
        return OrderVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .payAmount(order.getPayAmount())
                .freight(order.getFreight())
                .status(order.getStatus())
                .statusDesc(order.getStatus().getDescription())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress())
                .remark(order.getRemark())
                .payTime(order.getPayTime())
                .shipTime(order.getShipTime())
                .receiveTime(order.getReceiveTime())
                .completeTime(order.getCompleteTime())
                .cancelTime(order.getCancelTime())
                .createTime(order.getCreateTime())
                .items(itemVOs)
                .build();
    }
}

