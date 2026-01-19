package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.SceneMerchantMapper;
import com.scene.model.dto.SceneMerchantListItem;
import com.scene.model.dto.SceneMerchantListResponse;
import com.scene.model.dto.SceneMerchantUpsertRequest;
import com.scene.model.entity.SceneMerchant;
import com.scene.service.SceneMerchantService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SceneMerchantServiceImpl extends ServiceImpl<SceneMerchantMapper, SceneMerchant> implements SceneMerchantService {

    @Override
    public SceneMerchantListResponse pageList(long page, long size) {
        Page<SceneMerchant> mpPage = new Page<>(page, size);
        this.lambdaQuery().page(mpPage);

        List<SceneMerchant> records = mpPage.getRecords();
        List<SceneMerchantListItem> list = new ArrayList<>(records.size());
        for (SceneMerchant merchant : records) {
            SceneMerchantListItem item = new SceneMerchantListItem();
            item.setId(merchant.getId());
            item.setName(merchant.getName());
            item.setContact(merchant.getContact());
            item.setLat(merchant.getLat());
            item.setLng(merchant.getLng());
            list.add(item);
        }

        SceneMerchantListResponse resp = new SceneMerchantListResponse();
        resp.setList(list);
        resp.setTotal(mpPage.getTotal());
        return resp;
    }

    @Override
    public void create(SceneMerchantUpsertRequest request) {
        SceneMerchant merchant = new SceneMerchant();
        merchant.setId(UUID.randomUUID().toString().replace("-", ""));
        merchant.setName(request.getName());
        merchant.setContact(request.getContact());
        merchant.setLat(request.getLat());
        merchant.setLng(request.getLng());
        this.save(merchant);
    }

    @Override
    public void deleteById(String id) {
        // 检查数据是否存在
        SceneMerchant merchant = this.getById(id);
        if (merchant == null) {
            throw new NotFoundException("商家不存在");
        }
        super.removeById(id);
    }
}