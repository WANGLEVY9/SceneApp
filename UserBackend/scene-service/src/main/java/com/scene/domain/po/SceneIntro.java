package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 景区引领区实体类
 */
@Data
@TableName("scene_intro")
public class SceneIntro {
    /**
     * 主键ID
     */
    @TableId
    private String id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 详细介绍文本
     */
    private String text;
    
    /**
     * 图片URL列表（逗号分隔）
     */
    private String images;
    
    /**
     * 视频URL
     */
    private String video;
}