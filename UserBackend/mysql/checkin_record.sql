-- 8. 打卡记录表（checkin_record）
CREATE TABLE `checkin_record` (
  `id` varchar(32) NOT NULL COMMENT '打卡记录ID（雪花ID）',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `user_name` varchar(64) NOT NULL COMMENT '用户名称（冗余）',
  `spot_id` varchar(32) NOT NULL COMMENT '打卡点ID',
  `spot_name` varchar(64) NOT NULL COMMENT '打卡点名称（冗余）',
  `image` varchar(255) DEFAULT '' COMMENT '打卡照片URL',
  `remark` varchar(512) DEFAULT '' COMMENT '打卡备注',
  `lat` decimal(10,6) NOT NULL COMMENT '用户打卡时纬度',
  `lng` decimal(10,6) NOT NULL COMMENT '用户打卡时经度',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `checkin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-违规（已删除）' -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';