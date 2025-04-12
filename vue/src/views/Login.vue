<template>
  <!-- 登录页面背景容器 -->
  <div class="bg">
    <!-- 登录表单卡片 -->
    <div
        style="width: 350px;
        height: 400px;
        background-color: #fff;
        opacity: 0.7;
        border-radius: 5px;
        padding: 40px 20px;
        box-shadow: 0 0 10px rgba(0,0,0,0.2)">
      <!-- Element Plus表单，绑定数据模型和验证规则 -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <!-- 标题 -->
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">
          欢 迎 登 录
        </div>
        <!-- 用户名输入框 -->
        <el-form-item prop="username">
          <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock"
                    placeholder="请输入密码"/>
        </el-form-item>
        <!-- 角色选择下拉框 -->
        <el-form-item prop="role">
          <el-select v-model="data.form.role">
            <el-option label="普通用户" value="user"></el-option>
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <!-- 登录按钮 -->
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%" size="large" type="primary" @click="login">登 录</el-button>
        </div>
        <!-- 注册链接 -->
        <div style="text-align: right">
          还没有账号？请<a color="#409EFF" href="/register"> 注册</a>
        </div>

      </el-form>

    </div>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from '@/utils/request.js';
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

/**
 * 表单引用，用于表单验证
 */
const formRef = ref()

/**
 * 响应式数据对象
 * 包含表单数据和验证规则
 */
const data = reactive({
  form: {
    role: 'user', // 默认选择普通用户角色
  },
  rules: {
    // 用户名验证规则
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'}, // 必填验证
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}, // 长度验证
    ],
    // 密码验证规则
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'}, // 必填验证
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}, // 长度验证
    ],
  }
})

/**
 * 登录方法
 * 1. 验证表单
 * 2. 发送登录请求
 * 3. 根据响应处理登录结果
 * 4. 保存用户信息并导航到对应页面
 */
const login = () => {
  // 表单验证
  formRef.value.validate((valid) => {
    if (valid) {
      // 发送登录请求
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          // 根据用户角色选择不同的存储键
          const storageKey = data.form.role === 'admin' ? 'admin_user' : 'normal_user';
          
          // 存储用户信息到localStorage
          localStorage.setItem(storageKey, JSON.stringify(res.data || {}))
          
          // 添加当前用户角色信息，用于request.js中判断
          localStorage.setItem('current_user', JSON.stringify({role: data.form.role}))
          
          // 显示登录成功消息
          ElMessage.success('登录成功')
          
          // 根据角色导航到不同的首页
          if (data.form.role === 'user') {
            router.push('/front/home') // 普通用户跳转到前台首页
          } else {
            router.push('/manager/home') // 管理员跳转到后台首页
          }
        } else {
          // 显示错误消息
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>


<style scoped>
/* 登录页背景样式 */
.bg {
  height: 100vh; /* 视口高度 */
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  overflow: hidden;
  /*background-image: url("@/assets/imgs/");*/
  background-size: cover;
}
</style>