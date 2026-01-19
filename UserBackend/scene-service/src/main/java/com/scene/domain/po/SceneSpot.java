package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 景点实体类
 */
@Data
@TableName("scene_spot")
public class SceneSpot {
    /**
     * 景点ID（雪花ID）
     */
    @TableId
    private String id;
    
    /**
     * 景点名称
     */
    private String name;
    
    /**
     * 纬度
     */
    private BigDecimal lat;
    
    /**
     * 经度
     */
    private BigDecimal lng;
    
    /**
     * 景点描述
     */
    private String description;
}