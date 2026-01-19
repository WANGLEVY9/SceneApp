package com.scene.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.CheckinRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡记录Mapper接口
 */
@Mapper
public interface CheckinRecordMapper extends BaseMapper<CheckinRecord> {
}