package com.scene.service;

import com.scene.domain.vo.CheckinRankVO;

import java.util.List;

/**
 * 打卡排行榜服务接口
 */
public interface ICheckinRankService {

    /**
     * 获取打卡排行榜
     * @param type 排行榜类型：day-日榜，week-周榜，total-总榜
     * @return 排行榜列表
     */
    List<CheckinRankVO> getRankingList(String type);
}