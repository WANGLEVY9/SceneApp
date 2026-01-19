package com.scene.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author 吴少
 */
public class RegexUtils {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否是无效用户名格式
     * @param username 要校验的用户名
     * @return true:符合，false：不符合
     */
    public static boolean isUsernameInvalid(String username){
        return mismatch(username, RegexPatterns.USERNAME_REGEX);
    }
    /**
     * 是否是无效密码格式
     * @param password 要校验的用户名
     * @return true:符合，false：不符合
     */
    public static boolean isPasswordInvalid(String password){
        return mismatch(password, RegexPatterns.PASSWORD_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
