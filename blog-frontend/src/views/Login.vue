<template>
  <div style="width:350px;margin:100px auto;padding:20px;border:1px solid #ccc;border-radius:8px;">
    <h2 style="text-align:center;">登录</h2>

    <!-- 错误提示 -->
    <div v-if="errorMsg" style="color:#f56c6c;text-align:center;margin-bottom:10px;background:#fef0f0;padding:8px;border-radius:4px;">
      {{ errorMsg }}
    </div>

    <div style="margin:15px 0;">
      <label>用户名：</label>
      <input v-model="form.username" style="width:100%;padding:6px;" placeholder="请输入用户名" @keyup.enter="login" />
    </div>

    <div style="margin:15px 0;">
      <label>密码：</label>
      <input v-model="form.password" type="password" style="width:100%;padding:6px;" placeholder="请输入密码" @keyup.enter="login" />
    </div>

    <button
      style="width:100%;padding:8px;background:#42b983;color:white;border:none;border-radius:4px;"
      :disabled="loading"
      @click="login"
    >
      {{ loading ? '登录中...' : '登录' }}
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const errorMsg = ref('')
const form = ref({
  username: '',
  password: ''
})

const login = async () => {
  errorMsg.value = ''

  if (!form.value.username || !form.value.password) {
    errorMsg.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  try {
    const res = await request.post('/api/login', form.value)
    localStorage.setItem('token', res)
    router.push('/')
  } catch (e) {
    const msg = e?.response?.data?.msg || e?.message || '登录失败，请检查网络'
    errorMsg.value = msg
  } finally {
    loading.value = false
  }
}
</script>
