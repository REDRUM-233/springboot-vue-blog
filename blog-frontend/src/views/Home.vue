<template>
  <div style="max-width:700px;margin:30px auto;padding:0 20px;">
    <h2>用户管理（CRUD 完整）</h2>

    <!-- 退出登录按钮 -->
    <button
        @click="handleLogout"
        style="background:#f56c6c;color:white;border:none;padding:6px 12px;border-radius:4px;cursor:pointer;margin-bottom:10px;margin-left:10px;"
    >
      退出登录
    </button>

    <!-- 新增按钮 -->
    <button type="primary" @click="handleAdd" style="margin-bottom:10px;">
      新增用户
    </button>

    <!-- 加载/错误提示 -->
    <div v-if="loading">加载中...</div>
    <div v-if="error" style="color:red;">{{ error }}</div>

    <!-- 用户列表 -->
    <table border="1" cellpadding="6" cellspacing="0" style="width:100%;">
      <thead>
      <tr>
        <th>ID</th>
        <th>用户名</th>
        <th>昵称</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="u in list" :key="u.id">
        <td>{{ u.id }}</td>
        <td>{{ u.username }}</td>
        <td>{{ u.nickname }}</td>
        <td>
          <button @click="handleEdit(u)" style="margin-right:6px;">编辑</button>
          <button @click="handleDelete(u.id)" style="color:red;">删除</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 弹窗：新增/编辑 -->
    <div v-if="dialogVisible"
         style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;">
      <div style="background:#fff;padding:20px;border-radius:6px;width:400px;">
        <h3>{{ isEdit ? '编辑用户' : '新增用户' }}</h3>
        <div style="margin:10px 0;">
          <label>用户名：</label>
          <input v-model="form.username" placeholder="请输入用户名" style="width:100%;">
        </div>
        <div style="margin:10px 0;">
          <label>密码：</label>
          <input v-model="form.password" type="password"
                 :placeholder="isEdit ? '留空则不修改密码' : '请输入密码'"
                 style="width:100%;">
        </div>
        <div style="margin:10px 0;">
          <label>昵称：</label>
          <input v-model="form.nickname" placeholder="请输入昵称" style="width:100%;">
        </div>
        <div style="text-align:right;margin-top:20px;">
          <button @click="dialogVisible=false" style="margin-right:6px;">取消</button>
          <button type="primary" @click="submitForm">确定</button>
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

// 列表数据
const list = ref([])
const loading = ref(false)
const error = ref('')

// 弹窗 & 表单
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: '',
  username: '',
  password: '',
  nickname: ''
})

// 退出登录
const handleLogout = () => {
  if (!confirm('确定要退出登录吗？')) return
  localStorage.removeItem('token')
  router.push('/login')
  alert('已退出登录')
}

// 1. 获取列表
const getList = async () => {
  loading.value = true
  error.value = ''
  try {
    const data = await request.get('/api/users')
    list.value = data
  } catch (e) {
    error.value = '请求失败'
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 2. 新增：打开弹窗（清空表单）
const handleAdd = () => {
  isEdit.value = false
  form.value = { id: '', username: '', password: '', nickname: '' }
  dialogVisible.value = true
}

// 3. 编辑：打开弹窗 + 回显数据
const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 4. 提交表单（新增/编辑共用）
const submitForm = async () => {
  if (!form.value.username || !form.value.password || !form.value.nickname) {
    alert('请填写完整信息')
    return
  }
  try {
    if (isEdit.value) {
      await request.put(`/api/users/${form.value.id}`, form.value)
      alert('编辑成功')
    } else {
      await request.post('/api/users', form.value)
      alert('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (e) {
    alert('提交失败')
    console.error(e)
  }
}

// 5. 删除 + 确认提示
const handleDelete = async (id) => {
  if (!confirm('确定要删除这条记录吗？')) return
  try {
    await request.delete(`/api/users/${id}`)
    alert('删除成功')
    getList()
  } catch (e) {
    alert('删除失败')
    console.error(e)
  }
}

onMounted(() => {
  getList()
})
</script>
