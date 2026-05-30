<template>
  <div class="page">
    <!-- 顶部栏 -->
    <header class="header">
      <div class="header-left">
        <h1 class="logo">Blog</h1>
        <nav>
          <router-link to="/" class="nav-link active">用户管理</router-link>
          <router-link to="/articles" class="nav-link">文章管理</router-link>
        </nav>
      </div>
      <div class="header-right">
        <span class="user-badge">
          <span class="avatar">{{ currentUser.username?.charAt(0)?.toUpperCase() }}</span>
          {{ currentUser.username }}
          <em class="role-tag">{{ currentUser.role === 'admin' ? '管理员' : '用户' }}</em>
        </span>
        <button class="btn btn-outline" @click="handleLogout">退出</button>
      </div>
    </header>

    <!-- 工具栏 -->
    <div class="toolbar">
      <span class="count">共 {{ list.length }} 位用户</span>
    </div>

    <!-- 加载 / 错误 -->
    <div v-if="loading" class="state">加载中...</div>
    <div v-else-if="error" class="state error">{{ error }}</div>

    <!-- 用户卡片列表 -->
    <div v-else class="card-list">
      <div v-for="u in list" :key="u.id" class="card">
        <div class="card-body">
          <div class="card-avatar">{{ u.username?.charAt(0)?.toUpperCase() }}</div>
          <div class="card-info">
            <div class="card-title">
              {{ u.username }}
              <span :class="['role-chip', u.role === 'admin' ? 'role-admin' : '']">
                {{ u.role === 'admin' ? '管理员' : '用户' }}
              </span>
            </div>
            <div class="card-meta">@{{ u.username }} · ID: {{ u.id }}</div>
          </div>
        </div>
        <div v-if="currentUser.role === 'admin'" class="card-actions">
          <button class="btn btn-sm" @click="handleEdit(u)">编辑</button>
          <button class="btn btn-sm btn-danger" @click="handleDelete(u.id)">删除</button>
        </div>
      </div>
    </div>

    <!-- 弹窗 -->
    <div v-if="dialogVisible" class="modal" @click.self="dialogVisible=false">
      <div class="modal-box">
        <h3>编辑用户</h3>
        <div class="form-group">
          <label>用户名</label>
          <input :value="form.username" disabled class="input-disabled" />
        </div>
        <div class="form-group">
          <label>角色</label>
          <select v-model="form.role">
            <option value="user">普通用户</option>
            <option value="admin">管理员</option>
          </select>
        </div>
        <div class="modal-actions">
          <button class="btn" @click="dialogVisible=false">取消</button>
          <button class="btn btn-primary" @click="submitForm">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const currentUser = ref(JSON.parse(localStorage.getItem('user') || '{}'))

const list = ref([])
const loading = ref(false)
const error = ref('')
const dialogVisible = ref(false)
const form = ref({ id: '', username: '', role: 'user' })

