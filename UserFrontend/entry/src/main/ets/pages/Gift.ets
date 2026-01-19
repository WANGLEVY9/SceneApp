import { GiftApi, ExchangeGiftRequest } from '../api/giftApi';
import { UserApi } from '../api/userApi';
import { GiftInfo, GiftExchangeRecord, PointsRecord } from '../models/gift';
import { User,LoginResponse } from '../models/user';
import { PageQuery } from '../models/common';
import { promptAction } from '@kit.ArkUI';

@Component
export struct Gift {
  @State currentTab: number = 0 // 0-礼品商城，1-兑换记录，2-积分记录
  @State giftList: GiftInfo[] = []
  @State exchangeRecordList: GiftExchangeRecord[] = []
  @State pointsRecordList: PointsRecord[] = []
  @State currentUser: LoginResponse | null = null
  @State loading: boolean = false
  @State showExchangeDialog: boolean = false
  @State selectedGift: GiftInfo | null = null
  @State exchangeNumber: number = 1
  
  // 显示提示信息
  showToast(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 加载当前用户信息
  async loadCurrentUser(): Promise<void> {
    try {
      this.currentUser = await UserApi.getCurrentUser()
    } catch (e) {
      console.error('加载用户信息失败:', e)
    }
  }
  
  // 加载礼品列表
  async loadGiftList(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      this.giftList = await GiftApi.getGiftList()
    } catch (e) {
      this.showToast('加载礼品列表失败')
      console.error('加载礼品列表失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 加载兑换记录
  async loadExchangeRecords(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      const params: PageQuery = { page: 1, size: 50 }
      const result = await GiftApi.getExchangeRecordList(params)
      this.exchangeRecordList = result.records || []
    } catch (e) {
      this.showToast('加载兑换记录失败')
      console.error('加载兑换记录失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 加载积分记录
  async loadPointsRecords(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      const params: PageQuery = { page: 1, size: 50 }
      const result = await GiftApi.getPointsRecordList(params)
      this.pointsRecordList = result.records || []
    } catch (e) {
      this.showToast('加载积分记录失败')
      console.error('加载积分记录失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 切换标签页
  onTabChange(index: number): void {
    this.currentTab = index
    if (index === 0 && this.giftList.length === 0) {
      this.loadGiftList()
    } else if (index === 1 && this.exchangeRecordList.length === 0) {
      this.loadExchangeRecords()
    } else if (index === 2 && this.pointsRecordList.length === 0) {
      this.loadPointsRecords()
    }
  }
  
  // 打开兑换对话框
  openExchangeDialog(gift: GiftInfo): void {
    if (gift.status !== 1) {
      this.showToast('该礼品暂不可兑换')
      return
    }
    
    if (gift.stock <= 0) {
      this.showToast('该礼品已售罄')
      return
    }
    
    if (!this.currentUser || this.currentUser.points < gift.points) {
      this.showToast('积分不足')
      return
    }
    
    this.selectedGift = gift
    this.exchangeNumber = 1
    this.showExchangeDialog = true
  }
  
  // 兑换礼品
  async exchangeGift(): Promise<void> {
    if (!this.selectedGift) {
      return
    }
    
    const totalPoints = this.selectedGift.points * this.exchangeNumber
    
    if (!this.currentUser || this.currentUser.points < totalPoints) {
      this.showToast('积分不足')
      return
    }
    
    if (this.selectedGift.stock < this.exchangeNumber) {
      this.showToast('库存不足')
      return
    }
    
    try {
      const request: ExchangeGiftRequest = {
        giftId: this.selectedGift.id,
        number: this.exchangeNumber
      }
      await GiftApi.exchangeGift(request)
      
      this.showToast('兑换成功')
      this.showExchangeDialog = false
      this.selectedGift = null
      
      // 重新加载数据
      this.loadCurrentUser()
      this.loadGiftList()
      this.loadExchangeRecords()
    } catch (e) {
      this.showToast('兑换失败，请稍后重试')
      console.error('兑换失败:', e)
    }
  }
  
  // 获取状态文本
  getStatusText(status: string): string {
    const statusMap: Record<string, string> = {
      'pending': '待审核',
      'approved': '已核销',
      'delivered': '已发货',
      'received': '已收货',
      'rejected': '已拒绝'
    }
    return statusMap[status] || status
  }
  
  // 获取状态颜色
  getStatusColor(status: string): ResourceColor {
    const colorMap: Record<string, ResourceColor> = {
      'pending': '#FF9800',
      'approved': '#2196F3',
      'delivered': '#4CAF50',
      'received': '#4CAF50',
      'rejected': '#F44336'
    }
    return colorMap[status] || Color.Black
  }
  
  // 礼品卡片构建器
  @Builder
  GiftCardBuilder(gift: GiftInfo) {
    Column({ space: 12 }) {
      // 礼品图片
      if (gift.image) {
        Image(gift.image)
          .width('100%')
          .height(150)
          .borderRadius(8)
          .objectFit(ImageFit.Cover)
      } else {
        Column()
          .width('100%')
          .height(150)
          .backgroundColor('#E0E0E0')
          .borderRadius(8)
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
      }
      
      // 礼品信息
      Column({ space: 6 }) {
        Text(gift.name)
          .fontSize(16)
          .fontWeight(FontWeight.Medium)
          .fontColor(Color.Black)
          .maxLines(1)
          .textOverflow({ overflow: TextOverflow.Ellipsis })
        
        if (gift.desc) {
          Text(gift.desc)
            .fontSize(12)
            .fontColor('#666666')
            .maxLines(2)
            .textOverflow({ overflow: TextOverflow.Ellipsis })
        }
        
        Row({ space: 8 }) {
          Text(`${gift.points}积分`)
            .fontSize(14)
            .fontWeight(FontWeight.Bold)
            .fontColor('#FF6B00')
          
          Text(`库存: ${gift.stock}`)
            .fontSize(12)
            .fontColor('#999999')
            .layoutWeight(1)
        }
        .width('100%')
        
        Button(gift.status === 1 && gift.stock > 0 ? '立即兑换' : '暂不可兑换')
          .width('100%')
          .height(36)
          .fontSize(14)
          .backgroundColor(gift.status === 1 && gift.stock > 0 ? '#0A59F7' : Color.Grey)
          .fontColor(Color.White)
          .borderRadius(8)
          .enabled(gift.status === 1 && gift.stock > 0)
          .onClick(() => {
            this.openExchangeDialog(gift)
          })
      }
      .width('100%')
      .alignItems(HorizontalAlign.Start)
    }
    .width('100%')
    .padding(12)
    .backgroundColor(Color.White)
    .borderRadius(12)
    .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
  }

  // 兑换对话框
  @Builder
  ExchangeDialogBuilder(gift: GiftInfo) {
    Column() {
      Blank()
      
      Column({ space: 16 }) {
        Text('兑换礼品')
          .fontSize(18)
          .fontWeight(FontWeight.Bold)
          .width('100%')
        
        Row({ space: 12 }) {
          Column()
            .width(80)
            .height(80)
            .backgroundColor('#E0E0E0')
            .borderRadius(8)
          
          Column({ space: 6 }) {
            Text(gift.name)
              .fontSize(16)
              .fontWeight(FontWeight.Medium)
              .fontColor(Color.Black)
            
            Text(`${gift.points}积分/件`)
              .fontSize(14)
              .fontColor('#666666')
            
            Text(`库存: ${gift.stock}`)
              .fontSize(12)
              .fontColor('#999999')
          }
          .layoutWeight(1)
          .alignItems(HorizontalAlign.Start)
        }
        .width('100%')
        
        Row({ space: 12 }) {
          Text('兑换数量:')
            .fontSize(14)
            .fontColor(Color.Black)
          
          Button('-')
            .width(36)
            .height(36)
            .backgroundColor('#F5F5F5')
            .fontColor(Color.Black)
            .borderRadius(18)
            .enabled(this.exchangeNumber > 1)
            .onClick(() => {
              if (this.exchangeNumber > 1) {
                this.exchangeNumber--
              }
            })
          
          Text(this.exchangeNumber.toString())
            .fontSize(16)
            .fontWeight(FontWeight.Medium)
            .width(40)
            .textAlign(TextAlign.Center)
          
          Button('+')
            .width(36)
            .height(36)
            .backgroundColor('#F5F5F5')
            .fontColor(Color.Black)
            .borderRadius(18)
            .enabled(this.exchangeNumber < gift.stock)
            .onClick(() => {
              if (this.exchangeNumber < gift.stock) {
                this.exchangeNumber++
              }
            })
          
          Text(`共需: ${gift.points * this.exchangeNumber}积分`)
            .fontSize(14)
            .fontColor('#FF6B00')
            .layoutWeight(1)
            .textAlign(TextAlign.End)
        }
        .width('100%')
        .alignItems(VerticalAlign.Center)
        
        Row({ space: 12 }) {
          Button('取消')
            .layoutWeight(1)
            .height(44)
            .backgroundColor('#F5F5F5')
            .fontColor(Color.Black)
            .borderRadius(8)
            .onClick(() => {
              this.showExchangeDialog = false
              this.selectedGift = null
            })
          
          Button('确认兑换')
            .layoutWeight(1)
            .height(44)
            .backgroundColor('#0A59F7')
            .fontColor(Color.White)
            .borderRadius(8)
            .onClick(() => {
              this.exchangeGift()
            })
        }
        .width('100%')
      }
      .width('90%')
      .padding(20)
      .backgroundColor(Color.White)
      .borderRadius(16)
      .shadow({ radius: 10, color: '#000000', offsetX: 0, offsetY: 2 })
      
      Blank()
    }
    .width('100%')
    .height('100%')
    .backgroundColor('rgba(0, 0, 0, 0.5)')
    .justifyContent(FlexAlign.Center)
    .alignItems(HorizontalAlign.Center)
    .onClick(() => {
      this.showExchangeDialog = false
      this.selectedGift = null
    })
  }
  
  // 兑换记录卡片构建器
  @Builder
  ExchangeRecordCardBuilder(record: GiftExchangeRecord) {
    Row({ space: 12 }) {
      Column()
        .width(60)
        .height(60)
        .backgroundColor('#E0E0E0')
        .borderRadius(8)
      
      Column({ space: 6 }) {
        Row() {
          Text(record.giftName)
            .fontSize(16)
            .fontWeight(FontWeight.Medium)
            .fontColor(Color.Black)
            .layoutWeight(1)
          
          Text(this.getStatusText(record.status))
            .fontSize(12)
            .fontColor(this.getStatusColor(record.status))
            .padding({ left: 8, right: 8, top: 4, bottom: 4 })
            .backgroundColor('#F5F5F5')
            .borderRadius(12)
        }
        .width('100%')
        
        Text(`数量: ${record.giftNumber} | 消耗积分: ${record.points}`)
          .fontSize(12)
          .fontColor('#666666')
        
        Text(`申请时间: ${record.applyTime}`)
          .fontSize(12)
          .fontColor('#999999')
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
  }
  
  // 积分记录卡片构建器
  @Builder
  PointsRecordCardBuilder(record: PointsRecord) {
    Row({ space: 12 }) {
      Column({ space: 6 }) {
        Row() {
          Text(this.getPointsTypeText(record.type))
            .fontSize(16)
            .fontWeight(FontWeight.Medium)
            .fontColor(Color.Black)
            .layoutWeight(1)
          
          Text(record.points > 0 ? `+${record.points}` : `${record.points}`)
            .fontSize(16)
            .fontWeight(FontWeight.Bold)
            .fontColor(record.points > 0 ? '#4CAF50' : '#F44336')
        }
        .width('100%')
        
        if (record.remark) {
          Text(record.remark)
            .fontSize(12)
            .fontColor('#666666')
            .width('100%')
        }
        
        Text(record.createTime)
          .fontSize(12)
          .fontColor('#999999')
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
  }
  
  // 获取积分类型文本
  getPointsTypeText(type: string): string {
    const typeMap: Record<string, string> = {
      'checkin': '打卡',
      'like': '点赞',
      'comment': '评论',
      'be_liked': '被点赞',
      'exchange': '礼品兑换'
    }
    return typeMap[type] || type
  }
  
  build() {
    Stack({ alignContent: Alignment.Center }) {
      Column() {
        // 积分显示卡片
        Row({ space: 16 }) {
          Column({ space: 8 }) {
            Text('我的积分')
              .fontSize(14)
              .fontColor('#666666')
            
            Text(this.currentUser?.points?.toString() || '0')
              .fontSize(32)
              .fontWeight(FontWeight.Bold)
              .fontColor('#0A59F7')
          }
          .layoutWeight(1)
          .alignItems(HorizontalAlign.Start)
          
          Column({ space: 8 }) {
            Text('可兑换礼品')
              .fontSize(14)
              .fontColor('#666666')
            
            Text(this.giftList.filter(g => g.status === 1 && g.stock > 0).length.toString())
              .fontSize(32)
              .fontWeight(FontWeight.Bold)
              .fontColor('#FF6B00')
          }
          .layoutWeight(1)
          .alignItems(HorizontalAlign.Start)
        }
        .width('100%')
        .padding(20)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .margin({ left: 16, right: 16, top: 12, bottom: 12 })
        .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
        
        // 标签页切换
        Row({ space: 0 }) {
          Button('礼品商城')
            .layoutWeight(1)
            .height(44)
            .fontSize(14)
            .backgroundColor(this.currentTab === 0 ? '#0A59F7' : Color.Transparent)
            .fontColor(this.currentTab === 0 ? Color.White : Color.Black)
            .borderRadius({ topLeft: 8, bottomLeft: 8 })
            .onClick(() => {
              this.onTabChange(0)
            })
          
          Button('兑换记录')
            .layoutWeight(1)
            .height(44)
            .fontSize(14)
            .backgroundColor(this.currentTab === 1 ? '#0A59F7' : Color.Transparent)
            .fontColor(this.currentTab === 1 ? Color.White : Color.Black)
            .onClick(() => {
              this.onTabChange(1)
            })
          
          Button('积分记录')
            .layoutWeight(1)
            .height(44)
            .fontSize(14)
            .backgroundColor(this.currentTab === 2 ? '#0A59F7' : Color.Transparent)
            .fontColor(this.currentTab === 2 ? Color.White : Color.Black)
            .borderRadius({ topRight: 8, bottomRight: 8 })
            .onClick(() => {
              this.onTabChange(2)
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
          .layoutWeight(1)
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
        } else {
          Scroll() {
            Column() {
              if (this.currentTab === 0) {
                if (this.giftList.length === 0) {
                  Column() {
                    Text('暂无礼品')
                      .fontSize(14)
                      .fontColor('#999999')
                  }
                  .width('100%')
                  .padding(40)
                  .justifyContent(FlexAlign.Center)
                  .alignItems(HorizontalAlign.Center)
                } else {
                  Grid() {
                    ForEach(this.giftList, (gift: GiftInfo) => {
                      GridItem() {
                        this.GiftCardBuilder(gift)
                      }
                    })
                  }
                  .columnsTemplate('1fr 1fr')
                  .columnsGap(12)
                  .rowsGap(12)
                  .width('100%')
                  .padding({ left: 16, right: 16 })
                }
              } else if (this.currentTab === 1) {
                if (this.exchangeRecordList.length === 0) {
                  Column() {
                    Text('暂无兑换记录')
                      .fontSize(14)
                      .fontColor('#999999')
                  }
                  .width('100%')
                  .padding(40)
                  .justifyContent(FlexAlign.Center)
                  .alignItems(HorizontalAlign.Center)
                } else {
                  Column() {
                    ForEach(this.exchangeRecordList, (record: GiftExchangeRecord) => {
                      this.ExchangeRecordCardBuilder(record)
                    })
                  }
                  .width('100%')
                  .padding({ left: 16, right: 16 })
                }
              } else {
                if (this.pointsRecordList.length === 0) {
                  Column() {
                    Text('暂无积分记录')
                      .fontSize(14)
                      .fontColor('#999999')
                  }
                  .width('100%')
                  .padding(40)
                  .justifyContent(FlexAlign.Center)
                  .alignItems(HorizontalAlign.Center)
                } else {
                  Column() {
                    ForEach(this.pointsRecordList, (record: PointsRecord) => {
                      this.PointsRecordCardBuilder(record)
                    })
                  }
                  .width('100%')
                  .padding({ left: 16, right: 16 })
                }
              }
            }
            .width('100%')
            .padding({ bottom: 12 })
          }
          .width('100%')
          .layoutWeight(1)
          .scrollable(ScrollDirection.Vertical)
          .scrollBar(BarState.Auto)
        }
      }
      .width('100%')
      .height('100%')
      .onAppear(() => {
        this.loadCurrentUser()
        this.loadGiftList()
      })
      .backgroundColor('#F5F5F5')
      
      // 兑换对话框
      if (this.showExchangeDialog && this.selectedGift) {
        this.ExchangeDialogBuilder(this.selectedGift)
      }
    }
    .width('100%')
    .height('100%')
  }
}
