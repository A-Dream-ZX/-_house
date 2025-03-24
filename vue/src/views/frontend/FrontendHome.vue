<template>
  <div class="home">
    <!-- 顶部横幅 -->
    <div class="banner">
      <div class="banner-content">
        <h1 class="banner-title">找到您理想的住所</h1>
        <p class="banner-subtitle">我们提供多样化的房源选择，满足您的各种需求</p>
        
        <!-- 搜索框 -->
        <div class="banner-search">
          <el-input
              v-model="data.search"
              placeholder="输入地址、小区名称搜索"
              class="search-input"
              :prefix-icon="Search"
              @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
        </div>
      </div>
    </div>
    
    <!-- 筛选区域 -->
    <div class="filter-section">
      <div class="filter-container">
        <!-- 房型筛选 -->
        <div class="filter-group">
          <div class="filter-label">房型筛选</div>
          <div class="room-filter-options">
            <!-- 卧室数量 -->
            <el-select
                v-model="data.filters.rooms"
                placeholder="卧室"
                clearable
                @change="handleFilterChange">
              <el-option label="不限" value=""/>
              <el-option v-for="i in 5" :key="`room-${i}`" :label="`${i}室`" :value="`${i}`" />
              <el-option label="5室以上" value="5+" />
            </el-select>
            
            <!-- 客厅数量 -->
            <el-select
                v-model="data.filters.livingRooms"
                placeholder="客厅"
                clearable
                @change="handleFilterChange">
              <el-option label="不限" value=""/>
              <el-option v-for="i in 3" :key="`living-${i}`" :label="`${i}厅`" :value="`${i}`" />
            </el-select>
            
            <!-- 卫生间数量 -->
            <el-select
                v-model="data.filters.bathrooms"
                placeholder="卫生间"
                clearable
                @change="handleFilterChange">
              <el-option label="不限" value=""/>
              <el-option v-for="i in 3" :key="`bath-${i}`" :label="`${i}卫`" :value="`${i}`" />
            </el-select>
          </div>
        </div>

        <!-- 价格区间 -->
        <div class="filter-group">
          <div class="filter-label">价格区间 (元/月)</div>
          <el-slider
              v-model="data.priceRange"
              range
              :min="0"
              :max="10000"
              :step="500"
              :marks="priceMarks"
              class="price-slider"
              @change="handlePriceChange"
          />
        </div>
        
        <!-- 重置按钮 -->
        <div class="filter-actions">
          <el-button @click="resetFilters" icon="RefreshRight">重置筛选</el-button>
        </div>
      </div>

      <!-- 当前筛选条件 -->
      <div class="active-filters" v-if="hasActiveFilters">
        <div class="filter-tags">
          <span class="filter-tag-label">当前筛选:</span>
          <el-tag 
            v-if="data.filters.rooms" 
            closable 
            @close="data.filters.rooms = ''; handleFilterChange()">
            {{ data.filters.rooms === '5+' ? '5室以上' : `${data.filters.rooms}室` }}
          </el-tag>
          <el-tag 
            v-if="data.filters.livingRooms" 
            closable 
            @close="data.filters.livingRooms = ''; handleFilterChange()">
            {{ `${data.filters.livingRooms}厅` }}
          </el-tag>
          <el-tag 
            v-if="data.filters.bathrooms" 
            closable 
            @close="data.filters.bathrooms = ''; handleFilterChange()">
            {{ `${data.filters.bathrooms}卫` }}
          </el-tag>
          <el-tag 
            v-if="isPriceFiltered" 
            closable 
            @close="resetPriceFilter()">
            价格: ￥{{ data.priceRange[0] }} - ￥{{ data.priceRange[1] === 10000 ? '10000+' : data.priceRange[1] }}
          </el-tag>
          <el-tag 
            v-if="data.search" 
            closable 
            @close="data.search = ''; handleSearch()">
            位置: {{ data.search }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 房源列表区域 -->
    <div class="house-section">
      <h2 class="section-title">
        <span v-if="data.houses.length > 0">为您找到 {{ data.houses.length }} 套房源</span>
        <span v-else>房源列表</span>
      </h2>
      
      <!-- 加载状态 -->
      <el-skeleton :loading="loading" animated :count="8" class="house-skeleton">
        <!-- 房源列表 -->
        <template #default>
          <div v-if="data.houses.length > 0" class="house-list">
            <HouseItem
                v-for="house in data.houses"
                :key="house.id"
                :house="house"
                @click="viewDetail(house.id)"
            />
          </div>
          <el-empty v-else description="暂无符合条件的房源" />
        </template>
      </el-skeleton>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, computed} from 'vue';
