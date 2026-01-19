package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "登录成功返回信息")
public class UserVO {
    @ApiModelProperty("JWT令牌，后续请求携带在Header: Authorization: Bearer {token}")
    private String token;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("用户名（未设置则为空）")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像URL")
    private String avatarUrl;

    @ApiModelProperty("积分")
    private Integer points;
}