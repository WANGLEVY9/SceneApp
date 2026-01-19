<template>
  <div class="page">
    <div class="page-head">
      <div class="title">礼品兑换管理</div>
      <div class="subtitle">查看并处理用户的礼品兑换申请</div>
    </div>

    <div class="card">
      <div class="filters">
        <select v-model="status">
          <option value="">全部状态</option>
          <option value="pending">待审核</option>
          <option value="approved">已核销</option>
          <option value="delivered">已发货</option>
          <option value="received">已收货</option>
          <option value="rejected">已拒绝</option>
        </select>
        <button class="button" @click="load">查询</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead><tr><th>用户</th><th>礼品</th><th>数量</th><th>申请时间</th><th>状态</th><th style="width:240px">操作</th></tr></thead>
        <tbody>
          <tr v-for="e in list" :key="e.id">
            <td>{{ e.userName || e.user_name || '-' }}</td>
            <td>{{ e.giftName || e.gift_name || '-' }}</td>
            <td>{{ e.giftNumber ?? e.gift_number ?? '-' }}</td>
            <td>{{ e.applyTime || e.apply_time || '-' }}</td>
            <td>{{ statusLabel(e.status) }}</td>
            <td>
              <template v-if="e.status === 'pending'">
                <button class="button" @click="updateStatus(e.id, 'approved')">核销</button>
                <button class="button danger" style="margin-left:8px" @click="updateStatus(e.id, 'rejected')">拒绝</button>
              </template>
              <template v-else-if="e.status === 'approved'">
                <button class="button" @click="updateStatus(e.id, 'delivered')">发货</button>
              </template>
              <template v-else-if="e.status === 'delivered'">
                <button class="button" @click="updateStatus(e.id, 'received')">签收</button>
              </template>
              <template v-else-if="e.status === 'received'">
                <button class="button danger" @click="remove(e.id)">删除</button>
              </template>
              <template v-else-if="e.status === 'rejected'">
                <button class="button danger" @click="remove(e.id)">删除</button>
              </template>
              <span v-else style="color:#6b7280">—</span>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="6" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const status = ref('')
const list = ref([])
const loading = ref(false)
const error = ref(null)
const statusMap = { pending:'待审核', approved:'已核销', delivered:'已发货', received:'已收货', rejected:'已拒绝' }

function statusLabel(s) { return statusMap[s] || s || '-' }

async function load() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.gifts.exchanges({ status: status.value, page:1, size:100 })
    const raw = resp?.data?.data?.list || []
    list.value = raw.map(e => ({
      ...e,
      userName: e.userName ?? e.user_name,
      giftName: e.giftName ?? e.gift_name,
      giftNumber: e.giftNumber ?? e.gift_number,
      applyTime: e.applyTime ?? e.apply_time
    }))
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function updateStatus(id, s) {
  await api.gifts.updateExchange({ id, status: s })
  await load()
}

async function remove(id) {
  if (!confirm('确认删除该申请？')) return
  await api.gifts.delete(id)
  await load()
}

onMounted(load)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.filters { display:flex; align-items:center; gap:10px; margin-bottom:10px; }
.error { color:#e44f4f; }
select { padding:10px 12px; border:1px solid #e6eef7; border-radius:8px; background:#fff; }
</style>
