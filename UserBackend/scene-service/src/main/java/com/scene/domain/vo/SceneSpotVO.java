package com.scene.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 景点VO
 */
@Data
public class SceneSpotVO {

    /**
     * 景点ID
     */
    private String id;

    /**
     * 景点名称
     */
    private String name;

    /**
     * 兼容前端需要的经纬度
     */
    private BigDecimal lat;

    private BigDecimal lng;

    /**
     * 位置信息
     */
    private LocationVO location;

    /**
     * 与用户当前位置的距离
     */
    private String distance;

    /**
     * 景点描述
     */
    private String description;

    /**
     * 前端字段兼容
     */
    private String desc;

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
