import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Home from '../views/Home.vue'
import Articles from '../views/Articles.vue'

const routes = [
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/', component: Home },
    { path: '/articles', component: Articles }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫：未登录跳转到登录页
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && to.path !== '/register' && to.path !== '/articles' && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router
