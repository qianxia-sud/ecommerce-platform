package com.ecommerce.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 管理后台页面控制器
 */
@Controller
public class AdminController {
    
    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    
    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
    
    /**
     * 首页（根路径重定向到登录）
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
    
    /**
     * 首页
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    
    /**
     * 用户管理页面
     */
    @GetMapping("/users")
    public String users() {
        return "users";
    }
    
    /**
     * 商品管理页面
     */
    @GetMapping("/products")
    public String products() {
        return "products";
    }
    
    /**
     * 分类管理页面
     */
    @GetMapping("/categories")
    public String categories() {
        return "categories";
    }
    
    /**
     * 订单管理页面
     */
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
    
    /**
     * 库存管理页面
     */
    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";
    }
}

