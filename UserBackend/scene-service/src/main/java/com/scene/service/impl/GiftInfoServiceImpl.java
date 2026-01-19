package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scene.domain.po.GiftInfo;
import com.scene.domain.vo.GiftInfoVO;
import com.scene.mapper.GiftInfoMapper;
import com.scene.service.IGiftInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftInfoServiceImpl implements IGiftInfoService {

    @Autowired
    private GiftInfoMapper giftInfoMapper;

    @Override
    public List<GiftInfoVO> getGiftList() {
        // 创建查询条件，只查询可兑换的礼品
        QueryWrapper<GiftInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        // 查询所有可兑换的礼品
        List<GiftInfo> giftInfoList = giftInfoMapper.selectList(queryWrapper);
        
        // 转换为VO
        return giftInfoList.stream().map(giftInfo -> {
            GiftInfoVO vo = new GiftInfoVO();
            BeanUtils.copyProperties(giftInfo, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}