package com.scene.domain.dto;

import com.scene.enums.LoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "登录表单实体")
public class LoginFormDTO {
    @ApiModelProperty(value = "用户名/手机号", required = false)
    private String loginKey;

    @ApiModelProperty(value = "密码", required = false)
    private String password;

    @ApiModelProperty(value = "验证码", required = false)
    private String code;

    @ApiModelProperty(value = "登陆方式", required = true)
    @NotNull(message = "请选择登陆方式")
    private LoginType loginType;
}
