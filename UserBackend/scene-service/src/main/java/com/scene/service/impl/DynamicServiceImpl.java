package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.po.CheckinLike;
import com.scene.domain.vo.DynamicVO;
import com.scene.mapper.CheckinLikeMapper;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.service.IDynamicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 动态流服务实现类
 */
@Service
public class DynamicServiceImpl implements IDynamicService {

    @Autowired
    private CheckinRecordMapper checkinRecordMapper;

    @Autowired
    private CheckinLikeMapper checkinLikeMapper;

    @Override
    public Page<DynamicVO> getDynamicFlow(Integer page, Integer size) {
        // 查询打卡记录（按时间倒序）
        QueryWrapper<CheckinRecord> recordQuery = new QueryWrapper<>();
        recordQuery.orderByDesc("checkin_time");
        Page<CheckinRecord> recordPage = checkinRecordMapper.selectPage(new Page<>(page, size), recordQuery);

        // 获取所有打卡记录的点赞数
        List<String> recordIds = recordPage.getRecords().stream()
                .map(CheckinRecord::getId)
                .collect(Collectors.toList());

        QueryWrapper<CheckinLike> likeQuery = new QueryWrapper<>();
        likeQuery.in("checkin_id", recordIds);
        List<CheckinLike> likeList = checkinLikeMapper.selectList(likeQuery);

        Map<String, Long> likeCountMap = likeList.stream()
                .collect(Collectors.groupingBy(CheckinLike::getCheckinId, Collectors.counting()));

        // 转换为VO
        List<DynamicVO> voList = recordPage.getRecords().stream().map(record -> {
            DynamicVO vo = new DynamicVO();
            BeanUtils.copyProperties(record, vo);
            
            // 设置点赞数
            Long likeCount = likeCountMap.get(record.getId());
            vo.setLikeCount(likeCount != null ? likeCount.intValue() : 0);
            
            return vo;
        }).collect(Collectors.toList());

        // 构建返回的分页对象
        Page<DynamicVO> resultPage = new Page<>();
        resultPage.setRecords(voList);
        resultPage.setTotal(recordPage.getTotal());
        resultPage.setCurrent(recordPage.getCurrent());
        resultPage.setSize(recordPage.getSize());
        resultPage.setPages(recordPage.getPages());

        return resultPage;
    }
}