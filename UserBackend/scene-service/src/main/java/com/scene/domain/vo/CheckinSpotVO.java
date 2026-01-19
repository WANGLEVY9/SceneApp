package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 打卡点位视图对象
 */
@Data
@ApiModel(value = "打卡点位视图对象", description = "打卡点位信息")
public class CheckinSpotVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡点ID")
    private String id;

    @ApiModelProperty(value = "打卡点名称")
    private String name;

    @ApiModelProperty(value = "位置信息")
    private LocationVO location;

    @Data
    @ApiModel(value = "位置信息")
    public static class LocationVO implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "纬度")
        private BigDecimal lat;

        @ApiModelProperty(value = "经度")
        private BigDecimal lng;
    }
}