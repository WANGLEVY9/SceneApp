package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.model.dto.CheckinRecordListItem;
import com.scene.model.dto.CheckinRecordListResponse;
import com.scene.model.entity.CheckinRecord;
import com.scene.service.CheckinRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckinRecordServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements CheckinRecordService {

    @Override
    public CheckinRecordListResponse pageList(String userName, String spotName, long page, long size) {
        Page<CheckinRecord> mpPage = new Page<>(page, size);
        this.lambdaQuery()
                .like(StringUtils.hasText(userName), CheckinRecord::getUserName, userName)
                .like(StringUtils.hasText(spotName), CheckinRecord::getSpotName, spotName)
                .page(mpPage);

        List<CheckinRecord> records = mpPage.getRecords();
        List<CheckinRecordListItem> list = new ArrayList<>(records.size());
        for (CheckinRecord record : records) {
            CheckinRecordListItem item = new CheckinRecordListItem();
            item.setId(record.getId());
            item.setUserName(record.getUserName());
            item.setSpotName(record.getSpotName());
            item.setTime(record.getCheckinTime());
            item.setImage(record.getImage());
            item.setRemark(record.getRemark());
            item.setLikeCount(record.getLikeCount());
            list.add(item);
        }

        CheckinRecordListResponse resp = new CheckinRecordListResponse();
        resp.setList(list);
        resp.setTotal(mpPage.getTotal());
        return resp;
    }

    @Override
    public void deleteById(String id) {
        // 检查数据是否存在
        CheckinRecord record = this.getById(id);
        if (record == null) {
            throw new NotFoundException("打卡记录不存在");
        }
        super.removeById(id);
    }
}

