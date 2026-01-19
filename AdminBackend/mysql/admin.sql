-- 2. 管理端管理员表（admin）
CREATE TABLE `admin` (
  `username` varchar(32) NOT NULL COMMENT '登录用户名',
  `password` varchar(64) NOT NULL COMMENT '加密密码（BCrypt算法）' -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理端管理员表';