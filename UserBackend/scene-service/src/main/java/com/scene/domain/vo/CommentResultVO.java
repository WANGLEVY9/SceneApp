package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论结果视图对象
 */
@Data
@ApiModel(value = "评论结果视图对象", description = "评论结果信息")
public class CommentResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获得积分")
    private Integer points;
}