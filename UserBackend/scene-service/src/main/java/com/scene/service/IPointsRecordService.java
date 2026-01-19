package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.vo.PointsRecordVO;

public interface IPointsRecordService {
    Page<PointsRecordVO> getPointsRecords(Long userId, Integer page, Integer size);
}