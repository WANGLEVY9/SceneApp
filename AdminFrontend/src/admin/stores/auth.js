import { defineStore } from 'pinia'
import api from '../api'
import router from '../../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({ token: localStorage.getItem('admin_token') || null }),
  actions: {
    async login(username, password) {
      const resp = await api.auth.login({ username, password })
      if (resp.data && resp.data.data && resp.data.data.token) {
        this.token = resp.data.data.token
        localStorage.setItem('admin_token', this.token)
        router.push('/admin')
      }
      return resp
    },
    async logout() {
      try {
        await api.auth.logout()
      } catch (e) {
        // ignore
      }
      this.token = null
      localStorage.removeItem('admin_token')
      router.push('/admin/login')
    }
  }
})

