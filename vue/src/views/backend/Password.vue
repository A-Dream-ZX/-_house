<template>
  <div class="card" style="width: 50%; align-items: center">
    <div style="font-size: 20px; margin-left: 40px;">修改密码</div>

    <el-form ref="formRef" :model="data.user" :rules="data.rules"
             label-width="80px"
             style="padding: 20px 30px 20px 0px">

      <el-form-item prop="password" label="原密码">
        <el-input show-password v-model="data.user.password" autocomplete="off" prefix-icon="Lock"
                  placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item prop="newpassword" label="新密码">
        <el-input show-password v-model="data.user.newPassword" autocomplete="off" prefix-icon="Lock"
                  placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item prop="new2password" label="确认密码">
        <el-input show-password v-model="data.user.new2Password" autocomplete="off" prefix-icon="Lock"
                  placeholder="请输入密码"/>
      </el-form-item>

      <div style="text-align: center">
        <el-button type="primary" @click="updatePassword" style="padding: 20px 35px">提交</el-button>
      </div>
    </el-form>
  </div>

</template>


<script setup>

import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";


const data = reactive({
  user: JSON.parse(localStorage.getItem('admin_user') || {}),
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
// const updatePassword = () => {
//   formRef.value.validate(valid => {
//     if (valid) {
//       request.post('/updatePassword', data.user).then(res=>{
//         if(res.code==='200'){
//           console.log(res.data)
//           ElMessage.success('修改成功')
//           // location.removeItem('user')
//           // location.href = '/login'
//         }else{
//           ElMessage.error(res.msg)
//         }
//       })
//     }
//   })
// }


const updatePassword = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功')
          localStorage.removeItem('admin_user')
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

</style>