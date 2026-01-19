package com.scene.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.scene.common.exception.BadRequestException;
import com.scene.common.exception.ForbiddenException;
import com.scene.config.JwtProperties;
import com.scene.domain.dto.*;
import com.scene.domain.po.User;
import com.scene.domain.vo.UserVO;
import com.scene.enums.LoginType;
import com.scene.enums.UserStatus;
import com.scene.mapper.UserMapper;
import com.scene.service.IUserService;
import com.scene.utils.JwtTool;
import com.scene.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.scene.utils.Constants.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendCode(String phone) {
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        if (phoneInvalid) {
            throw new BadRequestException("手机号格式有误");
        }
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(LOGIN_LIMIT_KEY + phone))) {
            throw new BadRequestException("操作太频繁，请60秒后重试");
        }
        String code = RandomUtil.randomNumbers(6);
        // set key value ex 300
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(LOGIN_LIMIT_KEY + phone, "code", LOGIN_LIMIT_TTL, TimeUnit.MINUTES);
        log.debug("发送短信验证码成功，验证码：{}", code);
    }

    @Override
    public UserVO login(LoginFormDTO loginDTO) {
        User user = null;
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        String code = loginDTO.getCode();
        if (loginDTO.getLoginType() == LoginType.CODE) {
            user = loginOrRegisterByCode(loginKey, code);
        } else if (loginDTO.getLoginType() == LoginType.PASSWORD) {
            user = loginByPassword(loginKey, password);
        }
        // 校验是否禁用
        if (user.getStatus() == UserStatus.FROZEN) {
            throw new ForbiddenException("用户被冻结");
        }
        // 生成TOKEN
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 封装VO返回
        UserVO vo = buildUserVO(user, token);
        return vo;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BadRequestException("用户不存在");
        }
        return buildUserVO(user, null); // token 传 null，前端会忽略
    }

    @Override
    public void setupAccount(Long userId, SetupAccountDTO setupAccountDTO) {
        User user = getById(userId);
        if (user == null) throw new BadRequestException("用户不存在");
        if (StringUtils.hasText(user.getUsername()) || StringUtils.hasText(user.getPassword())) {
            throw new BadRequestException("已设置过用户名或密码，无法重复初次设置");
        }
        if (RegexUtils.isUsernameInvalid(setupAccountDTO.getUsername())) {
            throw new BadRequestException("用户名格式不合法");
        }
        if (isUsernameExist(setupAccountDTO.getUsername())) {
            throw new BadRequestException("用户名已被占用");
        }

        lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getUsername, setupAccountDTO.getUsername())
                .set(User::getPassword, passwordEncoder.encode(setupAccountDTO.getPassword()))
                .set(User::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public void changeUsername(Long userId, String username) {
        User user = getById(userId);
        if (user == null) throw new BadRequestException("用户不存在");
        if (RegexUtils.isUsernameInvalid(username)) {
            throw new BadRequestException("用户名格式不合法");
        }
        if (isUsernameExist(username)) {
            throw new BadRequestException("用户名已被占用");
        }

        lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getUsername, username)
                .set(User::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = getById(userId);
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("旧密码错误");
        }
        if (RegexUtils.isPasswordInvalid(changePasswordDTO.getNewPassword())) {
            throw new BadRequestException("密码格式不合法");
        }

        lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getPassword, passwordEncoder.encode(changePasswordDTO.getNewPassword()))
                .set(User::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
        User user = getById(userId);
        if (user == null) throw new BadRequestException("用户不存在");
        lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getAvatarUrl, updateProfileDTO.getAvatarUrl())
                .set(User::getNickname, updateProfileDTO.getNickname())
                .set(User::getUpdateTime, LocalDateTime.now())
                .update();
    }

    private User createUserWithPhone(String phone) {
        //1.创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setStatus(UserStatus.NORMAL);
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        //2.保存用户
        save(user);
        return user;
    }

    public User loginOrRegisterByCode(String phone, String code) {
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (code == null || !code.equals(cacheCode)) {
            throw new BadRequestException("验证码错误或已过期");
        }
        // 验证码通过，就删除（用完即失效）
        stringRedisTemplate.delete(LOGIN_CODE_KEY + phone);
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        if (user == null) {
            user = createUserWithPhone(phone);
        }
        return user;
    }

    public User loginByPassword(String loginKey, String password) {
        User user = null;
        if (!RegexUtils.isPhoneInvalid(loginKey)) {
            user = lambdaQuery().eq(User::getPhone, loginKey).one();
        } else if (!RegexUtils.isUsernameInvalid(loginKey)) {
            user = lambdaQuery().eq(User::getUsername, loginKey).one();
        }
        if (user == null) {
            throw new BadRequestException("用户名或手机号错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        return user;
    }

    private UserVO buildUserVO(User user, String token) {
        UserVO vo = new UserVO();
        vo.setUserId(user.getId());
        vo.setPhone(user.getPhone());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPoints(user.getPoints() == null ? 0 : user.getPoints());
        vo.setToken(token);
        return vo;
    }

    private boolean isUsernameExist(String username) {
        return lambdaQuery()
                .apply("username = {0} COLLATE utf8mb4_0900_as_cs", username)
                .count() > 0;
    }
}
