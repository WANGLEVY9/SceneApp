package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.vo.RecommendCheckinSpotVO;

import java.util.List;

/**
 * 推荐打卡点服务接口
 */
public interface IRecommendCheckinService {

    /**
     * 获取推荐打卡点列表
     *
     * @param sort 排序方式：distance-按距离排序，hot-按热度排序
     * @param page 页码
     * @param size 每页数量
     * @param lat 用户纬度
     * @param lng 用户经度
     * @return 分页后的推荐打卡点列表
     */
    Page<RecommendCheckinSpotVO> getRecommendCheckinSpots(String sort, Integer page, Integer size, Double lat, Double lng);
}