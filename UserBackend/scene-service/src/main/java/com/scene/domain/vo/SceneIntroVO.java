package com.scene.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 景区引领区VO
 */
@Data
public class SceneIntroVO {
    /**
     * 主键ID
     */
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
     * 图片URL列表
     */
    private List<String> images;
    
    /**
     * 视频URL
     */
    private String video;
}