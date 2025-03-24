<template>
  <div>
    <!-- 头部区域开始 -->
    <div style="height: 60px; border-bottom: 1px solid #eee; display: flex;align-items: center; ">
      <div style="display: flex; align-items: center; padding-left: 20px">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="../../assets/imgs/logo.png" alt="图标"/>
        <span style="font-size: 15px; font-weight: bold;
            color: black;
            margin-left: 10px;">租房推荐系统</span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; padding-left: 100px;">
        <span style=" margin-right: 5px; cursor: pointer;" @click="router.push('/manager/home')">首页</span>/
        <span style=" margin-left: 5px;">
        {{ router.currentRoute.value.meta.name }}
        </span>
      </div>
      <div style="width: fit-content; padding-right: 20px; display: flex; align-items: center;">
        <el-dropdown>
          <div style="display: flex; align-items: center;">
            <img v-if="data.user?.avatar" style="width: 40px; height: 40px; border-radius: 50%; "
                 :src="data.user?.avatar" alt="">
            <img v-else style="width: 40px; height: 40px; border-radius: 50%; " src="../../assets/imgs/管理员.png
            " alt="">
            <span style="margin-left: 5px;">{{ data.user?.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

      </div>
    </div>
    <!-- 头部区域结束 -->

    <!-- 下方区域开始 -->
    <div style="display: flex">
      <!-- 菜单区域开始 -->
      <div style="width: 240px">
        <el-menu router
                 :default-opened="['1', '2']"
                 :default-active="router.currentRoute.value.path"
                 class="el-menu-vertical-demo"
                 style="min-height: calc(100vh - 60px);"
        >
          <el-menu-item index="/manager/home">
            <el-icon>
              <House/>
            </el-icon>
            <span>首页</span>
          </el-menu-item>

          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <User/>
              </el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>

          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <ChatSquare/>
              </el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/house">房源信息</el-menu-item>
            <el-menu-item index="/manager/favorite">收藏信息</el-menu-item>
            <el-menu-item index="/manager/rental">租赁信息</el-menu-item>
            <el-menu-item index="/manager/notice">系统公告</el-menu-item>

          </el-sub-menu>

        </el-menu>
      </div>
      <!-- 菜单区域结束 -->


      <!-- 数据区域开始 -->
      <div style="flex:1; width: 0; padding: 10px; background-color: rgb(241, 241, 241);">
        <RouterView @updateName="updateName"/>
      </div>
      <!-- 数据区域结束 -->
    </div>

    <!-- 下方区域结束 -->


  </div>
</template>

<script setup>
import router from '@/router/index.js';
import {useRoute} from 'vue-router';
import {reactive} from 'vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('admin_user') || '{}')
})


// 获取路由信息
const route = useRoute();

// 退出登录
const logout = () => {
  try {
    localStorage.removeItem('admin_user'); // 清除本地存储中的用户信息
    router.push('/login'); // 跳转到登录页
  } catch (error) {
    console.error('Error during logout:', error);
  }
}

// 判断是否登录
// 前端登录校验 后端也有校验 所以取消
// if(!data.user?.id){
//   router.push('/login')
// }


const updateName = () => {
  data.user = JSON.parse(localStorage.getItem('admin_user') || '{}')
}


</script>

<style>
.el-dropdown {
  cursor: pointer;
}

.el-tooltip__trigger {
  outline: none;

}

.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}

</style>