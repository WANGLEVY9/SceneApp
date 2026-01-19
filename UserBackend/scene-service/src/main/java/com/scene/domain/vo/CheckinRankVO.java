package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 打卡排行榜VO
 */
@Data
@ApiModel(value = "打卡排行榜VO", description = "打卡排行榜信息")
public class CheckinRankVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "打卡次数")
    private Integer count;

    @ApiModelProperty(value = "获赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "排名")
    private Integer rank;
}