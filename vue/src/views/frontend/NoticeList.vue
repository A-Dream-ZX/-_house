<template>
  <div class="notice-container">
    <div class="notice-header">
      <h2>系统公告</h2>
    </div>
    <div class="notice-content">
      <el-timeline>
        <el-timeline-item
            v-for="item in data.noticeData"
            :key="item.id"
            :timestamp="item.time"
            color="#0bbd87"
            placement="top">
          <el-card class="notice-card" shadow="hover">
            <h3 class="notice-title">{{ item.title }}</h3>
            <p class="notice-text">{{ item.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || {}),
  noticeData: [],


})

const loadNotice = () => {
  request.get('notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
      if(data.noticeData.length >3){
        data.noticeData = data.noticeData.slice(0,3)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()


</script>

<style scoped>
.notice-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.notice-header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.notice-header h2 {
  color: #303133;
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.notice-content {
  padding: 0 20px;
}

.notice-card {
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.notice-card:hover {
  transform: translateY(-2px);
}

.notice-title {
  color: #303133;
  font-size: 18px;
  margin: 0 0 10px 0;
}

.notice-text {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .notice-container {
    margin: 10px;
    padding: 15px;
  }

  .notice-content {
    padding: 0 10px;
  }

  .notice-header h2 {
    font-size: 20px;
  }

  .notice-title {
    font-size: 16px;
  }
}
</style>