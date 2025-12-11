# 电商平台订单交易系统

基于 Spring Cloud 微服务架构的电商平台订单交易系统，包含用户、商品、库存、订单、支付等核心服务。

## 技术栈

- **框架**: Spring Boot 2.7.x + Spring Cloud 2021.x
- **构建工具**: Maven
- **数据库**: MySQL 8.0
- **数据访问**: Spring Data JPA / JDBC
- **服务注册**: Eureka
- **服务网关**: Spring Cloud Gateway
- **服务调用**: OpenFeign
- **前端**: Thymeleaf + Bootstrap 5

## 项目结构

```
ecommerce-platform/
├── pom.xml                      # 父POM
├── sql/                         # 数据库脚本
│   └── init.sql                 # 初始化脚本
├── ecommerce-common/            # 公共模块
├── ecommerce-eureka/            # 服务注册中心 (端口: 8761)
├── ecommerce-gateway/           # 网关服务 (端口: 8080)
├── ecommerce-user/              # 用户服务 (端口: 8081)
├── ecommerce-product/           # 商品服务 (端口: 8082)
├── ecommerce-inventory/         # 库存服务 (端口: 8083)
├── ecommerce-order/             # 订单服务 (端口: 8084)
├── ecommerce-payment/           # 支付服务 (端口: 8085)
└── ecommerce-admin/             # 管理后台 (端口: 8090)
```

## 核心功能

### 用户服务 (user-service)
- 用户注册/登录 (JWT认证)
- 用户信息管理
- 收货地址管理

### 商品服务 (product-service)
- 商品CRUD操作
- 商品分类管理
- 商品上下架

### 库存服务 (inventory-service)
- 库存锁定/扣减/释放
- 库存预警查询
- 库存操作日志

### 订单服务 (order-service)
- 订单创建
- 订单状态流转（状态机模式）
- 订单查询

### 支付服务 (payment-service)
- 预留支付接口（微信/支付宝）
- 模拟支付功能
- 退款处理

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 1. 初始化数据库

```bash
# 登录MySQL并执行初始化脚本
mysql -u root -p < sql/init.sql
```

或者在MySQL客户端中执行 `sql/init.sql` 脚本。

### 2. 修改数据库配置

根据实际情况修改各服务的 `application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_xxx?...
    username: root
    password: your_password
```

### 3. 编译项目

```bash
cd ecommerce-platform
mvn clean install -DskipTests
```

### 4. 启动服务

按以下顺序启动服务：

```bash
# 1. 启动Eureka注册中心
cd ecommerce-eureka
mvn spring-boot:run

# 2. 启动网关服务
cd ecommerce-gateway
mvn spring-boot:run

# 3. 启动业务服务（可同时启动）
cd ecommerce-user && mvn spring-boot:run
cd ecommerce-product && mvn spring-boot:run
cd ecommerce-inventory && mvn spring-boot:run
cd ecommerce-order && mvn spring-boot:run
cd ecommerce-payment && mvn spring-boot:run

# 4. 启动管理后台
cd ecommerce-admin
mvn spring-boot:run
```

### 5. 访问系统

- **Eureka控制台**: http://localhost:8761
- **管理后台**: http://localhost:8090
- **API网关**: http://localhost:8080

## API接口说明

所有API通过网关访问，基础路径为 `http://localhost:8080/api`

### 用户服务 API

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/user/user/register | POST | 用户注册 |
| /api/user/user/login | POST | 用户登录 |
| /api/user/user/{id} | GET | 获取用户信息 |
| /api/user/user/list | GET | 获取用户列表 |

### 商品服务 API

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/product/product | POST | 创建商品 |
| /api/product/product/{id} | GET | 获取商品详情 |
| /api/product/product/list | GET | 商品列表（分页） |
| /api/product/product/category/tree | GET | 获取分类树 |

### 订单服务 API

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/order/order | POST | 创建订单 |
| /api/order/order/{id} | GET | 获取订单详情 |
| /api/order/order/user/{userId} | GET | 获取用户订单 |
| /api/order/order/{id}/pay | PUT | 支付订单 |
| /api/order/order/{id}/cancel | PUT | 取消订单 |

### 支付服务 API

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/payment/payment/create | POST | 创建支付 |
| /api/payment/payment/{id}/mock-pay | POST | 模拟支付 |
| /api/payment/payment/{id}/refund | POST | 申请退款 |

## 测试账号

系统初始化后包含以下测试账号（密码均为 `123456`）：

| 用户名 | 角色 |
|--------|------|
| admin | 管理员 |
| user1 | 测试用户1 |
| user2 | 测试用户2 |

## 数据结构应用

本项目应用了以下数据结构知识：

1. **状态机（图）**: 订单状态流转使用图结构实现状态机，支持状态转换合法性检查
2. **队列**: 库存操作使用并发队列记录操作日志
3. **HashMap**: 商品分类使用HashMap实现内存缓存，支持快速查询

## 项目特点

1. **微服务架构**: 采用Spring Cloud微服务架构，服务间通过Feign进行调用
2. **统一网关**: 使用Spring Cloud Gateway实现统一入口和路由转发
3. **服务注册**: 使用Eureka实现服务注册与发现
4. **统一响应**: 使用Result<T>封装统一响应格式
5. **全局异常处理**: 统一的异常处理机制
6. **支付预留接口**: 支付服务预留了微信/支付宝接口，可方便扩展

## 注意事项

1. 确保MySQL服务已启动并创建了相应的数据库
2. 服务启动顺序：Eureka -> Gateway -> 其他服务
3. 如需修改端口，请同时修改网关的路由配置
4. 支付功能为模拟实现，如需接入真实支付请实现对应的PaymentChannelService

## License

MIT License

