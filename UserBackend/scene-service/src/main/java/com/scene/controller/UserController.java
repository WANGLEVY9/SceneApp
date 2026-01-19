package com.scene.controller;


import com.scene.common.domain.R;
import com.scene.common.exception.BadRequestException;
import com.scene.common.utils.UserContext;
import com.scene.domain.dto.*;
import com.scene.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("api/client/auth")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("用户登录")
    @PostMapping("login")
    public R login(@RequestBody @Validated LoginFormDTO loginFormDTO){
        return R.ok(userService.login(loginFormDTO));
    }

    @ApiOperation("发送验证码")
    @PostMapping("send-code")
    public R sendCode(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        log.debug("phone:" + phone);
        return R.ok("发送验证码成功");
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping("/me")
    public R getUserInfo() {
        Long userId = UserContext.getUser();
        return R.ok(userService.getUserInfo(userId));
    }

    @ApiOperation("初次设置用户名和密码")
    @PostMapping("/setup-account")
    public R setupAccount(@RequestBody @Validated SetupAccountDTO setupAccountDTO) {
        userService.setupAccount(UserContext.getUser(), setupAccountDTO);
        return R.ok("设置用户名和密码成功");
    }

    @ApiOperation("修改用户名")
    @PutMapping("/username")
    public R changeUsername(@RequestParam("username") String username) {
        userService.changeUsername(UserContext.getUser(), username);
        return R.ok("修改用户名成功");
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public R changePassword(@RequestBody @Validated ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(UserContext.getUser(), changePasswordDTO);
        return R.ok("修改密码成功");
    }

    @ApiOperation("更新个人资料（昵称、头像）")
    @PutMapping("/profile")
    public R updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        userService.updateProfile(UserContext.getUser(), updateProfileDTO);
        return R.ok("更新个人资料成功");
    }



}

