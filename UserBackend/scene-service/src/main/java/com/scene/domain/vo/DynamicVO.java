package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动态流视图对象
 */
@Data
@ApiModel(value = "动态流视图对象", description = "动态流信息")
public class DynamicVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡记录ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "打卡内容")
    private String content;

    @ApiModelProperty(value = "打卡照片URL")
    private String image;

    @ApiModelProperty(value = "打卡时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;
}