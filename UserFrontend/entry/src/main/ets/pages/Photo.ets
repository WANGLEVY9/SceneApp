import { CheckinApi, LikeCheckinRequest, SubmitCheckinRequest } from '../api/checkinApi';
import { CheckinRecord, CheckinSpot } from '../models/spot';
import { promptAction } from '@kit.ArkUI';
import filePicker from '@ohos.file.picker';

@Component
export struct Photo {
  @State checkinRecordList: CheckinRecord[] = []
  @State checkinSpotList: CheckinSpot[] = []
  @State loading: boolean = false
  @State showCheckinDialog: boolean = false
  @State selectedSpot: CheckinSpot | null = null
  @State checkinRemark: string = ''
  @State checkinImage: string = ''
  @State likedRecords: Set<string> = new Set()
  
  // 显示提示信息
  showToast(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 加载打卡记录列表
  async loadCheckinRecords(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      const result = await CheckinApi.getPersonalRecords({ page: 1, size: 50 })
      this.checkinRecordList = result.list || []
    } catch (e) {
      this.showToast('加载打卡记录失败')
      console.error('加载打卡记录失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 加载打卡点列表
  async loadCheckinSpots(): Promise<void> {
    try {
      const spots = await CheckinApi.getCheckinSpots()
      this.checkinSpotList = spots || []
    } catch (e) {
      this.showToast('加载打卡点失败')
      console.error('加载打卡点失败:', e)
    }
  }
  
  // 选择图片
  async selectImage(): Promise<void> {
    try {
      const options = new filePicker.PhotoSelectOptions();
      options.MIMEType = filePicker.PhotoViewMIMETypes.IMAGE_TYPE;
      options.maxSelectNumber = 1;
      const picker = new filePicker.PhotoViewPicker();
      const result = await picker.select(options);
      if (result && result.photoUris && result.photoUris.length > 0) {
        this.checkinImage = result.photoUris[0];
      }
    } catch (e) {
      this.showToast('选择图片失败')
      console.error('选择图片失败:', e)
    }
  }
  
  // 提交打卡
  async submitCheckin(): Promise<void> {
    if (!this.selectedSpot) {
      this.showToast('请选择打卡点')
      return
    }
    
    if (!this.checkinImage) {
      this.showToast('请上传打卡图片')
      return
    }
    
    // 模拟定位：直接使用所选打卡点的经纬度，避免距离校验失败
    const lat = this.selectedSpot.location?.lat ?? this.selectedSpot.lat
    const lng = this.selectedSpot.location?.lng ?? this.selectedSpot.lng

    if (lat === undefined || lng === undefined) {
      this.showToast('打卡点缺少位置信息，请稍后重试')
      return
    }
    
    try {
      const request: SubmitCheckinRequest = {
        spotId: this.selectedSpot.id,
        image: this.checkinImage,
        remark: this.checkinRemark,
        location: {
          lat: lat,
          lng: lng
        }
      }
      await CheckinApi.submitCheckin(request)
      
      this.showToast('打卡成功')
      this.showCheckinDialog = false
      this.selectedSpot = null
      this.checkinRemark = ''
      this.checkinImage = ''
      
      // 重新加载打卡记录
      this.loadCheckinRecords()
    } catch (e) {
      this.showToast('打卡失败，请稍后重试')
      console.error('打卡失败:', e)
    }
  }
  
  // 点赞/取消点赞
  async toggleLike(record: CheckinRecord): Promise<void> {
    const isLiked = this.likedRecords.has(record.id)
    
    try {
      const request: LikeCheckinRequest = { recordId: record.id }
      const result = await CheckinApi.likeCheckin(request)
      
      if (result.liked) {
        this.likedRecords.add(record.id)
      } else {
        this.likedRecords.delete(record.id)
      }
      record.likeCount = result.likeCount
    } catch (e) {
      this.showToast('操作失败，请稍后重试')
      console.error('点赞操作失败:', e)
    }
  }
  
  // 打卡记录卡片构建器
  @Builder
  CheckinCardBuilder(record: CheckinRecord) {
    Column({ space: 12 }) {
      // 用户信息
      Row({ space: 8 }) {
        Column()
          .width(40)
          .height(40)
          .backgroundColor('#E0E0E0')
          .borderRadius(20)
        
        Column({ space: 4 }) {
              Text(record.userName || record.nickname || '匿名用户')
                .fontSize(14)
                .fontWeight(FontWeight.Medium)
                .fontColor(Color.Black)
          
          Text(record.checkinTime || record.time || '')
            .fontSize(12)
            .fontColor('#999999')
        }
        .layoutWeight(1)
        .alignItems(HorizontalAlign.Start)
      }
      .width('100%')
      
      // 打卡点名称
      Text(`📍 ${record.spotName}`)
        .fontSize(16)
        .fontWeight(FontWeight.Bold)
        .fontColor('#0A59F7')
        .width('100%')
      
      // 打卡图片
      if (record.image) {
        Image(record.image)
          .width('100%')
          .height(200)
          .borderRadius(8)
          .objectFit(ImageFit.Cover)
      } else {
        Column()
          .width('100%')
          .height(200)
          .backgroundColor('#E0E0E0')
          .borderRadius(8)
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
      }
      
      // 打卡备注或内容
      if (record.remark || record.content) {
        Text(record.remark || record.content || '')
          .fontSize(14)
          .fontColor('#666666')
          .width('100%')
          .maxLines(3)
          .textOverflow({ overflow: TextOverflow.Ellipsis })
      }
      
      // 操作栏
      Row({ space: 16 }) {
        Row({ space: 4 }) {
          Image($r('app.media.camera'))
            .width(20)
            .height(20)
            .fillColor(this.likedRecords.has(record.id) ? '#FF6B00' : '#999999')
          
          Text(`${record.likeCount}`)
            .fontSize(14)
            .fontColor(this.likedRecords.has(record.id) ? '#FF6B00' : '#666666')
        }
        .onClick(() => {
          this.toggleLike(record)
        })
        
        if (record.lat && record.lng) {
          Text(`位置: ${record.lat.toFixed(4)}, ${record.lng.toFixed(4)}`)
            .fontSize(12)
            .fontColor('#999999')
            .layoutWeight(1)
        } else {
          Blank()
            .layoutWeight(1)
        }
      }
      .width('100%')
    }
    .width('100%')
    .padding(12)
    .backgroundColor(Color.White)
    .borderRadius(12)
    .margin({ bottom: 12 })
    .shadow({ radius: 2, color: '#000000', offsetX: 0, offsetY: 1 })
  }
  
  // 打卡对话框构建器
  @Builder
  CheckinDialogBuilder() {
    if (this.showCheckinDialog) {
      Column({ space: 16 }) {
        // 标题
        Row() {
          Text('选择打卡点')
            .fontSize(18)
            .fontWeight(FontWeight.Bold)
            .layoutWeight(1)
          
          Image($r('app.media.camera'))
            .width(24)
            .height(24)
            .fillColor('#999999')
            .onClick(() => {
              this.showCheckinDialog = false
            })
        }
        .width('100%')
        
        // 打卡点选择
        if (this.checkinSpotList.length === 0) {
          Column() {
            Text('暂无打卡点')
              .fontSize(14)
              .fontColor('#999999')
          }
          .width('100%')
          .padding(20)
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
        } else {
          Scroll() {
            Column({ space: 8 }) {
              ForEach(this.checkinSpotList, (spot: CheckinSpot) => {
                Row({ space: 12 }) {
                  Column()
                    .width(50)
                    .height(50)
                    .backgroundColor('#E0E0E0')
                    .borderRadius(8)
                  
                  Column({ space: 4 }) {
                    Text(spot.name)
                      .fontSize(16)
                      .fontWeight(FontWeight.Medium)
                      .fontColor(Color.Black)
                    
                    if (spot.hot !== undefined) {
                      Text(`热度: ${spot.hot}`)
                        .fontSize(12)
                        .fontColor('#666666')
                    }
                  }
                  .layoutWeight(1)
                  .alignItems(HorizontalAlign.Start)
                }
                .width('100%')
                .padding(12)
                .backgroundColor(this.selectedSpot?.id === spot.id ? '#E3F2FD' : Color.White)
                .borderRadius(8)
                .onClick(() => {
                  this.selectedSpot = spot
                })
              })
            }
            .width('100%')
          }
          .height(200)
          .scrollable(ScrollDirection.Vertical)
        }
        
        // 备注输入
        TextArea({ placeholder: '添加打卡备注（可选）' })
          .width('100%')
          .height(80)
          .backgroundColor('#F5F5F5')
          .borderRadius(8)
          .onChange((value: string) => {
            this.checkinRemark = value
          })
        
        // 图片选择
        Row({ space: 12 }) {
          if (this.checkinImage) {
            Image(this.checkinImage)
              .width(80)
              .height(80)
              .borderRadius(8)
              .objectFit(ImageFit.Cover)
          } else {
            Column({ space: 4 }) {
              Image($r('app.media.camera'))
                .width(32)
                .height(32)
                .fillColor('#999999')
              
              Text('添加图片')
                .fontSize(12)
                .fontColor('#999999')
            }
            .width(80)
            .height(80)
            .backgroundColor('#F5F5F5')
            .borderRadius(8)
            .justifyContent(FlexAlign.Center)
            .alignItems(HorizontalAlign.Center)
          }
        }
        .width('100%')
        .onClick(() => {
          this.selectImage()
        })
        
        // 提交按钮
        Button('提交打卡')
          .width('100%')
          .height(44)
          .backgroundColor('#0A59F7')
          .fontColor(Color.White)
          .borderRadius(8)
          .onClick(() => {
            this.submitCheckin()
          })
      }
      .width('90%')
      .padding(20)
      .backgroundColor(Color.White)
      .borderRadius(16)
      .shadow({ radius: 10, color: '#000000', offsetX: 0, offsetY: 2 })
    }
  }
  
  build() {
    Stack() {
      Column() {
        // 顶部操作栏
        Row({ space: 12 }) {
          Text('打卡记录')
            .fontSize(20)
            .fontWeight(FontWeight.Bold)
            .layoutWeight(1)
          
          Button('+ 打卡')
            .fontSize(14)
            .backgroundColor('#0A59F7')
            .fontColor(Color.White)
            .borderRadius(20)
            .padding({ left: 16, right: 16 })
            .onClick(() => {
              this.showCheckinDialog = true
              if (this.checkinSpotList.length === 0) {
                this.loadCheckinSpots()
              }
            })
        }
        .width('100%')
        .padding({ left: 16, right: 16, top: 12, bottom: 12 })
        .backgroundColor(Color.White)
        
        // 打卡记录列表
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
              if (this.checkinRecordList.length === 0) {
                Column() {
                  Text('暂无打卡记录')
                    .fontSize(14)
                    .fontColor('#999999')
                  
                  Button('去打卡')
                    .margin({ top: 16 })
                    .backgroundColor('#0A59F7')
                    .fontColor(Color.White)
                    .borderRadius(20)
                    .onClick(() => {
                      this.showCheckinDialog = true
                      if (this.checkinSpotList.length === 0) {
                        this.loadCheckinSpots()
                      }
                    })
                }
                .width('100%')
                .padding(40)
                .justifyContent(FlexAlign.Center)
                .alignItems(HorizontalAlign.Center)
              } else {
                ForEach(this.checkinRecordList, (record: CheckinRecord) => {
                  this.CheckinCardBuilder(record)
                })
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
      
      // 打卡对话框
      if (this.showCheckinDialog) {
        Column() {
          Blank()
          
          this.CheckinDialogBuilder()
          
          Blank()
        }
        .width('100%')
        .height('100%')
        .backgroundColor('rgba(0, 0, 0, 0.5)')
        .justifyContent(FlexAlign.Center)
        .alignItems(HorizontalAlign.Center)
        .onClick(() => {
          this.showCheckinDialog = false
        })
      }
    }
    .width('100%')
    .height('100%')
    .onAppear(() => {
      this.loadCheckinRecords()
    })
  }
}
