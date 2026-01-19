import { UserApi } from '../api/userApi';
import { HttpUtil } from '../utils/http';
import { promptAction } from '@kit.ArkUI';

// PageOne.ets
@Builder
export function AdBuilder() {
  Ad()
}

@Component
struct Ad {
  pathStack: NavPathStack = new NavPathStack()
  
  // 登录方式：0-密码登录，1-验证码登录
  @State loginType: number = 0
  
  // 密码登录相关
  @State username: string = ''
  @State password: string = ''
  
  // 验证码登录相关
  @State phoneNumber: string = ''
  @State verificationCode: string = ''
  @State codeButtonText: string = '获取验证码'
  @State codeButtonDisabled: boolean = false
  @State countdown: number = 0
  
  // 加载状态
  @State loading: boolean = false
  
  // 跳转到首页
  navigateToHome(): void {
    this.pathStack.replacePathByName("Home", null, false)
  }
  
  // 显示提示信息
  showToast(message: string): void {
    promptAction.showToast({
      message: message,
      duration: 2000
    });
  }
  
  // 获取验证码
  async getVerificationCode(): Promise<void> {
    if (!this.phoneNumber || this.phoneNumber.length !== 11) {
      this.showToast('请输入正确的手机号')
      return
    }
    
    try {
      await UserApi.sendCode(this.phoneNumber)
      this.showToast('验证码已发送')
      
      // 开始倒计时
      this.codeButtonDisabled = true
      this.countdown = 60
      this.codeButtonText = `${this.countdown}秒后重新获取`
      
      const timer = setInterval(() => {
        this.countdown--
        if (this.countdown > 0) {
          this.codeButtonText = `${this.countdown}秒后重新获取`
        } else {
          this.codeButtonDisabled = false
          this.codeButtonText = '获取验证码'
          clearInterval(timer)
        }
      }, 1000)
    } catch (e) {
      const message = e instanceof Error ? e.message : '获取验证码失败，请稍后重试'
      this.showToast(message)
      console.error('sendCode error:', e)
    }
  }
  
  // 登录
  async handleLogin(): Promise<void> {
    if (this.loading) {
      return
    }
    
    this.loading = true
    
    try {
      if (this.loginType === 0) {
        // 密码登录验证
        const loginKey = this.username.trim()
        if (!loginKey || !this.password) {
          this.showToast('请输入用户名/手机号和密码')
          this.loading = false
          return
        }
        
        // 调用登录API
        const response = await UserApi.login({
          loginKey: loginKey,
          password: this.password,
          loginType: 0
        })

        // 保存token
        await HttpUtil.saveToken(response.token)
        
        this.showToast('登录成功')
        // 登录成功后跳转
        setTimeout(() => {
          this.navigateToHome()
        }, 500)
      } else {
        // 验证码登录验证
        const phone = this.phoneNumber.trim()
        const code = this.verificationCode.trim()
        if (!phone || !code) {
          this.showToast('请输入手机号和验证码')
          this.loading = false
          return
        }
        
        if (phone.length !== 11) {
          this.showToast('请输入正确的手机号')
          this.loading = false
          return
        }

        const response = await UserApi.login({
          loginKey: phone,
          code: code,
          loginType: 1
        })

        await HttpUtil.saveToken(response.token)
        this.showToast('登录成功')
        setTimeout(() => {
          this.navigateToHome()
        }, 500)
      }
    } catch (e) {
      this.showToast(e instanceof Error ? e.message : '登录失败，请稍后重试')
    } finally {
      this.loading = false
    }
  }

