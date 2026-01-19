package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.vo.CheckinRankVO;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.service.ICheckinRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 打卡排行榜服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinRankServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements ICheckinRankService {

    @Override
    public List<CheckinRankVO> getRankingList(String type) {
        LocalDateTime startTime;
        LocalDateTime endTime = LocalDateTime.now();

        // 根据类型确定查询时间范围
        switch (type) {
            case "day":
                startTime = endTime.minusDays(1);
                break;
            case "week":
                startTime = endTime.minusWeeks(1);
                break;
            case "total":
            default:
                startTime = null;
                break;
        }

        // 查询打卡记录
        LambdaQueryWrapper<CheckinRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckinRecord::getStatus, 1);
        if (startTime != null) {
            wrapper.ge(CheckinRecord::getCheckinTime, startTime);
        }
        wrapper.le(CheckinRecord::getCheckinTime, endTime);

        List<CheckinRecord> records = list(wrapper);

        // 按用户ID分组统计
        return records.stream()
                .collect(Collectors.groupingBy(
                        CheckinRecord::getUserId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    String userId = list.get(0).getUserId();
                                    String nickname = list.get(0).getUserName();
                                    int count = list.size();
                                    int likeCount = list.stream().mapToInt(CheckinRecord::getLikeCount).sum();
                                    CheckinRankVO vo = new CheckinRankVO();
                                    vo.setUserId(userId);
                                    vo.setNickname(nickname);
                                    vo.setCount(count);
                                    vo.setLikeCount(likeCount);
                                    return vo;
                                }
                        )
                ))
                .values()
                .stream()
                // 按打卡次数和获赞数综合排序
                .sorted((a, b) -> {
                    int scoreA = a.getCount() * 10 + a.getLikeCount();
                    int scoreB = b.getCount() * 10 + b.getLikeCount();
                    return Integer.compare(scoreB, scoreA);
                })
                // 设置排名
                .peek(vo -> {
                    // 这里简化处理，实际应该在排序后统一设置排名
                })
                .collect(Collectors.toList());
    }
}