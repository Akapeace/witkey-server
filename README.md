# witkey-server - 项目上下文文档

## 项目概述

**witkey-server(维特奇)** 是一个基于 Spring Boot 的多模块博客系统后端项目,采用前后端分离架构。

### 技术栈

- **核心框架**: Spring Boot 2.6.3
- **开发语言**: Java 1.8
- **构建工具**: Maven (多模块项目)
- **日志框架**: Logback + SLF4J
- **主要依赖**:
    - Lombok 1.18.28 (减少样板代码)
    - Guava 31.1-jre (Google核心库)
    - Commons-lang3 3.12.0 (Apache通用工具)
    - Jackson 2.15.2 (JSON处理)
    - Spring Validation (参数校验)

### 架构模式

- **分层架构**: Controller 层 → Service 层 → Repository 层
- **统一响应**: 所有接口返回统一的 Response 格式
- **统一异常处理**: GlobalExceptionHandler 捕获并处理所有异常
- **AOP 切面**: 使用 @ApiOperationLog 记录 API 操作日志
- **多模块设计**: 按功能职责划分模块,提高代码复用性

---

## 项目架构

### 模块划分

```
witkey-server (父工程)
├── witkey-web (前台Web模块)
│   └── 依赖: witkey-admin, witkey-common
├── witkey-admin (管理后台模块)
│   └── 依赖: witkey-common
└── witkey-common (公共模块)
    └── 独立模块,无其他依赖
```

### 模块职责

| 模块 | 职责 | 状态 |
|------|------|------|
| **witkey-web** | 前台展示功能,项目入口和打包模块 | ✅ 开发中 |
| **witkey-admin** | 管理后台相关功能 | ⚠️ 待开发(仅有 pom.xml) |
| **witkey-common** | 通用功能模块(异常处理、工具类、AOP等) | ✅ 完成 |

### 依赖关系

```
witkey-web
    ├── witkey-admin
    │       └── witkey-common
    └── witkey-common
```

**说明**: witkey-web 作为最上层模块,依赖 witkey-admin 和 witkey-common。witkey-admin 作为中间层,仅依赖 witkey-common。witkey-common 作为基础模块,不依赖其他业务模块。

---

## 构建和运行

### 环境要求

- JDK 1.8+
- Maven 3.6+

### 构建命令

```bash
# 清理并编译整个项目
mvn clean install

# 仅编译单个模块
cd witkey-web
mvn clean install
```

### 运行应用

```bash
# 方式1: 使用 Maven 插件运行
cd witkey-web
mvn spring-boot:run

# 方式2: 打包后运行
mvn clean package
java -jar witkey-web/target/witkey-web-1.0-SNAPSHOT.jar
```

### 访问地址

- **默认端口**: 8080
- **测试接口**: POST `http://localhost:8080/test`
- **健康检查**: GET `http://localhost:8080/actuator/health` (如果配置了 actuator)

### 多环境配置

项目支持多环境配置,通过 `spring.profiles.active` 切换:

- **开发环境**: `dev` (默认)
- **生产环境**: `prod`

配置文件位置:
- `witkey-web/src/main/resources/application.yml` (主配置)
- `witkey-web/src/main/resources/application-dev.yml` (开发环境)
- `witkey-web/src/main/resources/application-prod.yml` (生产环境)

**当前状态**: `application-dev.yml` 和 `application-prod.yml` 为空,待完善数据库、缓存等配置。

---

## 开发约定

### 代码结构规范

```
src/main/java/com/witkey/
├── [模块名]/
│   ├── controller/      # 控制器层
│   ├── service/         # 服务层
│   ├── repository/      # 数据访问层
│   ├── model/           # 数据模型
│   ├── dto/             # 数据传输对象
│   ├── vo/              # 视图对象
│   └── config/          # 配置类
```

### 统一响应格式

所有接口必须返回统一的 `Response<T>` 格式:

```java
{
    "success": true,      // 是否成功
    "message": "操作成功",  // 提示信息
    "errorCode": null,    // 错误码(失败时)
    "data": {}            // 返回数据
}
```

**使用方式**:

```java
// 成功响应(无数据)
return Response.success();

// 成功响应(带数据)
return Response.success(user);

// 失败响应(自定义消息)
return Response.fail("用户不存在");

// 失败响应(使用异常)
throw new BizException("USER_NOT_FOUND", "用户不存在");
```

**文件位置**: `witkey-common/src/main/java/com/witkey/common/utils/Response.java`

