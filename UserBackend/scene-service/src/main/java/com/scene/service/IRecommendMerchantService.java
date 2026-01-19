package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.vo.RecommendMerchantVO;

/**
 * 推荐商家服务接口
 */
public interface IRecommendMerchantService {

    /**
     * 获取推荐商家列表
     *
     * @param sort 排序方式：hot-按热度排序
     * @param page 页码
     * @param size 每页数量
     * @param lat 用户纬度
     * @param lng 用户经度
     * @return 分页后的推荐商家列表
     */
    Page<RecommendMerchantVO> getRecommendMerchants(String sort, Integer page, Integer size, Double lat, Double lng);
}