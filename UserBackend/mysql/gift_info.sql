-- 13. 礼品表（gift_info）
CREATE TABLE `gift_info` (
  `id` varchar(32) NOT NULL COMMENT '礼品ID（雪花ID）',
  `name` varchar(64) NOT NULL COMMENT '礼品名称',
  `points` int NOT NULL COMMENT '兑换所需积分',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `image` varchar(255) DEFAULT '' COMMENT '礼品图片URL',
  `description` varchar(512) DEFAULT '' COMMENT '礼品描述',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-可兑换，0-不可兑换',
  PRIMARY KEY (`id`) -- 移除末尾逗号
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼品表';