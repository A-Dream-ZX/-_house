<template>
  <div class="bg">
    <div
        style="width: 350px;
        height: 400px;
        background-color: #fff;
        opacity: 0.7;
        border-radius: 5px;
        padding: 40px 20px;
        box-shadow: 0 0 10px rgba(0,0,0,0.2)">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">
          欢 迎 登 录
        </div>
        <el-form-item prop="username">
          <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock"
                    placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="data.form.role">
            <el-option label="普通用户" value="user"></el-option>
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%" size="large" type="primary" @click="login">登 录</el-button>
        </div>
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


const formRef = ref()
const data = reactive({
  form: {
    role: 'user',

  },
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
    ],
  }

})


const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          // 根据用户角色选择不同的存储键
          const storageKey = data.form.role === 'admin' ? 'admin_user' : 'normal_user';
          
          // 存储用户信息
          localStorage.setItem(storageKey, JSON.stringify(res.data || {}))
          
          ElMessage.success('登录成功')
          if (data.form.role === 'user') {
            router.push('/front/home')
          } else {
            router.push('/manager/home')
          }
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>


<style scoped>
.bg {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  /*background-image: url("@/assets/imgs/");*/
  background-size: cover;
}

</style>