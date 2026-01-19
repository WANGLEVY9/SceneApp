package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.domain.PageDTO;
import com.scene.common.utils.BeanUtils;
import cn.hutool.core.util.StrUtil;

import com.scene.domain.dto.SceneSpotQueryDTO;
import com.scene.domain.po.SceneSpot;
import com.scene.domain.vo.SceneSpotVO;
import com.scene.mapper.SceneSpotMapper;
import com.scene.service.ISceneSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 景点服务实现类
 */
@Service
@RequiredArgsConstructor
public class SceneSpotServiceImpl extends ServiceImpl<SceneSpotMapper, SceneSpot> implements ISceneSpotService {

    @Override
    public PageDTO<SceneSpotVO> querySpots(SceneSpotQueryDTO queryDTO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<SceneSpot> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StrUtil.isNotBlank(queryDTO.getKeyword())) {
            wrapper.like(SceneSpot::getName, queryDTO.getKeyword());
        }
        
        // 2. 分页查询
        Page<SceneSpot> result = page(queryDTO.toMpPage(), wrapper);
        
        // 3. 封装返回结果
        return PageDTO.of(result, sceneSpot -> {
            SceneSpotVO vo = BeanUtils.copyBean(sceneSpot, SceneSpotVO.class);
            
            // 设置位置信息
            SceneSpotVO.LocationVO location = new SceneSpotVO.LocationVO();
            location.setLat(sceneSpot.getLat());
            location.setLng(sceneSpot.getLng());
            vo.setLocation(location);
            
            // 计算距离
            if (queryDTO.getLat() != null && queryDTO.getLng() != null) {
                double distance = calculateDistance(
                        queryDTO.getLat(), queryDTO.getLng(),
                        sceneSpot.getLat(), sceneSpot.getLng()
                );
                vo.setDistance(formatDistance(distance));
            } else {
                vo.setDistance("未知");
            }
            
            return vo;
        });
    }
    
    /**
     * 计算两点之间的距离（单位：千米）
     */
    private double calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        // 地球半径（千米）
        double R = 6371.0;
        
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lng2Rad = Math.toRadians(lng2.doubleValue());
        
        // 计算经纬度差
        double dLat = lat2Rad - lat1Rad;
        double dLng = lng2Rad - lng1Rad;
        
        // 使用Haversine公式计算距离
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        
        return distance;
    }
    
    /**
     * 格式化距离显示
     */
    private String formatDistance(double distance) {
        if (distance < 1) {
            // 小于1千米，显示米
            return String.format("%.0fm", distance * 1000);
        } else {
            // 大于等于1千米，显示千米，保留一位小数
            return String.format("%.1fkm", distance);
        }
    }
}