### 异常处理机制

#### 1. 自定义业务异常

```java
throw new BizException("USER_NOT_FOUND", "用户不存在");
```

**文件位置**: `witkey-common/src/main/java/com/witkey/common/exception/BizException.java`

#### 2. 响应码枚举

使用 `ResponseCodeEnum` 定义标准错误码:

```java
SYSTEM_ERROR("10000", "出错啦,后台小哥正在努力修复中...")
PARAM_NOT_VALID("10001", "参数错误")
PRODUCT_NOT_FOUND("20000", "该产品不存在(测试使用)")
```

**文件位置**: `witkey-common/src/main/java/com/witkey/common/enums/ResponseCodeEnum.java`

#### 3. 全局异常处理器

`GlobalExceptionHandler` 会自动捕获以下异常:
- `BizException`: 业务异常
- `MethodArgumentNotValidException`: 参数校验异常
- `Exception`: 通用异常

**文件位置**: `witkey-common/src/main/java/com/witkey/common/exception/GlobalExceptionHandler.java`

### API 操作日志

使用 `@ApiOperationLog` 注解记录 API 操作日志:

```java
@ApiOperationLog(description = "用户登录")
@PostMapping("/login")
public Response<User> login(@RequestBody LoginRequest request) {
    // 业务逻辑
}
```

**日志内容**:
- 请求开始时间
- 类名和方法名
- 请求参数
- 响应结果
- 执行耗时
- traceId (用于链路追踪)

**文件位置**:
- 注解: `witkey-common/src/main/java/com/witkey/common/aspect/ApiOperationLog.java`
- 切面: `witkey-common/src/main/java/com/witkey/common/aspect/ApiOperationLogAspect.java`

### 参数校验

使用 `javax.validation` 注解进行参数校验:

```java
@Data
public class User {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotNull(message = "性别不能为空")
    private Integer sex;

    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄必须大于或等于 18")
    @Max(value = 100, message = "年龄必须小于或等于 100")
    private Integer age;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

**在 Controller 中使用**:

```java
@PostMapping("/test")
public Response<String> test(@Valid @RequestBody User user) {
    // 如果校验失败,GlobalExceptionHandler 会自动处理
    return Response.success("测试成功");
}
```

---

## 核心功能模块

### witkey-common 模块

公共模块,提供通用功能,不依赖其他业务模块。

#### 1. 统一响应封装

**类**: `com.witkey.common.utils.Response`

**功能**:
- 统一 API 响应格式
- 提供成功和失败的快速构建方法

#### 2. 全局异常处理

**类**: `com.witkey.common.exception.GlobalExceptionHandler`

**功能**:
- 统一捕获和处理所有异常
- 自动转换为标准响应格式

**支持的异常类型**:
- `BizException`: 业务异常
- `MethodArgumentNotValidException`: 参数校验异常
- `Exception`: 通用异常

#### 3. 业务异常

**类**: `com.witkey.common.exception.BizException`

**功能**:
- 自定义业务异常
- 包含错误码和错误信息

#### 4. 响应码枚举

**类**: `com.witkey.common.enums.ResponseCodeEnum`

**功能**:
- 定义标准错误码
- 统一错误信息管理

#### 5. API 操作日志切面

**注解**: `com.witkey.common.aspect.ApiOperationLog`

**切面类**: `com.witkey.common.aspect.ApiOperationLogAspect`

**功能**:
- 使用 AOP 记录 API 操作日志
- 自动生成 traceId 用于链路追踪
- 记录请求参数、响应结果和执行耗时

#### 6. JSON 工具类

**类**: `com.witkey.common.utils.JsonUtil`

**功能**:
- 提供 JSON 序列化和反序列化方法
- 封装 Jackson ObjectMapper

---

### witkey-web 模块

前台 Web 模块,项目的入口和打包模块。

#### 1. 应用入口类

**类**: `com.witkey.web.WitkeyWebApplication`

```java
@SpringBootApplication
@ComponentScan({"com.witkey.*"})
public class WitkeyWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WitkeyWebApplication.class, args);
    }
}
```

**说明**:
- 使用 `@ComponentScan` 扫描所有 `com.witkey` 包下的组件
- 支持跨模块扫描

#### 2. 测试控制器

**类**: `com.witkey.web.controller.TestController`

**接口**: POST `/test`

**功能**:
- 测试统一响应格式
- 测试参数校验
- 测试异常处理机制
- 演示 `@ApiOperationLog` 的使用

#### 3. 用户模型

**类**: `com.witkey.web.model.User`

**功能**:
- 定义用户数据结构
- 包含完整的参数校验注解

---

### witkey-admin 模块

管理后台模块,当前状态:

**状态**: ⚠️ 待开发

**当前内容**:
- 仅有 `pom.xml` 文件
- 依赖 `witkey-common` 模块
- 无任何 Java 源代码

**预期功能**:
- 管理后台相关的控制器
- 管理后台相关的服务
- 管理后台相关的数据模型

---

## 技术栈详情

### Spring Boot 2.6.3

**核心特性**:
- 自动配置
- 内嵌 Tomcat 服务器
- Spring MVC 支持
- Spring AOP 支持

**主要 Starter**:
- `spring-boot-starter-web`: Web 开发
- `spring-boot-starter-aop`: AOP 支持
- `spring-boot-starter-validation`: 参数校验
- `spring-boot-starter-test`: 测试支持

### Java 1.8

**使用的特性**:
- Lambda 表达式
- Stream API
- Optional 类
- Date/Time API

### Maven

**多模块管理**:
- 父工程使用 `<modules>` 管理子模块
- 使用 `<dependencyManagement>` 统一管理依赖版本

**构建生命周期**:
- `clean`: 清理构建输出
- `compile`: 编译源代码
- `test`: 运行测试
- `package`: 打包
- `install`: 安装到本地仓库

### 日志框架

**Logback + SLF4J**:

**日志配置文件**: `witkey-web/src/main/resources/logback-spring.xml`

**日志格式**:
```
[TraceId: %X{traceId}] %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
```

**日志输出**:
- **开发环境**: 控制台输出
- **生产环境**: 文件输出到 `/app/witkey/logs/`

**日志轮转**:
- 按天分割
- 保留 30 天
- 单文件最大 10MB

---

## 配置说明

### 主配置文件

**文件位置**: `witkey-web/src/main/resources/application.yml`

**当前配置**:
```yaml
spring:
  profiles:
    active: dev
