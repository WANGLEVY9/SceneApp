CREATE TABLE IF NOT EXISTS `user`
(
    `id`          bigint                                                  NOT NULL AUTO_INCREMENT,
    `username`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '用户名（唯一）',
    `password`    varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
    `phone`       varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '注册手机号',
    `nickname`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '用户昵称',
    `avatar_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像URL',
    `points`      int                                                     DEFAULT '0' COMMENT '用户积分（默认0）',
    `create_time` datetime                                                NOT NULL COMMENT '创建时间',
    `update_time` datetime                                                NOT NULL COMMENT '更新时间',
    `status`      int                                                     DEFAULT '1' COMMENT '使用状态（1正常 2冻结）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8mb3
  ROW_FORMAT = COMPACT COMMENT ='用户表';
