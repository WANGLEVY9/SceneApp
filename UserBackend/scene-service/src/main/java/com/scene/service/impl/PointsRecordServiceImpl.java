package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.po.PointsRecord;
import com.scene.domain.vo.PointsRecordVO;
import com.scene.mapper.PointsRecordMapper;
import com.scene.service.IPointsRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsRecordServiceImpl implements IPointsRecordService {

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Override
    public Page<PointsRecordVO> getPointsRecords(Long userId, Integer page, Integer size) {
        // 创建查询条件
        QueryWrapper<PointsRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        // 分页查询
        Page<PointsRecord> pointsRecordPage = new Page<>(page, size);
        Page<PointsRecord> resultPage = pointsRecordMapper.selectPage(pointsRecordPage, queryWrapper);
        
        // 转换为VO
        List<PointsRecordVO> voList = resultPage.getRecords().stream().map(pointsRecord -> {
            PointsRecordVO vo = new PointsRecordVO();
            BeanUtils.copyProperties(pointsRecord, vo);
            return vo;
        }).collect(Collectors.toList());
        
        // 封装返回结果
        Page<PointsRecordVO> voPage = new Page<>(page, size);
        voPage.setRecords(voList);
        voPage.setTotal(resultPage.getTotal());
        
        return voPage;
    }
}