import { SceneSpot, CheckinSpot, CheckinRecord } from '../models/spot';
import { PageDTO, PageQuery } from '../models/common';
import { SceneApi } from './sceneApi';
import { CheckinApi, SubmitCheckinRequest, SubmitCheckinResponse } from './checkinApi';

export interface LegacyCheckinRecordRequest {
  spotId: string;
  image: string;
  remark?: string;
  lat: number;
  lng: number;
}

/**
 * 景点相关API（已合并到SceneApi，保留此类以兼容旧代码）
 * @deprecated 请使用 SceneApi.getSpotList
 */
export class SpotApi {
  /**
   * 获取景点列表
   */
  static async getSpotList(params?: PageQuery): Promise<PageDTO<SceneSpot>> {
    return SceneApi.getSpotList(params);
  }
  
  /**
   * 获取打卡点列表（已迁移到CheckinApi）
   * @deprecated 请使用 CheckinApi.getCheckinSpots
   */
  static async getCheckinSpotList(): Promise<CheckinSpot[]> {
    return CheckinApi.getCheckinSpots();
  }
  
  /**
   * 获取打卡记录列表（已迁移到CheckinApi）
   * @deprecated 请使用 CheckinApi.getPersonalRecords
   */
  static async getCheckinRecordList(params?: PageQuery): Promise<PageDTO<CheckinRecord>> {
    return CheckinApi.getPersonalRecords(params);
  }
  
  /**
   * 创建打卡记录（已迁移到CheckinApi）
   * @deprecated 请使用 CheckinApi.submitCheckin
   */
  static async createCheckinRecord(record: LegacyCheckinRecordRequest): Promise<SubmitCheckinResponse> {
    const payload: SubmitCheckinRequest = {
      spotId: record.spotId,
      image: record.image,
      remark: record.remark,
      location: {
        lat: record.lat,
        lng: record.lng
      }
    };
    return CheckinApi.submitCheckin(payload);
  }
  
  /**
   * 点赞打卡记录（已迁移到CheckinApi）
   * @deprecated 请使用 CheckinApi.likeCheckin
   */
  static async likeCheckinRecord(id: string): Promise<void> {
    await CheckinApi.likeCheckin({ recordId: id });
  }
  
  /**
   * 取消点赞打卡记录（已迁移到CheckinApi）
   * @deprecated 请使用 CheckinApi.likeCheckin
   */
  static async unlikeCheckinRecord(id: string): Promise<void> {
    // 后端API不支持取消点赞，只能再次点赞切换状态
    await CheckinApi.likeCheckin({ recordId: id });
  }
}
