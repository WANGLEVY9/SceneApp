/**
 * 景区相关数据模型
 */

export interface SceneIntro {
  id: string;
  title: string;
  text: string;
  images?: string[]; // 逗号分隔的图片URL列表
  video?: string;
}
