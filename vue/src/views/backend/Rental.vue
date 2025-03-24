<template>
  <div>
    <!-- 查询区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.userId"
                placeholder="请输入租户ID查询"
                :prefix-icon="Search"/>
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.houseId"
                placeholder="请输入房源ID查询"
                :prefix-icon="Search"/>
      <el-select v-model="data.status" clearable placeholder="请选择状态" style="width: 200px; margin-right: 10px;">
        <el-option label="待确认" value="pending"/>
        <el-option label="进行中" value="ongoing"/>
        <el-option label="已完成" value="completed"/>
        <el-option label="已取消" value="canceled"/>
      </el-select>

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
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="userId" label="租户ID" width="100"/>
        <el-table-column prop="houseId" label="房源ID" width="100"/>
        <el-table-column prop="startDate" label="起租日期" width="120"/>
        <el-table-column prop="endDate" label="到期日期" width="120"/>
        <el-table-column prop="totalPrice" label="总租金" width="120"/>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip/>
        <el-table-column prop="createdAt" label="创建时间" width="180"/>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 'pending'"
              type="success" 
              size="small" 
              @click="handleConfirm(scope.row)">确认
            </el-button>
            <el-button 
              v-if="scope.row.status === 'pending'"
              type="danger" 
              size="small" 
              @click="handleCancel(scope.row)">取消
            </el-button>
            <el-button 
              v-if="scope.row.status === 'ongoing'"
              type="warning" 
              size="small" 
              @click="handleComplete(scope.row)">完成
            </el-button>
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
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
    <el-dialog v-model="data.formVisible" :title="data.form.id ? '编辑租约' : '新增租约'" width="500px">
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px">
        <el-form-item label="租户ID" prop="userId">
          <el-input v-model="data.form.userId" placeholder="请输入租户ID"/>
        </el-form-item>
        <el-form-item label="房源ID" prop="houseId">
          <el-input v-model="data.form.houseId" placeholder="请输入房源ID"/>
        </el-form-item>
        <el-form-item label="起租日期" prop="startDate">
          <el-date-picker
              v-model="data.form.startDate"
              type="date"
              placeholder="选择起租日期"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="到期日期" prop="endDate">
          <el-date-picker
              v-model="data.form.endDate"
              type="date"
              placeholder="选择到期日期"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="总租金" prop="totalPrice">
          <el-input-number v-model="data.form.totalPrice" :min="0" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="data.form.status" style="width: 100%">
            <el-option label="待确认" value="pending"/>
            <el-option label="进行中" value="ongoing"/>
            <el-option label="已完成" value="completed"/>
            <el-option label="已取消" value="canceled"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="data.form.remark" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from 'element-plus'

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || '{}'),
  userId: null,
  houseId: null,
  status: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    userId: [{required: true, message: '请输入租户ID', trigger: 'blur'}],
    houseId: [{required: true, message: '请输入房源ID', trigger: 'blur'}],
    startDate: [{required: true, message: '请选择起租日期', trigger: 'change'}],
    endDate: [{required: true, message: '请选择到期日期', trigger: 'change'}],
    totalPrice: [{required: true, message: '请输入总租金', trigger: 'blur'}],
    status: [{required: true, message: '请选择状态', trigger: 'change'}]
  },
  rows: []
})

const formRef = ref()

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

// 确认租约
const handleConfirm = (row) => {
  ElMessageBox.confirm('确认接受该租约申请?', '提示', {type: 'warning'}).then(() => {
    request.put(`/rental/updateStatus/${row.id}`, null, {
      params: {status: 'ongoing'}
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已确认租约')
        load()
      }
    })
  })
}

// 取消租约
const handleCancel = (row) => {
  ElMessageBox.confirm('确认取消该租约?', '提示', {type: 'warning'}).then(() => {
    request.put(`/rental/updateStatus/${row.id}`, null, {
      params: {status: 'canceled'}
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已取消租约')
        load()
      }
    })
  })
}

// 完成租约
const handleComplete = (row) => {
  ElMessageBox.confirm('确认将该租约标记为已完成?', '提示', {type: 'warning'}).then(() => {
    request.put(`/rental/updateStatus/${row.id}`, null, {
      params: {status: 'completed'}
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('已完成租约，房源已恢复可租状态');
        load()
      }
    })
  })
}

// 分页查询
const load = () => {
  request.get('/rental/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.userId,
      houseId: data.houseId,
      status: data.status
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

// 重置查询
const reset = () => {
  data.userId = null
  data.houseId = null
  data.status = null
  load()
}

// 新增
const handleAdd = () => {
  data.formVisible = true
  data.form = {
    status: 'pending',
    adminId: data.user.id,
    createdAt: new Date().toISOString()
  }
}

// 保存
const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      const url = data.form.id ? '/rental/update' : '/rental/add'
      const method = data.form.id ? 'put' : 'post'
      request[method](url, data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success(data.form.id ? '修改成功' : '新增成功')
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 表格多选
const handleSelectionChange = (rows) => {
  data.rows = rows
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  ElMessageBox.confirm('确定删除所选记录吗?', '提示', {type: 'warning'}).then(() => {
    request.delete('/rental/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}

// 初始加载
load()
</script>

<style scoped>
.el-input-number {
  width: 100%;
}
</style>