server:
  port: 8080
```

**说明**:
- 默认使用 `dev` 环境
- 服务端口为 8080

### 环境配置文件

**开发环境**: `application-dev.yml`
**生产环境**: `application-prod.yml`

**当前状态**: ⚠️ 两个文件都为空

**待添加配置**:
- 数据库连接配置(MySQL、PostgreSQL等)
- Redis 缓存配置
- 邮件服务配置
- 文件上传配置
- 第三方服务配置(如 OSS、短信等)

### 日志配置

**文件位置**: `witkey-web/src/main/resources/logback-spring.xml`

**配置要点**:
- 使用 Spring Profile 区分环境
- 开发环境: 控制台输出,使用彩色日志
- 生产环境: 文件输出,包含 ERROR 级别单独文件
- 支持链路追踪(traceId)

---

## 关键文件清单

### 配置文件

| 文件路径 | 说明 |
|---------|------|
| `pom.xml` | 父工程 POM 文件,管理依赖版本 |
| `witkey-common/pom.xml` | 公共模块依赖配置 |
| `witkey-web/pom.xml` | Web 模块依赖配置 |
| `witkey-admin/pom.xml` | 管理模块依赖配置 |
| `witkey-web/src/main/resources/application.yml` | 主配置文件 |
| `witkey-web/src/main/resources/application-dev.yml` | 开发环境配置 |
| `witkey-web/src/main/resources/application-prod.yml` | 生产环境配置 |
| `witkey-web/src/main/resources/logback-spring.xml` | 日志配置 |

### 核心代码文件

#### witkey-common 模块

| 文件路径 | 说明 |
|---------|------|
| `src/main/java/com/witkey/common/utils/Response.java` | 统一响应封装类 |
| `src/main/java/com/witkey/common/exception/GlobalExceptionHandler.java` | 全局异常处理器 |
| `src/main/java/com/witkey/common/exception/BizException.java` | 业务异常类 |
| `src/main/java/com/witkey/common/exception/BaseExceptionInterface.java` | 异常接口定义 |
| `src/main/java/com/witkey/common/enums/ResponseCodeEnum.java` | 响应码枚举 |
| `src/main/java/com/witkey/common/aspect/ApiOperationLog.java` | API 操作日志注解 |
| `src/main/java/com/witkey/common/aspect/ApiOperationLogAspect.java` | API 操作日志切面 |
| `src/main/java/com/witkey/common/utils/JsonUtil.java` | JSON 工具类 |

#### witkey-web 模块

| 文件路径 | 说明 |
|---------|------|
| `src/main/java/com/witkey/web/WitkeyWebApplication.java` | 应用入口类 |
| `src/main/java/com/witkey/web/controller/TestController.java` | 测试控制器 |
| `src/main/java/com/witkey/web/model/User.java` | 用户模型 |

#### witkey-admin 模块

| 文件路径 | 说明 |
|---------|------|
| (无 Java 源代码) | ⚠️ 待开发 |

---

## 注意事项

### ⚠️ 待开发模块

1. **witkey-admin 模块**
    - 当前仅有 `pom.xml` 文件
    - 需要添加管理后台相关的控制器、服务和模型
    - 建议参考 witkey-web 模块的结构

2. **数据库配置**
    - `application-dev.yml` 和 `application-prod.yml` 为空
    - 需要添加数据库连接配置
    - 建议配置数据库连接池(HikariCP)

3. **业务功能开发**
    - 项目目前只有测试代码
    - 需要根据博客系统需求开发核心功能:
        - 用户管理
        - 文章管理
        - 评论管理
        - 标签和分类管理
        - 文件上传

4. **权限认证**
    - 当前未实现用户认证和权限控制
    - 建议集成 Spring Security 或 JWT

5. **API 文档**
    - 未集成 API 文档工具
    - 建议集成 Swagger/Knife4j

6. **单元测试**
    - 当前仅有测试接口,无单元测试
    - 需要添加完整的单元测试和集成测试

### 测试接口说明

**接口**: POST `/test`

**功能**: 测试统一响应、参数校验和异常处理

**测试数据**:
```json
{
    "username": "test",
    "sex": 1,
    "age": 25,
    "email": "test@example.com"
}
```

**预期行为**:
- 参数校验通过: 返回成功响应
- 参数校验失败: 返回参数错误
- 触发除零异常: 返回系统错误(测试用)

**日志输出**: 会记录操作日志,包含 traceId、入参、出参和执行耗时

---

## 开发建议

### 代码风格

1. 使用 Lombok 减少样板代码(`@Data`, `@Getter`, `@Setter`, `@Slf4j` 等)
2. 统一使用 `Response` 作为接口返回类型
3. 业务逻辑抛出 `BizException`,由全局处理器统一处理
4. 使用 `@ApiOperationLog` 记录关键操作
5. 参数校验使用 `@Valid` 和 JSR-303 注解

### 模块依赖原则

1. **witkey-common** 不依赖任何业务模块
2. **witkey-admin** 只依赖 **witkey-common**
3. **witkey-web** 可以依赖 **witkey-admin** 和 **witkey-common**
4. 避免循环依赖

### 日志记录

1. 使用 `@ApiOperationLog` 记录 API 操作
2. 使用 `@Slf4j` 记录业务日志
3. 使用 traceId 进行链路追踪
4. 区分日志级别(ERROR, WARN, INFO, DEBUG)

### 异常处理

1. 业务异常使用 `BizException`
2. 参数校验异常由全局处理器自动处理
3. 避免捕获后不处理的异常
4. 错误码统一在 `ResponseCodeEnum` 中管理

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/Akapeace/witkey-server.git
cd witkey-server
```

### 2. 构建项目

```bash
mvn clean install
```

### 3. 运行应用

```bash
cd witkey-web
mvn spring-boot:run
```

### 4. 测试接口

```bash
curl -X POST http://localhost:8080/test \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "sex": 1,
    "age": 25,
    "email": "test@example.com"
  }'
```

---

## 项目状态

- ✅ 项目结构搭建完成
- ✅ 公共模块开发完成
- ✅ 统一响应和异常处理机制完成
- ✅ API 操作日志功能完成
- ⚠️ 业务功能开发中
- ⚠️ 管理后台模块待开发
- ⚠️ 数据库配置待完善
- ⚠️ 权限认证待实现
- ⚠️ API 文档待集成
- ⚠️ 单元测试待补充

---

## 版本信息

- **项目名称**: witkey-server (维特奇)
- **当前版本**: 1.0-SNAPSHOT
- **Spring Boot**: 2.6.3
- **Java**: 1.8
- **最后更新**: 2026-03-05

---

## 联系方式

- **GitHub**: https://github.com/Akapeace/witkey-server
- **项目文档**: 见 README.md

---

