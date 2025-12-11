package com.ecommerce.order.feign;

import com.ecommerce.common.entity.UserAddress;
import com.ecommerce.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service")
public interface UserFeignClient {
    
    /**
     * 获取用户地址
     */
    @GetMapping("/user/address/{addressId}")
    Result<UserAddress> getAddressById(@PathVariable("addressId") Long addressId);
}

