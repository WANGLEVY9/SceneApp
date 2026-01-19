/**
 * 礼品相关数据模型
 */

export interface GiftInfo {
  id: string;
  name: string;
  points: number;
  stock: number;
  image?: string;
  desc?: string;
  status: number;
}

export interface GiftExchangeRecord {
  id: string;
  userId: string;
  userName: string;
  giftId: string;
  giftName: string;
  giftNumber: number;
  points: number;
  applyTime: string;
  status: string; // pending-待审核，approved-已核销，delivered-已发货，received-已收货，rejected-已拒绝
}

export interface PointsRecord {
  id: string;
  userId: string;
  type: string; // checkin-打卡，like-点赞，comment-评论，be_liked-被点赞，exchange-礼品兑换
  points: number;
  remark?: string;
  createTime: string;
}
