package com.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.common.domain.R;
import com.scene.common.utils.UserContext;
import com.scene.domain.dto.GiftExchangeDTO;
import com.scene.domain.vo.GiftExchangeRecordVO;
import com.scene.domain.vo.GiftInfoVO;
import com.scene.service.IGiftExchangeService;
import com.scene.service.IGiftInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "礼品相关接口")
@RestController
@RequestMapping("api/client/gifts")
@RequiredArgsConstructor
public class GiftController {

    private final IGiftInfoService giftInfoService;
    private final IGiftExchangeService giftExchangeService;

    @ApiOperation("获取礼品列表")
    @GetMapping
    public R<List<GiftInfoVO>> getGiftList() {
        List<GiftInfoVO> giftList = giftInfoService.getGiftList();
        return R.ok(giftList);
    }

    @ApiOperation("兑换礼品")
    @PostMapping("/exchange")
    public R<String> exchangeGift(@RequestBody @Validated GiftExchangeDTO giftExchangeDTO) {
        Long userId = UserContext.getUser();
        giftExchangeService.exchangeGift(userId, giftExchangeDTO);
        return R.ok("兑换申请提交成功");
    }

    @ApiOperation("获取兑换记录")
    @GetMapping("/records")
    public R<Page<GiftExchangeRecordVO>> getExchangeRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Long userId = UserContext.getUser();
        Page<GiftExchangeRecordVO> result = giftExchangeService.getExchangeRecords(userId, page, size);
        return R.ok(result);
    }
}