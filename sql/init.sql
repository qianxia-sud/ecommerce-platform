-- =============================================
-- 电商平台订单交易系统 - 数据库初始化脚本
-- MySQL 8.0
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ecommerce_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ecommerce_product DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ecommerce_inventory DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ecommerce_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ecommerce_payment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 用户服务数据库
-- =============================================
USE ecommerce_user;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(MD5加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    role VARCHAR(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户地址表
CREATE TABLE IF NOT EXISTS t_user_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) NOT NULL COMMENT '省份',
    city VARCHAR(50) NOT NULL COMMENT '城市',
    district VARCHAR(50) NOT NULL COMMENT '区/县',
    detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
    is_default INT NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户地址表';

-- =============================================
-- 商品服务数据库
-- =============================================
USE ecommerce_product;

-- 商品分类表
CREATE TABLE IF NOT EXISTS t_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    level INT NOT NULL DEFAULT 1 COMMENT '分类层级：1-一级，2-二级，3-三级',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    icon VARCHAR(255) COMMENT '分类图标',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS t_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    market_price DECIMAL(10,2) COMMENT '市场价',
    main_image VARCHAR(255) COMMENT '主图URL',
    images TEXT COMMENT '商品图片（多张，用逗号分隔）',
    status INT NOT NULL DEFAULT 1 COMMENT '商品状态：0-下架，1-上架',
    sales INT NOT NULL DEFAULT 0 COMMENT '销量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- =============================================
-- 库存服务数据库
-- =============================================
USE ecommerce_inventory;

-- 库存表
CREATE TABLE IF NOT EXISTS t_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    product_id BIGINT NOT NULL UNIQUE COMMENT '商品ID',
    total_stock INT NOT NULL DEFAULT 0 COMMENT '总库存',
    available_stock INT NOT NULL DEFAULT 0 COMMENT '可用库存',
    locked_stock INT NOT NULL DEFAULT 0 COMMENT '锁定库存（已下单未支付）',
    warning_threshold INT NOT NULL DEFAULT 10 COMMENT '预警库存阈值',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- 库存操作日志表
CREATE TABLE IF NOT EXISTS t_inventory_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    order_id BIGINT COMMENT '订单ID',
    operation_type VARCHAR(20) NOT NULL COMMENT '操作类型：LOCK-锁定, DEDUCT-扣减, RELEASE-释放, ADD-增加',
    quantity INT NOT NULL COMMENT '变动数量',
    before_stock INT NOT NULL COMMENT '操作前库存',
    after_stock INT NOT NULL COMMENT '操作后库存',
    remark VARCHAR(255) COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存操作日志表';

-- =============================================
-- 订单服务数据库
-- =============================================
USE ecommerce_order;

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    freight DECIMAL(10,2) DEFAULT 0.00 COMMENT '运费',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) COMMENT '订单备注',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    complete_time DATETIME COMMENT '完成时间',
    cancel_time DATETIME COMMENT '取消时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单商品明细表
CREATE TABLE IF NOT EXISTS t_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称（快照）',
    product_image VARCHAR(255) COMMENT '商品图片（快照）',
    price DECIMAL(10,2) NOT NULL COMMENT '商品单价（快照）',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品明细表';

-- =============================================
-- 支付服务数据库
-- =============================================
USE ecommerce_payment;

-- 支付记录表
CREATE TABLE IF NOT EXISTS t_payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
    payment_no VARCHAR(32) NOT NULL UNIQUE COMMENT '支付流水号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    channel VARCHAR(20) NOT NULL COMMENT '支付渠道：WECHAT-微信, ALIPAY-支付宝, MOCK-模拟',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态',
    trade_no VARCHAR(64) COMMENT '第三方支付交易号',
    pay_time DATETIME COMMENT '支付时间',
    refund_amount DECIMAL(10,2) COMMENT '退款金额',
    refund_time DATETIME COMMENT '退款时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_payment_no (payment_no),
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- =============================================
-- 初始化测试数据
-- =============================================

-- 插入测试用户
USE ecommerce_user;
INSERT INTO t_user (username, password, nickname, email, phone, status, role, create_time, update_time) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin@example.com', '13800138000', 1, 'ADMIN', NOW(), NOW()),
('user1', 'e10adc3949ba59abbe56e057f20f883e', '测试用户1', 'user1@example.com', '13800138001', 1, 'USER', NOW(), NOW()),
('user2', 'e10adc3949ba59abbe56e057f20f883e', '测试用户2', 'user2@example.com', '13800138002', 1, 'USER', NOW(), NOW());
-- 密码都是: 123456 (MD5加密后)
-- admin 是管理员角色，user1/user2 是普通用户角色

-- 插入测试地址
INSERT INTO t_user_address (user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, create_time, update_time) VALUES
(1, '张三', '13800138000', '北京市', '北京市', '朝阳区', '某某街道123号', 1, NOW(), NOW()),
(2, '李四', '13800138001', '上海市', '上海市', '浦东新区', '某某路456号', 1, NOW(), NOW());

-- 插入测试分类
USE ecommerce_product;
INSERT INTO t_category (name, parent_id, level, sort, status, create_time, update_time) VALUES
('电子产品', 0, 1, 1, 1, NOW(), NOW()),
('服装服饰', 0, 1, 2, 1, NOW(), NOW()),
('食品饮料', 0, 1, 3, 1, NOW(), NOW()),
('手机', 1, 2, 1, 1, NOW(), NOW()),
('电脑', 1, 2, 2, 1, NOW(), NOW()),
('男装', 2, 2, 1, 1, NOW(), NOW()),
('女装', 2, 2, 2, 1, NOW(), NOW());

-- 插入测试商品
INSERT INTO t_product (name, description, category_id, price, market_price, main_image, status, sales, create_time, update_time) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro芯片', 4, 8999.00, 9999.00, 'https://via.placeholder.com/300x300?text=iPhone15', 1, 100, NOW(), NOW()),
('MacBook Pro 14', '苹果笔记本电脑，M3 Pro芯片', 5, 14999.00, 16999.00, 'https://via.placeholder.com/300x300?text=MacBook', 1, 50, NOW(), NOW()),
('华为Mate60 Pro', '华为旗舰手机，麒麟芯片回归', 4, 6999.00, 7999.00, 'https://via.placeholder.com/300x300?text=Mate60', 1, 200, NOW(), NOW()),
('纯棉T恤', '100%纯棉，舒适透气', 6, 99.00, 199.00, 'https://via.placeholder.com/300x300?text=T-Shirt', 1, 500, NOW(), NOW()),
('连衣裙', '夏季新款，时尚优雅', 7, 299.00, 499.00, 'https://via.placeholder.com/300x300?text=Dress', 1, 300, NOW(), NOW());

-- 插入测试库存
USE ecommerce_inventory;
INSERT INTO t_inventory (product_id, total_stock, available_stock, locked_stock, warning_threshold, create_time, update_time) VALUES
(1, 100, 100, 0, 10, NOW(), NOW()),
(2, 50, 50, 0, 5, NOW(), NOW()),
(3, 200, 200, 0, 20, NOW(), NOW()),
(4, 1000, 1000, 0, 50, NOW(), NOW()),
(5, 500, 500, 0, 30, NOW(), NOW());

SELECT '数据库初始化完成！' AS message;

