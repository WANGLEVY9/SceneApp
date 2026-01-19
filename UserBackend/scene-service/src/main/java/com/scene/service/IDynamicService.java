package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.vo.DynamicVO;

/**
 * 动态流服务接口
 */
public interface IDynamicService {

    /**
     * 获取动态流
     *
     * @param page 页码
     * @param size 每页数量
     * @return 分页后的动态流列表
     */
    Page<DynamicVO> getDynamicFlow(Integer page, Integer size);
}