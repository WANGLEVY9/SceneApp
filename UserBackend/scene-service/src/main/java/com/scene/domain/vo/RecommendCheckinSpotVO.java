package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 推荐打卡点视图对象
 */
@Data
@ApiModel(value = "推荐打卡点视图对象", description = "推荐打卡点信息")
public class RecommendCheckinSpotVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡点ID")
    private String spotId;

    @ApiModelProperty(value = "打卡点名称")
    private String name;

    @ApiModelProperty(value = "距离")
    private String distance;

    @ApiModelProperty(value = "热度值")
    private Integer hot;
}