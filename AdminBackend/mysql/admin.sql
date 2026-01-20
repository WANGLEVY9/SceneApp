-- 确保库已创建
CREATE DATABASE IF NOT EXISTS scene DEFAULT CHARACTER SET utf8mb4;
USE scene;

-- 管理端管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `username` varchar(32) NOT NULL COMMENT '登录用户名',
  `password` varchar(64) NOT NULL COMMENT '加密密码（BCrypt算法）',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理端管理员表';

-- 如需初始化账号，请先生成 BCrypt 密码后手动插入，例如：
-- INSERT INTO admin (username, password) VALUES ('13900001111', '<BCrypt_Encoded_Password>');