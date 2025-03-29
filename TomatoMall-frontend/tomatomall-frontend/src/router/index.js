import {createRouter, createWebHashHistory} from "vue-router"

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
        redirect: '/personal',
        component: () => import('../pages/personal.vue'),
        meta: {title: '个人中心'}
    }, {
        path: '/personal',
        component: () => import('../pages/personal.vue'),
        meta: {title: '个人中心'}
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
