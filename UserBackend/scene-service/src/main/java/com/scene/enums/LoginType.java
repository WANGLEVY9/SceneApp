package com.scene.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum LoginType {
    PASSWORD(0, "使用密码登录"),
    CODE(1, "使用验证码登录"),
    ;
    @EnumValue
    int value;
    String desc;

    LoginType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
