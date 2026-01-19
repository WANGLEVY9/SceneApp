package com.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scene.domain.dto.GiftExchangeDTO;
import com.scene.domain.vo.GiftExchangeRecordVO;

public interface IGiftExchangeService {
    void exchangeGift(Long userId, GiftExchangeDTO giftExchangeDTO);
    Page<GiftExchangeRecordVO> getExchangeRecords(Long userId, Integer page, Integer size);
}