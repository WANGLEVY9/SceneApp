<template>
  <div class="page">
    <div class="page-head">
      <div class="title">礼品管理</div>
      <div class="subtitle">维护可兑换的礼品信息</div>
    </div>

    <div class="card">
      <div class="filters">
        <button class="button" @click="showAdd = !showAdd">{{ showAdd ? '收起' : '新增礼品' }}</button>
      </div>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error" class="error">{{ error }}</div>
      <table v-else class="table">
        <thead><tr><th>名称</th><th>积分</th><th>库存</th><th>状态</th><th style="width:220px">操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.name }}</td>
            <td>{{ item.points }}</td>
            <td>{{ item.stock }}</td>
            <td>{{ item.status === 1 ? '上架' : '下架' }}</td>
            <td>
              <button class="button" @click="showImage(item)">图片</button>
              <button class="button danger" @click="del(item.id)" style="margin-left:8px">删除</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="5" style="text-align:center;color:#6b7280">暂无数据</td></tr>
        </tbody>
      </table>
    </div>

    <div v-if="showAdd" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">新增礼品</div>
          <button class="close" @click="showAdd=false">×</button>
        </div>
        <div class="form-row"><label class="label">名称</label><input v-model="g.name" placeholder="名称" /></div>
        <div class="form-row"><label class="label">积分</label><input v-model.number="g.points" type="number" step="1" placeholder="积分" /></div>
        <div class="form-row"><label class="label">库存</label><input v-model.number="g.stock" type="number" step="1" placeholder="库存" /></div>
        <div class="form-row">
          <label class="label">图片</label>
          <div class="upload-box">
            <div v-if="g.image" class="preview-img"><img :src="g.image" alt="" /></div>
            <label class="upload-btn">
              <input type="file" accept="image/*" @change="onGiftImage" />
              上传图片
            </label>
          </div>
        </div>
        <div class="form-row"><label class="label">描述</label><input v-model="g.desc" placeholder="描述" /></div>
        <div class="actions"><button class="button" @click="add">保存</button><button class="button" @click="showAdd=false">取消</button></div>
      </div>
    </div>

    <div v-if="detail" class="modal-mask">
      <div class="modal">
        <div class="modal-header">
          <div class="modal-title">礼品图片</div>
          <button class="close" @click="detail=null">×</button>
        </div>
        <div v-if="detail.image" class="img-wrap"><img :src="detail.image" alt="礼品图片" /></div>
        <div v-else>暂无图片</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { uploadToOSS } from '../api/oss'

const list = ref([])
const showAdd = ref(false)
const g = ref({ name:'', points:0, stock:0, image:'', desc:'' })
const loading = ref(false)
const error = ref(null)
const detail = ref(null)

async function load() {
  loading.value = true
  error.value = null
  try {
    const resp = await api.gifts.list({ page:1, size:100 })
    list.value = resp?.data?.data?.list || []
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function add() {
  await api.gifts.create(g.value)
  g.value = { name:'', points:0, stock:0, image:'', desc:'' }
  showAdd.value=false
  await load()
}

async function del(id) {
  if (!confirm('确认删除该礼品？')) return
  await api.gifts.delete(id)
  await load()
}

function showImage(item) {
  detail.value = item
}

async function onGiftImage(e) {
  const file = e.target.files?.[0]
  if (!file) return
  const url = await uploadToOSS(file, 'gifts/images')
  g.value.image = url
  e.target.value = ''
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
.img-wrap { max-height:320px; overflow:hidden; }
.img-wrap img { max-width:100%; border-radius:8px; border:1px solid #e6eef7; }
.upload-box { display:flex; align-items:center; gap:12px; }
.preview-img { width:120px; height:80px; border:1px dashed #d0d7e2; border-radius:10px; overflow:hidden; background:#f8fafc; }
.preview-img img { width:100%; height:100%; object-fit:cover; }
.upload-btn { display:inline-flex; align-items:center; justify-content:center; padding:10px 14px; border:1px solid #0a6ba5; color:#0a6ba5; border-radius:10px; cursor:pointer; background:#fff; font-weight:600; width:140px; text-align:center; }
.upload-btn input { display:none; }
</style>
