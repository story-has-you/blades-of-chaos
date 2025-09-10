# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个名为 "blades-of-chaos" 的 Java Spring Boot 工具库项目，提供常用的企业开发工具类和注解。项目使用 Gradle 管理依赖，基于 Java 21 和 Spring Boot 3.5.5。

## 构建和测试命令

### Gradle 命令
- `./gradlew clean compileJava` - 编译项目
- `./gradlew build` - 构建 JAR 包
- `./gradlew test` - 运行所有测试
- `./gradlew test --tests ClassNameTest` - 运行单个测试类
- `./gradlew test --tests ClassNameTest.methodName` - 运行单个测试方法
- `./gradlew publishToMavenLocal` - 编译、测试并安装到本地 Maven 仓库
- `./gradlew publish` - 发布到阿里云私有仓库
- `./gradlew tasks` - 查看所有可用任务

## 核心架构

### 包结构
- `com.storyhasyou.kratos.annotation` - 核心注解定义
  - `@EnableBladesOfChaos` - 启用工具库的主注解
  - `@AccessLimiter` - 基于 Redis 的限流注解
  - `@ConcurrencyLock` - 分布式并发锁注解
  - `@Sensitive` - 敏感数据序列化注解
  - 数据验证注解：`@CitizenId`, `@Mobile`

- `com.storyhasyou.kratos.config` - Spring 配置类
  - `BladesOfChaosConfig` - 主配置类，注册异常处理器、RestTemplate 等 Bean
  - `BladesOfChaosSelector` - 配置选择器，自动导入配置
  - `ThreadPoolConfiguration` - 线程池配置
  - `RedisConfig` - Redis 配置

- `com.storyhasyou.kratos.handler` - AOP 处理器
  - `accesslimiter/` - 限流功能的切面实现
  - `concurrencylock/` - 并发锁功能的切面实现

- `com.storyhasyou.kratos.utils` - 工具类集合
  - 常用工具：`BeanUtils`, `DateUtils`, `CollectionUtils`, `NumberUtils`
  - JSON 处理：`JacksonUtils`
  - 数据处理：`SensitiveUtils`, `ChineseUtils`, `IpUtils`
  - ID 生成：`IdUtils`, `Sequence`

- `com.storyhasyou.kratos.result` - 通用返回结果
  - `Result<T>` - 标准化 API 响应封装
  - `ResultCode` - 状态码枚举

- `com.storyhasyou.kratos.dto` - 数据传输对象
  - `PageRequest`, `PageResponse` - 分页支持
  - 针对 JPA 和 PageHelper 的分页构建器

### 主要特性
1. **限流控制** - 通过 `@AccessLimiter` 注解实现基于 Redis 的方法级限流
2. **分布式锁** - 通过 `@ConcurrencyLock` 注解实现 Redis 分布式锁
3. **敏感数据处理** - 通过 `@Sensitive` 注解自动脱敏序列化
4. **数据验证** - 提供身份证号、手机号等中国特色验证注解
5. **分页支持** - 同时支持 JPA 和 PageHelper 的分页实现
6. **统一响应** - `Result<T>` 提供标准化的 API 响应格式

### 项目启用方式
在 Spring Boot 主类上添加 `@EnableBladesOfChaos` 注解即可启用所有功能。

## 开发约定

### 代码风格
- 使用 Lombok 减少模板代码
- 遵循 Spring Boot 最佳实践
- 工具类使用静态方法设计
- 异常处理采用统一的 `BusinessException`

### 测试
- 测试类位于 `src/test/java/com/storyhasyou/kratos/utils/` 目录
- 测试类命名规范：`ClassNameTest`
- 主要测试工具类的核心方法

### 依赖管理
- 核心依赖：Spring Boot 3.5.5, Java 21
- 工具库：Hutool, Guava, Commons Lang3, OkHttp
- 可选依赖：JPA, Validation, Redis, PageHelper (使用 compileOnly 配置)

## Gradle 特定配置

### 依赖配置说明
- `implementation` - 核心必需依赖，会传递给使用者
- `compileOnly` - 可选依赖，编译时需要但不传递给使用者（原 Maven 的 optional 依赖）
- `testImplementation` - 测试依赖

### 构建特性
- 使用 Gradle 8.10.2
- 启用并行构建和配置缓存优化
- 支持源码 JAR 自动生成
- 禁用 Spring Boot 可执行 JAR（这是一个库项目）