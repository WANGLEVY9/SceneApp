import http from '@ohos.net.http';
import { preferences } from '@kit.ArkData';
import { Context } from '@kit.AbilityKit';
import { ApiResult } from '../models/common';

/**
 * HTTP请求工具类
 */
export class HttpUtil {
  private static baseUrl: string = 'http://10.0.2.2:8080'; // 后端服务地址（模拟器访问宿主机请使用 10.0.2.2）
  private static tokenKey: string = 'token';
  private static appContext: Context | null = null;
  
  /**
   * 设置应用上下文
   */
  static setAppContext(ctx: Context): void {
    HttpUtil.appContext = ctx;
  }
  
  /**
   * 获取存储的token
   */
  private static async getToken(): Promise<string | null> {
    if (!HttpUtil.appContext) {
      return null;
    }
    try {
      const dataPreferences = await preferences.getPreferences(HttpUtil.appContext, 'app_data');
      const value = await dataPreferences.get(HttpUtil.tokenKey, '');
      return typeof value === 'string' ? value : String(value);
    } catch (e) {
      return null;
    }
  }
  
  /**
   * 保存token
   */
  static async saveToken(token: string): Promise<void> {
    if (!HttpUtil.appContext) {
      return;
    }
    try {
      const dataPreferences = await preferences.getPreferences(HttpUtil.appContext, 'app_data');
      await dataPreferences.put(HttpUtil.tokenKey, token);
      await dataPreferences.flush();
    } catch (e) {
      console.error('保存token失败:', e);
    }
  }
  
  /**
   * 清除token
   */
  static async clearToken(): Promise<void> {
    if (!HttpUtil.appContext) {
      return;
    }
    try {
      const dataPreferences = await preferences.getPreferences(HttpUtil.appContext, 'app_data');
      await dataPreferences.delete(HttpUtil.tokenKey);
      await dataPreferences.flush();
    } catch (e) {
      console.error('清除token失败:', e);
    }
  }
  
  /**
   * GET请求
   */
  static async get<T>(url: string, params?: Record<string, string | number>): Promise<T> {
    let requestUrl: string = HttpUtil.baseUrl + url;
    if (params) {
      const queryString: string = Object.keys(params)
        .map((key: string) => `${encodeURIComponent(key)}=${encodeURIComponent(String(params[key]))}`)
        .join('&');
      requestUrl += '?' + queryString;
    }
    
    return HttpUtil.request<T>(requestUrl, {
      method: http.RequestMethod.GET
    });
  }
  
  /**
   * POST请求
   */
  static async post<T>(url: string, data?: object): Promise<T> {
    return HttpUtil.request<T>(HttpUtil.baseUrl + url, {
      method: http.RequestMethod.POST,
      extraData: JSON.stringify(data || {}),
      header: {
        'Content-Type': 'application/json'
      }
    });
  }
  
  /**
   * PUT请求
   */
  static async put<T>(url: string, data?: object, params?: Record<string, string | number>): Promise<T> {
    let requestUrl: string = HttpUtil.baseUrl + url;
    if (params) {
      const queryString: string = Object.keys(params)
        .map((key: string) => `${encodeURIComponent(key)}=${encodeURIComponent(String(params[key]))}`)
        .join('&');
      requestUrl += '?' + queryString;
    }
    
    return HttpUtil.request<T>(requestUrl, {
      method: http.RequestMethod.PUT,
      extraData: data ? JSON.stringify(data) : undefined,
      header: {
        'Content-Type': 'application/json'
      }
    });
  }
  
  /**
   * DELETE请求
   */
  static async delete<T>(url: string): Promise<T> {
    return HttpUtil.request<T>(HttpUtil.baseUrl + url, {
      method: http.RequestMethod.DELETE
    });
  }
  
  /**
   * 通用请求方法
   */
  private static async request<T>(url: string, options: http.HttpRequestOptions): Promise<T> {
    const requestOptions: http.HttpRequestOptions = {
      method: options.method,
      header: options.header,
      extraData: options.extraData,
      readTimeout: options.readTimeout,
      connectTimeout: options.connectTimeout,
      expectDataType: options.expectDataType,
      priority: options.priority
    };
    return new Promise<T>(async (resolve, reject) => {
      const httpRequest = http.createHttp();
      
      // 添加token到请求头
      const token = await HttpUtil.getToken();
      let headers: Record<string, string>;
      if (requestOptions.header) {
        headers = requestOptions.header as Record<string, string>;
      } else {
        headers = {} as Record<string, string>;
      }
      if (token) {
        headers['Authorization'] = token;
      }
      requestOptions.header = headers;
      
      httpRequest.request(url, requestOptions)
        .then((data: http.HttpResponse) => {
          if (data.responseCode === 200 && data.result) {
            try {
              const bodyText: string = typeof data.result === 'string'
                ? data.result
                : data.result.toString();
              const result: ApiResult<T> = JSON.parse(bodyText) as ApiResult<T>;
              if (result && result.code === 200) {
                if (result.data !== undefined) {
                  resolve(result.data as T);
                } else {
                  // 成功但无数据，返回显式的 undefined
                  resolve(undefined as T);
                }
              } else {
                reject(new Error(result?.msg || '请求失败'));
              }
            } catch (e) {
              reject(new Error('响应解析失败'));
            }
          } else {
            reject(new Error(`HTTP错误: ${data.responseCode}`));
          }
        })
        .catch((err: Error) => {
          reject(err);
        })
        .finally(() => {
          httpRequest.destroy();
        });
    });
  }
  
  /**
   * 设置基础URL
   */
  static setBaseUrl(url: string): void {
    HttpUtil.baseUrl = url;
  }
}
