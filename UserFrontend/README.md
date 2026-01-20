# Scene 用户端前台（HarmonyOS | ArkTS）

基于 HarmonyOS ArkTS 的移动端应用，提供景区导览、打卡、积分与礼品兑换等用户功能，对接用户后端 `/api/client/**`。

## 技术栈
- HarmonyOS NEXT（ArkTS）、Stage 模型。
- 网络：`@ohos.net.http` 封装于 `entry/src/main/ets/utils/http.ets`，支持 token 注入与统一错误处理。
- 数据存储：`@kit.ArkData.preferences` 存储 JWT token。
- 构建：hvigor、ohpm（依赖管理），DevEco Studio 运行调试。

## 目录结构
- `entry/src/main/ets/pages/`：核心页面
	- `Home.ets`/`Index.ets`：首页与导航入口
	- `Introduction.ets`：景区介绍
	- `Place.ets`/`Photo.ets`：打卡点位与照片记录
	- `Gift.ets`：礼品与兑换
	- `Ad.ets`：活动/广告位
	- `User.ets`：个人中心
- `entry/src/main/ets/api/`：后端 API 封装（用户、打卡、动态、礼品、商家、场景、打卡点等）。
- `entry/src/main/ets/utils/http.ets`：HTTP 工具，默认基础地址 `http://10.0.2.2:8080`（模拟器访问宿主机），支持 `HttpUtil.setBaseUrl()` 重写；`HttpUtil.setAppContext()` 需在 Ability 启动时调用以启用 token 存储。
- `entry/src/main/ets/models/`：DTO/VO 定义。
- `AppScope/`、`entry/src/main/resources/`：应用配置与资源。
- `hvigorfile.ts`、`oh-package*.json5`：构建与依赖声明。

## 运行与调试
### DevEco Studio
1) 打开仓库 `UserFrontend/`，等待同步 hvigor/ohpm 依赖。
2) 连接模拟器或真机，配置 signature（如需）。
3) 在 IDE 选择 `entry` 模块，Run/Debug 即可安装并启动。

### 命令行
```bash
cd UserFrontend
ohpm install                       # 安装依赖
./hvigorw assembleDebug -p entry   # Windows 使用 hvigorw.bat
```
生成的 hap 位于 `entry/build/outputs/`。

## 接口与鉴权
- 所有接口基于 `/api/client/**`，`HttpUtil` 会在请求头添加 `Authorization`（存储在 preferences 的 token）。
- 登录/验证码/资料更新接口参见 `entry/src/main/ets/api/userApi.ets`；打卡、动态、礼品等 API 分布在对应文件。

## 项目演示说明（功能/架构/过程）
- 功能：导览、打卡、动态、积分、礼品兑换完整用户闭环；与用户后端同一数据源联动。
- 架构：页面-API-工具分层，Http 工具集中处理基址、token 与错误；可展示非功能证据（token 缓存、错误提示、日志）。
- 过程与产物：DevEco 项目结构、hap 产物、ohpm 锁文件可作为交付与团队分工证明。
