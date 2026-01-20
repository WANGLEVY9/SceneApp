# 部署文档

> SceneApp 全套（用户端后端、管理端后端、管理端前台、用户端前台）部署与运行步骤。

## 前置依赖
- JDK 11+，Maven 3.6+。
- Node.js 18+，npm。
- MySQL 5.7/8.0。
- DevEco Studio（或命令行 hvigor/ohpm）用于 HarmonyOS 构建。
- 可选：Nginx/反向代理、HTTPS 证书。

## 数据库准备
1) 在 MySQL 创建数据库 `scene`（或自定义名称）。
2) 进入 AdminBackend 或 UserBackend 的 `mysql/` 目录，执行建表脚本（含 admin.sql、user.sql、scene_spot.sql 等）。
3) 手工插入管理员账号：使用 BCrypt 生成密码，执行 `INSERT INTO admin (username, password) VALUES ('13900001111', '<BCrypt_Encoded_Password>');`。
4) 如缺少 `desc` 列，执行：
```sql
USE scene;
ALTER TABLE gift_info ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER image;
ALTER TABLE scene_spot ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
ALTER TABLE scene_merchant ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
```

## 后端部署（两套服务）
### 通用配置
- 在各自 `scene-service/src/main/resources/application-*.yaml` 设置：
  - `spring.datasource.*` 指向 MySQL。
  - `jwt` 密钥、过期时间。
  - 端口：可分别设置，例如用户端 8080，管理端 8081。

### 构建与运行
```bash
# 用户端后端 UserBackend
cd UserBackend
mvn -DskipTests clean package
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev
# 或 java -jar scene-service/target/scene-service-*.jar --spring.profiles.active=dev

# 管理端后端 AdminBackend
cd ../AdminBackend
mvn -DskipTests clean package
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev
# 或 java -jar scene-service/target/scene-service-*.jar --spring.profiles.active=dev
```

## 管理端前台部署（Vue 3 + Vite）
```bash
cd AdminFrontend
npm install
# 开发
npm run dev                 # 默认 http://localhost:5173，需配置代理或 VITE_API_BASE 指向管理端后端
# 生产构建
npm run build               # 生成 dist/
```
- 生产上线：将 dist/ 部署至 Web 服务器或 CDN；如用 Nginx，配置反向代理到管理端后端 `/api/admin`。示例：
```
location /api/admin/ {
  proxy_pass http://localhost:8081/api/admin/;
}
```

## 用户端前台部署（HarmonyOS ArkTS）
- DevEco Studio：打开 `UserFrontend`，同步依赖，选择 `entry` 模块 Run/Debug。
- 命令行：
```bash
cd UserFrontend
ohpm install
./hvigorw assembleDebug -p entry   # Windows 用 hvigorw.bat
# 生成的 hap 位于 entry/build/outputs/
```
- 后端地址：`HttpUtil` 默认 `http://10.0.2.2:8080`；生产环境调用 `HttpUtil.setBaseUrl()` 或在构建前修改配置指向实际用户端后端地址。

## 反向代理与跨域建议
- 开发：AdminFrontend 默认以 Vite 代理或 VITE_API_BASE 直连后端；用户端通过直连后端，不依赖浏览器 CORS。
- 生产：建议统一域名/子路径，Nginx 反代 `/api/client` 到用户后端，`/api/admin` 到管理后端，静态前端部署于根路径或 `/admin/`。

## 日志与监控
- 后端 logback 输出至 `logs/`（可在配置中调整路径/级别）。
- 建议接入基础监控：JVM 指标、接口访问日志、慢查询；演示时可提供日志文件作为稳定性证据。

## 常见问题排查
- 启动报表缺失：确认已执行全量 SQL 脚本。
- 401 鉴权失败：检查 token 是否附带；管理端需要 `Bearer ` 前缀。
- 跨域：开发期通过 Vite 代理，生产期用反向代理同域化。
- 端口占用：调整 `server.port`，并同步前端基址配置。
