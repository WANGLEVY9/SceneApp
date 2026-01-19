import { HttpUtil } from '../utils/http';
import { User, LoginRequest, LoginResponse } from '../models/user';

export interface SetupAccountRequest {
  username: string;
  password: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export interface UpdateProfileRequest {
  nickname?: string;
  avatarUrl?: string;
}

export interface ChangeUsernameRequest {
  username: string;
}

export interface SendCodeRequest {
  phone: string;
}

/**
 * 用户相关API
 */
export class UserApi {
  /**
   * 密码登录
   */
  static async login(request: LoginRequest): Promise<LoginResponse> {
    return HttpUtil.post<LoginResponse>('/api/client/auth/login', request);
  }
  
  /**
   * 发送验证码
   */
  static async sendCode(phone: string): Promise<void> {
    // 后端使用 @RequestParam 接收 phone，按查询参数传递
    const url = `/api/client/auth/send-code?phone=${encodeURIComponent(phone)}`;
    return HttpUtil.post<void>(url);
  }
  
  /**
   * 获取当前用户信息
   */
  static async getCurrentUser(): Promise<LoginResponse> {
    return HttpUtil.get<LoginResponse>('/api/client/auth/me');
  }
  
  /**
   * 初次设置用户名和密码
   */
  static async setupAccount(request: SetupAccountRequest): Promise<void> {
    return HttpUtil.post<void>('/api/client/auth/setup-account', request);
  }
  
  /**
   * 修改用户名
   */
  static async changeUsername(request: ChangeUsernameRequest): Promise<void> {
    return HttpUtil.put<void>('/api/client/auth/username', request);
  }
  
  /**
   * 修改密码
   */
  static async changePassword(request: ChangePasswordRequest): Promise<void> {
    return HttpUtil.put<void>('/api/client/auth/password', request);
  }
  
  /**
   * 更新个人资料（昵称、头像）
   */
  static async updateProfile(request: UpdateProfileRequest): Promise<void> {
    return HttpUtil.put<void>('/api/client/auth/profile', request);
  }
}
