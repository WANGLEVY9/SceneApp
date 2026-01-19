package com.scene.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.domain.PageDTO;
import com.scene.common.utils.BeanUtils;
import com.scene.domain.dto.SceneMerchantQueryDTO;
import com.scene.domain.po.SceneMerchant;
import com.scene.domain.vo.SceneMerchantVO;
import com.scene.mapper.SceneMerchantMapper;
import com.scene.service.ISceneMerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家服务实现类
 */
@Slf4j
@Service
public class SceneMerchantServiceImpl extends ServiceImpl<SceneMerchantMapper, SceneMerchant> implements ISceneMerchantService {

    @Override
    public PageDTO<SceneMerchantVO> queryMerchants(SceneMerchantQueryDTO queryDTO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<SceneMerchant> wrapper = new LambdaQueryWrapper<>();

        // 2. 分页查询
        Page<SceneMerchant> result = page(queryDTO.toMpPage(), wrapper);

        // 3. 转换为VO并计算距离
        List<SceneMerchantVO> voList = result.getRecords().stream().map(sceneMerchant -> {
            SceneMerchantVO vo = BeanUtils.copyBean(sceneMerchant, SceneMerchantVO.class);

            // 设置位置信息
            SceneMerchantVO.LocationVO location = new SceneMerchantVO.LocationVO();
            location.setLat(sceneMerchant.getLat());
            location.setLng(sceneMerchant.getLng());
            vo.setLocation(location);

            // 兼容前端字段（直出经纬度与描述）
            vo.setLat(sceneMerchant.getLat());
            vo.setLng(sceneMerchant.getLng());
            vo.setDesc(sceneMerchant.getDescription());

            // 设置热度值
            vo.setHot(sceneMerchant.getHot());

            // 计算距离
            if (queryDTO.getLat() != null && queryDTO.getLng() != null) {
                double distance = calculateDistance(
                        queryDTO.getLat(), queryDTO.getLng(),
                        sceneMerchant.getLat(), sceneMerchant.getLng()
                );
                vo.setDistance(formatDistance(distance));
                vo.setDistanceValue(distance);
            } else {
                vo.setDistance("未知");
                vo.setDistanceValue(Double.MAX_VALUE);
            }

            return vo;
        }).collect(Collectors.toList());

        // 4. 根据排序条件进行内存排序
        if (StrUtil.isNotBlank(queryDTO.getSort())) {
            if ("distance".equals(queryDTO.getSort())) {
                voList.sort(Comparator.comparing(SceneMerchantVO::getDistanceValue));
            } else if ("hot".equals(queryDTO.getSort())) {
                // 使用VO中的热度值进行排序
                voList.sort((a, b) -> Integer.compare(
                        b.getHot() != null ? b.getHot() : 0,
                        a.getHot() != null ? a.getHot() : 0
                ));
            }
        }

        // 5. 创建新的Page对象，包含排序后的数据
        Page<SceneMerchantVO> sortedPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        sortedPage.setRecords(voList);

        // 6. 使用PageDTO的of方法创建返回结果
        return PageDTO.of(sortedPage, vo -> vo);
    }

    /**
     * 计算两点之间的距离（单位：米） 使用Haversine公式计算球面两点间距离
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
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 地球半径（米）
        double earthRadius = 6371000;

        return earthRadius * c;
    }

    /**
     * 格式化距离显示
     */
    private String formatDistance(double distance) {
        if (distance < 1000) {
            return BigDecimal.valueOf(distance).setScale(0, RoundingMode.HALF_UP) + "m";
        } else {
            return BigDecimal.valueOf(distance / 1000).setScale(1, RoundingMode.HALF_UP) + "km";
        }
    }

}
