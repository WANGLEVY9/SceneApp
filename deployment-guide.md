一、Docker Compose 一键部署
1. 目录准备
● 在 SceneApp 根目录创建 docker/ 目录（用于存放构建产物与 compose 文件）。
● 先本地构建产物：
  ○ 后端：在各自目录执行 mvn -DskipTests clean package，产物位于 scene-service/target/scene-service-*.jar。
  ○ 管理端前台：cd AdminFrontend && npm install && npm run build，生成 dist/。
2. docker-compose.yml
将下列文件保存为 docker/docker-compose.yml（可按需调整端口/密码/路径）。
version: '3.9'
services:
  db:
    image: mysql:8.0
    container_name: scene-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: scene
    command: ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]
    ports:
      - "3306:3306"
    volumes:
      - ./mysql-init:/docker-entrypoint-initdb.d
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  user-backend:
    image: eclipse-temurin:17-jre
    container_name: scene-user-backend
    depends_on:
      db:
        condition: service_healthy
    volumes:
      - ./jars/user-backend.jar:/app/app.jar:ro
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/scene?useSSL=false&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    working_dir: /app
    command: ["java", "-jar", "app.jar"]
    ports:
      - "8080:8080"

  admin-backend:
    image: eclipse-temurin:17-jre
    container_name: scene-admin-backend
    depends_on:
      db:
        condition: service_healthy
    volumes:
      - ./jars/admin-backend.jar:/app/app.jar:ro
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/scene?useSSL=false&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SERVER_PORT: 8081
    working_dir: /app
    command: ["java", "-jar", "app.jar"]
    ports:
      - "8081:8081"

  admin-frontend:
    image: nginx:1.25
    container_name: scene-admin-frontend
    depends_on:
      - admin-backend
    volumes:
      - ./admin-frontend-dist:/usr/share/nginx/html:ro
      - ./nginx/conf.d:/etc/nginx/conf.d:ro
    ports:
      - "5173:80"
3. 目录与文件
● docker/mysql-init/：放置 SQL 初始化脚本（可拷贝 AdminBackend/mysql 或 UserBackend/mysql 目录下的脚本）。容器启动时自动导入。
● docker/jars/user-backend.jar：拷贝自 UserBackend/scene-service/target/scene-service-*.jar 并重命名。
● docker/jars/admin-backend.jar：拷贝自 AdminBackend/scene-service/target/scene-service-*.jar 并重命名。
● docker/admin-frontend-dist/：拷贝 AdminFrontend/dist/ 构建产物。
● docker/nginx/conf.d/default.conf：示例：
server {
  listen 80;
  root /usr/share/nginx/html;
  index index.html;

  location /api/admin/ {
    proxy_pass http://admin-backend:8081/api/admin/;
  }

  try_files $uri /index.html;
}
4. 启动与停止
cd docker
docker compose up -d  # 启动
docker compose logs -f  # 查看日志
docker compose down  # 停止并移除容器
5. 生产化可选项
● 替换 MySQL 密码、端口与数据卷持久化路径。
● 如需 HTTPS，可在 nginx 增加证书配置或前置负载均衡。
● 可将 user-backend 与 admin-backend 打成自定义镜像（FROM eclipse-temurin:17-jre）以减少卷挂载。
二、非容器化运行部署指南
● 保留本地直跑方案，便于开发/调试/课堂演示。
0. 通用准备
● JDK 11+、Maven 3.6+、Node.js 18+、MySQL 5.7/8.0。
● 数据库：执行 AdminBackend/mysql 或 UserBackend/mysql 下 SQL，建库 scene 并创建管理员账号（BCrypt 密码）。如缺 desc 列，执行文首补丁 SQL。
● 配置：在各自 scene-service/src/main/resources/application-dev.yaml 或 application-local.yaml 设置 spring.datasource.*、jwt、server.port（建议用户端 8080，管理端 8081）。
1. 用户端后端（UserBackend）
cd UserBackend
mvn -DskipTests clean package
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev
# 或 java -jar scene-service/target/scene-service-*.jar --spring.profiles.active=dev
● 可用环境变量覆盖：SPRING_DATASOURCE_URL/USERNAME/PASSWORD、SERVER_PORT、JWT_SECRET（对应配置文件字段）。
● 日志：默认按 logback 输出到 logs/（可在配置中调整路径/级别）。
2. 管理端后端（AdminBackend）
cd AdminBackend
mvn -DskipTests clean package
mvn -pl scene-service spring-boot:run -Dspring-boot.run.profiles=dev
# 或 java -jar scene-service/target/scene-service-*.jar --spring.profiles.active=dev
● 若与用户端同机运行，请确保端口不同（如 8081）。
3. 管理端前台（Vue 3 + Vite）
cd AdminFrontend
npm install
npm run dev                 # 开发，默认 http://localhost:5173
npm run build               # 生产构建，输出 dist/
● 开发态代理：在 .env 或命令行设置 VITE_API_BASE=http://localhost:8081/api/admin，避免 CORS。
● 生产部署：将 dist/ 放入任意静态服务器或 Nginx，反代到管理后端：
location /api/admin/ {
  proxy_pass http://localhost:8081/api/admin/;
}

try_files $uri $uri/ /index.html;
4. 用户端前台（HarmonyOS ArkTS）
● DevEco Studio：打开 UserFrontend，同步依赖，选 entry 模块 Run/Debug；真机需签名。
● 命令行：
cd UserFrontend
ohpm install
./hvigorw assembleDebug -p entry   # Windows 用 hvigorw.bat
● 产物：hap 位于 entry/build/outputs/。
● 后端地址：HttpUtil 默认 http://10.0.2.2:8080（模拟器访问宿主机），可在代码中调用 HttpUtil.setBaseUrl() 或预置配置改为实际用户端后端地址。
5. 反向代理与同域化（非容器化场景）
● 开发：AdminFrontend 用 Vite 代理；用户端直接请求后端。
● 生产：Nginx 统一域名示例：
server {
  listen 80;
  server_name example.com;
  root /var/www/admin-frontend;
  index index.html;

  location /api/admin/ { proxy_pass http://127.0.0.1:8081/api/admin/; }
  location /api/client/ { proxy_pass http://127.0.0.1:8080/api/client/; }
  try_files $uri $uri/ /index.html;
}
6. 端口与环境冲突排查
● 若 8080/8081 被占用，可在 application-*.yaml 或启动参数 --server.port= 中调整，同时同步前端基址。
● 确保 MySQL 已启动，且 spring.datasource.* 指向正确实例。
三、常见问题排查
● 启动报表缺失：确认已执行全量 SQL 脚本。
● 401 鉴权失败：检查 token 是否附带；管理端需要 Bearer 前缀。
● 跨域：开发期通过 Vite 代理，生产期用反向代理同域化。
● 端口占用：调整 server.port，并同步前端基址配置。