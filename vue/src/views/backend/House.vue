<template>
  <div>
    <!-- 查询区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.title"
                placeholder="请输入房源标题查询"
                :prefix-icon="Search"/>
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.location"
                placeholder="请输入位置查询"
                :prefix-icon="Search"/>

      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="primary" @click="reset">重 置</el-button>
    </div>

    <!-- 操作按钮区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="primary" @click="deleteBatch">批量删除</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          :header-cell-style="{ color: '#333', backgroundColor: '#fff' }"
      >
        <el-table-column type="selection" width="55"/>
        
        <el-table-column label="封面图">
          <template #default="scope">
            <el-image
                v-if="scope.row.imageUrl"
                :src="scope.row.imageUrl"
                :preview-src-list="[scope.row.imageUrl]"
                :preview-teleported="true"
                style="width: 80px; height: 60px; object-fit: cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="price" label="价格"/>
        <el-table-column prop="area" label="面积"/>
        <el-table-column prop="roomType" label="房型"/>
        <el-table-column prop="location" label="位置"/>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'available' ? 'success' : 'info'">
              {{ scope.row.status === 'available' ? '可租' : '已租' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="Del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <!-- 对话框区域 -->
    <el-dialog v-model="data.formVisible" title="房源信息" width="600px" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px"
               style="padding: 20px 30px 20px 0px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" placeholder="请输入房源标题"/>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="data.form.price" :min="0" placeholder="请输入价格"/>
        </el-form-item>
        <el-form-item label="面积" prop="area">
          <el-input v-model="data.form.area" placeholder="请输入面积"/>
        </el-form-item>
        <el-form-item label="房型" prop="roomType">
          <div class="room-type-selector">
            <div class="room-type-row">
              <span class="room-label">室：</span>
              <el-select v-model="roomConfig.rooms" placeholder="选择" style="width: 100px">
                <el-option v-for="i in 10" :key="`room-${i}`" :label="i" :value="i" />
                <el-option label="未知" value="未知" />
              </el-select>
            </div>
            
            <div class="room-type-row">
              <span class="room-label">厅：</span>
              <el-select v-model="roomConfig.livingRooms" placeholder="选择" style="width: 100px">
                <el-option v-for="i in 5" :key="`living-${i}`" :label="i" :value="i" />
                <el-option label="未知" value="未知" />
              </el-select>
            </div>
            
            <div class="room-type-row">
              <span class="room-label">卫：</span>
              <el-select v-model="roomConfig.bathrooms" placeholder="选择" style="width: 100px">
                <el-option v-for="i in 5" :key="`bath-${i}`" :label="i" :value="i" />
                <el-option label="未知" value="未知" />
              </el-select>
            </div>
          </div>
          <div class="room-type-preview">
            当前选择: {{ formattedRoomType }}
          </div>
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="data.form.location" placeholder="请输入位置"/>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="data.form.description" placeholder="请输入描述"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="data.form.status" placeholder="请选择状态">
            <el-option label="可租" value="available"/>
            <el-option label="已租" value="rented"/>
          </el-select>
        </el-form-item>
        <el-form-item label="封面图" prop="imageUrl">
          <el-upload
              action="http://localhost:8080/files/upload"
              :data="{ type: 'house' }"
              list-type="picture-card"
              :headers="{token: data.user.token}"
              :on-success="handleMainImageSuccess"
              :limit="1"
          >
            <el-button type="primary">上传主图</el-button>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="房源图片集" prop="images">
          <el-upload
              action="http://localhost:8080/files/upload"
              :data="{ type: 'house' }"
              list-type="picture-card"
              :headers="{token: data.user.token}"
              :on-success="handleImagesSuccess"
              :on-remove="handleImageRemove"
              multiple
              :limit="5"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref, computed, watch} from 'vue'
import {Search} from '@element-plus/icons-vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from 'element-plus'

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || '{}'),
  title: null,
  location: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    title: [{required: true, message: '请输入标题', trigger: 'blur'}],
    price: [{required: true, message: '请输入价格', trigger: 'blur'}],
    area: [{required: true, message: '请输入面积', trigger: 'blur'}],
    roomType: [{required: true, message: '请选择房型', trigger: 'change'}],
    location: [{required: true, message: '请输入位置', trigger: 'blur'}],
    status: [{required: true, message: '请选择状态', trigger: 'change'}]
  },
  rows: []
})

const formRef = ref()

// 房型配置
const roomConfig = reactive({
  rooms: 1,
  livingRooms: 1,
  bathrooms: 1
});

