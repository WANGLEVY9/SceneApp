import LoginView from './views/LoginView.vue'
import DashboardView from './views/DashboardView.vue'
import IntroView from './views/IntroView.vue'
import SpotsView from './views/ScenicSpotsView.vue'
import MerchantsView from './views/MerchantsView.vue'
import GiftsView from './views/GiftsView.vue'
import CheckinSpotsView from './views/CheckinSpotsView.vue'
import CheckinRecordsView from './views/CheckinRecordsView.vue'
import GiftExchangesView from './views/GiftExchangesView.vue'

// 导出为相对路径的 admin 子路由数组，供主路由作为 /admin 的 children 使用
const adminRoutes = [
  { path: '', component: DashboardView },
  { path: 'login', component: LoginView, meta: { public: true } },
  { path: 'intro', component: IntroView },
  { path: 'spots', component: SpotsView },
  { path: 'merchants', component: MerchantsView },
  { path: 'checkin-spots', component: CheckinSpotsView },
  { path: 'checkin-records', component: CheckinRecordsView },
  { path: 'gifts', component: GiftsView },
  { path: 'gift-exchanges', component: GiftExchangesView },
  { path: 'checkins', redirect: 'checkin-spots' }
]

export default adminRoutes
