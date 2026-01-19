<template>
  <div class="login-shell">
    <div class="login-panel card">
      <div class="title">景区导览 · 管理端</div>
      <div class="subtitle">请输入管理员账号密码登录</div>
      <form @submit.prevent="submit" class="form">
        <label class="field">账号
          <input v-model="username" placeholder="请输入账号" />
        </label>
        <label class="field">密码
          <input v-model="password" type="password" placeholder="请输入密码" />
        </label>
        <div class="actions">
          <span v-if="error" class="error">{{ error }}</span>
          <button class="button" :disabled="loading">{{ loading ? '登录中...' : '登录' }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)
const auth = useAuthStore()
const router = useRouter()

async function submit() {
  error.value = null
  if (!username.value || !password.value) {
    error.value = '请输入账号和密码'
    return
  }
  loading.value = true
  try {
    const resp = await auth.login(username.value, password.value)
    if (resp?.data?.code === 200) {
      router.push('/admin')
    } else {
      error.value = resp?.data?.msg || '登录失败'
    }
  } catch (e) {
    error.value = e?.response?.data?.msg || e.message || '网络错误'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-shell { min-height:100vh; display:flex; align-items:center; justify-content:center; background:
  linear-gradient(135deg, #3a8fda 0%, #1f6fbf 35%, #0c559f 70%, #0a4a86 100%),
  radial-gradient(circle at 20% 20%, rgba(255,255,255,0.45) 0, rgba(255,255,255,0) 40%),
  radial-gradient(circle at 80% 30%, rgba(255,255,255,0.35) 0, rgba(255,255,255,0) 32%),
  linear-gradient(135deg, rgba(255,255,255,0.1) 25%, transparent 25%) 0 0 / 28px 28px,
  linear-gradient(45deg, rgba(255,255,255,0.08) 25%, transparent 25%) 0 0 / 36px 36px;
  padding:24px; box-sizing:border-box; background-blend-mode: screen, normal, normal, overlay, overlay; }
.login-panel { width:400px; border:1px solid #e2e8f0; border-radius:18px; box-shadow:0 20px 48px rgba(0,0,0,0.14); backdrop-filter: blur(6px); background:rgba(255,255,255,0.92); padding:28px; }
.title { font-size:22px; font-weight:800; color:#0a6ba5; margin-bottom:10px; letter-spacing:0.3px; }
.subtitle { color:#6b7280; margin-bottom:20px; }
.form { display:flex; flex-direction:column; gap:14px; }
.field { display:flex; flex-direction:column; gap:6px; font-weight:600; color:#374151; }
.field input { width:100%; padding:12px 14px; border:1px solid #e6eef7; border-radius:12px; background:#f9fbff; transition:border-color .15s ease, box-shadow .15s ease; }
.field input:focus { outline:none; border-color:#0a7dc2; box-shadow:0 0 0 3px rgba(18,161,240,0.18); }
.actions { display:flex; align-items:center; justify-content:flex-end; gap:10px; margin-top:6px; }
.error { color:#e44f4f; font-weight:600; margin-right:auto; }
.button { padding:10px 24px; border:none; border-radius:12px; background:linear-gradient(135deg,#0d8ad6,#0b6fb5); color:#fff; font-weight:700; cursor:pointer; transition:transform .12s ease, box-shadow .12s ease, opacity .12s ease; }
.button:hover { box-shadow:0 10px 20px rgba(13,138,214,0.25); transform:translateY(-1px); }
.button:disabled { opacity:0.7; cursor:not-allowed; transform:none; box-shadow:none; }
</style>
