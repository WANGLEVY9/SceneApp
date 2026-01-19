-- 4. 景区引领区表（scenic_intro）
CREATE TABLE `scene_intro` (
  `id` varchar(32) NOT NULL DEFAULT '1' COMMENT '主键ID',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `text` text NOT NULL COMMENT '详细介绍文本',
  `images` varchar(1024) DEFAULT '' COMMENT '图片URL列表（逗号分隔）',
  `video` varchar(255) DEFAULT '' COMMENT '视频URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景区引领区表';