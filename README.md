# SceneApp（罗浮山景区导览）

三端一体的景区导览应用：
- 用户端后端（UserBackend）：Spring Boot 微服务，提供 `/api/client/**` 能力。
- 管理端后端（AdminBackend）：Spring Boot 管理服务，提供 `/api/admin/**` 能力。
- 管理端前台（AdminFrontend）：Vue 3 + Vite 后台 UI，运营人员可管理景点/商家/礼品/打卡数据。
- 用户端前台（UserFrontend）：HarmonyOS ArkTS App，提供导览、打卡、积分与礼品兑换体验。

## 技术总览
- 后端：Java 11+、Spring Boot、Spring Security + JWT、MyBatis / MyBatis-Plus、Maven、MySQL。
- 前端：Vue 3、Vite、Axios、Vue Router；HarmonyOS ArkTS（Stage）、hvigor、ohpm。
- 公共：`scene-common` 模块统一返回体、异常处理、工具；日志基于 logback。

## 端侧架构与能力
- 用户端后端（UserBackend）：认证、场景/商家/景点、打卡、动态、积分、礼品兑换等接口；详见 [UserBackend/README.md](UserBackend/README.md)。
- 管理端后端（AdminBackend）：管理员登录、景区介绍、景点/商家/打卡点、礼品、兑换记录、打卡记录管理；详见 [AdminBackend/README.md](AdminBackend/README.md)。
- 管理端前台（AdminFrontend）：登录态管理、仪表盘、景区内容、打卡与礼品配置等页面；详见 [AdminFrontend/README.md](AdminFrontend/README.md)。
- 用户端前台（UserFrontend）：导览、打卡、动态、积分与礼品兑换移动体验；详见 [UserFrontend/README.md](UserFrontend/README.md)。

## 快速上手（本地开发顺序示例）
1) 数据库：在 AdminBackend 或 UserBackend 的 `mysql/` 目录执行建表脚本，准备 `scene` 数据库与测试数据，创建管理员账号。
2) 后端：在各自根目录执行 `mvn -DskipTests clean package`，分别用 `spring-boot:run -Dspring-boot.run.profiles=dev` 启动用户/管理服务。
3) 管理前台：`cd AdminFrontend && npm install && npm run dev`，设置 `VITE_API_BASE` 指向管理后端。
4) 用户前台：`cd UserFrontend && ohpm install`，使用 DevEco Studio 运行 `entry` 模块，或 `./hvigorw assembleDebug -p entry` 生成 hap；如需模拟器访问宿主机，确保 `HttpUtil` 基址 `http://10.0.2.2:8080` 可达。

## 项目展示说明（评分维度对照）
- 功能（60%）：展示客户端导览/打卡/积分/兑换闭环、管理端可视化配置与后端接口联动，可补充 LLM/推荐策略或数据看板作为亮点。
- 架构（20%）：说明分层设计、公用模块复用、安全（JWT/拦截器）、日志与配置分环境化，提供接口/日志输出作为证据。
- 过程（10%）：展示开发分工（后端/前端/移动端）、使用的工具链（DevEco Studio、Vite、Maven）、代码与脚本同步。
- 产物（10%）：代码仓库、SQL 脚本、构建产物（jar/dist/hap）与文档（各子 README）。
