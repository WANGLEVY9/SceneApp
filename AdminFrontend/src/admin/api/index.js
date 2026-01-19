import axios from 'axios'

// 优先使用环境变量 VITE_API_BASE（例如可以设置为 http://localhost:8080/api/admin）
// 若未设置，则使用相对路径 '/api/admin'，以便在开发时通过 Vite proxy 转发到后端，避免浏览器 CORS
const base = import.meta.env.VITE_API_BASE || '/api/admin'

const api = axios.create({
  baseURL: base,
  headers: { 'Content-Type': 'application/json' }
})

// Request interceptor to add token
api.interceptors.request.use(config => {
  try {
    const token = localStorage.getItem('admin_token')
    if (token) config.headers = { ...config.headers, Authorization: `Bearer ${token}` }
  } catch (e) {
    // ignore
  }
  return config
})

export default {
  auth: {
    login(data) { return api.post('/auth/login', data) },
    logout() { return api.post('/auth/logout') }
  },
  intro: {
    get() { return api.get('/intro') },
    update(data) { return api.put('/intro', data) }
  },
  spots: {
    list(params) { return api.get('/scenic-spots', { params }) },
    create(data) { return api.post('/scenic-spots', data) },
    delete(id) { return api.delete(`/scenic-spots/${id}`) }
  },
  merchants: {
    list(params) { return api.get('/merchants', { params }) },
    create(data) { return api.post('/merchants', data) },
    delete(id) { return api.delete(`/merchants/${id}`) }
  },
  checkin: {
    spots() { return api.get('/checkin/spots') },
    createSpot(data) { return api.post('/checkin/spots', data) },
    deleteSpot(id) { return api.delete(`/checkin/spots/${id}`) },
    records(params) { return api.get('/checkin/records', { params }) },
    deleteRecord(id) { return api.delete(`/checkin/records/${id}`) }
  },
  gifts: {
    list(params) { return api.get('/gifts', { params }) },
    create(data) { return api.post('/gifts', data) },
    delete(id) { return api.delete(`/gifts/${id}`) },
    exchanges(params) { return api.get('/gifts/exchange', { params }) },
    updateExchange(params) { return api.put('/gifts', null, { params }) }
  }
}
