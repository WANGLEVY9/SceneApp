import { HttpUtil } from '../utils/http';
import { Dynamic, CommentResult, RecommendCheckinSpot, RecommendMerchant } from '../models/dynamic';
import { Page, PageQuery } from '../models/common';

export interface RecommendQuery extends PageQuery {
  sort: string;
  lat: number;
  lng: number;
}

export interface CommentRequest {
  dynamicId: string;
  content: string;
}

/**
 * 动态与推荐相关API
 */
export class DynamicApi {
  /**
   * 获取推荐打卡点
   */
  static async getRecommendCheckinSpots(params: RecommendQuery): Promise<Page<RecommendCheckinSpot>> {
    const query: Record<string, string | number> = {
      "sort": params.sort,
      "lat": params.lat,
      "lng": params.lng,
    };
    if (params.page !== undefined) {
      query.page = params.page;
    }
    if (params.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<Page<RecommendCheckinSpot>>('/api/client/recommend/checkin', query);
  }
  
  /**
   * 获取推荐商家
   */
  static async getRecommendMerchants(params: RecommendQuery): Promise<Page<RecommendMerchant>> {
    const query: Record<string, string | number> = {
      "sort": params.sort,
      "lat": params.lat,
      "lng": params.lng,
    };
    if (params.page !== undefined) {
      query.page = params.page;
    }
    if (params.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<Page<RecommendMerchant>>('/api/client/recommend/merchants', query);
  }
  
  /**
   * 获取动态流
   */
  static async getDynamicFlow(params?: PageQuery): Promise<Page<Dynamic>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<Page<Dynamic>>('/api/client/dynamic', params ? query : undefined);
  }
  
  /**
   * 评论动态
   */
  static async commentDynamic(request: CommentRequest): Promise<CommentResult> {
    return HttpUtil.post<CommentResult>('/api/client/dynamic/comment', request);
  }
}
