-- 6. 商家表（scenic_merchant）
CREATE TABLE `scene_merchant` (
  `id` varchar(32) NOT NULL COMMENT '商家ID（雪花ID）',
  `name` varchar(64) NOT NULL COMMENT '商家名称',
  `contact` varchar(32) DEFAULT '' COMMENT '联系方式（电话）',
  `lat` decimal(10,6) NOT NULL COMMENT '纬度',
  `lng` decimal(10,6) NOT NULL COMMENT '经度',
  `desc` varchar(512) DEFAULT '' COMMENT '商家描述',
  `hot` int NOT NULL DEFAULT 0 COMMENT '热度值（用于排序）' -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';