package com.scene.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商家VO
 */
@Data
public class SceneMerchantVO {
    /**
     * 商家ID
     */
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
     * 位置信息
     */
    private LocationVO location;
    
    /**
     * 与用户当前位置的距离
     */
    private String distance;
    
    /**
     * 距离（原始数值，用于排序）
     */
    private transient Double distanceValue;
    
    /**
     * 热度值（用于排序）
     */
    private Integer hot;
    
    /**
     * 位置信息内部类
     */
    @Data
    public static class LocationVO {
        /**
         * 纬度
         */
        private BigDecimal lat;
        
        /**
         * 经度
         */
        private BigDecimal lng;
    }
}