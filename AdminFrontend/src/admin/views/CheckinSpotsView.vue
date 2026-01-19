<template>
  <div class="page">
    <div class="page-head">
      <div class="title">打卡点管理</div>
      <div class="subtitle">配置景区内的打卡点位</div>
    </div>

    <div class="card">
      <div class="card-actions">
        <button class="button" @click="showAdd = true">新增打卡点</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead><tr><th>名称</th><th>位置</th><th>范围(米)</th><th style="width:120px">操作</th></tr></thead>
        <tbody>
          <tr v-for="s in spots" :key="s.id">
            <td>{{ s.name || '-' }}</td>
            <td>{{ formatLocation(s.location) }}</td>
            <td>{{ formatRadius(s.radius) }}</td>
            <td><button class="button danger" @click="delSpot(s.id)">删除</button></td>
          </tr>
          <tr v-if="!spots.length"><td colspan="4" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAdd" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">新增打卡点</div>
          <button class="close" @click="showAdd=false">×</button>
        </div>
        <div class="form-row"><label class="label">名称</label><input v-model="ns.name" placeholder="请输入名称" /></div>
        <div class="form-row"><label class="label">纬度</label><input v-model.number="ns.location.lat" type="number" step="0.000001" placeholder="纬度" /></div>
        <div class="form-row"><label class="label">经度</label><input v-model.number="ns.location.lng" type="number" step="0.000001" placeholder="经度" /></div>
        <div class="form-row"><label class="label">半径(m)</label><input v-model.number="ns.radius" type="number" step="1" placeholder="影响半径" /></div>
        <div class="actions"><button class="button" @click="addSpot">保存</button><button class="button" @click="showAdd=false">取消</button></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const spots = ref([])
const ns = ref({ name: '', location: { lat: 0, lng: 0 }, radius: 200 })
const loading = ref(false)
const error = ref(null)
const showAdd = ref(false)

function formatLocation(loc) {
  if (!loc) return '-'
  if (typeof loc === 'string') {
    try { return formatLocation(JSON.parse(loc)) } catch { return loc }
  }
  const lat = loc.lat ?? loc.latitude
  const lng = loc.lng ?? loc.lon ?? loc.long ?? loc.longitude
  if (lat != null && lng != null) {
    const fmt = v => Number.isFinite(Number(v)) ? Number(v).toFixed(6) : v
    return `${fmt(lat)}, ${fmt(lng)}`
  }
  if (Array.isArray(loc) && loc.length >= 2) {
    const fmt = v => Number.isFinite(Number(v)) ? Number(v).toFixed(6) : v
    return `${fmt(loc[0])}, ${fmt(loc[1])}`
  }
  return '-'
}

function formatRadius(r) {
  if (r == null) return '-'
  const num = Number(r)
  return Number.isFinite(num) ? `${num}` : `${r}`
}

async function loadSpots() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.checkin.spots()
    const raw = resp?.data?.data || []
    spots.value = raw.map(item => ({
      ...item,
      location: item.location || { lat: item.lat, lng: item.lng }
    }))
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function addSpot() {
  const payload = {
    name: ns.value.name,
    lat: ns.value.location?.lat,
    lng: ns.value.location?.lng,
    radius: ns.value.radius
  }
  await api.checkin.createSpot(payload)
  ns.value = { name: '', location: { lat:0, lng:0}, radius:200 }
  showAdd.value = false
  await loadSpots()
}

async function delSpot(id) {
  if (!confirm('确认删除该点位？')) return
  await api.checkin.deleteSpot(id)
  await loadSpots()
}

onMounted(loadSpots)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.actions { display:flex; gap:10px; margin:8px 0; justify-content:flex-end; }
.error { color:#e44f4f; }
.label { width:90px; color:#4b5563; font-weight:600; }
.modal-mask { position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:50; }
.modal { background:#fff; padding:18px; border-radius:12px; width:420px; box-shadow:0 16px 40px rgba(0,0,0,0.15); }
.modal-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:10px; }
.modal-title { font-weight:700; color:#0a6ba5; }
.close { border:none; background:transparent; font-size:20px; cursor:pointer; color:#6b7280; }
.card-actions { display:flex; justify-content:flex-start; margin-bottom:10px; }
</style>
