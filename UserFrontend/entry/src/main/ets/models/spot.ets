/**
 * 景点相关数据模型
 */

export interface SceneSpot {
  id: string;
  name: string;
  lat: number;
  lng: number;
  desc?: string;
}

export interface Location {
  lat: number;
  lng: number;
}

export interface CheckinSpot {
  id: string;
  name: string;
  location?: Location;
  lat?: number; // 兼容旧代码
  lng?: number; // 兼容旧代码
  hot?: number;
}

export interface CheckinRecord {
  id: string;
  userId?: string;
  userName?: string;
  nickname?: string; // 动态流中的昵称
  spotId?: string;
  spotName: string;
  image?: string;
  remark?: string;
  content?: string; // 动态流中的内容
  lat?: number;
  lng?: number;
  likeCount: number;
  checkinTime?: string;
  time?: string; // 动态流中的时间
  status?: number;
}
