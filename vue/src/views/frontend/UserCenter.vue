<template>
  <div class="user-center">
    <el-card class="user-info">
      <template #header>
        <div class="card-header">
          <span>
            个人信息
          </span>
          <el-button type="primary" link @click="router.push('/front/personal-info')">
            编辑资料
          </el-button>
        </div>
      </template>
      <div class="info-content">
        <div class="user-profile">
          <el-avatar
              :size="80"
              :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
          />
          <div class="user-details">
            <h2>{{ user.name }}</h2>
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ favoriteList.length }}</div>
                <div class="stat-label">收藏</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ rentalList.length }}</div>
                <div class="stat-label">租房</div>
              </div>
            </div>
          </div>
        </div>
        <div class="contact-info">
          <div class="info-item">
            <el-icon>
              <Message/>
            </el-icon>
            <span>{{ user.email || '未设置邮箱' }}</span>
          </div>
          <div class="info-item">
            <el-icon>
              <Phone/>
            </el-icon>
            <span>{{ user.phone || '未设置手机号' }}</span>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="main-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="租房记录" name="rental">
          <el-table :data="rentalList" style="width: 100%">
            <el-table-column prop="houseTitle" label="房源标题">
              <template #default="scope">
                <span>{{ scope.row.houseInfo ? scope.row.houseInfo.title : '加载中...' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="startDate" label="开始日期"/>
            <el-table-column prop="endDate" label="结束日期"/>
            <el-table-column prop="totalPrice" label="总价">
              <template #default="scope">
                <span>￥{{ scope.row.totalPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button type="primary" link @click="viewHouseDetail(scope.row.houseId)">查看详情</el-button>
                <el-button
                    v-if="scope.row.status === 'pending'"
                    type="danger"
                    link
                    @click="cancelRental(scope.row.id)">取消预订
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="rentalList.length === 0" class="empty-data">
            <el-empty description="暂无租房记录"/>
          </div>
        </el-tab-pane>

        <el-tab-pane label="收藏列表" name="favorite">
          <el-row :gutter="20">
            <el-col v-for="item in favoriteList" :key="item.id" :span="8">
              <el-card :body-style="{ padding: '0px' }" class="house-card">
                <el-image
                    :src="item.house.imageUrl"
                    fit="cover"
                    style="width: 100%; height: 200px"
                />
                <div style="padding: 14px;">
                  <h4>{{ item.house.title }}</h4>
                  <p>{{ item.house.location }}</p>
                  <div class="price">￥{{ item.house.price }} / 月</div>
                  <div class="operations">
                    <el-button link type="primary" @click="viewHouseDetail(item.houseId)">查看详情</el-button>
                    <el-button link type="danger" @click="cancelFavorite(item.id)">取消收藏</el-button>

                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>


<script setup>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage, ElMessageBox} from 'element-plus';
import {Message, Phone} from '@element-plus/icons-vue';
import request from '@/utils/request.js';


const router = useRouter();
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'));
const activeTab = ref('rental');
const rentalList = ref([]);
const favoriteList = ref([]);

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    'pending': 'warning',
    'ongoing': 'success',
    'completed': 'info',
    'canceled': 'danger'
  }
  return types[status] || 'info'
}

// 获取状态显示文本
const getStatusText = (status) => {
  const texts = {
    'pending': '待确认',
    'ongoing': '进行中',
    'completed': '已完成',
    'canceled': '已取消'
  }
  return texts[status] || status
}

// 加载租房记录
const loadRentalList = async () => {
  try {
    const res = await request.get('/rental/list', {
      params: {userId: user.value.id}
    });
    if (res.code === '200') {
      // 获取租房记录
      rentalList.value = res.data;

      // 获取每个租房记录对应的房源信息
      for (const rental of rentalList.value) {
        try {
          const houseRes = await request.get(`/house/detail/${rental.houseId}`);
          if (houseRes.code === '200') {
            rental.houseInfo = houseRes.data;
          }
        } catch (error) {
          console.error(`Error loading house info for rental ${rental.id}:`, error);
        }
      }
    } else {
      ElMessage.error(res.msg || '加载租房记录失败');
    }
  } catch (error) {
    console.error('Error loading rental list:', error);
    ElMessage.error('加载租房记录失败');
  }
};

// 加载收藏列表
const loadFavoriteList = async () => {
  try {
    const res = await request.get('/favorite/list', {
      params: {userId: user.value.id}
    });
    if (res.code === '200') {
      favoriteList.value = res.data;
    } else {
      ElMessage.error(res.msg || '加载收藏列表失败');
    }
  } catch (error) {
    console.error('Error loading favorite list:', error);
    ElMessage.error('加载收藏列表失败');
  }
};

// 取消收藏
const cancelFavorite = async (id) => {
  try {
    const res = await request.delete(`/favorite/delete/${id}`);
    if (res.code === '200') {
      ElMessage.success('取消收藏成功');
      loadFavoriteList();
    } else {
      ElMessage.error(res.msg || '取消收藏失败');
    }
  } catch (error) {
    console.error('Error canceling favorite:', error);
    ElMessage.error('取消收藏失败');
  }
};

// 取消预订
const cancelRental = async (id) => {
  try {
    await ElMessageBox.confirm('确定要取消此预订吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    const res = await request.put(`/rental/updateStatus/${id}`, null, {
      params: {status: 'canceled'}
    });

    if (res.code === '200') {
      ElMessage.success('已取消预订');
      loadRentalList(); // 重新加载列表
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error canceling rental:', error);
      ElMessage.error('操作失败');
    }
  }
};

// 查看房源详情
const viewHouseDetail = (id) => {
  router.push(`/front/house-detail/${id}`);
};

onMounted(() => {
  if (!user.value.id) {
    router.push('/login');
    return;
  }
  loadRentalList();
  loadFavoriteList();
})


</script>


<style scoped>
.user-center {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  margin-bottom: 20px;
}

.info-content {
  padding: 20px;
}

.user-profile {
  display: flex;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 24px;
}

.user-details {
  flex: 1;
}

.user-details h2 {
  margin: 0 0 16px 0;
  font-size: 24px;
}

.user-stats {
  display: flex;
  gap: 32px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 4px;
}

.contact-info {
  border-top: 1px solid #eee;
  padding-top: 20px;
  margin-top: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #666;
}

.info-item .el-icon {
  font-size: 16px;
  color: #909399;
}

.house-card {
  margin-bottom: 20px;
}

.house-card h4 {
  margin: 0;
  font-size: 16px;
}

.house-card p {
  color: #666;
  font-size: 14px;
  margin: 5px 0;
}

.price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
  margin: 10px 0;
}

.operations {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.empty-data {
  padding: 30px 0;
  text-align: center;
}

.form-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 24px;

  .el-form-item {
    margin-bottom: 24px;
  }

  .el-input {
    --el-input-height: 40px;

    .el-input__wrapper {
      border-radius: 8px;
    }
  }

  .el-button {
    width: 100%;
    height: 44px;
    font-size: 16px;
    border-radius: 8px;
  }
}

.list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;

  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: #2c3e50;
    }
  }

  .el-table {
    border-radius: 12px;
    overflow: hidden;

    th {
      background-color: #f5f7fa;
      font-weight: 600;
    }
  }

  .empty-placeholder {
    padding: 40px 0;
    text-align: center;
    color: #909399;
  }
}
</style>