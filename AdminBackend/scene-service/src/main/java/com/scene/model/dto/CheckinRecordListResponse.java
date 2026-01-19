package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 打卡记录分页响应
 */
@Data
public class CheckinRecordListResponse {
    private List<CheckinRecordListItem> list;
    private long total;
}

