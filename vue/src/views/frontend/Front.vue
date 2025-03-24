<template>
  <div class="frontend-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo-container">
          <img class="logo-img" src="../../assets/imgs/logo.png" alt="租房系统图标"/>
          <span class="logo-text">搜索一个家</span>
        </div>
        
        <div class="nav-menu">
          <el-menu mode="horizontal" router :default-active="$route.path" class="main-menu">
            <el-menu-item index="/front/home">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/front/notice-list">
              <el-icon><Bell /></el-icon>
              <span>公告</span>
            </el-menu-item>
            <el-menu-item index="/front/user-center">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-menu-item>
          </el-menu>
        </div>
        
        <div class="user-dropdown">
          <el-dropdown trigger="click">
            <div class="user-info">
              <el-avatar 
                :size="40" 
                :src="data.user?.avatar || '../../assets/imgs/管理员.png'" 
              />
              <span class="username">{{ data.user?.name || '游客' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/front/personal-info')">
                  <el-icon><UserFilled /></el-icon>
                  <span>个人信息</span>
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/front/change-password')">
                  <el-icon><Lock /></el-icon>
                  <span>修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="logout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <RouterView @updateUser="updateUser"/>
    </div>

    <!-- 页脚 -->
    <el-footer class="footer">
      <div class="footer-content">
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">联系方式</a>
          <a href="#">帮助中心</a>
          <a href="#">隐私政策</a>
        </div>
        <p class="copyright">© 2024 租房推荐系统 All Rights Reserved</p>
      </div>
    </el-footer>
  </div>
</template>


<script setup>
import router from '@/router/index.js';
import { useRoute } from 'vue-router';
import { reactive, onMounted } from 'vue';
import { 
  HomeFilled, 
  Bell, 
  User, 
  UserFilled, 
  Lock, 
  SwitchButton, 
  ArrowDown 
} from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const data = reactive({
  user: JSON.parse(localStorage.getItem('normal_user') || '{}')
});

// 获取路由信息
const route = useRoute();

// 退出登录
const logout = () => {
  try {
    localStorage.removeItem('normal_user'); // 清除本地存储中的用户信息
    ElMessage.success('已成功退出登录');
    router.push('/login'); // 跳转到登录页
  } catch (error) {
    console.error('Error during logout:', error);
    ElMessage.error('退出登录失败，请重试');
  }
};

// 添加更新用户信息的方法
const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('normal_user') || '{}');
};

// 页面加载时检查用户状态
onMounted(() => {
  updateUser();
});
</script>

<style scoped>
.frontend-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 1000;
  padding: 0;
  height: 64px;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.logo-container {
  display: flex;
  align-items: center;
  min-width: 200px;
}

.logo-img {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  object-fit: cover;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-left: 12px;
}

.nav-menu {
  flex: 1;
}

.main-menu {
  border-bottom: none;
  justify-content: center;
}

.main-menu .el-menu-item {
  font-size: 16px;
  height: 64px;
  line-height: 64px;
}

.main-menu .el-menu-item.is-active {
  font-weight: 600;
}

.user-dropdown {
  margin-left: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  margin: 0 8px;
  font-size: 15px;
  color: #606266;
}

.main-content {
  flex: 1;
  margin-top: 64px;
  padding-bottom: 30px;
}

.footer {
  background-color: #2c3e50;
  color: #fff;
  padding: 30px 0;
  margin-top: auto;
  height: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.footer-links {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 20px;
}

.footer-links a {
  color: #dcdfe6;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #fff;
}

.copyright {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    padding: 0 10px;
  }
  
  .logo-text {
    font-size: 16px;
  }
  
  .main-menu .el-menu-item {
    padding: 0 10px;
    font-size: 14px;
  }
  
  .username {
    display: none;
  }
  
  .footer-links {
    flex-direction: column;
    gap: 10px;
  }
}
</style>