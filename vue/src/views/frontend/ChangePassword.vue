<template>
  <div class="change-password">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>

      <el-form
          ref="formRef"
          :model="data.user"
          :rules="rules"
          label-width="100px"
          class="password-form"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
              v-model="data.user.password"
              type="password"
              show-password
              placeholder="请输入原密码"
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="data.user.newPassword"
              type="password"
              show-password
              placeholder="请输入新密码"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="new2Password">
          <el-input
              v-model="data.user.new2Password"
              type="password"
              show-password
              placeholder="请确认新密码"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="updatePassword">确认修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>



<script setup>
import { reactive, ref } from 'vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';
import router from "@/router/index.js";


const data = reactive({
  user: JSON.parse(localStorage.getItem('normal_user') || {}),
  rules: {
    password: [
      {required: true, message: '请输入旧密码', trigger: 'blur'},
    ],
    newPassword: [
      {required: true, message: '请输入新密码', trigger: 'blur'},
    ],
    new2Password: [
      {required: true, message: '请确认新密码', trigger: 'blur'},
    ],

  }

})

const formRef = ref()

const updatePassword = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('密码修改成功')
          localStorage.removeItem('normal_user')
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
.change-password {
  max-width: 600px;
  margin: 20px auto;
  padding: 0 20px;
}

.password-form {
  margin-top: 20px;
}
</style>