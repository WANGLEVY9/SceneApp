<template>
  <div class="page">
    <div class="page-head">
      <div class="title">打卡记录管理</div>
      <div class="subtitle">查看并筛选用户的打卡记录</div>
    </div>

    <div class="card">
      <div class="filters">
        <input v-model="q.userName" placeholder="按用户筛选" />
        <input v-model="q.spotName" placeholder="按景点筛选" />
        <button class="button" @click="loadRecords">查询</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead><tr><th>用户</th><th>点位</th><th>时间</th><th>点赞数</th><th style="width:180px">操作</th></tr></thead>
        <tbody>
          <tr v-for="r in records" :key="r.id">
            <td>{{ r.userName }}</td>
            <td>{{ r.spotName }}</td>
            <td>{{ formatTime(r.time) }}</td>
            <td>{{ r.likeCount ?? '-' }}</td>
            <td>
              <button class="button" @click="showDetail(r)">详情</button>
              <button class="button danger" @click="delRecord(r.id)" style="margin-left:8px">删除</button>
            </td>
          </tr>
          <tr v-if="!records.length"><td colspan="5" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>

    <div v-if="detail" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">打卡详情</div>
          <button class="close" @click="detail=null">×</button>
        </div>
        <div class="detail-block"><strong>文字：</strong><span>{{ detail.remark || '无' }}</span></div>
        <div class="detail-block">
          <strong>图片：</strong>
          <div v-if="detail.image" class="img-wrap"><img :src="detail.image" alt="打卡图片" /></div>
          <span v-else>无</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const records = ref([])
const q = ref({ userName: '', spotName: '' })
const loading = ref(false)
const error = ref(null)
const detail = ref(null)

async function loadRecords() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.checkin.records({ userName: q.value.userName, spotName: q.value.spotName, page:1, size:100 })
    records.value = resp?.data?.data?.list || []
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function formatTime(t) {
  if (!t) return '-'
  return String(t).replace('T', ' ')
}

function showDetail(r) {
  detail.value = r
}

async function delRecord(id) {
  if (!confirm('确认删除该记录？')) return
  await api.checkin.deleteRecord(id)
  await loadRecords()
}

onMounted(loadRecords)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.filters { display:flex; align-items:center; gap:10px; margin-bottom:10px; }
.filters input { width:220px; padding:10px 12px; border:1px solid #e6eef7; border-radius:8px; }
.error { color:#e44f4f; }
.modal-mask { position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:50; }
.modal { background:#fff; padding:18px; border-radius:12px; width:420px; box-shadow:0 16px 40px rgba(0,0,0,0.15); }
.modal-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:10px; }
.modal-title { font-weight:700; color:#0a6ba5; }
.close { border:none; background:transparent; font-size:20px; cursor:pointer; color:#6b7280; }
.detail-block { margin-bottom:10px; color:#374151; }
.img-wrap { margin-top:6px; max-height:260px; overflow:hidden; }
.img-wrap img { max-width:100%; border-radius:8px; border:1px solid #e6eef7; }
</style>