  build() {
    NavDestination() {
      Stack({ alignContent: Alignment.Center}) {
        Image($r("app.media.ad"))
          .width('100%')
          .height('100%')
          .expandSafeArea([SafeAreaType.SYSTEM],[SafeAreaEdge.TOP,SafeAreaEdge.BOTTOM])
        
        // 登录表单卡片
        Column() {
          // 登录方式选择
          Row({ space: 0 }) {
            Button('密码登录')
              .width('50%')
              .height(40)
              .fontSize(16)
              .backgroundColor(this.loginType === 0 ? Color.Blue : Color.Transparent)
              .fontColor(this.loginType === 0 ? Color.White : Color.Black)
              .borderRadius({ topLeft: 8, bottomLeft: 8 })
              .onClick(() => {
                this.loginType = 0
              })
            
            Button('验证码登录')
              .width('50%')
              .height(40)
              .fontSize(16)
              .backgroundColor(this.loginType === 1 ? Color.Blue : Color.Transparent)
              .fontColor(this.loginType === 1 ? Color.White : Color.Black)
              .borderRadius({ topRight: 8, bottomRight: 8 })
              .onClick(() => {
                this.loginType = 1
              })
          }
          .width('100%')
          .border({ width: 1, color: Color.Grey })
          .borderRadius(8)
          .margin({ top: 20, bottom: 30 })
          
          // 密码登录表单
          if (this.loginType === 0) {
            Column({ space: 15 }) {
              TextInput({ placeholder: '请输入用户名或手机号' })
                .width('100%')
                .height(45)
                .backgroundColor(Color.White)
                .border({ width: 1, color: Color.Grey })
                .borderRadius(8)
                .padding({ left: 12, right: 12 })
                .onChange((value: string) => {
                  this.username = value
                })
              
              TextInput({ placeholder: '请输入密码' })
                .width('100%')
                .height(45)
                .type(InputType.Password)
                .backgroundColor(Color.White)
                .border({ width: 1, color: Color.Grey })
                .borderRadius(8)
                .padding({ left: 12, right: 12 })
                .onChange((value: string) => {
                  this.password = value
                })
            }
            .width('100%')
          }
          
          // 验证码登录表单
          if (this.loginType === 1) {
            Column({ space: 15 }) {
              TextInput({ placeholder: '请输入手机号' })
                .width('100%')
                .height(45)
                .type(InputType.PhoneNumber)
                .backgroundColor(Color.White)
                .border({ width: 1, color: Color.Grey })
                .borderRadius(8)
                .padding({ left: 12, right: 12 })
                .onChange((value: string) => {
                  this.phoneNumber = value
                })
              
              Row({ space: 10 }) {
                TextInput({ placeholder: '请输入验证码' })
                  .layoutWeight(1)
                  .height(45)
                  .type(InputType.Number)
                  .backgroundColor(Color.White)
                  .border({ width: 1, color: Color.Grey })
                  .borderRadius(8)
                  .padding({ left: 12, right: 12 })
                  .onChange((value: string) => {
                    this.verificationCode = value
                  })
                
                Button(this.codeButtonText)
                  .width(120)
                  .height(45)
                  .fontSize(12)
                  .backgroundColor(this.codeButtonDisabled ? Color.Grey : Color.Blue)
                  .fontColor(Color.White)
                  .enabled(!this.codeButtonDisabled)
                  .onClick(() => {
                    this.getVerificationCode()
                  })
              }
              .width('100%')
            }
            .width('100%')
          }
          
          // 登录按钮
          Button(this.loading ? '登录中...' : '登录')
            .width('100%')
            .height(45)
            .margin({ top: 20 })
            .backgroundColor(this.loading ? Color.Grey : Color.Blue)
            .fontColor(Color.White)
            .borderRadius(8)
            .enabled(!this.loading)
            .onClick(() => {
              this.handleLogin()
            })
        }
        .width('85%')
        .padding(20)
        .backgroundColor(Color.White)
        .borderRadius(12)
        .shadow({
          radius: 10,
          color: Color.Black,
          offsetX: 0,
          offsetY: 2
        })
      }
    }
    .onReady((context: NavDestinationContext) => {
      this.pathStack = context.pathStack
    })
  }
}