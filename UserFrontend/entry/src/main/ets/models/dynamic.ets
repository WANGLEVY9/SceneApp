/**
 * 动态相关数据模型
 */

export interface Dynamic {
  id: string;
  userId: string;
  nickname: string;
  content?: string;
  image?: string;
  time: string;
  likeCount: number;
}

export interface Comment {
  id: string;
  dynamicId: string;
  userId: string;
  nickname: string;
  content: string;
  createTime: string;
}

export interface CommentResult {
  points: number;
}

export interface RecommendCheckinSpot {
  spotId: string;
  name: string;
  distance: string;
  hot: number;
}

export interface RecommendMerchant {
  id: string;
  name: string;
  distance: string;
}

export interface CheckinRank {
  userId: string;
  nickname: string;
  count: number;
  likeCount: number;
  rank: number;
}