import {useRouter} from 'vue-router';
import {Search, RefreshRight} from '@element-plus/icons-vue';
import {ElMessage} from 'element-plus';
import request from '@/utils/request.js';
import HouseItem from '@/views/frontend/HouseItem.vue';  // 添加这行来引入 HouseItem 组件

const router = useRouter();
const loading = ref(false);

// 统一数据管理
const data = reactive({
  houses: [],
  search: '',
  priceRange: [0, 10000],
  filters: {
    rooms: '',        // 室
    livingRooms: '',  // 厅
    bathrooms: '',    // 卫
    priceRange: ''
  }
});

// 价格标记点
const priceMarks = {
  0: '0',
  2000: '2000',
  4000: '4000',
  6000: '6000',
  8000: '8000',
  10000: '10000+'
};

// 是否有激活的筛选条件
const hasActiveFilters = computed(() => {
  return data.filters.rooms || 
         data.filters.livingRooms || 
         data.filters.bathrooms || 
         isPriceFiltered.value || 
         data.search;
});

// 价格是否被筛选
const isPriceFiltered = computed(() => {
  return data.priceRange[0] > 0 || data.priceRange[1] < 10000;
});

// 构建房型筛选条件
const buildRoomTypeFilter = () => {
  // 如果是5+室的情况，特殊处理
  if (data.filters.rooms === '5+') {
    return null; // 返回null表示需要特殊处理
  }
  
  // 如果只选择了室，使用模糊匹配
  if (data.filters.rooms && !data.filters.livingRooms && !data.filters.bathrooms) {
    return `${data.filters.rooms}室`; // 只匹配"n室"部分，不管后面是什么
  }
  
  // 如果只选择了厅，使用模糊匹配
  if (!data.filters.rooms && data.filters.livingRooms && !data.filters.bathrooms) {
    return `${data.filters.livingRooms}厅`; // 只匹配"n厅"部分
  }
  
  // 如果只选择了卫，使用模糊匹配
  if (!data.filters.rooms && !data.filters.livingRooms && data.filters.bathrooms) {
    return `${data.filters.bathrooms}卫`; // 只匹配"n卫"部分
  }
  
  // 如果选择了多个条件，组合匹配
  const parts = [];
  if (data.filters.rooms) parts.push(`${data.filters.rooms}室`);
  if (data.filters.livingRooms) parts.push(`${data.filters.livingRooms}厅`);
  if (data.filters.bathrooms) parts.push(`${data.filters.bathrooms}卫`);
  
  return parts.length > 0 ? parts.join('') : '';
};

