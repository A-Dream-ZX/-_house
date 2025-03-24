import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const request = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 5000,  // 请求超时时间
});

// request拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    
    // 从 localStorage 中读取当前用户的角色信息
    const currentUser = JSON.parse(localStorage.getItem('current_user') || '{}');
    const storageKey = currentUser.role === 'admin' ? 'admin_user' : 'normal_user';
    
    let user = JSON.parse(localStorage.getItem(storageKey) || '{}');
    config.headers['token'] = user.token;
    return config;
}, error => {
    return Promise.reject(error);
});

// response拦截器
request.interceptors.response.use(response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        if (res.code === '401') {
            ElMessage.error(res.msg);
            
            // 从 localStorage 中读取当前用户的角色信息
            const currentUser = JSON.parse(localStorage.getItem('current_user') || '{}');
            const storageKey = currentUser.role === 'admin' ? 'admin_user' : 'normal_user';
            
            localStorage.removeItem(storageKey);
            router.push("/login");
        } else {
            return res;
        }
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('服务器找不到该资源');
        } else if (error.response.status === 500) {
            ElMessage.error('服务器内部错误');
        } else {
            console.log(error);
        }
        return Promise.reject(error);
    });

export default request;