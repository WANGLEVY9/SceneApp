-- 9. 打卡点赞表（checkin_like）
CREATE TABLE `checkin_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(32) NOT NULL COMMENT '点赞用户ID',
  `checkin_id` varchar(32) NOT NULL COMMENT '打卡记录ID',
  PRIMARY KEY (`id`) -- 补充主键（原脚本缺失）
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡点赞表';