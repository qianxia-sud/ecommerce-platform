package com.ecommerce.product.controller;

import com.ecommerce.common.dto.ProductDTO;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.vo.ProductVO;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 创建商品
     */
    @PostMapping
    public Result<ProductVO> createProduct(@RequestBody @Validated ProductDTO productDTO) {
        return Result.success(productService.createProduct(productDTO));
    }
    
    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public Result<ProductVO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return Result.success(productService.updateProduct(id, productDTO));
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }
    
    /**
     * 获取商品实体（内部调用）
     */
    @GetMapping("/{id}/entity")
    public Result<Product> getProductEntityById(@PathVariable Long id) {
        return Result.success(productService.getProductEntityById(id));
    }
    
    /**
     * 分页查询商品
     */
    @GetMapping("/list")
    public Result<PageResult<ProductVO>> getProductList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.getProductList(pageNum, pageSize, keyword));
    }
    
    /**
     * 根据分类查询商品
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<ProductVO>> getProductsByCategory(@PathVariable Long categoryId) {
        return Result.success(productService.getProductsByCategory(categoryId));
    }
    
    /**
     * 上架商品
     */
    @PutMapping("/{id}/on-shelf")
    public Result<Void> onShelf(@PathVariable Long id) {
        productService.onShelf(id);
        return Result.success();
    }
    
    /**
     * 下架商品
     */
    @PutMapping("/{id}/off-shelf")
    public Result<Void> offShelf(@PathVariable Long id) {
        productService.offShelf(id);
        return Result.success();
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
    
    /**
     * 增加销量（内部调用）
     */
    @PutMapping("/{id}/sales")
    public Result<Void> increaseSales(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.increaseSales(id, quantity);
        return Result.success();
    }
    
    /**
     * 获取商品总数
     */
    @GetMapping("/count")
    public Result<Long> getProductCount() {
        return Result.success(productService.getProductCount());
    }
}

