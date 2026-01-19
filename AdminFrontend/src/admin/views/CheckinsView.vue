<template>
  <div class="page">
    <div class="page-head">
      <div class="title">打卡管理</div>
      <div class="subtitle">配置打卡点位并查看用户打卡记录</div>
    </div>

    <div class="card">
      <div class="card-title">打卡点位</div>
      <div class="form-row"><input v-model="ns.name" placeholder="名称" /><input v-model.number="ns.location.lat" placeholder="纬度" /><input v-model.number="ns.location.lng" placeholder="经度" /><input v-model.number="ns.radius" placeholder="半径(m)" /></div>
      <div class="actions"><button class="button" @click="addSpot">新增点位</button></div>
      <div v-if="spotLoading">加载中...</div>
      <div v-else-if="spotError" class="error">{{ spotError }}</div>
      <ul v-else class="list">
        <li v-for="s in spots" :key="s.id">{{ s.name }} ({{ s.location?.lat }},{{ s.location?.lng }}) <button class="button danger" @click="delSpot(s.id)">删除</button></li>
        <li v-if="!spots.length" class="empty">暂无点位</li>
      </ul>
    </div>

    <div class="card">
      <div class="card-title">打卡记录</div>
      <div class="form-row"><input v-model="q.userName" placeholder="用户" /><input v-model="q.spotName" placeholder="景点" /><button class="button" @click="loadRecords">查询</button></div>
      <div v-if="recordLoading">加载中...</div>
      <div v-else-if="recordError" class="error">{{ recordError }}</div>
      <table v-else class="table">
        <thead><tr><th>用户</th><th>点位</th><th>时间</th><th style="width:120px">操作</th></tr></thead>
        <tbody>
          <tr v-for="r in records" :key="r.id">
            <td>{{ r.userName }}</td>
            <td>{{ r.spotName }}</td>
            <td>{{ r.time }}</td>
            <td><button class="button danger" @click="delRecord(r.id)">删除</button></td>
          </tr>
          <tr v-if="!records.length"><td colspan="4" style="text-align:center;color:#6b7280">暂无记录</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const spots = ref([])
const ns = ref({ name: '', location: { lat: 0, lng: 0 }, radius: 200 })
const records = ref([])
const q = ref({ userName: '', spotName: '' })
const spotLoading = ref(false)
const recordLoading = ref(false)
const spotError = ref(null)
const recordError = ref(null)

async function loadSpots() {
  spotLoading.value = true
  spotError.value = null
  try {
    const resp = await api.checkin.spots()
    spots.value = resp?.data?.data || []
  } catch (e) {
    spotError.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    spotLoading.value = false
  }
}

async function addSpot() {
  await api.checkin.createSpot(ns.value)
  ns.value = { name: '', location: { lat:0, lng:0}, radius:200 }
  await loadSpots()
}

async function delSpot(id) {
  if (!confirm('确认删除该点位？')) return
  await api.checkin.deleteSpot(id)
  await loadSpots()
}

async function loadRecords() {
  recordLoading.value = true
  recordError.value = null
  try {
    const resp = await api.checkin.records({ userName: q.value.userName, spotName: q.value.spotName, page:1, size:100 })
    records.value = resp?.data?.data?.list || []
  } catch (e) {
    recordError.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    recordLoading.value = false
  }
}

async function delRecord(id) {
  if (!confirm('确认删除该记录？')) return
  await api.checkin.deleteRecord(id)
  await loadRecords()
}

onMounted(() => { loadSpots(); loadRecords(); })
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.actions { display:flex; gap:10px; margin:8px 0; }
.error { color:#e44f4f; }
.list { list-style:none; padding-left:0; display:flex; flex-direction:column; gap:8px; }
.empty { color:#6b7280; }
</style>
