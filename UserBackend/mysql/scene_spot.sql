-- 5. 景点表（scenic_spot）
CREATE TABLE `scene_spot` (
  `id` varchar(32) NOT NULL COMMENT '景点ID（雪花ID）',
  `name` varchar(64) NOT NULL COMMENT '景点名称',
  `lat` decimal(10,6) NOT NULL COMMENT '纬度',
  `lng` decimal(10,6) NOT NULL COMMENT '经度',
  `description` varchar(512) DEFAULT '' COMMENT '景点描述' -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';