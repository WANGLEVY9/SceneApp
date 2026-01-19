package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 景区引领区介绍表实体
 */
@Data
@TableName("scene_intro")
public class SceneIntro {

    @TableId
    private String id;

    private String title;

    private String text;

    private String images;

    private String video;
}

