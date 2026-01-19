import { HttpUtil } from '../utils/http';
import { CheckinSpot, CheckinRecord, Location } from '../models/spot';
import { PageDTO, PageQuery } from '../models/common';
import { CheckinRank } from '../models/dynamic';

export interface SubmitCheckinRequest {
  spotId: string;
  image: string;
  remark?: string;
  location: Location;
}

export interface LikeCheckinRequest {
  recordId: string;
}

export interface SubmitCheckinResponse {
  points: number;
}

export interface LikeCheckinResponse {
  liked: boolean;
  likeCount: number;
}

export interface RankingQueryRecord extends Record<string, string | number> {
  type: string;
}

export interface RankingQuery {
  type: string;
}

/**
 * 打卡相关API
 */
export class CheckinApi {
  /**
   * 获取打卡点位列表
   */
  static async getCheckinSpots(): Promise<CheckinSpot[]> {
    return HttpUtil.get<CheckinSpot[]>('/api/client/checkin/spots');
  }
  
  /**
   * 提交打卡
   */
  static async submitCheckin(request: SubmitCheckinRequest): Promise<SubmitCheckinResponse> {
    return HttpUtil.post<SubmitCheckinResponse>('/api/client/checkin', request);
  }
  
  /**
   * 获取个人打卡记录
   */
  static async getPersonalRecords(params?: PageQuery): Promise<PageDTO<CheckinRecord>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<PageDTO<CheckinRecord>>('/api/client/checkin/records', params ? query : undefined);
  }
  
  /**
   * 为打卡点赞
   */
  static async likeCheckin(request: LikeCheckinRequest): Promise<LikeCheckinResponse> {
    return HttpUtil.post<LikeCheckinResponse>('/api/client/checkin/like', request);
  }
  
  /**
   * 获取打卡排行榜
   */
  static async getRanking(type?: string): Promise<CheckinRank[]> {
    let query: RankingQueryRecord | undefined;
    if (type !== undefined) {
      const rankingQuery: RankingQueryRecord = { type: type };
      query = rankingQuery;
    }
    return HttpUtil.get<CheckinRank[]>('/api/client/checkin/rank', query);
  }
}
