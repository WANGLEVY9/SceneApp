一、功能
● 用户侧闭环
  ○ 导览：景区介绍、景点/商家列表、距离展示与推荐接口预留地理参数。
  ○ 打卡：提交打卡、个人记录、排行榜，点赞/评论动态；积分规则（打卡 +10，点赞/评论 +1/+2，被点赞 +2）。
  ○ 积分与礼品：积分记录查询，礼品列表与兑换，兑换记录可回查。
  ○ 账号：短信验证码登录、首次设置用户名/密码、资料/密码/用户名修改。
● 管理侧闭环
  ○ 内容运营：景区介绍、景点/商家、打卡点 CRUD。
  ○ 打卡与兑换管理：打卡记录查询/删除，礼品新增/删除，兑换记录状态更新。
  ○ 登录鉴权：管理员登录/退出，JWT 鉴权。
● 前端实现
  ○ 移动端：HarmonyOS ArkTS，多页面（首页/导览、打卡、礼品、我的），Http 工具封装 token 与错误处理。
  ○ 管理端：Vue 3 + Vite，路由-API-视图分层，Axios 拦截器自动注入 Bearer token，表格/表单 CRUD。
● 亮点/扩展
  ○ 预留推荐/排序参数，可接入 LLM/大数据做个性化推荐。
  ○ 积分规则明确，便于扩展活动运营。
二、架构
● 后端
  ○ 双服务：UserBackend（/api/client/）、AdminBackend（/api/admin/），公共模块 scene-common 复用统一返回体、异常处理、工具。
  ○ 鉴权与可扩展性：JWT 无状态设计 + 拦截器 ThreadLocal（防串扰）；SnowflakeIdUtil 提供高并发下的分布式唯一 ID，避免数据库自增瓶颈；分层架构 controller/service/mapper。
  ○ 高可用/高并发措施：
    - 无会话状态：JWT + ThreadLocal，便于水平扩容与无粘滞负载均衡。
    - 雪花 ID：`SnowflakeIdUtil` 以时间戳+数据中心+机器位+序列号生成 ID，`synchronized` 保证并发安全，减少主键冲突与锁竞争。
    - 读写分离/连接池（可扩展）：基于 Spring + MyBatis，可配置数据源池与只读副本；现有配置预留数据源扩展点。
    - 幂等与校验：打卡/兑换等关键操作通过业务校验与统一异常处理，降低重复提交带来的数据不一致风险。
  ○ 配置分环境：application.yaml + application-dev/local.yaml，支持端口与数据源灵活切换。
● 前端
  ○ 管理端：Axios 封装、环境变量 VITE_API_BASE；路由子模块化。
  ○ 移动端：HttpUtil 统一基址/token/错误处理，preferences 存储 token。
● 部署
  ○ 提供 Docker Compose 一键部署（MySQL + 双后端 + Nginx 前端），也保留非容器化方案；Nginx 反代示例同域化。
● 非功能性
  ○ 性能目标：核心接口 <300ms，列表 <500ms，管理端首屏 <2s。
  ○ 安全：JWT + 角色隔离，输入校验，HTTPS 建议；日志与错误统一结构。
  ○ 观测：logback 输出，关键链路可追踪；健康检查可扩展。
三、过程
● 团队分工
  ○ 用户端后端、管理端后端、移动端前端、管理端前端、文档撰写、测试运维、部署调试
● 工具与流程
  ○ Git 代码管理，Maven/Vite/hvigor 构建，DevEco Studio & VS Code 开发；SQL 脚本支撑测试回放。
  ○ 迭代：原型（Lo/Mid/Hi-Fi）→ 开发 → 联调 → 演示；需求/接口在 README 与 API 文档同步。
四、产物
● 代码：四端代码（UserBackend/AdminBackend/AdminFrontend/UserFrontend）完备。
● 文档：
  ○ 架构与指南：根 README、各端 README、deployment-guide（含 Compose）、api-reference、non-functional-requirements。
  ○ 需求/原型/团队：requirements-usecases、prototype-notes、team-allocation。
● 部署产物：
  ○ 后端 jar（scene-service-*.jar）、管理端前台 dist/、移动端 hap（entry/build/outputs/）。
  ○ Docker Compose 目录结构与示例配置。
五、答辩演示建议
● 路线：管理端登录 → 新增景点/礼品 → 用户端登录 → 打卡/点赞/评论 → 查看积分与兑换礼品 → 管理端查看/更新兑换记录。
● 证据点：
  ○ 日志：展示 logback 输出与错误处理；
  ○ 架构：说明双后端 + 公共模块，JWT 无状态、雪花 ID；
  ○ 非功能：性能目标与可扩展接口；
  ○ 产物：出示 jar/dist/hap 与 Compose 文件夹。