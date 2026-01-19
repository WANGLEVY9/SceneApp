-- 12. 积分记录表（points_record）
CREATE TABLE `points_record` (
  `id` varchar(32) NOT NULL COMMENT '记录ID（雪花ID）',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `type` varchar(32) NOT NULL COMMENT '变动类型：checkin-打卡，like-点赞，comment-评论，be_liked-被点赞，exchange-礼品兑换',
  `points` int NOT NULL COMMENT '变动积分（正数-增加，负数-减少）',
  `remark` varchar(128) DEFAULT '' COMMENT '备注（如：打卡点位XXX，兑换礼品XXX）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变动时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_create_time` (`create_time`) -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';