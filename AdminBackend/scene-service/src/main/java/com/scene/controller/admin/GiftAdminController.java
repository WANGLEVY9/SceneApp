package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.GiftCreateRequest;
import com.scene.model.dto.GiftListResponse;
import com.scene.service.GiftInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 礼品管理控制器
 */
@RestController
@RequestMapping("/api/admin/gifts")
public class GiftAdminController {

    @Autowired
    private GiftInfoService giftInfoService;

    @GetMapping
    public R<GiftListResponse> list(@RequestParam(defaultValue = "1") long page,
                                     @RequestParam(defaultValue = "10") long size) {
        // 查询礼品列表（分页）
        GiftListResponse resp = giftInfoService.list(page, size);
        return new R<>(200, "操作成功", resp);
    }

    @PostMapping
    public R<Void> create(@Valid @RequestBody GiftCreateRequest request) {
        // 新增礼品
        giftInfoService.create(request);
        return new R<>(200, "操作成功", null);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 删除礼品
        giftInfoService.removeById(id);
        return new R<>(200, "操作成功", null);
    }
}