const handleLogout = () => {
  if (!confirm('确定要退出登录吗？')) return
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const getList = async () => {
  loading.value = true
  error.value = ''
  try {
    list.value = await request.get('/api/users')
  } catch (e) {
    error.value = '加载失败，请检查网络或登录状态'
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleEdit = (row) => {
  form.value = { id: row.id, username: row.username, role: row.role }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    await request.put(`/api/users/${form.value.id}`, {
      role: form.value.role
    })
    dialogVisible.value = false
    getList()
  } catch (e) { alert('提交失败'); console.error(e) }
}

const handleDelete = async (id) => {
  if (!confirm('确定删除该用户吗？')) return
  try { await request.delete(`/api/users/${id}`); getList() }
  catch (e) { alert('删除失败'); console.error(e) }
}

onMounted(() => getList())
</script>

<style scoped>
* { box-sizing: border-box; margin: 0; padding: 0; }
.page { min-height: 100vh; background: #f5f7fa; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; color: #2c3e50; }
.header { display: flex; justify-content: space-between; align-items: center; background: #fff; padding: 0 32px; height: 60px; box-shadow: 0 1px 3px rgba(0,0,0,.06); }
.header-left { display: flex; align-items: center; gap: 32px; }
.logo { font-size: 20px; font-weight: 700; color: #42b983; letter-spacing: -0.5px; }
nav { display: flex; gap: 4px; }
.nav-link { padding: 8px 16px; border-radius: 6px; text-decoration: none; color: #606266; font-size: 14px; transition: .2s; }
.nav-link:hover { background: #f0f9f4; color: #42b983; }
.nav-link.active { background: #e8f5e9; color: #42b983; font-weight: 600; }
.header-right { display: flex; align-items: center; gap: 16px; }
.user-badge { display: flex; align-items: center; gap: 8px; font-size: 14px; color: #606266; }
.avatar { width: 32px; height: 32px; border-radius: 50%; background: linear-gradient(135deg, #42b983, #3aa373); color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 14px; }
.role-tag { font-style: normal; font-size: 12px; padding: 2px 8px; border-radius: 10px; background: #f0f9f4; color: #42b983; }

.toolbar { margin: 24px 0 0; padding: 0 32px; display: flex; justify-content: space-between; align-items: center; }
.count { font-size: 14px; color: #909399; }

.card-list { margin: 16px 0 40px; padding: 0 32px; display: flex; flex-direction: column; gap: 10px; }
.card { background: #fff; border-radius: 10px; padding: 16px 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 4px rgba(0,0,0,.04); transition: .2s; }
.card:hover { box-shadow: 0 4px 12px rgba(0,0,0,.08); transform: translateY(-1px); }
.card-body { display: flex; align-items: center; gap: 14px; }
.card-avatar { width: 42px; height: 42px; border-radius: 50%; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 16px; flex-shrink: 0; }
.card-info { display: flex; flex-direction: column; gap: 4px; }
.card-title { font-size: 15px; font-weight: 600; display: flex; align-items: center; gap: 8px; }
.card-meta { font-size: 13px; color: #909399; }
.role-chip { font-size: 11px; padding: 1px 8px; border-radius: 10px; background: #f0f2f5; color: #909399; font-weight: 400; }
.role-chip.role-admin { background: #fef0f0; color: #f56c6c; }
.card-actions { display: flex; gap: 8px; }

.state { text-align: center; padding: 60px 0; color: #909399; font-size: 15px; }
.state.error { color: #f56c6c; }

/* 弹窗 */
.modal { position: fixed; inset: 0; background: rgba(0,0,0,.35); backdrop-filter: blur(4px); display: flex; align-items: flex-start; justify-content: center; z-index: 100; overflow-y: auto; padding: 60px 0; }
.modal-box { background: #fff; border-radius: 14px; padding: 28px; width: 420px; box-shadow: 0 20px 60px rgba(0,0,0,.15); }
.modal-box h3 { font-size: 18px; margin-bottom: 20px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; color: #606266; margin-bottom: 6px; font-weight: 500; }
.form-group input, .form-group select { width: 100%; padding: 10px 12px; border: 1px solid #dcdfe6; border-radius: 8px; font-size: 14px; transition: .2s; outline: none; background: #fff; }
.form-group input:focus, .form-group select:focus { border-color: #42b983; box-shadow: 0 0 0 3px rgba(66,185,131,.12); }
.input-disabled { background: #f5f7fa; color: #909399; cursor: not-allowed; }
.modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 24px; }

/* 按钮 */
.btn { padding: 8px 18px; border-radius: 8px; border: 1px solid #dcdfe6; background: #fff; color: #606266; font-size: 13px; cursor: pointer; transition: .2s; }
.btn:hover { border-color: #42b983; color: #42b983; }
.btn-primary { background: #42b983; color: #fff; border-color: #42b983; }
.btn-primary:hover { background: #3aa373; }
.btn-danger { color: #f56c6c; border-color: #fbc4c4; }
.btn-danger:hover { background: #fef0f0; }
.btn-outline { background: transparent; border: 1px solid #dcdfe6; }
.btn-sm { padding: 5px 12px; font-size: 12px; }
</style>
