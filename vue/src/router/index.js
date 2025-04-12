import {createRouter, createWebHistory} from 'vue-router';
import {ElMessage} from 'element-plus';

/**
 * 路由配置
 * 定义应用的所有路由路径和对应的组件
 */
const routes = [
    {
        path: '/',
        redirect: '/login', // 确保根路径直接重定向到登录页
    },
    {
        path: '/login',
        name: 'LogIn',
        component: () => import('../views/Login.vue'), // 懒加载登录组件
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue'), // 懒加载注册组件
    },
    {
        path: '/notFound',
        name: 'NotFound',
        component: () => import('../views/404.vue'), // 懒加载404页面组件
    },
    {
        path: '/:pathMatch(.*)*', // 通配符路径，匹配任何未定义的路由
        redirect: {name: 'NotFound'}, // 重定向到404页面
    },
    {
        // 管理员后台路由
        path: '/manager',
        name: 'Manager',
        component: () => import('../views/backend/Manager.vue'),
        redirect: '/manager/home', // 默认重定向到管理员首页
        children: [
            {
                path: 'home',
                meta: {name: '主页'}, // 用于菜单显示的元数据
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
        // 普通用户前台路由
        path: '/front',
        name: 'Front',
        component: () => import('../views/frontend/Front.vue'),
        redirect: '/front/home', // 默认重定向到用户首页
        children: [
            {
                path: 'home',
                meta: {name: '主页'},
                component: () => import('../views/frontend/FrontendHome.vue'),
            },
            {
                path: 'house-detail/:id', // 动态路由，用于显示特定房源详情
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

/**
 * 创建路由实例
 * 使用HTML5历史模式，可以通过正常的URL访问，不带#号
 */
const router = createRouter({
    history: createWebHistory(),
    routes
});

/**
 * 全局前置守卫
 * 用于路由访问权限控制和登录状态验证
 */
router.beforeEach((to, from, next) => {
    // 如果目标是登录页，始终允许访问
    if (to.path === '/login') {
        return next();
    }

    // 定义不需要登录就可访问的公共页面
    const publicPages = ['/login', '/register', '/notFound'];

    // 如果是访问公共页面，直接放行
    if (publicPages.includes(to.path)) {
        return next();
    }

    // 路径权限判断：区分管理员路径和普通用户路径
    const isManagerPath = to.path.startsWith('/manager');
    const isFrontPath = to.path.startsWith('/front');

    // 根据访问路径选择对应的存储键
    // admin_user 存储管理员信息，normal_user 存储普通用户信息
    const storageKey = isManagerPath ? 'admin_user' : 'normal_user';
    const userInfo = JSON.parse(localStorage.getItem(storageKey) || '{}');

    // 检查是否已登录（通过ID是否存在判断）
    if (!userInfo.id) {
        ElMessage.warning('请先登录');
        return next('/login'); // 未登录用户直接跳转到登录页
    }

    // 角色权限控制：管理员只能访问/manager路径
    if (isManagerPath && userInfo.role !== 'admin') {
        ElMessage.warning('您无权访问管理页面');
        return next('/front/home'); // 重定向到用户首页
    }

    // 角色权限控制：普通用户只能访问/front路径
    if (isFrontPath && userInfo.role !== 'user') {
        ElMessage.warning('您无权访问用户页面');
        return next('/manager/home'); // 重定向到管理员首页
    }

    // 所有检查通过，放行
    next();
});

export default router;