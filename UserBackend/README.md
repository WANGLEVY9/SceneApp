# Scene 用户后端（UserBackend）

本文档旨在帮助为 南京大学软件学院 移动互联网软件工程 鸿蒙开发项目SceneApp 的用户后端模块（UserBackend）。

- 概览：这是一个基于 Spring Boot + MyBatis-Plus 的微服务模块，提供景区场景内用户相关功能（用户认证、打卡、动态、积分与礼品兑换等）。

仓库概要
- 根目录
  - `pom.xml` — 父级 Maven 项目描述（聚合/管理子模块）
  - `README.md` — 本文件
  - `logs/` — 运行时日志目录（通常为容器或本地运行时输出）
  - `mysql/` — 建库建表及测试数据 SQL 脚本
  - `scene-common/` — 公共模块（通用 DTO、返回体 R、异常处理、通用配置）
  - `scene-service/` — 用户后端服务模块（Spring Boot 应用代码与资源）

技术栈
- Java + Spring Boot
- MyBatis-Plus（数据访问）
- Maven（构建）
- MySQL（关系型数据库，脚本位于 `mysql/`）
- Swagger 注解（接口文档注解）

建议的运行时/工具版本
- JDK 11 或更高（项目未在 README 中声明特定版本，若有 CI/父 pom 指定请优先以pom为准）
- Maven 3.6+
- MySQL 5.7 / 8.0（视脚本和字段类型兼容）

本地快速启动
1. 准备数据库
   - 在本地新建一个 MySQL 实例并创建空数据库（例如：scene_user_backend）
   - 依次运行 `mysql/` 目录中的 SQL 脚本来创建表结构和测试数据。常见的执行顺序建议：
     1. `admin.sql`（管理/权限表）
     2. `user.sql`（用户表）
     3. `scene_intro.sql`, `scene_merchant.sql`, `scene_spot.sql`（场景/商家/景点）
     4. `checkin_spot.sql`, `checkin_record.sql`, `checkin_like.sql`（打卡相关）
     5. `dynamic_comment.sql`（动态评论）
     6. `gift_info.sql`, `gift_exchange_record.sql`（礼品相关）
     7. `points_record.sql`（积分记录）
     8. `*_test_data.sql`（可选的测试数据）

2. 配置应用（资源文件）
   - `scene-service/src/main/resources/` 包含：
     - `application.yaml`（默认配置）
     - `application-dev.yaml`（开发环境覆盖）
     - `application-local.yaml`（本地调试覆盖）
     - `hmall.jks`（可能用于证书/HTTPS，注意不要泄露私钥）
   - 推荐在 `application-local.yaml` 或 `application-dev.yaml` 中修改数据库连接、日志级别和端口号等。

3. 构建与运行
   - 在项目根目录（包含父 `pom.xml`）执行：

```powershell
# 跳过测试构建所有模块（可选）
mvn -DskipTests clean package

# 仅构建并运行 scene-service 模块（开发时常用）
mvn -pl scene-service -am -DskipTests clean package

# 使用 Spring Boot 插件直接运行（用 dev 配置运行）
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev

# 或直接运行打包的 jar（路径依据构建输出）
java -jar scene-service/target/scene-service-<version>.jar --spring.profiles.active=dev
```

注：`<version>` 以构建产物实际版本替换，或直接在 target 目录里查看 jar 名称。

主要模块与代码位置
- 公共模块（通用类型/工具）
  - `scene-common/src/main/java/com/scene/common` — 包含返回结构 `R`、分页工具 `PageDTO`、全局异常处理与通用配置
- 服务模块（业务实现）
  - `scene-service/src/main/java/com/scene` — Spring Boot 启动类 `SceneApplication.java` 与配置
  - 主要包：
    - `controller/` — REST 控制器（常用接口在此）
    - `service/` — 业务接口与实现（核心逻辑）
    - `mapper/` — MyBatis 映射（与数据库交互）
    - `domain/dto`、`domain/vo`、`domain/po` — 请求 DTO、响应 VO、持久化 PO
    - `config/` — 安全、Jwt、MVC 等框架配置
    - `interceptor/` — 请求拦截器（用于鉴权/上下文）

数据库脚本（`mysql/`）
- 本目录包含建表 SQL 与若干测试数据脚本。启动前请阅读 `*_test_guide.md`（如存在）以了解测试数据的导入顺序与说明。

