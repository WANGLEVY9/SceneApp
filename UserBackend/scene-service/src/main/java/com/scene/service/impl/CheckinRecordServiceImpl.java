package com.scene.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.domain.PageDTO;
import com.scene.common.utils.BeanUtils;
import com.scene.domain.dto.CheckinSubmitDTO;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.po.CheckinSpot;
import com.scene.domain.vo.CheckinRecordVO;
import com.scene.domain.vo.CheckinResultVO;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.mapper.CheckinSpotMapper;
import com.scene.service.ICheckinRecordService;
import com.scene.service.IUserService;
import com.scene.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 打卡记录服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinRecordServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements ICheckinRecordService {

    private final CheckinSpotMapper checkinSpotMapper;
    private final IUserService userService;

    @Override
    public CheckinResultVO submitCheckin(String userId, CheckinSubmitDTO submitDTO) {
        // 1. 查询打卡点信息
        CheckinSpot spot = checkinSpotMapper.selectById(submitDTO.getSpotId());
        if (spot == null) {
            throw new RuntimeException("打卡点不存在");
        }
        
        // 2. 获取用户信息
        Long userIdLong = Long.parseLong(userId);
        UserVO userInfo = userService.getUserInfo(userIdLong);

        // 2. GPS校验 - 计算用户位置与打卡点的距离
        double distance = calculateDistance(
                submitDTO.getLocation().getLat(), submitDTO.getLocation().getLng(),
                spot.getLat(), spot.getLng()
        );

        // 检查是否在有效范围内
        if (distance > spot.getRadius()) {
            throw new RuntimeException("距离打卡点太远，请在有效范围内打卡");
        }

        // 3. 创建打卡记录
        CheckinRecord record = new CheckinRecord();
        record.setId(generateId());
        record.setUserId(userId);
        // 优先使用用户名，如果没有则使用昵称
        String userName = StrUtil.isNotBlank(userInfo.getUsername()) ? userInfo.getUsername() : userInfo.getNickname();
        record.setUserName(userName);
        record.setSpotId(spot.getId());
        record.setSpotName(spot.getName());
        record.setImage(submitDTO.getImage());
        record.setRemark(StrUtil.isNotBlank(submitDTO.getRemark()) ? submitDTO.getRemark() : "");
        record.setLat(submitDTO.getLocation().getLat());
        record.setLng(submitDTO.getLocation().getLng());
        record.setLikeCount(0);
        record.setCheckinTime(LocalDateTime.now());
        record.setStatus(1);

        // 4. 保存打卡记录
        save(record);

        // 5. 更新打卡点热度
        checkinSpotMapper.update(null, new LambdaUpdateWrapper<CheckinSpot>()
                .eq(CheckinSpot::getId, spot.getId())
                .setSql("hot = hot + 1"));

        // 6. 返回结果
        CheckinResultVO result = new CheckinResultVO();
        result.setCheckinId(record.getId());
        result.setPoints(10); // 打卡获得10积分

        return result;
    }

    @Override
    public PageDTO<CheckinRecordVO> getPersonalRecords(String userId, Integer page, Integer size) {
        // 1. 构建查询条件
        LambdaQueryWrapper<CheckinRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckinRecord::getUserId, userId)
                .eq(CheckinRecord::getStatus, 1)
                .orderByDesc(CheckinRecord::getCheckinTime);

        // 2. 分页查询
        Page<CheckinRecord> result = page(new Page<>(page, size), wrapper);

        // 3. 封装返回结果
        return PageDTO.of(result, record -> {
            CheckinRecordVO vo = BeanUtils.copyBean(record, CheckinRecordVO.class);
            vo.setTime(record.getCheckinTime());
            return vo;
        });
    }

    /**
     * 计算两点之间的距离（单位：米）
     */
    private double calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        // 转换为弧度
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lng2Rad = Math.toRadians(lng2.doubleValue());

        // Haversine公式
        double dLat = lat2Rad - lat1Rad;
        double dLng = lng2Rad - lng1Rad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 地球半径（米）
        double earthRadius = 6371000;

        return earthRadius * c;
    }

    /**
     * 生成ID
     */
    private String generateId() {
        return System.currentTimeMillis() + "";
    }
}