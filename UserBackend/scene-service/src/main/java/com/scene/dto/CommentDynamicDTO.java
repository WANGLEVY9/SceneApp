package com.scene.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 评论动态请求DTO
 */
@Data
@ApiModel(value = "评论动态请求DTO", description = "评论动态请求参数")
public class CommentDynamicDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "动态ID（打卡记录ID）", required = true)
    @NotBlank(message = "动态ID不能为空")
    private String dynamicId;

    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空")
    private String content;
}