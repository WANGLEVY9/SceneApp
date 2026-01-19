package com.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.common.domain.R;
import com.scene.common.utils.UserContext;
import com.scene.domain.vo.PointsRecordVO;
import com.scene.service.IPointsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "积分相关接口")
@RestController
@RequestMapping("api/client/points")
@RequiredArgsConstructor
public class PointsController {

    private final IPointsRecordService pointsRecordService;

    @ApiOperation("获取积分记录")
    @GetMapping("/records")
    public R<Page<PointsRecordVO>> getPointsRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Long userId = UserContext.getUser();
        Page<PointsRecordVO> result = pointsRecordService.getPointsRecords(userId, page, size);
        return R.ok(result);
    }
}