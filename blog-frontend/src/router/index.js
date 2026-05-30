import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
// 这里改成 Home，不要再引 App.vue 了！
import Home from '../views/Home.vue'

const routes = [
    { path: '/login', component: Login },
    // 这里也改成 Home
    { path: '/', component: Home }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫：未登录跳转到登录页
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router
