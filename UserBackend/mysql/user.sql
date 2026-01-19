/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

-- 导出 scene的数据库结构（仅保留用户表相关）
CREATE DATABASE IF NOT EXISTS `scene` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION = 'N' */;
USE `scene`;

-- 导出  表 scene.user 结构
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          bigint                                                  NOT NULL AUTO_INCREMENT,
    `username`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '用户名（唯一）',
    `password`    varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码，加密存储',
    `phone`       varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '注册手机号',
    `nickname`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '用户昵称',
    `avatar_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像URL',
    `points`      int                                                     DEFAULT '0' COMMENT '用户积分（默认0）',
    `create_time` datetime                                                NOT NULL COMMENT '创建时间',
    `update_time` datetime                                                NOT NULL COMMENT '更新时间',
    `status`      int                                                     DEFAULT '1' COMMENT '使用状态（1正常 2冻结）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username` (`username`) USING BTREE,
    UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='用户表';

-- 清空用户表原有数据（避免重复插入）
DELETE
FROM `user`;

-- 插入测试用户数据（适配新字段：昵称/头像URL/积分）
INSERT INTO `user` (`id`, `username`, `password`, `phone`, `nickname`, `avatar_url`, `points`, `create_time`,
                    `update_time`, `status`)
VALUES (1, 'Jack', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112224', '杰克',
        'https://example.com/avatar/jack.jpg', 500, '2017-08-19 20:50:21', '2017-08-19 20:50:21', 1),
       (2, 'Rose', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112223', '露丝',
        'https://example.com/avatar/rose.jpg', 800, '2017-08-19 21:00:23', '2017-08-19 21:00:23', 1),
       (3, 'Hope', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112222', '霍普',
        'https://example.com/avatar/hope.jpg', 1200, '2017-08-19 22:37:44', '2017-08-19 22:37:44', 1),
       (4, 'Thomas', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '17701265258', '托马斯',
        'https://example.com/avatar/thomas.jpg', 300, '2017-08-19 23:44:45', '2017-08-19 23:44:45', 1);

/*!40103 SET TIME_ZONE = IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE = IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS = IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES = IFNULL(@OLD_SQL_NOTES, 1) */;