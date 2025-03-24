import {createRouter, createWebHistory} from 'vue-router';
import { ElMessage } from 'element-plus';

const routes = [

    {
        path: '/login',
        name: 'LogIn',
        component: () => import('../views/Login.vue'),
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'),
    },
    {
        path: '/notFound',
        name: 'NotFound',
        component: () => import('../views/404.vue'),
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: {name: 'NotFound'},
    },
    {
        path: '/manager',
        name: 'Manager',
        component: () => import('../views/backend/Manager.vue'),
        redirect: '/manager/home',
        children: [
            {
                path: 'home',
                meta: {name: '主页'},
                component: () => import('../views/backend/BackendHome.vue'),
            },
            {
                path: 'admin',
                meta: {name: '管理员信息'},
                component: () => import('../views/backend/Admin.vue'),
            },
            {
                path: 'user',
                meta: {name: '用户信息'},
                component: () => import('../views/backend/User.vue'),
            },
            {
                path: 'person',
                meta: {name: '个人信息'},
                component: () => import('../views/backend/Person.vue'),
            },
            {
                path: 'password',
                meta: {name: '修改密码'},
                component: () => import('../views/backend/Password.vue'),
            },
            {
                path: 'notice',
                meta: {name: '系统公告'},
                component: () => import('../views/backend/Notice.vue'),
            },

            {
                path: 'house',
                meta: {name: '房源信息'},
                component: () => import('../views/backend/House.vue'),
            },
            {
                path: 'favorite',
                meta: {name: '收藏信息'},
                component: () => import('../views/backend/Favorite.vue'),
            },
            {
                path: 'rental',
                meta: {name: '租赁信息'},
                component: () => import('../views/backend/Rental.vue'),
            },

        ]
    },
    {
        // 重定向
        path: '/',
        redirect: '/login',
    },
    {
        path: '/front',
        name: 'Front',
        component: () => import('../views/frontend/Front.vue'),
        redirect: '/front/home',
        children: [
            {
                path: 'home',
                meta: {name: '主页'},
                component: () => import('../views/frontend/FrontendHome.vue'),
            },
            {
                path: 'house-detail/:id',
                meta: {name: '房源详情'},
                component: () => import('../views/frontend/HouseDetail.vue'),
            },
            {
                path: 'notice-list',
                meta: {name: '公告列表'},
                component: () => import('../views/frontend/NoticeList.vue'),
            },
            {
                path: 'user-center',
                meta: {name: '用户中心'},
                component: () => import('../views/frontend/UserCenter.vue'),
            },
            {
                path: 'favorite-list',
                meta: {name: '我的收藏'},
                component: () => import('../views/frontend/FavoriteList.vue'),
            },
            {
                path: 'rental-list',
                meta: {name: '我的租约'},
                component: () => import('../views/frontend/RentalList.vue'),
            },
            {
                path: 'personal-info',
                meta: {name: '个人资料'},
                component: () => import('../views/frontend/PersonalInfo.vue'),
            },
            {
                path: 'change-password',
                meta: {name: '修改密码'},
                component: () => import('../views/frontend/ChangePassword.vue'),
            }
        ]
    },

];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// 添加全局前置守卫
router.beforeEach((to, from, next) => {
    // 登录和注册页面以及404页面允许任何人访问
    const publicPages = ['/login', '/register', '/notFound'];
    
    // 如果是访问公共页面，直接放行
    if (publicPages.includes(to.path)) {
        return next();
    }
    
    // 检查用户角色和访问路径
    const isManagerPath = to.path.startsWith('/manager');
    const isFrontPath = to.path.startsWith('/front');
    
    // 根据访问路径选择对应的存储键
    const storageKey = isManagerPath ? 'admin_user' : 'normal_user';
    const userInfo = JSON.parse(localStorage.getItem(storageKey) || '{}');
    
    // 检查是否已登录
    if (!userInfo.id) {
        ElMessage.warning('请先登录');
        return next('/login');
    }
    
    // 管理员只能访问/manager路径
    if (isManagerPath && userInfo.role !== 'admin') {
        ElMessage.warning('您无权访问管理页面');
        return next('/front/home');
    }
    
    // 普通用户只能访问/front路径
    if (isFrontPath && userInfo.role !== 'user') {
        ElMessage.warning('您无权访问用户页面');
        return next('/manager/home');
    }
    
    // 所有检查通过，放行
    next();
});

export default router;