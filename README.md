# Blades of Chaos

一个强大的 Java Spring Boot 工具库，集成了企业开发中常用的功能组件和工具类。基于 **JDK 21** 现代化特性构建，提供高性能、线程安全的企业级解决方案。

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
- 使用 **JDK 21 Record 类** 实现不可变分页对象
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

### ⚡ JDK 21 现代化特性
- **Record 类**：不可变 DTO 对象，线程安全，内存优化
- **Virtual Threads**：轻量级并发，支持数百万级并发任务
- **现代集合 API**：减少外部依赖，提升性能
- **Text Blocks**：优雅的多行字符串处理

## 快速开始

### 1. 添加依赖

#### Gradle
```groovy
dependencies {
    implementation 'com.storyhasyou.kratos:blades-of-chaos:3.3.7'
}
```

#### Maven
```xml
<dependency>
    <groupId>com.storyhasyou.kratos</groupId>
    <artifactId>blades-of-chaos</artifactId>
    <version>3.3.7</version>
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

#### JDK 21 分页 Record 类
```java
// 分页请求 - Record 类，不可变且线程安全
PageRequest pageRequest = PageRequest.of(1, 10);

// 分页响应 - 泛型 Record，支持 Builder 模式
PageResponse<User> pageResponse = PageResponse.<User>builder()
    .rows(userList)
    .current(1L)
    .limit(10L)
    .records(100L)
    .build();
```

#### Virtual Threads 高并发支持
```java
@Service
public class UserService {
    
    // I/O 密集型任务使用 Virtual Thread
    @Autowired
    @Qualifier("virtualThreadExecutor")
    private Executor virtualThreadExecutor;
    
    public void processUsers(List<Long> userIds) {
        userIds.forEach(id -> virtualThreadExecutor.execute(() -> {
            // 网络请求、数据库操作等 I/O 密集型任务
            processUserAsync(id);
        }));
    }
}
```

## 技术栈

- **Java 21** - 使用最新 LTS 版本，支持 Record、Virtual Threads 等现代特性
- **Spring Boot 3.5.5** - 企业级应用框架
- **Gradle 8.10.2** - 现代化构建工具，支持配置缓存
- **Redis** - 分布式缓存和锁实现
- **Jackson** - JSON 序列化，完美支持 Record 类

### JDK 21 特性优势
- **🚀 性能提升**：Record 类内存占用减少 30-50%
- **⚡ 高并发**：Virtual Threads 支持数百万级并发
- **🛡️ 线程安全**：不可变 Record 对象天然线程安全
- **📦 依赖精简**：减少外部依赖，使用 JDK 原生 API

## 构建命令

```bash
# 编译项目
./gradlew clean compileJava

# 运行测试
./gradlew test

# 构建JAR包
./gradlew build

# 安装到本地仓库
./gradlew publishToMavenLocal

# 发布到阿里云仓库
./gradlew publish

# 查看所有可用任务
./gradlew tasks
```

## 项目结构

```
src/main/java/com/storyhasyou/kratos/
├── annotation/          # 核心注解定义
├── config/             # Spring 配置类（含 Virtual Threads 支持）
├── handler/            # AOP 处理器
├── utils/              # 工具类集合（JDK 21 优化）
├── result/             # 通用返回结果
└── dto/                # Record 数据传输对象（JDK 21）
```

### 核心模块说明
- **annotation/** - 限流、锁、验证等注解
- **config/** - ThreadPoolConfiguration 提供 Virtual Thread 执行器
- **dto/** - 基于 Record 的不可变数据传输对象
- **utils/** - 现代化工具类，减少外部依赖
- **handler/** - AOP 切面处理，支持分布式功能

## 许可证

仅限个人使用！如需商业使用请联系作者。

## 联系方式

如有问题或建议，请联系：`fangxi.inori@gmail.com`

