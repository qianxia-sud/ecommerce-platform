package com.ecommerce.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成工具类
 */
public class OrderNoGenerator {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /** 序列号，用于同一秒内的订单区分 */
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    
    /** 上次生成时间 */
    private static volatile String lastTime = "";
    
    /**
     * 生成订单号
     * 格式：yyyyMMddHHmmss + 4位序列号
     * @return 订单号
     */
    public static synchronized String generate() {
        String currentTime = LocalDateTime.now().format(FORMATTER);
        
        if (!currentTime.equals(lastTime)) {
            lastTime = currentTime;
            SEQUENCE.set(0);
        }
        
        int seq = SEQUENCE.incrementAndGet();
        if (seq > 9999) {
            // 如果超过9999，等待下一秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return generate();
        }
        
        return currentTime + String.format("%04d", seq);
    }
    
    /**
     * 生成支付流水号
     * 格式：PAY + yyyyMMddHHmmss + 4位序列号
     * @return 支付流水号
     */
    public static String generatePaymentNo() {
        return "PAY" + generate();
    }
    
    /**
     * 生成退款流水号
     * 格式：REF + yyyyMMddHHmmss + 4位序列号
     * @return 退款流水号
     */
    public static String generateRefundNo() {
        return "REF" + generate();
    }
}

