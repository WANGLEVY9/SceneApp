package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.scene.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 吴少
 * @since 2025-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user") // 对应数据库中的user表
public class User implements Serializable {

    // 序列化版本号，固定值保证序列化/反序列化兼容性
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO) // 对应数据库自增主键id
    private Long id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码，BCrypt加密存储
     */
    private String password;

    /**
     * 注册手机号（唯一）
     */
    private String phone;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像URL
     * 数据库字段是avatar_url，MyBatis-Plus默认下划线转驼峰，无需额外注解
     */
    private String avatarUrl;

    /**
     * 用户积分（默认0）
     */
    private Integer points;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 使用状态（1正常 2冻结）
     * 枚举类型，MyBatis-Plus会自动映射枚举的code值（int）到数据库
     */
    private UserStatus status;

}