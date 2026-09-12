# DemoJ

一个基于 Spring Boot + MyBatis-Plus + Redis + JWT 的后台管理系统，前端使用 React + Vite + Ant Design。

## 项目简介

DemoJ 是一个前后端分离的后台管理项目，提供用户、角色、权限、操作日志、缓存监控等模块的完整增删改查能力，并基于 JWT 实现无状态认证，基于 Redis 实现接口限流与缓存。

## 项目架构

```
baseAdmin
├── demoJ/                 # 后端（Spring Boot）
│   └── src/main/java/com/crise/demoj
│       ├── annotation/    # 自定义注解（@RateLimit、@OperationLog）
│       ├── aspect/        # AOP 切面（限流、操作日志）
│       ├── config/        # 配置类（Redis、Swagger、MyBatis-Plus、Web）
│       ├── controller/    # 控制器
│       ├── dao/           # 实体 + Mapper
│       ├── dto/           # 请求/响应对象
│       ├── exception/     # 异常处理
│       ├── middlewares/   # JWT 拦截器
│       ├── service/       # 业务逻辑
│       └── utils/         # 工具类
└── demoJ-web/             # 前端（React + Vite + Ant Design）
    └── src
        ├── api/           # 接口请求封装
        ├── components/    # 通用组件
        ├── pages/         # 页面
        └── types/         # 类型定义
```

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
| --- | --- | --- |
| Java | 1.8 | 运行环境 |
| Spring Boot | 2.7.6 | 基础框架 |
| MyBatis-Plus | 3.5.3.1 | ORM 框架 |
| MySQL | 8.0.32 | 数据库 |
| Redis | - | 缓存 / 限流 |
| JWT (jjwt) | 0.9.1 | 无状态认证 |
| springdoc-openapi-ui | 1.7.0 | Swagger 接口文档 |
| Hutool | 5.8.9 | 工具类 |

### 前端

| 技术 | 版本 |
| --- | --- |
| React | 19 |
| Vite | 8 |
| Ant Design | 6 |
| TypeScript | 6 |
| React Router | 7 |

## 环境要求

- JDK 1.8+
- Maven 3.6+
- Node.js 18+（前端）
- MySQL 8.0+
- Redis

## 快速开始

### 1. 初始化数据库

创建数据库并依次执行 `src/main/resources/sql/` 下的脚本：

```bash
mysql -u root -p -e "CREATE DATABASE demodb DEFAULT CHARACTER SET utf8mb4;"
```

按顺序导入：

1. `user.sql` — 用户、角色、用户角色关联表
2. `migration_v2.sql` — 权限、日志表及基础权限数据
3. `migration_v3.sql` — 缓存监控权限（已有库升级用，可跳过）
4. `init_admin.sql` — 初始化管理员账号及权限关联

### 2. 修改配置

编辑 `src/main/resources/application.yaml`，配置数据库、Redis、JWT：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/demodb?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: demo_user
    password: 123456
  redis:
    host: 127.0.0.1
    port: 6379
    password:
```

### 3. 启动后端

```bash
cd demoJ
mvn spring-boot:run
```

或打包运行：

```bash
mvn clean package
java -jar target/demoJ-0.0.1-SNAPSHOT.jar
```

后端默认端口 `8080`（见 `application.properties`）。

### 4. 启动前端

```bash
cd demoJ-web
npm install
npm run dev
```

前端开发服务器默认端口 `3000`，已配置代理将 `/admin`、`/role`、`/permission`、`/cache` 转发到 `http://localhost:8080`。

### 默认账号

| 用户名 | 密码 | 角色 |
| --- | --- | --- |
| admin | 123456 | 管理员（拥有全部权限） |

## Swagger 文档

项目已集成 springdoc-openapi（`OpenApiConfig`），启动后端后即可访问：

- Swagger UI：<http://localhost:8080/swagger-ui.html>（或 <http://localhost:8080/swagger-ui/index.html>）
- OpenAPI JSON：<http://localhost:8080/v3/api-docs>

Swagger 已配置 JWT 鉴权方案，接口调试时点击右上角 `Authorize` 按钮，填入登录接口返回的 token 即可（无需加 `Bearer` 前缀，框架会自动拼接）。

Swagger 相关路径已在 `WebConfig` 中排除 JWT 拦截，无需登录即可访问文档页面。

## 主要功能模块

| 模块 | 请求路径 | 说明 |
| --- | --- | --- |
| 用户管理 | `/admin/**` | 注册、登录、用户增删改查、分配角色 |
| 角色管理 | `/role/**` | 角色增删改查、批量删除 |
| 权限管理 | `/permission/**` | 权限资源管理、给角色分配权限 |
| 缓存监控 | `/cache/**` | Redis 信息、键值管理、清空缓存 |

## 配置说明

| 配置项 | 说明 |
| --- | --- |
| `jwt.secret` | JWT 签名密钥 |
| `jwt.accessExpiration` | access token 有效期（秒） |
| `jwt.refreshThreshold` | 距过期剩余多少秒时自动续期 |
| `jwt.tokenHead` | token 前缀，默认 `Bearer` |