// 格式化房型显示
const formattedRoomType = computed(() => {
  if (roomConfig.rooms === '未知' && roomConfig.livingRooms === '未知' && roomConfig.bathrooms === '未知') {
    return '未知室0厅0卫';
  }
  
  const rooms = roomConfig.rooms === '未知' ? '未知' : `${roomConfig.rooms}`;
  const livingRooms = roomConfig.livingRooms === '未知' ? '0' : `${roomConfig.livingRooms}`;
  const bathrooms = roomConfig.bathrooms === '未知' ? '0' : `${roomConfig.bathrooms}`;
  
  return `${rooms}室${livingRooms}厅${bathrooms}卫`;
});

// 监听房型变化，自动更新表单数据
watch(formattedRoomType, (newValue) => {
  data.form.roomType = newValue;
});

// 解析已有房型数据
const parseRoomType = (roomTypeStr) => {
  if (!roomTypeStr) {
    roomConfig.rooms = 1;
    roomConfig.livingRooms = 1;
    roomConfig.bathrooms = 1;
    return;
  }
  
  // 处理未知室的情况
  if (roomTypeStr.includes('未知室')) {
    roomConfig.rooms = '未知';
  } else {
    // 匹配数字室
    const roomMatch = roomTypeStr.match(/(\d+)室/);
    roomConfig.rooms = roomMatch ? parseInt(roomMatch[1]) : 1;
  }
  
  // 匹配厅
  const livingMatch = roomTypeStr.match(/(\d+)厅/);
  roomConfig.livingRooms = livingMatch ? parseInt(livingMatch[1]) : 1;
  
  // 匹配卫
  const bathMatch = roomTypeStr.match(/(\d+)卫/);
  roomConfig.bathrooms = bathMatch ? parseInt(bathMatch[1]) : 1;
};

// 分页查询
const load = () => {
  request.get('/house/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
      location: data.location
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

// 重置
const reset = () => {
  data.title = null
  data.location = null
  load()
}

// 新增
const handleAdd = () => {
  data.formVisible = true
  data.form = {
    status: 'available',
    adminId: data.user.id,
    createdAt: new Date().toISOString()
  }
  
  // 重置房型选择
  roomConfig.rooms = 1;
  roomConfig.livingRooms = 1;
  roomConfig.bathrooms = 1;
  
  // 设置默认房型
  data.form.roomType = formattedRoomType.value;
}

// 保存
const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      if (!data.form.imageUrl) {
        ElMessage.warning('请上传主图');
        return;
      }
      const url = data.form.id ? '/house/update' : '/house/add';
      const method = data.form.id ? 'put' : 'post';
      request[method](url, data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success(data.form.id ? '修改成功' : '新增成功');
          data.formVisible = false;
          load();
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.form.imageList = data.form.images ? data.form.images.split(',') : [];
  
  // 解析房型
  parseRoomType(data.form.roomType);
  
  data.formVisible = true;
}

// 删除
const Del = (id) => {
  ElMessageBox.confirm('确定删除该记录吗?', '提示', {type: 'warning'}).then(() => {
    request.delete('/house/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

// 批量删除
const handleSelectionChange = (rows) => {
  data.rows = rows
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  ElMessageBox.confirm('确定删除所选记录吗?', '提示', {type: 'warning'}).then(() => {
    request.delete('/house/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

// 图片上传成功
const handleMainImageSuccess = (res) => {
  data.form.imageUrl = res.data;
};

// 图片集上传成功
const handleImagesSuccess = (res) => {
  data.form.imageList.push(res.data);
  data.form.images = data.form.imageList.join(',');
};

// 删除图片
const handleImageRemove = (file) => {
  const index = data.form.imageList.indexOf(file.url);
  if (index !== -1) {
    data.form.imageList.splice(index, 1);
    data.form.images = data.form.imageList.join(',');
  }
};
</script>

<style scoped>
.el-upload--picture-card {
  width: 150px;
  height: 150px;
  line-height: 150px;
}

.el-upload-list--picture-card .el-upload-list__item {
  width: 150px;
  height: 150px;
}

.room-type-selector {
  display: flex;
  gap: 15px;
  margin-bottom: 10px;
}

.room-type-row {
  display: flex;
  align-items: center;
  gap: 5px;
}

.room-label {
  font-size: 14px;
  color: #606266;
}

.room-type-preview {
  margin-top: 5px;
  color: #409EFF;
  font-size: 14px;
}
</style>