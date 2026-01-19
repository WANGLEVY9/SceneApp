<template>
  <div class="page">
    <div class="page-head">
      <div class="title">景点管理</div>
      <div class="subtitle">管理景区内的景点信息</div>
    </div>

    <div class="card">
      <div class="filters">
        <input v-model="keyword" placeholder="按名称搜索" />
        <button class="button" @click="load">搜索</button>
        <div class="spacer"></div>
        <button class="button" @click="showAdd = !showAdd">{{ showAdd ? '收起' : '新增景点' }}</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead>
          <tr><th>名称</th><th>位置</th><th>景点描述</th><th style="width:120px">操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.name || '-' }}</td>
            <td>{{ formatLocation(s.location) }}</td>
            <td>{{ s.desc || '-' }}</td>
            <td><button class="button danger" @click="remove(s.id)">删除</button></td>
          </tr>
          <tr v-if="!list.length"><td colspan="4" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAdd" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">新增景点</div>
          <button class="close" @click="showAdd=false">×</button>
        </div>
        <div class="form-row"><label class="label">名称</label><input v-model="newSpot.name" placeholder="请输入名称" /></div>
        <div class="form-row"><label class="label">纬度</label><input v-model.number="newSpot.location.lat" type="number" step="0.000001" placeholder="纬度，如 30.123456" /></div>
        <div class="form-row"><label class="label">经度</label><input v-model.number="newSpot.location.lng" type="number" step="0.000001" placeholder="经度，如 120.123456" /></div>
        <div class="form-row"><label class="label">景点描述</label><input v-model="newSpot.desc" placeholder="简要描述" /></div>
        <div class="actions"><button class="button" @click="add">保存</button><button class="button" @click="showAdd=false">取消</button></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const keyword = ref('')
const list = ref([])
const showAdd = ref(false)
const loading = ref(false)
const error = ref(null)
const newSpot = ref({ name: '', location: { lat: 0, lng: 0 }, desc: '' })

async function load() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.spots.list({ keyword: keyword.value, page: 1, size: 100 })
    const raw = resp?.data?.data?.list || []
    // 后端可能直接返回顶层 lat/lng，这里映射成统一的 location 对象便于展示
    list.value = raw.map(item => ({
      ...item,
      location: item.location || { lat: item.lat, lng: item.lng }
    }))
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function formatLocation(loc) {
  if (!loc) return '-'
  if (typeof loc === 'string') {
    try { return formatLocation(JSON.parse(loc)) } catch (e) { return loc }
  }
  const lats = loc.lat ?? loc.latitude
  const lngs = loc.lng ?? loc.lon ?? loc.long ?? loc.longitude
  if (lats != null && lngs != null) {
    const fmt = v => Number.isFinite(Number(v)) ? Number(v).toFixed(6) : v
    return `${fmt(lats)}, ${fmt(lngs)}`
  }
  if (Array.isArray(loc) && loc.length >= 2) {
    const fmt = v => Number.isFinite(Number(v)) ? Number(v).toFixed(6) : v
    return `${fmt(loc[0])}, ${fmt(loc[1])}`
  }
  return '-'
}

async function add() {
  const payload = {
    name: newSpot.value.name,
    lat: newSpot.value.location?.lat,
    lng: newSpot.value.location?.lng,
    desc: newSpot.value.desc
  }
  await api.spots.create(payload)
  newSpot.value = { name: '', location: { lat: 0, lng: 0 }, desc: '' }
  showAdd.value = false
  await load()
}

async function remove(id) {
  if (!confirm('确认删除该景点？')) return
  await api.spots.delete(id)
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.filters { display:flex; align-items:center; gap:10px; margin-bottom:10px; }
.filters input { width:220px; padding:10px 12px; border:1px solid #e6eef7; border-radius:8px; }
.spacer { flex:1; }
.actions { display:flex; gap:10px; margin-top:8px; justify-content:flex-end; }
.error { color:#e44f4f; }
.label { width:90px; color:#4b5563; font-weight:600; }
.modal-mask { position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:50; }
.modal { background:#fff; padding:18px; border-radius:12px; width:420px; box-shadow:0 16px 40px rgba(0,0,0,0.15); }
.modal-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:10px; }
.modal-title { font-weight:700; color:#0a6ba5; }
.close { border:none; background:transparent; font-size:20px; cursor:pointer; color:#6b7280; }
</style>
