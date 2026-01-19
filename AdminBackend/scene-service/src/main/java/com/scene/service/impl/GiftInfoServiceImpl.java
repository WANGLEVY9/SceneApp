package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.GiftInfoMapper;
import com.scene.model.dto.GiftCreateRequest;
import com.scene.model.dto.GiftListItem;
import com.scene.model.dto.GiftListResponse;
import com.scene.model.entity.GiftInfo;
import com.scene.service.GiftInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 礼品信息服务实现
 */
@Service
public class GiftInfoServiceImpl extends ServiceImpl<GiftInfoMapper, GiftInfo> implements GiftInfoService {

    @Override
    public GiftListResponse list(long page, long size) {
        Page<GiftInfo> mpPage = new Page<>(page, size);
        this.lambdaQuery().page(mpPage);

        List<GiftInfo> records = mpPage.getRecords();
        List<GiftListItem> list = new ArrayList<>(records.size());
        for (GiftInfo gift : records) {
            GiftListItem item = new GiftListItem();
            BeanUtils.copyProperties(gift, item);
            list.add(item);
        }

        GiftListResponse resp = new GiftListResponse();
        resp.setList(list);
        resp.setTotal(mpPage.getTotal());
        return resp;
    }

    @Override
    public void removeById(String id) {
        // 检查数据是否存在
        GiftInfo gift = this.getById(id);
        if (gift == null) {
            throw new NotFoundException("礼品不存在");
        }
        super.removeById(id);
    }

    @Override
    public void create(GiftCreateRequest request) {
        GiftInfo gift = new GiftInfo();
        gift.setId(UUID.randomUUID().toString().replace("-", ""));
        gift.setName(request.getName());
        gift.setPoints(request.getPoints());
        gift.setStock(request.getStock());
        gift.setImage(request.getImage());
        gift.setDesc(request.getDesc());
        gift.setStatus(1); // 默认可兑换
        this.save(gift);
    }
}

