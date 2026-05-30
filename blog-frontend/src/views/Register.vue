<template>
  <div class="page">
    <div class="card">
      <h1 class="logo">Blog</h1>
      <p class="subtitle">创建你的账号</p>

      <div v-if="errorMsg" class="error">{{ errorMsg }}</div>

      <div class="form-group">
        <label>用户名</label>
        <input v-model="form.username" placeholder="请输入用户名" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="form.password" type="password" placeholder="请输入密码" />
      </div>
      <div class="form-group">
        <label>确认密码</label>
        <input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
      </div>
      <button class="btn btn-primary" :disabled="loading" @click="register">
        {{ loading ? '注册中...' : '注 册' }}
      </button>

      <p class="footer">
        已有账号？<router-link to="/login">立即登录</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const errorMsg = ref('')
const form = ref({ username: '', password: '', confirmPassword: '' })

const register = async () => {
  errorMsg.value = ''
  if (!form.value.username || !form.value.password) { errorMsg.value = '请输入用户名和密码'; return }
  if (form.value.password !== form.value.confirmPassword) { errorMsg.value = '两次密码不一致'; return }
  if (form.value.password.length < 3) { errorMsg.value = '密码至少 3 位'; return }

  loading.value = true
  try {
    await request.post('/api/register', { username: form.value.username, password: form.value.password })
    alert('注册成功，请登录')
    router.push('/login')
  } catch (e) {
    errorMsg.value = e?.msg || '注册失败，请稍后再试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
* { box-sizing: border-box; margin: 0; padding: 0; }
.page { min-height: 100vh; background: linear-gradient(135deg, #e8f5e9 0%, #f5f7fa 50%, #e3f2fd 100%); display: flex; align-items: center; justify-content: center; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif; }
.card { background: #fff; border-radius: 16px; padding: 36px 36px; width: 380px; box-shadow: 0 20px 60px rgba(0,0,0,.08); }
.logo { font-size: 28px; font-weight: 800; color: #42b983; text-align: center; letter-spacing: -1px; }
.subtitle { text-align: center; color: #909399; font-size: 15px; margin: 8px 0 24px; }
.error { background: #fef0f0; color: #f56c6c; padding: 10px 14px; border-radius: 8px; font-size: 13px; margin-bottom: 16px; text-align: center; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; color: #606266; margin-bottom: 6px; font-weight: 500; }
.form-group input { width: 100%; padding: 10px 14px; border: 1px solid #dcdfe6; border-radius: 10px; font-size: 15px; outline: none; transition: .2s; }
.form-group input:focus { border-color: #42b983; box-shadow: 0 0 0 3px rgba(66,185,131,.12); }
.btn { width: 100%; padding: 12px; border-radius: 10px; border: none; font-size: 16px; cursor: pointer; transition: .2s; font-weight: 600; }
.btn-primary { background: linear-gradient(135deg, #42b983, #3aa373); color: #fff; margin-top: 6px; }
.btn-primary:hover { opacity: .9; transform: translateY(-1px); box-shadow: 0 6px 20px rgba(66,185,131,.3); }
.btn:disabled { opacity: .6; cursor: not-allowed; transform: none; }
.footer { text-align: center; margin-top: 20px; font-size: 14px; color: #909399; }
.footer a { color: #42b983; text-decoration: none; font-weight: 500; }
</style>
