package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.common.exception.BadRequestException;
import com.scene.common.exception.BizIllegalException;
import com.scene.domain.dto.GiftExchangeDTO;
import com.scene.domain.po.GiftExchangeRecord;
import com.scene.domain.po.GiftInfo;
import com.scene.domain.po.PointsRecord;
import com.scene.domain.po.User;
import com.scene.domain.vo.GiftExchangeRecordVO;
import com.scene.mapper.GiftExchangeRecordMapper;
import com.scene.mapper.GiftInfoMapper;
import com.scene.mapper.PointsRecordMapper;
import com.scene.mapper.UserMapper;
import com.scene.service.IGiftExchangeService;
import com.scene.util.SnowflakeIdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftExchangeServiceImpl implements IGiftExchangeService {

    @Autowired
    private GiftExchangeRecordMapper giftExchangeRecordMapper;
    
    @Autowired
    private GiftInfoMapper giftInfoMapper;
    
    @Autowired
    private PointsRecordMapper pointsRecordMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void exchangeGift(Long userId, GiftExchangeDTO giftExchangeDTO) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BadRequestException("用户不存在");
        }
        
        // 查询礼品信息
        GiftInfo giftInfo = giftInfoMapper.selectById(giftExchangeDTO.getGiftId());
        if (giftInfo == null) {
            throw new BadRequestException("礼品不存在");
        }
        
        // 检查礼品是否可兑换
        if (giftInfo.getStatus() != 1) {
            throw new BadRequestException("礼品当前不可兑换");
        }
        
        // 检查库存
        if (giftInfo.getStock() <= 0) {
            throw new BadRequestException("礼品库存不足");
        }
        
        // 检查用户积分是否足够
        if (user.getPoints() < giftInfo.getPoints()) {
            throw new BizIllegalException("积分不足");
        }
        
        // 扣减礼品库存
        giftInfo.setStock(giftInfo.getStock() - 1);
        giftInfoMapper.updateById(giftInfo);
        
        // 扣减用户积分
        user.setPoints(user.getPoints() - giftInfo.getPoints());
        userMapper.updateById(user);
        
        // 创建积分记录
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setId(SnowflakeIdUtil.generateId());
        pointsRecord.setUserId(userId.toString());
        pointsRecord.setType("exchange");
        pointsRecord.setPoints(-giftInfo.getPoints());
        pointsRecord.setRemark("兑换礼品：" + giftInfo.getName());
        pointsRecord.setCreateTime(LocalDateTime.now());
        pointsRecordMapper.insert(pointsRecord);
        
        // 创建兑换记录
        GiftExchangeRecord exchangeRecord = new GiftExchangeRecord();
        exchangeRecord.setId(SnowflakeIdUtil.generateId());
        exchangeRecord.setUserId(userId.toString());
        exchangeRecord.setUserName(user.getNickname() != null ? user.getNickname() : user.getUsername());
        exchangeRecord.setGiftId(giftInfo.getId());
        exchangeRecord.setGiftName(giftInfo.getName());
        exchangeRecord.setGiftNumber(1);
        exchangeRecord.setPoints(giftInfo.getPoints());
        exchangeRecord.setApplyTime(LocalDateTime.now());
        exchangeRecord.setStatus("pending");
        giftExchangeRecordMapper.insert(exchangeRecord);
    }

    @Override
    public Page<GiftExchangeRecordVO> getExchangeRecords(Long userId, Integer page, Integer size) {
        // 创建查询条件
        QueryWrapper<GiftExchangeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("apply_time");
        
        // 分页查询
        Page<GiftExchangeRecord> exchangeRecordPage = new Page<>(page, size);
        Page<GiftExchangeRecord> resultPage = giftExchangeRecordMapper.selectPage(exchangeRecordPage, queryWrapper);
        
        // 转换为VO
        List<GiftExchangeRecordVO> voList = resultPage.getRecords().stream().map(exchangeRecord -> {
            GiftExchangeRecordVO vo = new GiftExchangeRecordVO();
            BeanUtils.copyProperties(exchangeRecord, vo);
            return vo;
        }).collect(Collectors.toList());
        
        // 封装返回结果
        Page<GiftExchangeRecordVO> voPage = new Page<>(page, size);
        voPage.setRecords(voList);
        voPage.setTotal(resultPage.getTotal());
        
        return voPage;
    }
}