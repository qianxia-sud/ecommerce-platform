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
     * 管理后台首页
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
    
    // ==================== 用户购物前台页面 ====================
    
    /**
     * 购物首页（商品浏览）
     */
    @GetMapping("/shop")
    public String shop() {
        return "shop/index";
    }
    
    /**
     * 商品详情页
     */
    @GetMapping("/shop/product")
    public String shopProduct() {
        return "shop/product";
    }
    
    /**
     * 购物车页面
     */
    @GetMapping("/shop/cart")
    public String shopCart() {
        return "shop/cart";
    }
    
    /**
     * 结算页面
     */
    @GetMapping("/shop/checkout")
    public String shopCheckout() {
        return "shop/checkout";
    }
    
    /**
     * 支付页面
     */
    @GetMapping("/shop/payment")
    public String shopPayment() {
        return "shop/payment";
    }
    
    /**
     * 我的订单页面
     */
    @GetMapping("/shop/orders")
    public String shopOrders() {
        return "shop/orders";
    }
}

