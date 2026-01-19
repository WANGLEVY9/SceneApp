<template>
  <div class="page">
    <div class="page-head">
      <div class="title">商家管理</div>
      <div class="subtitle">管理景区内的商家信息</div>
    </div>

    <div class="card">
      <div class="filters">
        <button class="button" @click="showAdd = !showAdd">{{ showAdd ? '收起' : '新增商家' }}</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead><tr><th>名称</th><th>联系方式</th><th>位置</th><th style="width:120px">操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.name || '-' }}</td>
            <td>{{ item.contact || '-' }}</td>
            <td>{{ formatLocation(item.location) }}</td>
            <td><button class="button danger" @click="del(item.id)">删除</button></td>
          </tr>
          <tr v-if="!list.length"><td colspan="4" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAdd" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">新增商家</div>
          <button class="close" @click="showAdd=false">×</button>
        </div>
        <div class="form-row"><label class="label">名称</label><input v-model="m.name" placeholder="名称" /></div>
        <div class="form-row"><label class="label">联系方式</label><input v-model="m.contact" placeholder="联系方式" /></div>
        <div class="form-row"><label class="label">纬度</label><input v-model.number="m.location.lat" type="number" step="0.000001" placeholder="纬度" /></div>
        <div class="form-row"><label class="label">经度</label><input v-model.number="m.location.lng" type="number" step="0.000001" placeholder="经度" /></div>
        <div class="actions"><button class="button" @click="add">保存</button><button class="button" @click="showAdd=false">取消</button></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const list = ref([])
const showAdd = ref(false)
const m = ref({ name: '', contact: '', location: { lat: 0, lng: 0 } })
const loading = ref(false)
const error = ref(null)

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

async function load() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.merchants.list({ page:1,size:100 })
    const raw = resp?.data?.data?.list || []
    list.value = raw.map(item => ({ ...item, location: item.location || { lat: item.lat, lng: item.lng } }))
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function add() {
  const payload = {
    name: m.value.name,
    contact: m.value.contact,
    lat: m.value.location?.lat,
    lng: m.value.location?.lng
  }
  await api.merchants.create(payload)
  m.value = { name: '', contact: '', location: { lat: 0, lng: 0 } }
  showAdd.value=false
  await load()
}

async function del(id) {
  if (!confirm('确认删除该商家？')) return
  await api.merchants.delete(id)
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.filters { display:flex; align-items:center; gap:10px; margin-bottom:10px; }
.actions { display:flex; gap:10px; margin-top:8px; justify-content:flex-end; }
.error { color:#e44f4f; }
.label { width:90px; color:#4b5563; font-weight:600; }
.modal-mask { position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:50; }
.modal { background:#fff; padding:18px; border-radius:12px; width:420px; box-shadow:0 16px 40px rgba(0,0,0,0.15); }
.modal-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:10px; }
.modal-title { font-weight:700; color:#0a6ba5; }
.close { border:none; background:transparent; font-size:20px; cursor:pointer; color:#6b7280; }
</style>
