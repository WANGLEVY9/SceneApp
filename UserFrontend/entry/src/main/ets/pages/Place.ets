import { SpotApi } from '../api/spotApi';
import { MerchantApi } from '../api/merchantApi';
import { PageQuery } from '../models/common';
import { MerchantQuery } from '../api/sceneApi';
import { SceneSpot } from '../models/spot';
import { SceneMerchant } from '../models/merchant';
import { promptAction } from '@kit.ArkUI';

@Component
export struct Place {
  @State currentTab: number = 0 // 0-景点，1-商家
  @State spotList: SceneSpot[] = []
  @State merchantList: SceneMerchant[] = []
  @State loading: boolean = false
  @State searchKeyword: string = ''
  
  // 显示提示信息
  showToast(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 加载景点列表
  async loadSpotList(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      const params: PageQuery = { page: 1, size: 100 }
      const result = await SpotApi.getSpotList(params)
      this.spotList = result.list || []
    } catch (e) {
      this.showToast('加载景点列表失败')
      console.error('加载景点列表失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 加载商家列表
  async loadMerchantList(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      const params: MerchantQuery = { page: 1, size: 100 }
      const result = await MerchantApi.getMerchantList(params)
      this.merchantList = result.list || []
    } catch (e) {
      this.showToast('加载商家列表失败')
      console.error('加载商家列表失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 切换标签页
  onTabChange(index: number): void {
    this.currentTab = index
    if (index === 0 && this.spotList.length === 0) {
      this.loadSpotList()
    } else if (index === 1 && this.merchantList.length === 0) {
      this.loadMerchantList()
    }
  }
  
  // 搜索
  onSearch(): void {
    if (this.currentTab === 0) {
      this.loadSpotList()
    } else {
      this.loadMerchantList()
    }
  }
  
  // 拨打商家电话
  callMerchant(phone: string): void {
    if (!phone) {
      this.showToast('该商家未提供联系方式')
      return
    }
    // 这里可以调用系统电话功能
    this.showToast(`拨打: ${phone}`)
  }
  
  // 景点卡片构建器
  @Builder
  SpotCardBuilder(spot: SceneSpot) {
    Row({ space: 12 }) {
      // 景点图片占位
      Column()
        .width(80)
        .height(80)
        .backgroundColor('#E0E0E0')
        .borderRadius(8)
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
      
      // 景点信息
      Column({ space: 6 }) {
        Text(spot.name)
          .fontSize(16)
          .fontWeight(FontWeight.Medium)
          .fontColor(Color.Black)
          .maxLines(1)
          .textOverflow({ overflow: TextOverflow.Ellipsis })
        
        if (spot.desc) {
          Text(spot.desc)
            .fontSize(12)
            .fontColor('#666666')
            .maxLines(2)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
        }
        
        Row({ space: 8 }) {
          Text(`纬度: ${spot.lat.toFixed(6)}`)
            .fontSize(11)
            .fontColor('#999999')
          
          Text(`经度: ${spot.lng.toFixed(6)}`)
            .fontSize(11)
            .fontColor('#999999')
        }
        .width('100%')
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
      this.showToast(`查看${spot.name}详情`)
    })
  }
  
  // 商家卡片构建器
  @Builder
  MerchantCardBuilder(merchant: SceneMerchant) {
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
        Row() {
          Text(merchant.name)
            .fontSize(16)
            .fontWeight(FontWeight.Medium)
            .fontColor(Color.Black)
            .layoutWeight(1)
            .maxLines(1)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
          
          Text(`热度: ${merchant.hot}`)
            .fontSize(12)
            .fontColor('#FF6B00')
        }
        .width('100%')
        
        if (merchant.desc) {
          Text(merchant.desc)
            .fontSize(12)
            .fontColor('#666666')
            .maxLines(1)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
        }
        
        Row({ space: 8 }) {
          if (merchant.contact) {
            Text(`电话: ${merchant.contact}`)
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
                this.callMerchant(merchant.contact || '')
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
      this.showToast(`查看${merchant.name}详情`)
    })
  }
  
  build() {
    Column() {
      // 搜索框
      Row({ space: 8 }) {
        TextInput({ placeholder: this.currentTab === 0 ? '搜索景点名称' : '搜索商家名称' })
          .layoutWeight(1)
          .height(40)
          .backgroundColor(Color.White)
          .border({ width: 1, color: Color.Grey })
          .borderRadius(20)
          .padding({ left: 12, right: 12 })
          .onChange((value: string) => {
            this.searchKeyword = value
          })
        
        Button('搜索')
          .width(60)
          .height(40)
          .backgroundColor('#0A59F7')
          .fontColor(Color.White)
          .borderRadius(20)
          .onClick(() => {
            this.onSearch()
          })
      }
      .width('100%')
      .padding({ left: 16, right: 16, top: 12, bottom: 12 })
      .backgroundColor('#F5F5F5')
      
      // 标签页切换
      Row({ space: 0 }) {
        Button('景点')
          .layoutWeight(1)
          .height(44)
          .fontSize(16)
          .backgroundColor(this.currentTab === 0 ? '#0A59F7' : Color.Transparent)
          .fontColor(this.currentTab === 0 ? Color.White : Color.Black)
          .borderRadius({ topLeft: 8, bottomLeft: 8 })
          .onClick(() => {
            this.onTabChange(0)
          })
        
        Button('商家')
          .layoutWeight(1)
          .height(44)
          .fontSize(16)
          .backgroundColor(this.currentTab === 1 ? '#0A59F7' : Color.Transparent)
          .fontColor(this.currentTab === 1 ? Color.White : Color.Black)
          .borderRadius({ topRight: 8, bottomRight: 8 })
          .onClick(() => {
            this.onTabChange(1)
          })
      }
      .width('100%')
      .border({ width: 1, color: Color.Grey })
      .borderRadius(8)
      .margin({ left: 16, right: 16, bottom: 12 })
      
      // 内容区域
      if (this.loading) {
        Column() {
          LoadingProgress()
            .width(40)
            .height(40)
            .color('#0A59F7')
          
          Text('加载中...')
            .fontSize(14)
            .fontColor('#666666')
            .margin({ top: 12 })
        }
        .width('100%')
        .height('100%')
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
      } else {
        Scroll() {
          Column() {
            if (this.currentTab === 0) {
              if (this.spotList.length === 0) {
                Column() {
                  Text('暂无景点数据')
                    .fontSize(14)
                    .fontColor('#999999')
                }
                .width('100%')
                .padding(40)
                .justifyContent(FlexAlign.Center)
                .alignItems(HorizontalAlign.Center)
              } else {
                ForEach(this.spotList, (spot: SceneSpot) => {
                  this.SpotCardBuilder(spot)
                }, (spot: SceneSpot) => `${spot.id}`)
              }
            } else {
              if (this.merchantList.length === 0) {
                Column() {
                  Text('暂无商家数据')
                    .fontSize(14)
                    .fontColor('#999999')
                }
                .width('100%')
                .padding(40)
                .justifyContent(FlexAlign.Center)
                .alignItems(HorizontalAlign.Center)
              } else {
                ForEach(this.merchantList, (merchant: SceneMerchant) => {
                  this.MerchantCardBuilder(merchant)
                }, (merchant: SceneMerchant) => `${merchant.id}`)
              }
            }
          }
          .width('100%')
          .padding({ left: 16, right: 16, top: 12, bottom: 12 })
        }
        .width('100%')
        .layoutWeight(1)
        .scrollable(ScrollDirection.Vertical)
        .scrollBar(BarState.Auto)
      }
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#F5F5F5')
    .onAppear(() => {
      // 页面显示时加载默认数据
      if (this.currentTab === 0) {
        this.loadSpotList()
      } else {
        this.loadMerchantList()
      }
    })
  }
}
