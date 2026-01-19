package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 商家实体，对应表 scene_merchant
 */
@Data
@TableName("scene_merchant")
public class SceneMerchant {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;

    private String contact;

    private Double lat;

    private Double lng;

    @TableField("`desc`") 
    private String desc;

    private Integer hot;
}

