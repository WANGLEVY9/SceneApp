-- 10. 动态评论表（dynamic_comment）
CREATE TABLE `dynamic_comment` (
  `id` varchar(32) NOT NULL COMMENT '评论ID（雪花ID）',
  `checkin_id` varchar(32) NOT NULL COMMENT '对应的打卡ID（关联checkin_record.id）',
  `user_id` varchar(32) NOT NULL COMMENT '评论用户ID',
  `nickname` varchar(32) NOT NULL COMMENT '评论用户昵称（冗余）',
  `content` varchar(512) NOT NULL COMMENT '评论内容',
  `comment_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-已删除',
  PRIMARY KEY (`id`) -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态评论表';