认证与授权
- 多数客户端接口位于 `/api/client/**`。
- 用户登录相关接口位于 `/api/client/auth/**`（如登录、发送验证码、设置用户名/密码等）。
- 部分接口期望在请求头带上 `Authorization`（Bearer token）来识别用户；代码中有 `UserContext.getUser()` 调用，表明运行时会从请求上下文或拦截器中注入当前用户 ID。
- JWT/密钥配置位于 `scene-service/src/main/java/com/scene/config` 下的 `JwtProperties` / `AuthProperties`（请按实际配置文件确认密钥与过期时间）。

主要 REST 接口速览（按控制器分组）
- 用户（UserController）
  - POST  api/client/auth/login — 用户登录（RequestBody: LoginFormDTO）
  - POST  api/client/auth/send-code?phone={phone} — 发送短信验证码
  - GET   api/client/auth/me — 获取当前登录用户信息
  - POST  api/client/auth/setup-account — 初次设置用户名和密码
  - PUT   api/client/auth/username?username={username} — 修改用户名
  - PUT   api/client/auth/password — 修改密码（RequestBody: ChangePasswordDTO）
  - PUT   api/client/auth/profile — 更新昵称/头像等基本资料

- 场景（SceneController）
  - GET api/client/intro — 获取引领区（场景）详情
  - GET api/client/merchants — 获取商家列表（支持分页/查询参数）
  - GET api/client/scenic-spots — 获取景点/打卡点列表（支持分页/查询参数）

- 打卡与社交（CheckinController）
  - GET  /api/client/checkin/spots — 获取打卡点位列表
  - POST /api/client/checkin — 提交打卡（RequestBody: CheckinSubmitDTO）
  - GET  /api/client/checkin/records?page=&size= — 获取个人打卡记录（分页）
  - POST /api/client/checkin/like — 为打卡点赞（RequestBody: LikeDTO）
  - GET  /api/client/checkin/rank?type=total — 获取打卡排行榜

- 动态与推荐（DynamicController）
  - GET  /api/client/recommend/checkin?sort=&page=&size=&lat=&lng= — 推荐打卡点
  - GET  /api/client/recommend/merchants?sort=&page=&size=&lat=&lng= — 推荐商家
  - GET  /api/client/dynamic?page=&size= — 获取动态流（分页）
  - POST /api/client/dynamic/comment — 评论动态（RequestBody: CommentDynamicDTO，Header: Authorization）

- 礼品（GiftController）
  - GET  /api/client/gifts — 获取礼品列表
  - POST /api/client/gifts/exchange — 兑换礼品（RequestBody: GiftExchangeDTO）
  - GET  /api/client/gifts/records?page=&size= — 获取礼品兑换记录（分页）

- 积分（PointsController）
  - GET /api/client/points/records?page=&size= — 获取积分记录（分页）

返回结构
- 项目使用统一返回体 `com.scene.common.domain.R<T>`，成功通常用 `R.ok(...)`，错误通过抛出统一异常由 `CommonExceptionAdvice` 处理并返回标准化错误体。

日志与调试
- 本地运行时日志（如果按配置写入）位于仓库根 `logs/scene-service/spring.log`（可能由运行时配置决定）。
- 若出现启动失败或 Bean 注入错误：检查 `application-*.yaml` 中的数据源配置、JWT/密钥配置与端口冲突。

常见问题与排查建议
- 数据库连接异常：确认 MySQL 已启动并且 `application-local.yaml` 中的 `spring.datasource` 配置正确。
- 401/鉴权失败：检查请求是否包含 `Authorization`，并确认 Token 生成与解析逻辑（`JwtProperties`、拦截器）一致。
- Mapper/SQL 错误：查看 `scene-service/target/classes` 下的运行时日志与堆栈，确认 MyBatis 映射文件或注解映射与数据库表字段一致。

开发规范
- 在新增接口时，为 Controller 添加 Swagger 注解（@ApiOperation）以便自动生成接口文档。
- DTO/VO/PO 明确职责：Controller 层使用 DTO 接收参数，Service 返回 VO，Mapper 操作 PO（持久化对象）。
- 事务在 Service 层管理，尽量避免在 Controller 层处理事务。

贡献 & 联系
- 欢迎提交 PR（如仓库有贡献指南请遵循）。
- 若需快速沟通，请在代码仓库中查找维护者或在项目管理工具中联系负责人。
- 如有交流需要，请联系231250082@smail.nju.edu.cn

