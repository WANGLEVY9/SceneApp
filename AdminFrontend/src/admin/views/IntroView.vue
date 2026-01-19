<template>
  <div class="page">
    <div class="page-head">
      <div class="title">引领区内容</div>
      <div class="subtitle">编辑景区简介、图文与视频</div>
    </div>

    <div v-if="loading" class="card">加载中...</div>
    <div v-else class="card">
      <div class="form-row"><label class="label">标题</label><input v-model="form.title" placeholder="请输入标题" /></div>
      <div class="form-row"><label class="label">正文</label><textarea v-model="form.text" rows="5" placeholder="请输入正文"></textarea></div>

      <div class="form-row">
        <label class="label">图片列表</label>
        <div class="upload-box">
          <div class="preview-list">
            <div v-for="(img,idx) in form.images" :key="idx" class="thumb">
              <img :src="img" alt="" />
            </div>
          </div>
          <label class="upload-btn">
            <input type="file" accept="image/*" multiple @change="onImageChange" />
            上传图片
          </label>
        </div>
      </div>

      <div class="form-row">
        <label class="label">视频</label>
        <div class="upload-box">
          <div v-if="form.video" class="video-chip">
            <span>{{ form.video }}</span>
          </div>
          <label class="upload-btn">
            <input type="file" accept="video/*" @change="onVideoChange" />
            上传视频
          </label>
        </div>
      </div>

      <div class="actions"><button class="button" @click="save" :disabled="saving">{{ saving ? '保存中...' : '保存' }}</button></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { uploadToOSS } from '../api/oss'

const loading = ref(true)
const saving = ref(false)
const form = ref({ id: '', title: '', text: '', images: [], video: '' })

async function load() {
  loading.value = true
  try {
    const resp = await api.intro.get()
    if (resp?.data?.data) {
      Object.assign(form.value, resp.data.data)
      if (!Array.isArray(form.value.images)) form.value.images = []
    }
  } finally {
    loading.value = false
  }
}

async function onImageChange(e) {
  const files = Array.from(e.target.files || [])
  if (!files.length) return
  for (const file of files) {
    const url = await uploadToOSS(file, 'intro/images')
    form.value.images.push(url)
  }
  e.target.value = ''
}

async function onVideoChange(e) {
  const file = e.target.files?.[0]
  if (!file) return
  const url = await uploadToOSS(file, 'intro/videos')
  form.value.video = url
  e.target.value = ''
}

function removeImage(idx) {
  form.value.images.splice(idx,1)
}

async function save() {
  saving.value = true
  try {
    await api.intro.update(form.value)
    alert('保存成功')
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page { display:flex; flex-direction:column; gap:16px; }
.page-head .title { font-size:18px; font-weight:800; color:#0a6ba5; }
.page-head .subtitle { color:#6b7280; margin-top:4px; }
.label { width:90px; color:#4b5563; font-weight:600; }
.actions { display:flex; justify-content:flex-end; margin-top:20px; }
.button { padding:12px 28px; border:none; border-radius:12px; background:linear-gradient(135deg,#0d8ad6,#0b6fb5); color:#fff; font-weight:700; cursor:pointer; transition:transform .12s ease, box-shadow .12s ease, opacity .12s ease; min-width:140px; text-align:center; }
.button:hover { box-shadow:0 10px 20px rgba(13,138,214,0.25); transform:translateY(-1px); }
.button:disabled { opacity:0.7; cursor:not-allowed; transform:none; box-shadow:none; }
textarea { min-height:120px; resize: vertical; }
.upload-box { display:flex; flex-direction:column; gap:10px; }
.preview-list { display:flex; flex-wrap:wrap; gap:10px; }
.thumb { width:120px; height:80px; border:1px dashed #d0d7e2; border-radius:10px; overflow:hidden; position:relative; background:#f8fafc; }
.thumb img { width:100%; height:100%; object-fit:cover; }
.upload-btn { display:inline-flex; align-items:center; justify-content:center; padding:10px 14px; border:1px solid #0a6ba5; color:#0a6ba5; border-radius:10px; cursor:pointer; background:#fff; font-weight:600; width:140px; text-align:center; }
.upload-btn input { display:none; }
.video-chip { display:flex; align-items:center; gap:10px; padding:10px; background:#f0f6ff; border:1px solid #c5ddff; border-radius:10px; color:#1f2937; }
.chip { display:none; }
</style>
