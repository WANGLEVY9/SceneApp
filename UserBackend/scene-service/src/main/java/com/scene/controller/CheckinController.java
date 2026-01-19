package com.scene.controller;

import com.scene.common.domain.R;
import com.scene.common.domain.PageDTO;
import com.scene.common.utils.UserContext;
import com.scene.domain.dto.CheckinSubmitDTO;
import com.scene.domain.dto.LikeDTO;
import com.scene.domain.vo.CheckinRankVO;
import com.scene.domain.vo.CheckinRecordVO;
import com.scene.domain.vo.CheckinResultVO;
import com.scene.domain.vo.CheckinSpotVO;
import com.scene.domain.vo.LikeResultVO;
import com.scene.service.ICheckinLikeService;
import com.scene.service.ICheckinRankService;
import com.scene.service.ICheckinRecordService;
import com.scene.service.ICheckinSpotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 打卡与社交接口控制器
 */
@Api(tags = "打卡与社交接口")
@RestController
@RequestMapping("/api/client/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final ICheckinSpotService checkinSpotService;
    private final ICheckinRecordService checkinRecordService;
    private final ICheckinLikeService checkinLikeService;
    private final ICheckinRankService checkinRankService;

    /**
     * 获取打卡点位列表
     */
    @ApiOperation("获取打卡点位列表")
    @GetMapping("/spots")
    public R<List<CheckinSpotVO>> getSpots() {
        List<CheckinSpotVO> spots = checkinSpotService.listSpots();
        return R.ok(spots);
    }

    /**
     * 提交打卡
     */
    @ApiOperation("提交打卡")
    @PostMapping
    public R<CheckinResultVO> submitCheckin(@RequestBody CheckinSubmitDTO submitDTO) {
        // TODO: 从token中获取用户ID
        String userId = String.valueOf(UserContext.getUser());
        CheckinResultVO result = checkinRecordService.submitCheckin(userId, submitDTO);
        return R.ok(result);
    }

    /**
     * 获取个人打卡记录
     */
    @ApiOperation("获取个人打卡记录")
    @GetMapping("/records")
    public R<PageDTO<CheckinRecordVO>> getPersonalRecords(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        // TODO: 从token中获取用户ID
        String userId = String.valueOf(UserContext.getUser());
        PageDTO<CheckinRecordVO> records = checkinRecordService.getPersonalRecords(userId, page, size);
        return R.ok(records);
    }

    /**
     * 为打卡点赞
     */
    @ApiOperation("为打卡点赞")
    @PostMapping("/like")
    public R<LikeResultVO> likeCheckin(@RequestBody LikeDTO likeDTO) {
        String userId = String.valueOf(UserContext.getUser());
        LikeResultVO result = checkinLikeService.likeCheckin(userId, likeDTO);
        return R.ok(result);
    }

    /**
     * 获取打卡排行榜
     */
    @ApiOperation("获取打卡排行榜")
    @GetMapping("/rank")
    public R<List<CheckinRankVO>> getRanking(@RequestParam(defaultValue = "total") String type) {
        List<CheckinRankVO> ranking = checkinRankService.getRankingList(type);
        return R.ok(ranking);
    }
}