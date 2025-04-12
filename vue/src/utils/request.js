import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

/**
 * 创建axios实例
 * 配置基础URL和超时时间
 */
const request = axios.create({
    baseURL: "http://localhost:8080", // 后端API基础URL
    timeout: 5000,  // 请求超时时间，单位毫秒
});

/**
 * 请求拦截器
 * 在请求发送前统一处理请求头和权限验证
 */
request.interceptors.request.use(config => {
    // 设置Content-Type
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    
    // 定义不需要token的公共路径
    const publicPaths = ['/', '/login', '/register'];
    // 检查当前请求URL是否匹配公共路径
    const isPublicPath = publicPaths.some(path => config.url === path || config.url.startsWith(path + '?'));
    
    // 只有非公共路径才添加token
    if (!isPublicPath) {
        // 从 localStorage 中读取当前用户的角色信息
        const currentUser = JSON.parse(localStorage.getItem('current_user') || '{}');
        const storageKey = currentUser.role === 'admin' ? 'admin_user' : 'normal_user';
        
        // 获取用户信息并添加token到请求头
        let user = JSON.parse(localStorage.getItem(storageKey) || '{}');
        config.headers['token'] = user.token;
    }
    return config;
}, error => {
    // 请求错误处理
    return Promise.reject(error);
});

/**
 * 响应拦截器
 * 统一处理响应数据和错误情况
 */
request.interceptors.response.use(response => {
        // 获取响应数据
        let res = response.data;
        // 将字符串响应转换为JSON对象
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        // 处理401未授权错误
        if (res.code === '401') {
            // 显示错误消息
            ElMessage.error(res.msg);
            
            // 从 localStorage 中读取当前用户的角色信息
            const currentUser = JSON.parse(localStorage.getItem('current_user') || '{}');
            const storageKey = currentUser.role === 'admin' ? 'admin_user' : 'normal_user';
            
            // 清除登录信息
            localStorage.removeItem(storageKey);
            // 跳转到登录页
            router.push("/login");
        } else {
            // 正常响应直接返回数据
            return res;
        }
    },
    error => {
        // 处理HTTP错误
        if (error.response.status === 404) {
            ElMessage.error('服务器找不到该资源');
        } else if (error.response.status === 500) {
            ElMessage.error('服务器内部错误');
        } else {
            // 打印其他错误到控制台
            console.log(error);
        }
        return Promise.reject(error);
    });

export default request;