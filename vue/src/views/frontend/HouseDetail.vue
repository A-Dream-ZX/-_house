<template>
  <div class="house-detail" v-if="house">
    <el-card>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-carousel height="400px">
            <!-- 主图 -->
            <el-carousel-item v-if="house.imageUrl">
              <el-image
                  :src="house.imageUrl"
                  fit="cover"
                  style="width: 100%; height: 100%"
              />
            </el-carousel-item>
            <!-- 其他图片 -->
            <el-carousel-item v-for="(image, index) in houseImages" :key="index">
              <el-image
                  :src="image"
                  fit="cover"
                  style="width: 100%; height: 100%"
              />
            </el-carousel-item>
          </el-carousel>
        </el-col>
        <el-col :span="12">
          <div class="house-info">
            <h1>{{ house.title }}</h1>
            <div class="price">￥{{ house.price }} / 月</div>
            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span>{{ house.location }}</span>
            </div>
            <div class="info-item">
              <el-icon><House /></el-icon>
              <span>房型：{{ formatRoomType(house.roomType) }}</span>
            </div>
            <div class="info-item">
              <el-icon><House /></el-icon>
              <span>面积：{{ house.area }}</span>
            </div>
            <div class="description">
              {{ house.description }}
            </div>
            <div class="actions">
              <el-button
                  type="primary"
                  size="large"
                  @click="handleBook"
                  :disabled="isBooked || !isLoggedIn"
              >
                {{ isBooked ? '已预订' : '立即预订' }}
              </el-button>
              <el-button
                  type="warning"
                  size="large"
                  @click="handleFavorite"
                  :disabled="!isLoggedIn"
              >
                {{ isFavorited ? '取消收藏' : '立即收藏' }}
              </el-button>
              <el-button
                  v-if="!isLoggedIn"
                  type="info"
                  size="large"
                  @click="goToLogin"
              >
                登录后操作
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 房源详细信息 -->
      <div class="detail-section">
        <h2>房源详情</h2>
        <div class="room-details">
          <div class="room-detail-item" v-if="roomDetails.rooms">
            <div class="room-icon">
              <el-icon><House /></el-icon>
            </div>
            <div class="room-info">
              <div class="room-title">卧室</div>
              <div class="room-value">{{ roomDetails.rooms }}</div>
            </div>
          </div>
          
          <div class="room-detail-item" v-if="roomDetails.livingRooms">
            <div class="room-icon">
              <el-icon><House /></el-icon>
            </div>
            <div class="room-info">
              <div class="room-title">客厅</div>
              <div class="room-value">{{ roomDetails.livingRooms }}</div>
            </div>
          </div>
          
          <div class="room-detail-item" v-if="roomDetails.bathrooms">
            <div class="room-icon">
              <el-icon><House /></el-icon>
            </div>
            <div class="room-info">
              <div class="room-title">卫生间</div>
              <div class="room-value">{{ roomDetails.bathrooms }}</div>
            </div>
          </div>
        </div>
        
        <div class="introduction">
          {{ house.introduction }}
        </div>
      </div>
    </el-card>

    <!-- 添加预订对话框 -->
    <el-dialog v-model="bookingDialogVisible" title="预订确认" width="500px">
      <el-form ref="bookingFormRef" :model="bookingForm" :rules="bookingRules" label-width="100px">
        <el-form-item label="入住日期" prop="startDate">
          <el-date-picker
            v-model="bookingForm.startDate"
            type="date"
            placeholder="选择入住日期"
            :disabled-date="disabledStartDate"
            style="width: 100%"
            @change="handleStartDateChange"
          />
        </el-form-item>
        <el-form-item label="退房日期" prop="endDate">
          <el-date-picker
            v-model="bookingForm.endDate"
            type="date"
            placeholder="选择退房日期"
            :disabled-date="disabledEndDate"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            type="textarea"
            v-model="bookingForm.remark"
            placeholder="请输入备注信息（选填）"
          />
        </el-form-item>
        <div class="booking-info">
          <p>月租金：￥{{ house.price }}</p>
          <p>预计租期：{{ rentalPeriod }}</p>
          <p>预计总租金：￥{{ calculateTotalPrice }}</p>
        </div>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="bookingDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitBooking">确认预订</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>


<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Location, House } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import request from '@/utils/request';

const route = useRoute();
const router = useRouter();
const house = ref(null);
const isBooked = ref(false);

const isLoggedIn = computed(() => {
  const user = JSON.parse(localStorage.getItem('normal_user') || '{}');
  return !!user.id;
});

