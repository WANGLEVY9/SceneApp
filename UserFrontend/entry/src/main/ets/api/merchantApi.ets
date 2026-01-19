import { SceneMerchant } from '../models/merchant';
import { PageDTO } from '../models/common';
import { MerchantQuery, SceneApi } from './sceneApi';

/**
 * 商家相关API（已合并到SceneApi，保留此类以兼容旧代码）
 * @deprecated 请使用 SceneApi.getMerchantList
 */
export class MerchantApi {
  /**
   * 获取商家列表
   */
  static async getMerchantList(params?: MerchantQuery): Promise<PageDTO<SceneMerchant>> {
    return SceneApi.getMerchantList(params);
  }
}
