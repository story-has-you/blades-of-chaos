# Blades of Chaos

一个强大的 Java Spring Boot 工具库，集成了企业开发中常用的功能组件和工具类。

## 功能特性

### 🔒 限流控制
- 基于 Redis 的分布式限流
- 通过 `@AccessLimiter` 注解轻松实现方法级限流
- 支持自定义限流策略和时间窗口

### 🔐 分布式锁
- 基于 Redis 的分布式并发锁
- 通过 `@ConcurrencyLock` 注解防止并发冲突
- 支持自动超时和锁释放

### 🛡️ 敏感数据保护
- 通过 `@Sensitive` 注解自动脱敏
- 支持手机号、身份证号、邮箱等敏感信息处理
- JSON 序列化时自动应用脱敏规则

### ✅ 数据验证
- 身份证号验证：`@CitizenId`
- 手机号验证：`@Mobile`
- 符合中国标准的验证规则

### 📄 分页支持
- 同时支持 JPA 和 PageHelper
- 统一的分页请求和响应格式
- 简化分页开发流程

### 🛠️ 丰富的工具类
- Bean 操作工具：`BeanUtils`
- 日期时间工具：`DateUtils`
- 集合操作工具：`CollectionUtils`
- JSON 处理工具：`JacksonUtils`
- ID 生成工具：`IdUtils`, `Sequence`
- 中文处理工具：`ChineseUtils`
- IP 工具：`IpUtils`

### 📦 统一响应格式
- `Result<T>` 标准化 API 响应
- `ResultCode` 状态码枚举
- 统一的异常处理机制

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.storyhasyou</groupId>
    <artifactId>blades-of-chaos</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 启用功能

在 Spring Boot 主类上添加注解：

```java
@SpringBootApplication
@EnableBladesOfChaos
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. 使用示例

#### 限流控制
```java
@RestController
public class UserController {
    
    @AccessLimiter(key = "getUserInfo", limit = 10, period = 60)
    @GetMapping("/user/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        // 每分钟最多调用 10 次
        return Result.success(userService.getById(id));
    }
}
```

#### 分布式锁
```java
@Service
public class OrderService {
    
    @ConcurrencyLock(key = "order:#{orderId}")
    public void processOrder(Long orderId) {
        // 基于订单ID的分布式锁，防止重复处理
    }
}
```

#### 敏感数据脱敏
```java
public class User {
    private String name;
    
    @Sensitive(type = SensitiveType.MOBILE)
    private String phone;
    
    @Sensitive(type = SensitiveType.ID_CARD)
    private String idCard;
}
```

#### 数据验证
```java
public class UserRequest {
    @CitizenId
    private String idCard;
    
    @Mobile
    private String phone;
}
```

## 技术栈

- **Java 21**
- **Spring Boot 3.1.5**
- **Maven** 依赖管理
- **Redis** 分布式缓存和锁
- **Lombok** 减少模板代码

## 构建命令

```bash
# 编译项目
./mvnw clean compile

# 运行测试
./mvnw test

# 构建JAR包
./mvnw clean package

# 安装到本地仓库
./mvnw clean install
```

## 项目结构

```
src/main/java/com/storyhasyou/kratos/
├── annotation/          # 核心注解定义
├── config/             # Spring 配置类
├── handler/            # AOP 处理器
├── utils/              # 工具类集合
├── result/             # 通用返回结果
└── dto/                # 数据传输对象
```

## 许可证

仅限个人使用！如需商业使用请联系作者。

## 联系方式

如有问题或建议，请联系：`fangxi.inori@gmail.com`

