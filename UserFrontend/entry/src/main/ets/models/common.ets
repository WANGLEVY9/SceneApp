/**
 * 通用数据模型
 */

export interface PageDTO<T> {
  total: number;
  pages: number;
  list: T[];
}

// MyBatis-Plus的Page类型
export interface Page<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface PageQuery {
  page?: number;
  size?: number;
}

export interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}

export interface ApiResult<T> {
  code?: number;
  msg?: string;
  data?: T;
}
