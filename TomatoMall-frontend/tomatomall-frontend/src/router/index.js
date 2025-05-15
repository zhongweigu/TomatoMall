import {createRouter, createWebHashHistory} from "vue-router"
import ProductManagement from '@/pages/ProductManagement.vue'

const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
        path: '/',
        redirect: '/login',
    }, {
        path: '/login',
        component: () => import('../pages/login.vue'),
        meta: {title: '用户登录'}
    }, {
        path: '/register',
        component: () => import('../pages/register.vue'),
        meta: {title: '用户注册'}
    }, {
        path: '/home',
        component: () => import('../pages/index.vue'),
        meta: {title: '主页'}
    },  {
        path: '/cart',
        component: () => import('../pages/CartPage.vue'),
        meta: {title: '购物车'}
    },  {
        path: '/orders',
        component: () => import('../pages/OrdersPage.vue'),
        meta: {title: '我的订单'}
    }, {
        path: '/personal',
        component: () => import('../pages/personal.vue'),
        meta: {title: '个人中心'}
    }, {
        path: '/product-management',
        name: 'ProductManagement',
        component: ProductManagement,
        meta: {
            title: '商品管理',
            showNavbar: true,
            requiresAuth: true,
            adminOnly: true  // 可选：仅管理员可访问
        }
    },{
        path: '/product/:id', // 动态路由参数 :id
        name: 'ProductDetail',
        component: () => import('../pages/detail.vue'),
        meta: {title: '商品详情页'}
    } ,{
        path: '/forum',
        component: () => import('@/pages/ForumPage.vue'),
        meta: {
            showNavbar: true
        }
    }, {
        path: '/forum/post/:id',
        name: 'ForumDetailPage',
        component: () => import('../pages/ForumDetailPage.vue'),
        meta: {
            title: '帖子详情',
            showNavbar: true
        }
    }
    ]
})

router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem('token');
    const role = sessionStorage.getItem('role');
    const title = to.meta.title;
    const permission = to.meta.permission;

    // 设置页面标题
    if (title) {
        document.title = title;
    }

    // 如果有 token，判断权限
    if (token) {
        // 如果需要权限判断
        if (permission) {
            if (role && permission.includes(role)) {
                return next();  // 权限匹配，继续
            } else {
                return next('/unauthorized');  // 没有权限，跳转到无权限页面
            }
        }
        return next();  // 如果不需要权限判断，直接进入
    }

    // 没有 token，判断路径进行跳转
    const loginPaths = ['/login', '/register'];
    if (loginPaths.includes(to.path)) {
        return next();  // 如果是登录或注册页面，直接进入
    }

    return next('/login');  // 其他页面跳转到登录页
});


export {router}
