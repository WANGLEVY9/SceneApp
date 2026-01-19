-- 14. 礼品兑换记录表（gift_exchange_record）
CREATE TABLE `gift_exchange_record` (
  `id` varchar(32) NOT NULL COMMENT '兑换记录ID（雪花ID）',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `user_name` varchar(64) NOT NULL COMMENT '用户名称（冗余）',
  `gift_id` varchar(32) NOT NULL COMMENT '礼品ID',
  `gift_name` varchar(64) NOT NULL COMMENT '礼品名称（冗余）',
  `gift_number` int NOT NULL COMMENT '礼品数量', -- 修正：单引号→反引号（此处无特殊字符，不加也可，但统一格式更规范）
  `points` int NOT NULL COMMENT '兑换消耗积分',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `status` varchar(32) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待审核，approved-已核销，delivered-已发货，received-已收货，rejected-已拒绝'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼品兑换记录表';