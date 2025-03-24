<template>
  <div class="rental-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的租约</span>
        </div>
      </template>

      <div v-loading="loading">
        <el-empty v-if="rentals.length === 0" description="暂无租赁记录" />
        <el-table v-else :data="rentals" style="width: 100%">
          <el-table-column prop="house.title" label="房源标题" />
          <el-table-column prop="startDate" label="开始日期" />
          <el-table-column prop="endDate" label="结束日期" />
          <el-table-column prop="monthlyRent" label="月租金">
            <template #default="scope">
              ￥{{ scope.row.monthlyRent }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
                {{ scope.row.status === 'active' ? '生效中' : '已结束' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button
                  type="primary"
                  link
                  @click="viewHouseDetail(scope.row.houseId)"
              >
                查看详情
              </el-button>
              <el-button
                  v-if="scope.row.status === 'active'"
                  type="success"
                  link
                  @click="handleRenewal(scope.row.id)"
              >
                申请续租
              </el-button>
              <el-button
                  v-if="scope.row.status === 'active'"
                  type="danger"
                  link
                  @click="handleTerminate(scope.row.id)"
              >
                申请退租
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import request from '@/utils/request.js';
import { ElMessage } from 'element-plus';


    const router = useRouter();
    const rentals = ref([]);
    const loading = ref(false);
    const user = JSON.parse(localStorage.getItem('normal_user') || '{}');

    const loadRentals = async () => {
      loading.value = true;
      try {
        const res = await request.get('/rental/list', {
          params: { userId: user.id }
        });
        if (res.code === '200') {
          rentals.value = res.data;
        } else {
          ElMessage.error(res.msg || '加载租赁记录失败');
        }
      } catch (error) {
        console.error('Error loading rentals:', error);
        ElMessage.error('加载租赁记录失败');
      } finally {
        loading.value = false;
      }
    };

    const handleRenewal = async (id) => {
      try {
        const res = await request.put(`/rental/renewal/${id}`);
        if (res.code === '200') {
          ElMessage.success('续租申请提交成功');
          loadRentals();
        } else {
          ElMessage.error(res.msg || '续租申请失败');
        }
      } catch (error) {
        console.error('Error submitting renewal:', error);
        ElMessage.error('续租申请失败');
      }
    }

    const handleTerminate = async (id) => {
      try {
        const res = await request.put(`/rental/terminate/${id}`);
        if (res.code === '200') {
          ElMessage.success('退租申请提交成功');
          loadRentals();
        } else {
          ElMessage.error(res.msg || '退租申请失败');
        }
      } catch (error) {
        console.error('Error submitting termination:', error);
        ElMessage.error('退租申请失败');
      }
    }

    const viewHouseDetail = (id) => {
      router.push(`/house-detail/${id}`);
    }

    onMounted(() => {
      if (!user.id) {
        router.push('/login');
        return;
      }
      loadRentals();
    })


</script>


<style scoped>
.rental-list {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>