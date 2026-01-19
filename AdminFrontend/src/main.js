import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)

// 全局路由守卫：除标记了 meta.public 的路由外，需已登录
import { useAuthStore } from './admin/stores/auth'
router.beforeEach((to, from, next) => {
  if (to.matched.some(r => r.meta && r.meta.public)) return next()
  const auth = useAuthStore()
  if (!auth.token) return next('/admin/login')
  next()
})

app.mount('#app')
