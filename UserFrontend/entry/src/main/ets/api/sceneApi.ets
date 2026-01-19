import { HttpUtil } from '../utils/http';
import { SceneIntro } from '../models/scene';
import { PageDTO, PageQuery } from '../models/common';
import { SceneSpot } from '../models/spot';
import { SceneMerchant } from '../models/merchant';

export interface MerchantQuery extends PageQuery {
  lat?: number;
  lng?: number;
}

/**
 * 景区相关API
 */
export class SceneApi {
  /**
   * 获取引领区详情
   */
  static async getSceneIntro(): Promise<SceneIntro> {
    return HttpUtil.get<SceneIntro>('/api/client/intro');
  }
  
  /**
   * 获取景点列表
   */
  static async getSpotList(params?: PageQuery): Promise<PageDTO<SceneSpot>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    return HttpUtil.get<PageDTO<SceneSpot>>('/api/client/scenic-spots', params ? query : undefined);
  }
  
  /**
   * 获取商家列表
   */
  static async getMerchantList(params?: MerchantQuery): Promise<PageDTO<SceneMerchant>> {
    const query: Record<string, string | number> = {};
    if (params?.page !== undefined) {
      query.page = params.page;
    }
    if (params?.size !== undefined) {
      query.size = params.size;
    }
    if (params?.lat !== undefined) {
      query.lat = params.lat;
    }
    if (params?.lng !== undefined) {
      query.lng = params.lng;
    }
    return HttpUtil.get<PageDTO<SceneMerchant>>('/api/client/merchants', params ? query : undefined);
  }
}
