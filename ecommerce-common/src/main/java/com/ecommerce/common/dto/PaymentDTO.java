package com.ecommerce.common.dto;

import com.ecommerce.common.enums.PaymentChannel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 支付请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "支付渠道不能为空")
    private PaymentChannel channel;
}


