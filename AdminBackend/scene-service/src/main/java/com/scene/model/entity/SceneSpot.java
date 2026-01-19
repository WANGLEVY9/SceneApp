package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 景点实体，对应表 scene_spot
 */
@Data
@TableName("scene_spot")
public class SceneSpot {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;

    private Double lat;

    private Double lng;

    @TableField("`desc`") 
    private String desc;
}

