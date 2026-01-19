-- 7. 打卡点位表（checkin_spot）
CREATE TABLE `checkin_spot` (
  `id` varchar(32) NOT NULL COMMENT '打卡点ID（雪花ID）',
  `name` varchar(64) NOT NULL COMMENT '打卡点名称',
  `lat` decimal(10,6) NOT NULL COMMENT '纬度',
  `lng` decimal(10,6) NOT NULL COMMENT '经度',
  `radius` int NOT NULL DEFAULT 200 COMMENT '有效半径（米）- GPS校验用',
  `hot` int NOT NULL DEFAULT 0 COMMENT '热度值（用于推荐）' -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡点位表';
