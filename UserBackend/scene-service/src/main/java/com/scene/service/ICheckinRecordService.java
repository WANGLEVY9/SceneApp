package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.common.domain.PageDTO;
import com.scene.domain.dto.CheckinSubmitDTO;
import com.scene.domain.vo.CheckinRecordVO;
import com.scene.domain.vo.CheckinResultVO;

/**
 * 打卡记录服务接口
 */
public interface ICheckinRecordService {

    /**
     * 提交打卡
     * @param userId 用户ID
     * @param submitDTO 打卡提交信息
     * @return 打卡结果
     */
    CheckinResultVO submitCheckin(String userId, CheckinSubmitDTO submitDTO);

    /**
     * 获取个人打卡记录
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 分页打卡记录
     */
    PageDTO<CheckinRecordVO> getPersonalRecords(String userId, Integer page, Integer size);
}