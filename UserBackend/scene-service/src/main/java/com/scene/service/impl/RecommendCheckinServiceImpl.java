package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.po.CheckinSpot;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.vo.RecommendCheckinSpotVO;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.mapper.CheckinSpotMapper;
import com.scene.service.IRecommendCheckinService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐打卡点服务实现类
 */
@Service
public class RecommendCheckinServiceImpl implements IRecommendCheckinService {

    @Autowired
    private CheckinSpotMapper checkinSpotMapper;

    @Autowired
    private CheckinRecordMapper checkinRecordMapper;

    @Override
    public Page<RecommendCheckinSpotVO> getRecommendCheckinSpots(String sort, Integer page, Integer size, Double lat, Double lng) {
        // 查询所有打卡点
        QueryWrapper<CheckinSpot> spotQuery = new QueryWrapper<>();
        List<CheckinSpot> spotList = checkinSpotMapper.selectList(spotQuery);

        // 转换为VO并计算距离和热度
        List<RecommendCheckinSpotVO> voList = spotList.stream().map(spot -> {
            RecommendCheckinSpotVO vo = new RecommendCheckinSpotVO();
            BeanUtils.copyProperties(spot, vo);
            vo.setSpotId(spot.getId());

            // 计算距离
            if (lat != null && lng != null && spot.getLat() != null && spot.getLng() != null) {
                double distance = calculateDistance(lat, lng, spot.getLat(), spot.getLng());
                vo.setDistance(String.format("%.2f", distance) + "km");
            } else {
                vo.setDistance("未知");
            }

            // 计算热度（打卡次数）
            QueryWrapper<CheckinRecord> recordQuery = new QueryWrapper<>();
            recordQuery.eq("spot_id", spot.getId());
            Long checkinCount = Long.valueOf(checkinRecordMapper.selectCount(recordQuery));
            vo.setHot(checkinCount.intValue());

            return vo;
        }).collect(Collectors.toList());

        // 根据排序方式排序
        if ("hot".equals(sort)) {
            voList.sort((a, b) -> b.getHot().compareTo(a.getHot()));
        } else {
            // 默认按距离排序
            voList.sort((a, b) -> {
                try {
                    double distanceA = Double.parseDouble(a.getDistance().replace("km", ""));
                    double distanceB = Double.parseDouble(b.getDistance().replace("km", ""));
                    return Double.compare(distanceA, distanceB);
                } catch (NumberFormatException e) {
                    return 0;
                }
            });
        }

        // 分页处理
        Page<RecommendCheckinSpotVO> resultPage = new Page<>(page, size);
        int start = (page - 1) * size;
        int end = Math.min(start + size, voList.size());
        
        if (start < voList.size()) {
            resultPage.setRecords(voList.subList(start, end));
            resultPage.setTotal((long) voList.size());
        } else {
            resultPage.setRecords(List.of());
            resultPage.setTotal(0L);
        }

        return resultPage;
    }

    private double calculateDistance(Double lat, Double lng, BigDecimal lat1, BigDecimal lng1) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat);
        double lng2Rad = Math.toRadians(lng);

        // 地球半径（公里）
        double R = 6371;

        // 计算距离
        double dLat = lat2Rad - lat1Rad;
        double dLng = lng2Rad - lng1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * 计算两点之间的距离（单位：公里）
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // 地球半径（公里）
        double R = 6371;
        
        // 将经纬度转换为弧度
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lng2 - lng1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}