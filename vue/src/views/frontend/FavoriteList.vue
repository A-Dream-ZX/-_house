<template>
  <div class="favorite-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的收藏</span>
        </div>
      </template>

      <div v-loading="loading">
        <el-empty v-if="favorites.length === 0" description="暂无收藏的房源" />
        <el-row v-else :gutter="20">
          <el-col v-for="item in favorites" :key="item.id" :span="8">
            <el-card :body-style="{ padding: '0px' }" class="house-card">
              <el-image
                  :src="item.house.cover"
                  fit="cover"
                  style="width: 100%; height: 200px"
              />
              <div style="padding: 14px;">
                <h4>{{ item.house.title }}</h4>
                <p>{{ item.house.location }}</p>
                <div class="price">￥{{ item.house.price }} / 月</div>
                <div class="house-info">
                  <span>{{ item.house.area }}㎡</span>
                  <span>{{ item.house.roomType }}</span>
                </div>
                <div class="operations">
                  <el-button type="primary" link @click="viewHouseDetail(item.houseId)">
                    查看详情
                  </el-button>
                  <el-button type="danger" link @click="cancelFavorite(item.id)">
                    取消收藏
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>




<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';

export default {
  name: 'FavoriteList',
  setup() {
    const router = useRouter();
    const favorites = ref([]);
    const loading = ref(false);
    const user = JSON.parse(localStorage.getItem('normal_user') || '{}');

    const loadFavorites = async () => {
      loading.value = true;
      try {
        const res = await request.get('/favorite/list', {
          params: { userId: user.id }
        });
        if (res.code === '200') {
          favorites.value = res.data;
        } else {
          ElMessage.error(res.msg || '加载收藏列表失败');
        }
      } catch (error) {
        console.error('Error loading favorites:', error);
        ElMessage.error('加载收藏列表失败');
      } finally {
        loading.value = false;
      }
    };

    const cancelFavorite = async (id) => {
      try {
        const res = await request.delete(`/favorite/delete/${id}`);
        if (res.code === '200') {
          ElMessage.success('取消收藏成功');
          loadFavorites();
        } else {
          ElMessage.error(res.msg || '取消收藏失败');
        }
      } catch (error) {
        console.error('Error canceling favorite:', error);
        ElMessage.error('取消收藏失败');
      }
    };

    const viewHouseDetail = (id) => {
      router.push(`/house-detail/${id}`);
    };

    onMounted(() => {
      if (!user.id) {
        router.push('/login');
        return;
      }
      loadFavorites();
    });

    return {
      favorites,
      loading,
      cancelFavorite,
      viewHouseDetail
    };
  }
};
</script>


<style scoped>
.favorite-list {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.house-card {
  margin-bottom: 20px;
  transition: all 0.3s;
}

.house-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.house-card h4 {
  margin: 0;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
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

.house-info {
  display: flex;
  gap: 10px;
  color: #666;
  font-size: 14px;
  margin-bottom: 10px;
}

.operations {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>