# Scene 管理端前台（AdminFrontend）

基于 Vue 3 + Vite 的后台管理界面，面向运营人员提供景点、商家、打卡点、礼品与兑换记录的可视化管理，与后端 `/api/admin/**` 接口对接。

## 技术栈
- Vue 3、Vite、Vue Router。
- Axios（集中封装于 `src/admin/api/index.js`），本地存储 token 并在拦截器中注入 `Authorization: Bearer <token>`。
- CSS：`src/assets/base.css`、`src/assets/main.css`，可按需扩展。

## 功能模块
- 登录与会话：`LoginView`（保存 `admin_token` 到 localStorage）。
- 数据总览：`DashboardView`。
- 景区内容：`IntroView`（景区介绍）、`ScenicSpotsView`（景点/景区点）、`MerchantsView`（商家）。
- 打卡管理：`CheckinSpotsView`（打卡点）、`CheckinRecordsView`（打卡记录）。
- 礼品与兑换：`GiftsView`、`GiftExchangesView`（兑换单状态更新）。
- 路由：主路由见 `src/router/index.js`，子路由见 `src/admin/router.js`，默认跳转 `/admin`。

## 配置
- API 基址：优先读取环境变量 `VITE_API_BASE`，否则默认 `/api/admin` 以配合 Vite 代理转发。示例：`VITE_API_BASE=http://localhost:8080/api/admin`。
- 运行前请确保 AdminBackend 已启动并开放对应基址。

## 本地开发与构建
```bash
npm install               # 安装依赖（推荐 Node.js 18+）
npm run dev               # 启动开发服务器，默认 http://localhost:5173
npm run build             # 生产构建，输出到 dist/
npm run preview           # 本地预览生产包（可选）
```

## 演示要点（功能/架构/过程）
- 功能完备：覆盖所有后台 CRUD 流程，与后端 JWT 鉴权联动。
- 架构清晰：路由-视图-API 分层，Axios 拦截器统一注入 token；可通过 `.env` 注入后端地址，满足非功能需求（跨域、环境隔离）。
- 过程产物：代码 + 构建产物 `dist/`；可展示与 AdminBackend 的端到端联调作为证据。
