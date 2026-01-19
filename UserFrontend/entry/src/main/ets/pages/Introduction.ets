import { SpotApi } from '../api/spotApi';
import { MerchantApi } from '../api/merchantApi';
import { MerchantQuery, SceneApi } from '../api/sceneApi';
import { DynamicApi, RecommendQuery } from '../api/dynamicApi';
import { CheckinApi } from '../api/checkinApi';
import { CheckinSpot } from '../models/spot';
import { SceneMerchant } from '../models/merchant';
import { SceneIntro } from '../models/scene';
import { promptAction } from '@kit.ArkUI';

// Introduction.ets

@ComponentV2
export struct Introduction {
  @Local bannerList: string[] = []
  @Local currentBannerIndex: number = 0
  @Local sceneIntro: SceneIntro | null = null
  @Local nearbySpots: CheckinSpot[] = []
  @Local nearbyMerchants: SceneMerchant[] = []
  @Local loading: boolean = false
  @Local searchKeyword: string = ''

  // 用于跟踪按钮点击状态
  @Local buttonClickedMap: Map<string, boolean> = new Map()

  // 显示提示消息
  showMessage(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 加载景区介绍
  async loadSceneIntro(): Promise<void> {
    try {
      this.sceneIntro = await SceneApi.getSceneIntro()
      if (this.sceneIntro?.images) {
        this.bannerList = this.sceneIntro.images.filter(img => img.trim().length > 0)
      }
      // 如果没有图片，使用默认图片
      if (this.bannerList.length === 0) {
        this.bannerList = ['app.media.ad']
      }
    } catch (e) {
      console.error('加载景区介绍失败:', e)
      // 使用默认图片
      this.bannerList = ['app.media.ad']
    }
  }
  
  // 加载附近打卡点
  async loadNearbySpots(): Promise<void> {
    try {
      // 使用推荐接口获取附近打卡点
      // 这里应该传入当前位置的经纬度，暂时使用模拟数据
      const params: RecommendQuery = {
        sort: 'distance',
        page: 1,
        size: 10,
        lat: 23.123456,
        lng: 113.123456
      }
      const result = await DynamicApi.getRecommendCheckinSpots(params)
      // 转换推荐打卡点数据格式
      this.nearbySpots = (result.records || []).map((spot): CheckinSpot => ({
        id: spot.spotId,
        name: spot.name,
        location: { lat: 0, lng: 0 },
        hot: spot.hot || 0
      }))
    } catch (e) {
      console.error('加载附近打卡点失败:', e)
      // 如果推荐接口失败，尝试使用普通接口
      try {
        const spots = await CheckinApi.getCheckinSpots()
        this.nearbySpots = (spots || []).slice(0, 10).map((spot): CheckinSpot => ({
          id: spot.id,
          name: spot.name,
          location: {
            lat: spot.location?.lat || spot.lat || 0,
            lng: spot.location?.lng || spot.lng || 0
          },
          hot: spot.hot || 0
        }))
      } catch (e2) {
        console.error('加载打卡点失败:', e2)
      }
    }
  }
  
  // 加载附近商家
  async loadNearbyMerchants(): Promise<void> {
    try {
      const params: MerchantQuery = {
        page: 1,
        size: 10,
        lat: 23.123456,
        lng: 113.123456
      }
      const result = await MerchantApi.getMerchantList(params)
      this.nearbyMerchants = result.list || []
    } catch (e) {
      console.error('加载附近商家失败:', e)
    }
  }
  
  // 搜索
  onSearch(): void {
    if (this.searchKeyword.trim().length === 0) {
      this.showMessage('请输入搜索关键词')
      return
    }
    this.showMessage(`搜索: ${this.searchKeyword}`)
    // 这里可以调用搜索API
  }

  // 设置按钮点击状态
  setButtonClicked(merchantId: string, clicked: boolean): void {
    this.buttonClickedMap.set(merchantId, clicked)
  }

  // 获取按钮点击状态
  isButtonClicked(merchantId: string): boolean {
    return this.buttonClickedMap.get(merchantId) || false
  }

  // Banner轮播图组件
  @Builder
  BannerBuilder() {
    Swiper() {
      ForEach(this.bannerList, (banner: string, index: number) => {
        Image(banner.startsWith('http') ? banner : $r(`app.media.ad`))
          .width('100%')
          .height(200)
          .objectFit(ImageFit.Cover)
          .borderRadius(12)
      })
    }
    .indicator(true)
    .autoPlay(true)
    .interval(3000)
    .duration(500)
    .loop(true)
    .width('100%')
    .height(200)
    .onChange((index: number) => {
      this.currentBannerIndex = index
    })
    .onClick(() => {
      this.showMessage('跳转到引领区详情页')
    })
  }

  // 搜索框组件
  @Builder
  SearchBuilder() {
    Row({ space: 8 }) {
      Image($r('app.media.ad'))
        .width(20)
        .height(20)
        .fillColor(Color.Gray)

      TextInput({ placeholder: '输入景点/商家名称' })
        .layoutWeight(1)
        .fontSize(16)
        .onChange((value: string) => {
          this.searchKeyword = value
        })
        .onSubmit(() => {
          this.onSearch()
        })
    }
    .width('100%')
    .height(40)
    .padding({ left: 12, right: 12 })
    .backgroundColor(Color.White)
    .borderRadius(20)
    .shadow({ radius: 4, color: '#000000', offsetX: 0, offsetY: 2 })
  }

  // 核心入口区域组件
  @Builder
  CoreEntranceBuilder() {
    Grid() {
      // 引领区简介
      GridItem() {
        Column({ space: 8 }) {
          Image($r('app.media.ad'))
            .width(32)
            .height(32)
            .fillColor('#0A59F7')

          Text('引领区简介')
            .fontSize(12)
            .fontColor(Color.Black)
            .textAlign(TextAlign.Center)
        }
        .width('100%')
        .height(80)
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
        .onClick(() => {
          this.showMessage('跳转到引领区详情页')
        })
      }

      // 打卡排行
      GridItem() {
        Column({ space: 8 }) {
          Image($r('app.media.ad'))
            .width(32)
            .height(32)
            .fillColor('#0A59F7')

          Text('打卡排行')
            .fontSize(12)
            .fontColor(Color.Black)
            .textAlign(TextAlign.Center)
        }
        .width('100%')
        .height(80)
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
        .onClick(() => {
          this.showMessage('跳转到打卡排行榜')
        })
      }

      // 附近打卡点
      GridItem() {
        Column({ space: 8 }) {
          Image($r('app.media.ad'))
            .width(32)
            .height(32)
            .fillColor('#0A59F7')

          Text('附近打卡点')
            .fontSize(12)
            .fontColor(Color.Black)
            .textAlign(TextAlign.Center)
        }
        .width('100%')
        .height(80)
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
        .onClick(() => {
          this.showMessage('跳转到附近打卡点列表')
        })
      }

      // 活动说明
      GridItem() {
        Column({ space: 8 }) {
          Image($r('app.media.ad'))
            .width(32)
            .height(32)
            .fillColor('#0A59F7')

          Text('活动说明')
            .fontSize(12)
            .fontColor(Color.Black)
            .textAlign(TextAlign.Center)
        }
        .width('100%')
        .height(80)
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
        .onClick(() => {
          this.showMessage('跳转到活动说明')
        })
      }
    }
    .columnsTemplate('1fr 1fr')
    .rowsTemplate('1fr 1fr')
    .columnsGap(12)
    .rowsGap(12)
    .height(160)
    .margin({ top: 16, bottom: 16 })
  }

  // 附近推荐瀑布流组件
  @Builder
  NearbySpotsBuilder() {
    Column() {
      // 标题栏
      Row() {
        Text('附近推荐')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor(Color.Black)
          .layoutWeight(1)

        Text('查看更多')
          .fontSize(14)
          .fontColor('#666666')
          .onClick(() => {
            this.showMessage('跳转到附近打卡点列表')
          })
      }
      .width('100%')
      .margin({ bottom: 12 })

      // 打卡点列表
      Column() {
        ForEach(this.nearbySpots, (spot: CheckinSpot) => {
          Row({ space: 12 }) {
            // 打卡点图片占位
            Column()
              .width(80)
              .height(80)
              .backgroundColor('#E0E0E0')
              .borderRadius(8)
              .justifyContent(FlexAlign.Center)
              .alignItems(HorizontalAlign.Center)

            // 打卡点信息
            Column({ space: 6 }) {
              // 名称
              Text(spot.name)
                .fontSize(16)
                .fontWeight(FontWeight.Medium)
                .fontColor(Color.Black)
                .width('100%')

              // 热度信息
              Row() {
                Image($r('app.media.ad'))
                  .width(12)
                  .height(12)
                  .fillColor('#FF6B00')

                Text(`${spot.hot}人打卡`)
                  .fontSize(12)
                  .fontColor('#666666')
                  .margin({ left: 4 })
              }
              .width('100%')

              // 位置信息
              if (spot.lat && spot.lng) {
                Text(`位置: ${spot.lat.toFixed(4)}, ${spot.lng.toFixed(4)}`)
                  .fontSize(12)
                  .fontColor('#666666')
                  .maxLines(1)
                  .textOverflow({ overflow: TextOverflow.Ellipsis })
              }
            }
            .layoutWeight(1)
            .alignItems(HorizontalAlign.Start)
          }
          .width('100%')
          .padding(12)
          .backgroundColor(Color.White)
          .borderRadius(12)
          .margin({ bottom: 8 })
          .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
          .onClick(() => {
            this.showMessage(`跳转到${spot.name}详情`)
          })
        })
      }
    }
    .width('100%')
    .margin({ top: 16, bottom: 16 })
  }

  // 附近商家列表组件
  @Builder
  NearbyMerchantsBuilder() {
    Column() {
      // 标题栏
      Row() {
        Text('附近商家')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .fontColor(Color.Black)
          .layoutWeight(1)

        Text('查看更多')
          .fontSize(14)
          .fontColor('#666666')
          .onClick(() => {
            this.showMessage('跳转到商家列表')
          })
      }
      .width('100%')
      .margin({ bottom: 12 })

      // 商家列表
      Column() {
        ForEach(this.nearbyMerchants, (merchant: SceneMerchant) => {
          Row({ space: 12 }) {
            // 商家图片占位
            Column()
              .width(60)
              .height(60)
              .backgroundColor('#E0E0E0')
              .borderRadius(8)
              .justifyContent(FlexAlign.Center)
              .alignItems(HorizontalAlign.Center)

            // 商家信息
            Column({ space: 6 }) {
              // 名称和热度
              Row() {
                Text(merchant.name)
                  .fontSize(16)
                  .fontWeight(FontWeight.Medium)
                  .fontColor(Color.Black)
                  .layoutWeight(1)

                Text(`热度: ${merchant.hot}`)
                  .fontSize(12)
                  .fontColor('#FF6B00')
              }
              .width('100%')

              // 描述
              if (merchant.desc) {
                Text(merchant.desc)
                  .fontSize(12)
                  .fontColor('#666666')
                  .maxLines(1)
                  .textOverflow({ overflow: TextOverflow.Ellipsis })
                  .width('100%')
              }

              // 联系方式
              Row() {
                if (merchant.contact) {
                  Text(`电话：${merchant.contact}`)
                    .fontSize(12)
                    .fontColor('#666666')
                    .layoutWeight(1)

                  Button('一键拨打')
                    .fontSize(12)
                    .backgroundColor('#0A59F7')
                    .fontColor(Color.White)
                    .borderRadius(16)
                    .width(70)
                    .height(24)
                    .onClick(() => {
                      // 设置按钮点击状态
                      this.setButtonClicked(merchant.id, true)
                      this.showMessage(`拨打: ${merchant.contact}`)
                      // 100ms后重置状态
                      setTimeout(() => {
                        this.setButtonClicked(merchant.id, false)
                      }, 100)
                    })
                } else {
                  Text('暂无联系方式')
                    .fontSize(12)
                    .fontColor('#999999')
                }
              }
              .width('100%')
              .alignItems(VerticalAlign.Center)
            }
            .layoutWeight(1)
            .alignItems(HorizontalAlign.Start)
          }
          .width('100%')
          .padding(12)
          .backgroundColor(Color.White)
          .borderRadius(12)
          .margin({ bottom: 8 })
          .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
          .onClick(() => {
            // 如果按钮被点击了，就不执行卡片点击
            if (!this.isButtonClicked(merchant.id)) {
              this.showMessage(`跳转到${merchant.name}详情`)
            }
            // 重置按钮状态
            this.setButtonClicked(merchant.id, false)
          })
        })
      }
    }
    .width('100%')
    .margin({ top: 16, bottom: 16 })
  }

  build() {
    Scroll() {
      Column() {
        // 搜索框
        Row() {
          this.SearchBuilder()
        }
        .margin({ top: 12, bottom: 12 })

        // Banner轮播图
        this.BannerBuilder()

        // 核心入口区域
        this.CoreEntranceBuilder()

        // 附近推荐瀑布流
        this.NearbySpotsBuilder()

        // 附近商家列表
        this.NearbyMerchantsBuilder()
      }
      .width('100%')
      .padding({ left: 16, right: 16 })
      .backgroundColor('#F5F5F5')
    }
    .width('100%')
    .height('100%')
    .scrollable(ScrollDirection.Vertical)
    .scrollBar(BarState.Auto)
    .onAppear(() => {
      // 页面显示时加载数据
      this.loadSceneIntro()
      this.loadNearbySpots()
      this.loadNearbyMerchants()
    })
  }
}