// 加载房源列表
const loadHouses = () => {
  loading.value = true;
  
  // 构建房型筛选条件
  const roomTypeFilter = buildRoomTypeFilter();
  
  // 构建请求参数
  const params = {
    location: data.search,
    minPrice: data.priceRange[0],
    maxPrice: data.priceRange[1]
  };
  
  // 处理房型筛选
  if (roomTypeFilter !== null) {
    // 正常情况：使用后端筛选
    if (data.filters.rooms || data.filters.livingRooms || data.filters.bathrooms) {
      // 这里改为前端筛选，因为后端可能需要精确匹配
      // params.roomType = roomTypeFilter; // 注释掉后端筛选
    }
  } else if (data.filters.rooms === '5+') {
    // 5室以上的特殊处理
    params.minRooms = 5;
  }
  
  request.get('/house/available', { params })
    .then(res => {
      if (res.code === '200') {
        let houses = res.data.map(house => ({
          ...house,
          cover: house.imageUrl || house.cover
        }));
        
        // 前端筛选房型
        if (roomTypeFilter !== null && (data.filters.rooms || data.filters.livingRooms || data.filters.bathrooms)) {
          houses = houses.filter(house => {
            if (!house.roomType) return false;
            
            // 如果只选择了室
            if (data.filters.rooms && !data.filters.livingRooms && !data.filters.bathrooms) {
              const roomMatch = house.roomType.match(/(\d+)室/);
              return roomMatch && roomMatch[1] === data.filters.rooms;
            }
            
            // 如果只选择了厅
            if (!data.filters.rooms && data.filters.livingRooms && !data.filters.bathrooms) {
              const livingMatch = house.roomType.match(/(\d+)厅/);
              return livingMatch && livingMatch[1] === data.filters.livingRooms;
            }
            
            // 如果只选择了卫
            if (!data.filters.rooms && !data.filters.livingRooms && data.filters.bathrooms) {
              const bathMatch = house.roomType.match(/(\d+)卫/);
              return bathMatch && bathMatch[1] === data.filters.bathrooms;
            }
            
            // 如果选择了多个条件
            let matches = true;
            
            if (data.filters.rooms) {
              const roomMatch = house.roomType.match(/(\d+)室/);
              matches = matches && roomMatch && roomMatch[1] === data.filters.rooms;
            }
            
            if (data.filters.livingRooms) {
              const livingMatch = house.roomType.match(/(\d+)厅/);
              matches = matches && livingMatch && livingMatch[1] === data.filters.livingRooms;
            }
            
            if (data.filters.bathrooms) {
              const bathMatch = house.roomType.match(/(\d+)卫/);
              matches = matches && bathMatch && bathMatch[1] === data.filters.bathrooms;
            }
            
            return matches;
          });
        }
        
        // 如果是5室以上的筛选，在前端进行过滤
        if (data.filters.rooms === '5+') {
          houses = houses.filter(house => {
            const roomMatch = house.roomType.match(/(\d+)室/);
            const rooms = roomMatch ? parseInt(roomMatch[1]) : 0;
            return rooms >= 5;
          });
        }
        
        data.houses = houses;
      } else {
        ElMessage.error(res.msg || '加载房源失败');
      }
    })
    .catch(() => {
      ElMessage.error('加载房源失败');
    })
    .finally(() => {
      loading.value = false;
    });
};

// 处理价格范围变化
const handlePriceChange = () => {
  loadHouses();
};

// 重置价格筛选
const resetPriceFilter = () => {
  data.priceRange = [0, 10000];
  loadHouses();
};

// 处理房型筛选变化
const handleFilterChange = () => {
  loadHouses();
};

// 处理搜索
const handleSearch = () => {
  loadHouses();
};

// 查看房源详情
const viewDetail = (id) => {
  router.push(`/front/house-detail/${id}`);
};

// 重置筛选条件
const resetFilters = () => {
  data.search = '';
  data.filters.rooms = '';
  data.filters.livingRooms = '';
  data.filters.bathrooms = '';
  data.priceRange = [0, 10000];
  loadHouses();
};

// 页面加载时获取数据
onMounted(() => {
  loadHouses();
});
</script>

<style scoped>
.home {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
}

/* 顶部横幅 */
.banner {
  background: linear-gradient(135deg, #3498db, #8e44ad);
  color: white;
  padding: 60px 0;
  margin-bottom: 30px;
  border-radius: 0 0 10px 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.banner-content {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
  padding: 0 20px;
}

.banner-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 15px;
}

.banner-subtitle {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.banner-search {
  display: flex;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
}

.banner-search .el-button {
  margin-left: 10px;
  padding: 0 20px;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 30px;
  padding: 0 20px;
}

.filter-container {
  background: white;
  border-radius: 10px;
  padding: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-label {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.room-filter-options {
  display: flex;
  gap: 15px;
}

.room-filter-options .el-select {
  width: 120px;
}

.price-slider {
  width: 100%;
  padding: 10px 0;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

.active-filters {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.filter-tag-label {
  color: #606266;
  font-size: 14px;
  font-weight: 600;
}

/* 房源列表区域 */
.house-section {
  padding: 0 20px;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.house-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 25px;
  margin-bottom: 30px;
}

.house-skeleton {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner {
    padding: 40px 0;
  }
  
  .banner-title {
    font-size: 28px;
  }
  
  .banner-subtitle {
    font-size: 16px;
  }
  
  .banner-search {
    flex-direction: column;
  }
  
  .banner-search .el-button {
    margin-left: 0;
    margin-top: 10px;
  }
  
  .filter-container {
    padding: 15px;
  }
  
  .room-filter-options {
    flex-direction: column;
    gap: 10px;
  }
  
  .room-filter-options .el-select {
    width: 100%;
  }
  
  .house-list {
    grid-template-columns: 1fr;
  }
}
</style>