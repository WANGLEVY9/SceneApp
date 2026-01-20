积分获取规则：
打卡+10分，给他人点赞+1分，评论+2分，被点赞+2分。

## 后端数据库初始化（管理端）
- 确认 `application.yaml` 指向的数据库为 `scene`（默认端口 3306，账号 `root`，密码取自 `hm.db.pw`）。
- 在本地 MySQL 中执行 `mysql/admin.sql` 以及同目录下的其他表结构脚本，保证 `admin` 表存在；脚本已带 `CREATE DATABASE scene` 和 `USE scene`。
- 需要登录管理端时，请手动插入管理员账号：先用 BCrypt 生成密码（示例：`new BCryptPasswordEncoder().encode("123456")`），然后执行：
	`INSERT INTO admin (username, password) VALUES ('13900001111', '<BCrypt_Encoded_Password>');`
- 表缺失会导致 MyBatis 查询报错 `Table 'scene.admin' doesn't exist`，确保上述建表和插入完成后再启动服务。

## 常见数据库缺列修复
- 若访问礼品、景点、商家模块时报 `Unknown column 'desc' in 'field list'`，说明旧表缺少 `desc` 列，执行以下 SQL 后重启服务：
	```sql
	USE scene;
	ALTER TABLE gift_info ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER image;
	ALTER TABLE scene_spot ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
	ALTER TABLE scene_merchant ADD COLUMN IF NOT EXISTS `desc` varchar(512) DEFAULT '' AFTER lng;
	```