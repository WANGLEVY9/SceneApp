package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.utils.BeanUtils;
import com.scene.domain.po.CheckinSpot;
import com.scene.domain.vo.CheckinSpotVO;
import com.scene.mapper.CheckinSpotMapper;
import com.scene.service.ICheckinSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 打卡点位服务实现类
 */
@Service
@RequiredArgsConstructor
public class CheckinSpotServiceImpl extends ServiceImpl<CheckinSpotMapper, CheckinSpot> implements ICheckinSpotService {

    @Override
    public List<CheckinSpotVO> listSpots() {
        // 查询所有打卡点
        List<CheckinSpot> spots = list(new LambdaQueryWrapper<CheckinSpot>()
                .orderByDesc(CheckinSpot::getHot));

        // 转换为VO
        return spots.stream().map(spot -> {
            CheckinSpotVO vo = BeanUtils.copyBean(spot, CheckinSpotVO.class);

            // 设置位置信息
            CheckinSpotVO.LocationVO location = new CheckinSpotVO.LocationVO();
            location.setLat(spot.getLat());
            location.setLng(spot.getLng());
            vo.setLocation(location);

            // 直出关键字段，方便前端直接使用
            vo.setLat(spot.getLat());
            vo.setLng(spot.getLng());
            vo.setRadius(spot.getRadius());
            vo.setHot(spot.getHot());

            return vo;
        }).collect(Collectors.toList());
    }
}