// 处理图片数组
const houseImages = computed(() => {
  if (house.value && house.value.images) {
    return house.value.images.split(',').filter(url => url);
  }
  return [];
});

// 解析房型数据
const roomDetails = computed(() => {
  if (!house.value || !house.value.roomType) {
    return { rooms: 0, livingRooms: 0, bathrooms: 0 };
  }
  
  const roomType = house.value.roomType;
  const result = { rooms: 0, livingRooms: 0, bathrooms: 0 };
  
  // 处理未知室的情况
  if (roomType.includes('未知室')) {
    result.rooms = '未知';
  } else {
    // 匹配数字室
    const roomMatch = roomType.match(/(\d+)室/);
    result.rooms = roomMatch ? parseInt(roomMatch[1]) : 0;
  }
  
  // 匹配厅
  const livingMatch = roomType.match(/(\d+)厅/);
  result.livingRooms = livingMatch ? parseInt(livingMatch[1]) : 0;
  
  // 匹配卫
  const bathMatch = roomType.match(/(\d+)卫/);
  result.bathrooms = bathMatch ? parseInt(bathMatch[1]) : 0;
  
  return result;
});

// 格式化房型显示
const formatRoomType = (roomType) => {
  if (!roomType) return '';
  
  // 将"室厅卫"格式化为更友好的显示
  let formatted = roomType;
  
  // 替换"未知室"为"未知卧室"
  if (formatted.includes('未知室')) {
    formatted = formatted.replace('未知室', '未知卧室');
  } else {
    formatted = formatted.replace('室', '卧室');
  }
  
  formatted = formatted.replace('厅', '客厅');
  formatted = formatted.replace('卫', '卫生间');
  
  return formatted;
};

const loadHouseDetail = async () => {
  try {
    const res = await request.get(`/house/detail/${route.params.id}`);
    if (res.code === '200') {
      house.value = res.data;
      isBooked.value = res.data.booked || false;
    } else {
      ElMessage.error(res.msg || '加载房源详情失败');
    }
  } catch (error) {
    console.error('Error loading house detail:', error);
    ElMessage.error('加载房源详情失败');
  }
};

// 预订相关数据
const bookingDialogVisible = ref(false);
const bookingFormRef = ref();
const bookingForm = ref({
  startDate: new Date(), // 默认为当前日期
  endDate: '',
  remark: ''
});

// 表单验证规则
const bookingRules = {
  startDate: [{ required: true, message: '请选择入住日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择退房日期', trigger: 'change' }]
};

// 禁用今天之前的日期
const disabledStartDate = (time) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return time.getTime() < today.getTime();
};

// 禁用入住日期之前的日期，并确保至少租一个月
const disabledEndDate = (time) => {
  if (!bookingForm.value.startDate) {
    return true;
  }
  
  // 获取入住日期的副本
  const startDate = new Date(bookingForm.value.startDate);
  
  // 计算最短租期（至少一个月）
  const minEndDate = new Date(startDate);
  minEndDate.setMonth(minEndDate.getMonth() + 1);
  
  return time.getTime() <= startDate.getTime() || time.getTime() < minEndDate.getTime();
};

// 当入住日期变更时，如果退房日期早于入住日期，则重置退房日期
const handleStartDateChange = () => {
  if (bookingForm.value.endDate && 
      new Date(bookingForm.value.endDate) <= new Date(bookingForm.value.startDate)) {
    // 设置为入住日期后一个月
    const newEndDate = new Date(bookingForm.value.startDate);
    newEndDate.setMonth(newEndDate.getMonth() + 1);
    bookingForm.value.endDate = newEndDate;
  }
};

// 计算租期（几个月几天）
const rentalPeriod = computed(() => {
  if (!bookingForm.value.startDate || !bookingForm.value.endDate) {
    return '请选择日期';
  }
  
  const startDate = new Date(bookingForm.value.startDate);
  const endDate = new Date(bookingForm.value.endDate);
  const diffTime = Math.abs(endDate - startDate);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  const months = Math.floor(diffDays / 30);
  const days = diffDays % 30;
  
  let result = '';
  if (months > 0) {
    result += `${months}个月`;
  }
  if (days > 0) {
    result += `${days}天`;
  }
  
  return result;
});

