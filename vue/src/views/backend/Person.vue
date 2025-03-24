<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 20px; margin-left: 40px;">个人信息</div>
    <el-form ref="formRef" :model="data.user" :rules="data.rules" label-width="80px"
             style="padding: 20px 30px 20px 0px">
      <el-form-item label="账号" prop="username">
        <el-input v-model="data.user.username" autocomplete="off" placeholder="请输入账号"/>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="data.user.name" autocomplete="off" placeholder="请输入名称"/>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="data.user.phone" autocomplete="off" placeholder="请输入电话"/>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="data.user.email" autocomplete="off" placeholder="请输入邮箱"/>
      </el-form-item>

      <el-form-item label="头像" prop="avatar">
        <el-upload
            action="http://localhost:8080/files/upload"
            list-type="picture"
            :headers="{token:data.user.token}"
            :on-success="handleAvatarSuccess"
        >
          <el-button type="primary">上传头像</el-button>


        </el-upload>
      </el-form-item>

    </el-form>

    <div style="text-align: center">
      <el-button type="primary" @click="update" style="padding: 20px 35px">修改</el-button>


    </div>
  </div>

</template>

<script setup>
import {reactive} from 'vue'
import request from '@/utils/request.js';


const data = reactive({
  user: JSON.parse(localStorage.getItem('admin_user') || {}),
  // form:{},

})

const handleAvatarSuccess = (res) => {
  data.user.avatar = res.data

}

const emit = defineEmits(['updateName'])

const update = () => {
  let url
  if (data.user.role === "admin") {
    url = "/admin/update"
  }
  if (data.user.role === "user") {
    url = "/user/update"
  }
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('修改成功')
      setInterval(() => {
        localStorage.setItem('admin_user', JSON.stringify(data.user))
        emit('updateName')
      }, 1000)

    } else {
      ElMessage.error(res.msg)
    }
  })

}

</script>

<style>

</style>