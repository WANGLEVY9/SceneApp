package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.GiftExchangeRecordMapper;
import com.scene.model.dto.GiftExchangeListItem;
import com.scene.model.dto.GiftExchangeListResponse;
import com.scene.model.entity.GiftExchangeRecord;
import com.scene.service.GiftExchangeRecordService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 礼品兑换记录服务实现
 */
@Service
public class GiftExchangeRecordServiceImpl extends ServiceImpl<GiftExchangeRecordMapper, GiftExchangeRecord> implements GiftExchangeRecordService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public GiftExchangeListResponse list(String status, long page, long size) {
        Page<GiftExchangeRecord> mpPage = new Page<>(page, size);
        LambdaQueryWrapper<GiftExchangeRecord> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(GiftExchangeRecord::getStatus, status);
        }
        wrapper.orderByDesc(GiftExchangeRecord::getApplyTime);
        this.page(mpPage, wrapper);

        List<GiftExchangeRecord> records = mpPage.getRecords();
        List<GiftExchangeListItem> list = new ArrayList<>(records.size());
        for (GiftExchangeRecord record : records) {
            GiftExchangeListItem item = new GiftExchangeListItem();
            item.setId(record.getId());
            item.setUserName(record.getUserName());
            item.setGiftName(record.getGiftName());
            item.setGiftNumber(record.getGiftNumber());
            item.setPoints(record.getPoints());
            item.setStatus(record.getStatus());
            if (record.getApplyTime() != null) {
                item.setApplyTime(record.getApplyTime().format(DATE_TIME_FORMATTER));
            }
            list.add(item);
        }

        GiftExchangeListResponse resp = new GiftExchangeListResponse();
        resp.setList(list);
        resp.setTotal(mpPage.getTotal());
        return resp;
    }

    @Override
    public void updateStatus(String id, String status) {
        // 检查数据是否存在
        GiftExchangeRecord record = this.getById(id);
        if (record == null) {
            throw new NotFoundException("礼品兑换记录不存在");
        }
        record.setStatus(status);
        this.updateById(record);
    }

    @Override
    public void deleteById(String id) {
        // 检查数据是否存在
        GiftExchangeRecord record = this.getById(id);
        if (record == null) {
            throw new NotFoundException("礼品兑换记录不存在");
        }
        super.removeById(id);
    }
}

