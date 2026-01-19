package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家实体类
 */
@Data
@TableName("scene_merchant")
public class SceneMerchant {
    /**
     * 商家ID（雪花ID）
     */
    @TableId
    private String id;
    
    /**
     * 商家名称
     */
    private String name;
    
    /**
     * 联系方式（电话）
     */
    private String contact;
    
    /**
     * 纬度
     */
    private BigDecimal lat;
    
    /**
     * 经度
     */
    private BigDecimal lng;
    
    /**
     * 商家描述
     */
    private String description;
    
    /**
     * 热度值（用于排序）
     */
    private Integer hot;
}