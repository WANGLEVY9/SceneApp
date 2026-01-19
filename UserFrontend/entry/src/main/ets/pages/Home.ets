import { Gift } from "./Gift"
import { Introduction } from "./Introduction"
import { Photo } from "./Photo"
import { Place } from "./Place"
import { User } from "./User"

// PageOne.ets
interface TabClass {
  text: string
  icon: ResourceStr
}



@Builder
export function HomeBuilder() {
  Home()
}

@ComponentV2
struct Home {
  @Local currentIndex: number = 0
  pathStack: NavPathStack = new NavPathStack()

  tabData: TabClass[] = [
    {text: '主页', icon: $r('app.media.home')},
    {text: '景点/商家', icon: $r('app.media.house')},
    {text: '打卡', icon: $r('app.media.camera')},
    {text: '积分/礼品', icon: $r('app.media.gift')},
    {text: '我', icon: $r('app.media.user')}
  ]

  @Builder tabBuilder (item: TabClass,index: number) {
    Column({ space: 5}) {
      Image(item.icon)
        .width(24)
        .fillColor(this.currentIndex === index ? Color.Green : Color.Black)
      Text(item.text)
        .fontSize(14)
        .fontColor(this.currentIndex === index ? Color.Green : Color.Black)
    }
  }

  build() {
    NavDestination() {
      Tabs({ barPosition: BarPosition.End }) {
        ForEach(this.tabData,(item: TabClass,index: number) => {
          TabContent() {
            if (this.currentIndex === 0) {
              Introduction()
            } else if (this.currentIndex === 1) {
              Place()
            } else if (this.currentIndex === 2) {
              Photo()
            } else if (this.currentIndex === 3) {
              Gift()
            } else {
              User()
            }
          }
          .tabBar(this.tabBuilder(item, index))
        })
      }
      .onChange((index: number) => {
        this.currentIndex = index
      })
    }
    .onReady((context: NavDestinationContext) => {
      this.pathStack = context.pathStack
    })
  }
}