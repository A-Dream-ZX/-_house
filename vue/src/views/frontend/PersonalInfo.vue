<template>
  <div class="personal-info">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>

      <el-form :model="form" label-width="100px">
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-avatar v-if="form.avatar" :src="form.avatar" :size="100" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="form.name" />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" type="email" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="updateInfo">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>


<script setup>
import { ref, reactive, onMounted } from 'vue';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['updateUser']);  // 定义事件

const updateInfo = async () => {
  try {
    if (!form.id) {
      ElMessage.error('用户信息异常，请重新登录');
      return;
    }
    const res = await request.put(`/user/update`, form);
    if (res.code === '200') {
      ElMessage.success('个人信息更新成功');
      // 更新本地存储的用户信息
      const updatedUser = { ...user.value, ...form };
      localStorage.setItem('normal_user', JSON.stringify(updatedUser));
      user.value = updatedUser;
      emit('updateUser');  // 触发更新事件
    } else {
      ElMessage.error(res.msg || '更新失败');
    }
  } catch (error) {
    console.error('Error updating user info:', error);
    ElMessage.error('更新失败');
  }
};

const user = ref(JSON.parse(localStorage.getItem('normal_user') || '{}'));
const form = reactive({
  id: user.value.id,  // 添加用户ID
  name: user.value.name || '',
  email: user.value.email || '',
  phone: user.value.phone || '',
  avatar: user.value.avatar || ''
});

const handleAvatarSuccess = (response) => {
  if (response.code === '200') {
    form.avatar = response.data;
    ElMessage.success('头像上传成功');
  } else {
    ElMessage.error(response.msg || '头像上传失败');
  }
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!');
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
}

</script>


<style scoped>
.personal-info {
  max-width: 600px;
  margin: 20px auto;
  padding: 0 20px;
}

.avatar-uploader {
  text-align: center;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader-icon:hover {
  border-color: #409EFF;
}
</style>