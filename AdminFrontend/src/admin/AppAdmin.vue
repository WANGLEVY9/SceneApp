<template>
  <div class="admin-shell">
    <aside class="sidebar">
      <div class="brand">景区导览 · 管理端</div>
      <nav>
        <router-link :class="['nav-item', { active: route.path === '/admin' }]" to="/admin">概览</router-link>
        <router-link to="/admin/intro" class="nav-item" active-class="active">引领区内容</router-link>
        <router-link to="/admin/spots" class="nav-item" active-class="active">景点管理</router-link>
        <router-link to="/admin/merchants" class="nav-item" active-class="active">商家管理</router-link>
        <router-link to="/admin/checkin-spots" class="nav-item" active-class="active">打卡点管理</router-link>
        <router-link to="/admin/checkin-records" class="nav-item" active-class="active">打卡记录管理</router-link>
        <router-link to="/admin/gifts" class="nav-item" active-class="active">礼品管理</router-link>
        <router-link to="/admin/gift-exchanges" class="nav-item" active-class="active">礼品兑换</router-link>
      </nav>
      <button class="logout" @click="logout">退出登录</button>
    </aside>

    <div class="main-area">
      <header class="topbar">
        <div class="page-title">智慧景区后台</div>
        <div class="top-actions">
          <span class="user">管理员</span>
        </div>
      </header>
      <main class="content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAuthStore } from './stores/auth'
const route = useRoute()
const auth = useAuthStore()
const logout = () => auth.logout()
</script>

<style scoped>
.admin-shell { display:flex; min-height:100vh; background:#f5f7fb; color:#1f2937; }
.sidebar { width:240px; background:linear-gradient(180deg,#0b7fc2,#0a6ba5); color:#fff; padding:22px 16px; display:flex; flex-direction:column; gap:12px; box-shadow: 4px 0 16px rgba(0,0,0,0.08); position:sticky; top:0; height:100vh; }
.brand { font-weight:700; font-size:18px; letter-spacing:0.5px; padding:10px 10px; border-radius:12px; background:rgba(255,255,255,0.1); margin-bottom:18px; }
nav { display:flex; flex-direction:column; gap:8px; margin-top:8px; flex:1; }
.nav-item { display:block; padding:10px 12px; border-radius:10px; color:rgba(255,255,255,0.9); text-decoration:none; font-weight:600; transition:all .18s ease; background:transparent; }
.nav-item:hover { background:rgba(255,255,255,0.16); color:#fff; }
.nav-item.active { background:#fff; color:#0a6ba5; box-shadow:0 10px 20px rgba(0,0,0,0.12); }
.logout { margin-top:auto; border:none; background:rgba(255,255,255,0.12); color:#fff; padding:10px 12px; border-radius:10px; cursor:pointer; font-weight:600; transition:all .15s ease; }
.logout:hover { background:rgba(255,255,255,0.2); }
.main-area { flex:1; display:flex; flex-direction:column; min-width:0; }
.topbar { height:64px; background:#fff; display:flex; align-items:center; justify-content:space-between; padding:0 20px; border-bottom:1px solid #edf2f9; box-shadow:0 2px 8px rgba(19,40,74,0.04); position:sticky; top:0; z-index:10; }
.page-title { font-size:18px; font-weight:700; color:#0a6ba5; }
.content { flex:1; padding:20px; }
.user { color:#4b5563; font-weight:600; }

@media (max-width: 900px) {
  .admin-shell { flex-direction:column; }
  .sidebar { flex-direction:row; align-items:center; width:100%; height:auto; position:relative; }
  nav { flex-direction:row; flex-wrap:wrap; }
  .nav-item { flex:1 0 45%; text-align:center; }
  .main-area { min-height: calc(100vh - 120px); }
}
</style>
