import { createRouter, createWebHistory } from 'vue-router'
import AdminApp from '../admin/AppAdmin.vue'
import adminRoutes from '../admin/router'

const routes = [
  // 将根路径重定向到 /admin
  { path: '/', redirect: '/admin' },
  // 把 AdminApp 作为 /admin 的父路由，并将 adminRoutes 作为子路由
  { path: '/admin', component: AdminApp, children: adminRoutes }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
