# Scene 管理端后端（AdminBackend）

面向后台运营人员的 Spring Boot 服务，负责景区内容、礼品、打卡点等资源的管理，并向管理端前台提供 `/api/admin/**` 接口。

## 技术栈与架构
- Java 11+、Spring Boot、Spring Security（JWT）、MyBatis / MyBatis-Plus、Maven、MySQL。
- 分层结构：`controller`（REST 接口）→ `service`（业务）→ `mapper`（数据访问）→ MySQL；`config` 承载安全、JWT、MVC 配置，`interceptor` 用于请求拦截与鉴权。
- 公共能力抽离在 `scene-common`（统一返回体/异常处理/工具），后端两套服务共享，便于统一非功能需求（鉴权、日志、错误码）。

## 目录速览
- `pom.xml`：父模块聚合与依赖管理。
- `scene-common/`：通用模块（`R`、异常 advice、工具类等）。
- `scene-service/`：管理端服务主模块。
	- `SceneApplication.java`：启动类。
	- `config/`：`SecurityConfig`、`JwtProperties`、`AuthProperties`、`WebMvcConfig`。
	- `controller/admin/`：管理员接口入口（登录、景点/商家/礼品/打卡点、礼品兑换、系统介绍等）。
	- `service/`、`mapper/`、`model/dto|entity`：业务实现、数据库交互与数据模型。
	- `resources/`：`application.yaml`、`application-dev.yaml`、`logback.xml`。
- `mysql/`：建表及初始化脚本。

## 核心功能（接口分组）
- 认证：管理员登录、退出，基于 JWT 的请求头鉴权。
- 景区信息：场景介绍 CRUD、景点/商家/打卡点管理。
- 打卡记录：查询/删除打卡记录，配置打卡点。
- 礼品与兑换：礼品管理、兑换记录查询与状态更新。
- 数据一致性：基于统一返回体与异常处理保证接口幂等与可观测性。

## 数据库初始化
- 默认数据库名 `scene`，端口 3306，账号 `root`，密码读取自 `hm.db.pw`（在 `application*.yaml` 中配置）。
- 执行 `mysql/admin.sql` 及同目录其他脚本，脚本已包含 `CREATE DATABASE scene` 与 `USE scene`。
- 创建管理员账号：
	1) 使用 BCrypt 生成密码（示例：`new BCryptPasswordEncoder().encode("123456")`）。
	2) 执行：`INSERT INTO admin (username, password) VALUES ('13900001111', '<BCrypt_Encoded_Password>');`
- 若缺少 `desc` 列导致 `Unknown column 'desc'`，执行：
	```sql
	USE scene;
	ALTER TABLE gift_info ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER image;
	ALTER TABLE scene_spot ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
	ALTER TABLE scene_merchant ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
	```

## 本地运行步骤
1) 准备环境：JDK 11+、Maven 3.6+、MySQL 5.7/8.0；导入上文数据库脚本并创建管理员账号。
2) 配置：在 `scene-service/src/main/resources/application-dev.yaml` 或 `application-local.yaml` 中设置 `spring.datasource`、`jwt` 密钥与端口。
3) 构建与启动：
```powershell
mvn -DskipTests clean package
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev
# 或运行打包产物
java -jar scene-service/target/scene-service-*.jar --spring.profiles.active=dev
```
4) 访问接口：默认基路径 `/api/admin/**`，静态日志输出在 `logs/`（按 logback 配置）。

## 演示要点（功能/架构/过程）
- 功能：完整后台 CRUD 闭环（景点、商家、打卡点、礼品、兑换、打卡记录）+ JWT 鉴权，支撑管理端前台各页面。
- 架构：分层 + 公共模块复用，Security + JWT + 拦截器确保鉴权与可观测；logback + 统一异常提供非功能证据。
- 过程与产物：提供 SQL、代码、可运行 jar；结合管理端前台可展示端到端链路与团队分工（后端/前端/测试）。