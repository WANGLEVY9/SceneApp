<template>
  <div class="page">
    <div class="page-head">
      <div class="title">数据概览</div>
      <div class="subtitle">快速了解景区运营数据</div>
    </div>

    <div class="stat-grid">
      <div class="card stat" v-for="item in statCards" :key="item.label">
        <div class="stat-label">{{ item.label }}</div>
        <div class="stat-value">{{ item.value }}</div>
      </div>
    </div>

    <div class="card">
      <div class="card-head">
        <div>
          <div class="card-title">最近动态</div>
          <div class="card-sub">展示最新的内容更新与用户行为</div>
        </div>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <div v-else>暂无动态</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api'

const stats = ref({ spots: 0, merchants: 0, records: 0, gifts: 0 })
const loading = ref(false)
const error = ref(null)

const statCards = computed(() => [
  { label: '景点数量', value: stats.value.spots },
  { label: '商家数量', value: stats.value.merchants },
  { label: '打卡记录', value: stats.value.records },
  { label: '礼品数量', value: stats.value.gifts }
])

async function load() {
  loading.value = true
  error.value = null
  try {
    const [s, m, r, g] = await Promise.all([
      api.spots.list({ page:1,size:1 }),
      api.merchants.list({ page:1,size:1 }),
      api.checkin.records({ page:1,size:1 }),
      api.gifts.list({ page:1,size:1 })
    ])
    stats.value.spots = s.data?.data?.total ?? 0
    stats.value.merchants = m.data?.data?.total ?? 0
    stats.value.records = r.data?.data?.total ?? 0
    stats.value.gifts = g.data?.data?.total ?? 0
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:20px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.stat-grid { display:grid; grid-template-columns: repeat(auto-fit, minmax(200px,1fr)); gap:12px; }
.stat { padding:16px; background:linear-gradient(135deg,#f9fbff,#eef5ff); border:1px solid #e5ecf6; }
.stat-label { color:#6b7280; font-weight:600; }
.stat-value { font-size:28px; font-weight:800; color:#0a6ba5; margin-top:8px; }
.card-head { display:flex; justify-content:space-between; align-items:center; margin-bottom:8px; }
.card-title { font-weight:700; color:#1f2937; }
.card-sub { color:#6b7280; font-size:14px; }
.error { color:#e44f4f; }
</style>
