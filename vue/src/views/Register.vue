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
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin-bottom: 40px;text-align: center;font-weight: bold;font-size: 24px">
          欢 迎 注 册
        </div>
        <el-form-item prop="username">
          <el-input v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock"
                    placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input show-password v-model="data.form.confirmPassword" autocomplete="off" prefix-icon="Lock"
                    placeholder="请再次确认密码"/>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button style="width: 100%" size="large" type="primary" @click="register">注 册</el-button>
        </div>
        <div style="text-align: right">
          已有账号，请<a color="#409EFF" href="/login"> 登录</a>
        </div>

      </el-form>

    </div>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from '@/utils/request.js';
import router from "@/router/index.js";


// 验证密码
const validatePass = (rule, value, callback) => {
  if (value !== data.form.password) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
}


const formRef = ref()
const data = reactive({
  form: {},
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
    ],
    confirmPassword: [
        {required: true, message: '请再次输入密码', trigger: 'blur'},
      {validator: validatePass, trigger: 'blur'},
    ]
  }

})


const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          //存储用户信息
          ElMessage.success('注册成功')
          router.push('/login')
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
  background-image: url("@/assets/imgs/");
  background-size: cover;
}

</style>