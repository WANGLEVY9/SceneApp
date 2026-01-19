package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.GiftExchangeListResponse;
import com.scene.service.GiftExchangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 礼品兑换管理控制器
 */
@RestController
@RequestMapping("/api/admin/gifts")
public class GiftExchangeAdminController {

    @Autowired
    private GiftExchangeRecordService giftExchangeRecordService;

    @GetMapping("/exchange")
    public R<GiftExchangeListResponse> list(@RequestParam(required = false) String status,
                                             @RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long size) {
        // 查询礼品兑换记录列表（支持状态筛选和分页）
        GiftExchangeListResponse resp = giftExchangeRecordService.list(status, page, size);
        return new R<>(200, "操作成功", resp);
    }

    @PutMapping
    public R<Void> updateStatus(@RequestParam String id, @RequestParam String status) {
        // 修改礼品兑换申请状态
        giftExchangeRecordService.updateStatus(id, status);
        return new R<>(200, "操作成功", null);
    }

    @DeleteMapping("/exchange/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 删除礼品兑换申请
        giftExchangeRecordService.deleteById(id);
        return new R<>(200, "删除成功", null);
    }
}

