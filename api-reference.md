# 接口文档（总览）

> 概览用户端（/api/client/**）与管理端（/api/admin/**）主要接口。默认返回体：`R { code, msg, data }`，成功 code=200。

## 通用说明
- 认证
  - 用户端：登录后返回 token，调用时在 Header 添加 `Authorization: <token>`。
  - 管理端：管理员登录后返回 token，调用时在 Header 添加 `Authorization: Bearer <token>`。
- 响应示例
```json
{
  "code": 200,
  "msg": "ok",
  "data": { /* 业务数据 */ }
}
```

## 用户端后端（/api/client/**）
- 认证
  - POST /auth/login — 密码/验证码登录
  - POST /auth/send-code?phone= — 发送短信验证码
  - GET  /auth/me — 获取当前用户信息
  - POST /auth/setup-account — 初次设置用户名+密码
  - PUT  /auth/username — 修改用户名
  - PUT  /auth/password — 修改密码
  - PUT  /auth/profile — 更新昵称/头像
- 场景/内容
  - GET /intro — 景区介绍
  - GET /merchants — 商家列表（分页/查询）
  - GET /scenic-spots — 景点/打卡点列表（分页/查询）
- 打卡与社交
  - GET  /checkin/spots — 打卡点列表
  - POST /checkin — 提交打卡
  - GET  /checkin/records — 个人打卡记录（分页）
  - POST /checkin/like — 点赞打卡
  - GET  /checkin/rank — 打卡排行榜（type=total 等）
- 动态与推荐
  - GET /recommend/checkin — 推荐打卡点（支持排序/地理参数）
  - GET /recommend/merchants — 推荐商家（支持排序/地理参数）
  - GET /dynamic — 动态流
  - POST /dynamic/comment — 评论动态
- 礼品与积分
  - GET  /gifts — 礼品列表
  - POST /gifts/exchange — 兑换礼品
  - GET  /gifts/records — 礼品兑换记录
  - GET  /points/records — 积分记录

## 管理端后端（/api/admin/**）
- 认证
  - POST /auth/login — 管理员登录
  - POST /auth/logout — 退出
- 景区信息
  - GET  /intro — 获取景区介绍
  - PUT  /intro — 更新景区介绍
- 景点/商家
  - GET  /scenic-spots — 列表
  - POST /scenic-spots — 新增
  - DELETE /scenic-spots/{id} — 删除
  - GET  /merchants — 列表
  - POST /merchants — 新增
  - DELETE /merchants/{id} — 删除
- 打卡管理
  - GET  /checkin/spots — 打卡点列表
  - POST /checkin/spots — 新增打卡点
  - DELETE /checkin/spots/{id} — 删除打卡点
  - GET  /checkin/records — 打卡记录列表
  - DELETE /checkin/records/{id} — 删除记录
- 礼品与兑换
  - GET  /gifts — 礼品列表
  - POST /gifts — 新增礼品
  - DELETE /gifts/{id} — 删除礼品
  - GET  /gifts/exchange — 兑换记录列表
  - PUT  /gifts — 更新兑换记录状态（通过查询参数传递）

## 前端调用要点
- 管理端前台（Vue）：
  - Axios 实例基址：`VITE_API_BASE` 或 `/api/admin`（通过 Vite 代理转发）。
  - 登录后将 token 写入 `localStorage.admin_token`，拦截器自动带上 `Authorization: Bearer <token>`。
- 用户端前台（HarmonyOS）：
  - HttpUtil 基址默认 `http://10.0.2.2:8080`，可调用 `setBaseUrl` 覆盖。
  - token 存储于 preferences（key: `token`），请求时自动附带 `Authorization`。

## 错误与状态码约定
- 200：成功。
- 4xx：参数/鉴权错误（含 401 未登录、403 无权限、404 资源不存在）。
- 5xx：服务器内部错误。
- 统一由全局异常处理返回标准结构，前端需根据 code/msg 做提示或跳转登录。
