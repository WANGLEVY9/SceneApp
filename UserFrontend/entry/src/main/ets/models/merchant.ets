/**
 * 商家相关数据模型
 */

export interface SceneMerchant {
  id: string;
  name: string;
  contact?: string;
  lat: number;
  lng: number;
  desc?: string;
  hot: number;
}
