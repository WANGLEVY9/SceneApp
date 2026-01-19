package com.scene.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 点赞请求DTO
 */
@Data
@ApiModel(value = "点赞请求DTO", description = "点赞请求参数")
public class LikeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡记录ID", required = true)
    @NotBlank(message = "打卡记录ID不能为空")
    private String checkinId;
}