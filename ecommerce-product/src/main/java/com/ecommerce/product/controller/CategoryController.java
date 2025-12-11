package com.ecommerce.product.controller;

import com.ecommerce.common.entity.Category;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.structure.CategoryCache;
import com.ecommerce.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * 创建分类
     */
    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category) {
        return Result.success(categoryService.createCategory(category));
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return Result.success(categoryService.updateCategory(id, category));
    }
    
    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }
    
    /**
     * 获取子分类
     */
    @GetMapping("/children/{parentId}")
    public Result<List<Category>> getChildCategories(@PathVariable Long parentId) {
        return Result.success(categoryService.getChildCategories(parentId));
    }
    
    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    public Result<List<CategoryCache.CategoryTreeNode>> getCategoryTree() {
        return Result.success(categoryService.getCategoryTree());
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}