// 计算总租金
const calculateTotalPrice = computed(() => {
  if (!bookingForm.value.startDate || !bookingForm.value.endDate || !house.value) {
    return 0;
  }
  
  const startDate = new Date(bookingForm.value.startDate);
  const endDate = new Date(bookingForm.value.endDate);
  const diffTime = Math.abs(endDate - startDate);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  // 按月计算，不足一个月按比例计算
  const monthlyPrice = house.value.price;
  const totalPrice = (diffDays / 30) * monthlyPrice;
  
  return Math.ceil(totalPrice);
});

// 处理预订按钮点击
const handleBook = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录');
    return;
  }
  
  // 设置默认值
  bookingForm.value.startDate = new Date();
  const defaultEndDate = new Date();
  defaultEndDate.setMonth(defaultEndDate.getMonth() + 1);
  bookingForm.value.endDate = defaultEndDate;
  
  bookingDialogVisible.value = true;
};

// 提交预订
const submitBooking = async () => {
  if (!bookingFormRef.value) return;
  
  await bookingFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const user = JSON.parse(localStorage.getItem('normal_user') || '{}');
        const rentalData = {
          userId: user.id,
          houseId: parseInt(route.params.id),
          startDate: bookingForm.value.startDate,
          endDate: bookingForm.value.endDate,
          totalPrice: calculateTotalPrice.value,
          status: 'pending',
          remark: bookingForm.value.remark || '',
        };

        const res = await request.post('/rental/book', rentalData);
        
        if (res.code === '200') {
          ElMessage.success('预订申请已提交，请等待房东确认');
          bookingDialogVisible.value = false;
          isBooked.value = true;
          
          // 更新房源状态
          house.value.status = 'rented';
        } else {
          ElMessage.error(res.msg || '预订失败，请稍后重试');
        }
      } catch (error) {
        console.error('Error submitting booking:', error);
        ElMessage.error('预订失败，请稍后重试');
      }
    }
  });
};

const goToLogin = () => {
  router.push('/login');
};

// 添加收藏状态
const isFavorited = ref(false);

// 检查是否已收藏
const checkFavoriteStatus = async () => {
  if (!isLoggedIn.value) return;
  
  try {
    const user = JSON.parse(localStorage.getItem('normal_user') || '{}');
    const res = await request.get('/favorite/list', {
      params: { userId: user.id }
    });
    if (res.code === '200') {
      isFavorited.value = res.data.some(item => item.houseId === parseInt(route.params.id));
    }
  } catch (error) {
    console.error('Error checking favorite status:', error);
  }
};

// 处理收藏/取消收藏
const handleFavorite = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    const user = JSON.parse(localStorage.getItem('normal_user') || '{}');
    if (isFavorited.value) {
      // 查找收藏记录的ID
      const res = await request.get('/favorite/list', {
        params: { userId: user.id }
      });
      if (res.code === '200') {
        const favorite = res.data.find(item => item.houseId === parseInt(route.params.id));
        if (favorite) {
          // 取消收藏
          const deleteRes = await request.delete(`/favorite/delete/${favorite.id}`);
          if (deleteRes.code === '200') {
            ElMessage.success('取消收藏成功');
            isFavorited.value = false;
          }
        }
      }
    } else {
      // 添加收藏
      const res = await request.post('/favorite/add', {
        userId: user.id,
        houseId: parseInt(route.params.id),
        createdAt: new Date()
      });
      if (res.code === '200') {
        ElMessage.success('收藏成功');
        isFavorited.value = true;
      }
    }
  } catch (error) {
    console.error('Error handling favorite:', error);
    ElMessage.error('操作失败，请稍后重试');
  }
};

// 在页面加载时检查收藏状态
onMounted(() => {
  loadHouseDetail();
  checkFavoriteStatus();
});
</script>



<style scoped>
.house-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.house-info {
  padding: 0 20px;
}

.price {
  font-size: 24px;
  color: #f60;
  margin: 20px 0;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 10px 0;
  color: #666;
}

.description {
  margin: 20px 0;
  line-height: 1.6;
  color: #333;
}

.actions {
  margin-top: 30px;
  display: flex;
  gap: 15px;
}

.detail-section {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.room-details {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  margin: 20px 0;
}

.room-detail-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
  min-width: 120px;
}

.room-icon {
  font-size: 24px;
  color: #409EFF;
}

.room-info {
  display: flex;
  flex-direction: column;
}

.room-title {
  font-size: 14px;
  color: #909399;
}

.room-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.introduction {
  margin-top: 20px;
  line-height: 1.8;
  color: #666;
}

.booking-info {
  margin: 20px 0;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.booking-info p {
  margin: 5px 0;
  color: #666;
}
</style>