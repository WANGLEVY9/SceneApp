package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 点赞结果VO
 */
@Data
@ApiModel(value = "点赞结果VO", description = "点赞结果")
public class LikeResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "获得积分")
    private Integer points;
}