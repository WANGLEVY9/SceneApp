package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.CheckinRecordListResponse;
import com.scene.service.CheckinRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/checkin/records")
public class CheckinRecordAdminController {

    @Autowired
    private CheckinRecordService checkinRecordService;

    @GetMapping
    public R<CheckinRecordListResponse> list(@RequestParam(required = false) String userName,
                                             @RequestParam(required = false) String spotName,
                                             @RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long size) {
        // 查询打卡记录，支持用户/点位筛选
        CheckinRecordListResponse resp = checkinRecordService.pageList(userName, spotName, page, size);
        return new R<>(200, "OK", resp);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 删除打卡记录
        checkinRecordService.deleteById(id);
        return new R<>(200, "删除成功", null);
    }
}

