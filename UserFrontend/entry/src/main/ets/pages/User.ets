import { UpdateProfileRequest, UserApi } from '../api/userApi';
import { HttpUtil } from '../utils/http';
import { LoginResponse as LoginResponseModel } from '../models/user';
import { promptAction } from '@kit.ArkUI';

@Component
export struct User {
  @State currentUser: LoginResponseModel | null = null
  @State loading: boolean = false
  @State showEditDialog: boolean = false
  @State editNickname: string = ''
  @State editPhone: string = ''
  @State oldPassword: string = ''
  @State newPassword: string = ''
  @State confirmPassword: string = ''
  @State isFirstLogin: boolean = false
  @State isChangingPassword: boolean = false
  pathStack: NavPathStack = new NavPathStack()
  
  // 显示提示信息
  showToast(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 加载用户信息
  async loadUserInfo(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    try {
      this.currentUser = await UserApi.getCurrentUser()
      if (this.currentUser) {
        // 当用户名为默认生成的 user_xxx 或手机号缺失时，视为首次登录需要设置密码
        const username = this.currentUser.username || ''
        this.isFirstLogin = username.startsWith('user_') || !this.currentUser.phone
        this.editNickname = this.currentUser.nickname || ''
        this.editPhone = this.currentUser.phone || this.currentUser.username || ''
      }
    } catch (e) {
      this.showToast('加载用户信息失败')
      console.error('加载用户信息失败:', e)
    } finally {
      this.loading = false
    }
  }
  
  // 打开编辑对话框
  openEditDialog(): void {
    if (this.currentUser) {
      this.editNickname = this.currentUser.nickname || ''
      this.editPhone = this.currentUser.phone || this.currentUser.username || ''
      this.oldPassword = ''
      this.newPassword = ''
      this.confirmPassword = ''
      this.isChangingPassword = this.isFirstLogin
      this.showEditDialog = true
    }
  }
  
  // 保存用户信息
  async saveUserInfo(): Promise<void> {
    if (!this.currentUser) {
      return
    }
    
    try {
      const nickname = this.editNickname.trim()
      const phone = this.editPhone.trim()
      const oldPwd = this.oldPassword.trim()
      const newPwd = this.newPassword.trim()
      const confirmPwd = this.confirmPassword.trim()

      if (!nickname) {
        this.showToast('昵称不能为空')
        return
      }

      if (!phone || phone.length !== 11) {
        this.showToast('请输入正确的手机号')
        return
      }

      // 首次登录需要直接设置账号和密码
      if (this.isFirstLogin) {
        if (!newPwd || !confirmPwd) {
          this.showToast('请填写密码并确认')
          return
        }
        if (newPwd !== confirmPwd) {
          this.showToast('两次输入的密码不一致')
          return
        }
        // 设置账号密码
        await UserApi.setupAccount({ username: phone, password: newPwd })
        // 更新昵称
        await UserApi.updateProfile({ nickname })
        this.showToast('账号已设置')
      } else {
        // 非首次登录按需更新
        const updateTasks: Array<Promise<void>> = []

        if (nickname !== (this.currentUser.nickname || '')) {
          updateTasks.push(UserApi.updateProfile({ nickname }))
        }

        if (phone && phone !== this.currentUser.phone && phone !== this.currentUser.username) {
          updateTasks.push(UserApi.changeUsername({ username: phone }))
        }

        if (this.isChangingPassword) {
          if (!newPwd || !confirmPwd) {
            this.showToast('请填写新密码并确认')
            return
          }
          if (newPwd !== confirmPwd) {
            this.showToast('两次输入的密码不一致')
            return
          }

          // 如果旧密码为空，视为首次补充密码，调用 setupAccount
          if (!oldPwd) {
            updateTasks.push(UserApi.setupAccount({ username: phone, password: newPwd }))
          } else {
            updateTasks.push(UserApi.changePassword({ oldPassword: oldPwd, newPassword: newPwd }))
          }
        }

        if (updateTasks.length === 0) {
          this.showToast('没有需要保存的修改')
          return
        }

        for (const task of updateTasks) {
          await task
        }
        this.showToast('保存成功')
      }

      // 重新加载用户信息并关闭弹窗
      await this.loadUserInfo()
      this.isFirstLogin = false
      this.showEditDialog = false
    } catch (e) {
      this.showToast('保存失败，请稍后重试')
      console.error('保存用户信息失败:', e)
    }
  }
  
  // 退出登录
  async logout(): Promise<void> {
    try {
      await HttpUtil.clearToken()
      this.showToast('已退出登录')
      setTimeout(() => {
        this.pathStack.clear()
        this.pathStack.pushPathByName('Ad', null, false)
      }, 300)
    } catch (e) {
      this.showToast('退出登录失败')
      console.error('退出登录失败:', e)
    }
  }
  
  build() {
    NavDestination() {
      Stack() {
        // 主体内容
        Column() {
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
                // 用户信息卡片
                Column({ space: 16 }) {
                  Row({ space: 16 }) {
                    // 头像
                    Column()
                      .width(80)
                      .height(80)
                      .borderRadius(40)
                      .backgroundColor('#E0E0E0')
                      .justifyContent(FlexAlign.Center)
                      .alignItems(HorizontalAlign.Center)
                    
                    // 用户信息
                    Column({ space: 8 }) {
                      Text(this.currentUser?.nickname || this.currentUser?.username || '未设置昵称')
                        .fontSize(20)
                        .fontWeight(FontWeight.Bold)
                        .fontColor(Color.Black)
                      
                      Text(`用户名: ${this.currentUser?.username || ''}`)
                        .fontSize(14)
                        .fontColor('#666666')
                      
                      if (this.currentUser?.phone) {
                        Text(`手机号: ${this.currentUser.phone}`)
                          .fontSize(14)
                          .fontColor('#666666')
                      }
                    }
                    .layoutWeight(1)
                    .alignItems(HorizontalAlign.Start)
                  }
                  .width('100%')
                  
                  // 积分显示
                  Row({ space: 24 }) {
                    Column({ space: 4 }) {
                      Text('我的积分')
                        .fontSize(14)
                        .fontColor('#666666')
                      
                      Text(this.currentUser?.points?.toString() || '0')
                        .fontSize(24)
                        .fontWeight(FontWeight.Bold)
                        .fontColor('#FF6B00')
                    }
                    .layoutWeight(1)
                    .alignItems(HorizontalAlign.Center)
                    
                    Divider()
                      .vertical(true)
                      .height(40)
                      .color('#E0E0E0')
                  }
                  .width('100%')
                  .padding({ top: 16 })
                }
                .width('100%')
                .padding(20)
                .backgroundColor(Color.White)
                .margin({ top: 12 })
                
                // 功能菜单
                Column({ space: 0 }) {
                  // 编辑资料
                  Row({ space: 12 }) {
                    Image($r('app.media.user'))
                      .width(24)
                      .height(24)
                      .fillColor('#0A59F7')
                    
                    Text('编辑资料')
                      .fontSize(16)
                      .fontColor(Color.Black)
                      .layoutWeight(1)
                    
                    Image($r('app.media.user'))
                      .width(16)
                      .height(16)
                      .fillColor('#999999')
                  }
                  .width('100%')
                  .height(56)
                  .padding({ left: 16, right: 16 })
                  .backgroundColor(Color.White)
                  .onClick(() => {
                    this.openEditDialog()
                  })
                  
                  Divider()
                    .color('#E0E0E0')
                    .margin({ left: 52 })
                  
                  // 关于我们
                  Row({ space: 12 }) {
                    Image($r('app.media.user'))
                      .width(24)
                      .height(24)
                      .fillColor('#0A59F7')
                    
                    Text('关于我们')
                      .fontSize(16)
                      .fontColor(Color.Black)
                      .layoutWeight(1)
                    
                    Image($r('app.media.user'))
                      .width(16)
                      .height(16)
                      .fillColor('#999999')
                  }
                  .width('100%')
                  .height(56)
                  .padding({ left: 16, right: 16 })
                  .backgroundColor(Color.White)
                  .onClick(() => {
                    this.showToast('景区导览App v1.0.0')
                  })
                  
                  Divider()
                    .color('#E0E0E0')
                    .margin({ left: 52 })
                  
                  // 退出登录
                  Row({ space: 12 }) {
                    Image($r('app.media.user'))
                      .width(24)
                      .height(24)
                      .fillColor('#F44336')
                    
                    Text('退出登录')
                      .fontSize(16)
                      .fontColor('#F44336')
                      .layoutWeight(1)
                  }
                  .width('100%')
                  .height(56)
                  .padding({ left: 16, right: 16 })
                  .backgroundColor(Color.White)
                  .onClick(() => {
                    this.logout()
                  })
                }
                .width('100%')
                .margin({ top: 12 })
                .borderRadius(12)
                .backgroundColor(Color.White)
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
        .backgroundColor('#F5F5F5')
        .onAppear(() => {
          this.loadUserInfo()
        })

        // 编辑对话框覆盖层
        if (this.showEditDialog) {
          Column() {
            Blank()
            
            Column({ space: 16 }) {
              Text('编辑资料')
                .fontSize(18)
                .fontWeight(FontWeight.Bold)
                .width('100%')
              
              Column({ space: 8 }) {
                Text('昵称')
                  .fontSize(14)
                  .fontColor('#666666')
                  .width('100%')
                
                TextInput({ placeholder: '请输入昵称', text: this.editNickname })
                  .width('100%')
                  .height(44)
                  .backgroundColor('#F5F5F5')
                  .borderRadius(8)
                  .padding({ left: 12, right: 12 })
                  .onChange((value: string) => {
                    this.editNickname = value
                  })
              }
              .width('100%')

              Column({ space: 8 }) {
                Text('手机号')
                  .fontSize(14)
                  .fontColor('#666666')
                  .width('100%')

                TextInput({ placeholder: '请输入手机号', text: this.editPhone })
                  .width('100%')
                  .height(44)
                  .type(InputType.PhoneNumber)
                  .backgroundColor('#F5F5F5')
                  .borderRadius(8)
                  .padding({ left: 12, right: 12 })
                  .onChange((value: string) => {
                    this.editPhone = value
                  })
              }
              .width('100%')

              if (this.isFirstLogin) {
                Column({ space: 8 }) {
                  Text('首次登录，请设置密码')
                    .fontSize(14)
                    .fontColor('#666666')
                    .width('100%')

                  TextInput({ placeholder: '请输入密码' })
                    .width('100%')
                    .height(44)
                    .type(InputType.Password)
                    .backgroundColor('#F5F5F5')
                    .borderRadius(8)
                    .padding({ left: 12, right: 12 })
                    .onChange((value: string) => {
                      this.newPassword = value
                    })

                  TextInput({ placeholder: '请再次输入密码' })
                    .width('100%')
                    .height(44)
                    .type(InputType.Password)
                    .backgroundColor('#F5F5F5')
                    .borderRadius(8)
                    .padding({ left: 12, right: 12 })
                    .onChange((value: string) => {
                      this.confirmPassword = value
                    })
                }
                .width('100%')
              } else {
                Column({ space: 12 }) {
                  Row({ space: 8 }) {
                    Text('修改密码')
                      .fontSize(14)
                      .fontColor('#666666')
                      .layoutWeight(1)

                    Button(this.isChangingPassword ? '关闭' : '开启')
                      .height(32)
                      .width(72)
                      .borderRadius(16)
                      .backgroundColor(this.isChangingPassword ? '#0A59F7' : '#F5F5F5')
                      .fontColor(this.isChangingPassword ? Color.White : Color.Black)
                      .onClick(() => {
                        this.isChangingPassword = !this.isChangingPassword
                        if (!this.isChangingPassword) {
                          this.oldPassword = ''
                          this.newPassword = ''
                          this.confirmPassword = ''
                        }
                      })
                  }
                  .width('100%')
                  .alignItems(VerticalAlign.Center)

                  if (this.isChangingPassword) {
                    Column({ space: 8 }) {
                      TextInput({ placeholder: '请输入旧密码' })
                        .width('100%')
                        .height(44)
                        .type(InputType.Password)
                        .backgroundColor('#F5F5F5')
                        .borderRadius(8)
                        .padding({ left: 12, right: 12 })
                        .onChange((value: string) => {
                          this.oldPassword = value
                        })

                      TextInput({ placeholder: '请输入新密码' })
                        .width('100%')
                        .height(44)
                        .type(InputType.Password)
                        .backgroundColor('#F5F5F5')
                        .borderRadius(8)
                        .padding({ left: 12, right: 12 })
                        .onChange((value: string) => {
                          this.newPassword = value
                        })

                      TextInput({ placeholder: '请再次输入新密码' })
                        .width('100%')
                        .height(44)
                        .type(InputType.Password)
                        .backgroundColor('#F5F5F5')
                        .borderRadius(8)
                        .padding({ left: 12, right: 12 })
                        .onChange((value: string) => {
                          this.confirmPassword = value
                        })
                    }
                    .width('100%')
                  }
                }
                .width('100%')
              }
              
              Row({ space: 12 }) {
                Button('取消')
                  .layoutWeight(1)
                  .height(44)
                  .backgroundColor('#F5F5F5')
                  .fontColor(Color.Black)
                  .borderRadius(8)
                  .onClick(() => {
                    this.showEditDialog = false
                  })
                
                Button('保存')
                  .layoutWeight(1)
                  .height(44)
                  .backgroundColor('#0A59F7')
                  .fontColor(Color.White)
                  .borderRadius(8)
                  .onClick(() => {
                    this.saveUserInfo()
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
            this.showEditDialog = false
          })
          .zIndex(10)
        }
      }
    }
    .onReady((context: NavDestinationContext) => {
      this.pathStack = context.pathStack
    })
  }
}
