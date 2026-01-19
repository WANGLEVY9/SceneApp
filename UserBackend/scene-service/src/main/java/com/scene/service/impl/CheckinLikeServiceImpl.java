package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.domain.dto.LikeDTO;
import com.scene.domain.po.CheckinLike;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.vo.LikeResultVO;
import com.scene.mapper.CheckinLikeMapper;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.service.ICheckinLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 打卡点赞服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinLikeServiceImpl extends ServiceImpl<CheckinLikeMapper, CheckinLike> implements ICheckinLikeService {

    private final CheckinRecordMapper checkinRecordMapper;

    @Override
    @Transactional
    public LikeResultVO likeCheckin(String userId, LikeDTO likeDTO) {
        // 1. 检查打卡记录是否存在
        CheckinRecord record = checkinRecordMapper.selectById(likeDTO.getCheckinId());
        if (record == null) {
            throw new RuntimeException("打卡记录不存在");
        }

        // 2. 检查是否已经点赞
        long count = count(new LambdaQueryWrapper<CheckinLike>()
                .eq(CheckinLike::getUserId, userId)
                .eq(CheckinLike::getCheckinId, likeDTO.getCheckinId()));

        if (count > 0) {
            throw new RuntimeException("您已经点赞过了");
        }

        // 3. 添加点赞记录
        CheckinLike like = new CheckinLike();
        like.setUserId(userId);
        like.setCheckinId(likeDTO.getCheckinId());
        save(like);

        // 4. 更新打卡记录的点赞数
        checkinRecordMapper.update(null, new LambdaUpdateWrapper<CheckinRecord>()
                .eq(CheckinRecord::getId, likeDTO.getCheckinId())
                .setSql("like_count = like_count + 1"));

        // 5. 返回结果
        LikeResultVO result = new LikeResultVO();
        result.setLikeCount(record.getLikeCount() + 1);
        result.setPoints(5); // 点赞获得5积分

        return result;
    }
}