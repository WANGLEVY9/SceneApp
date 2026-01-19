import { HttpUtil } from '../utils/http';
import { GiftInfo, GiftExchangeRecord, PointsRecord } from '../models/gift';
import { Page, PageQuery } from '../models/common';

export interface ExchangeGiftRequest {
  giftId: string;
  number: number;
}

/**
 * 礼品相关API
 */
export class GiftApi {
  /**
   * 获取礼品列表
   */
  static async getGiftList(): Promise<GiftInfo[]> {
    return HttpUtil.get<GiftInfo[]>('/api/client/gifts');
  }
  
  /**
   * 兑换礼品
   */
  static async exchangeGift(request: ExchangeGiftRequest): Promise<void> {
    return HttpUtil.post<void>('/api/client/gifts/exchange', request);
  }
  
  /**
   * 获取兑换记录列表
   */
  static async getExchangeRecordList(params?: PageQuery): Promise<Page<GiftExchangeRecord>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<Page<GiftExchangeRecord>>('/api/client/gifts/records', params ? query : undefined);
  }
  
  /**
   * 获取积分记录列表
   */
  static async getPointsRecordList(params?: PageQuery): Promise<Page<PointsRecord>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<Page<PointsRecord>>('/api/client/points/records', params ? query : undefined);
  }
}
