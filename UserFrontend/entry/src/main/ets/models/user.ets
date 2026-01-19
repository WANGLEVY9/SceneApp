/**
 * 用户相关数据模型
 */

export interface User {
  id: number;
  username: string;
  phone?: string;
  nickname?: string;
  avatarUrl?: string;
  points: number;
  createTime: string;
  updateTime: string;
  status: number;
}

export interface LoginRequest {
  loginKey: string;
  password?: string;
  code?: string;
  loginType: number; // 0: password, 1: code
}

export interface LoginResponse {
  token: string;
  userId: number;
  phone?: string;
  username?: string;
  nickname?: string;
  avatarUrl?: string;
  points: number;
}
