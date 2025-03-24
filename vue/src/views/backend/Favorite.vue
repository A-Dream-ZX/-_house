<template>
  <div>
    <!-- 查询区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.userId"
                placeholder="请输入用户ID查询"
                :prefix-icon="Search"/>
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.houseId"
                placeholder="请输入房源ID查询"
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
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="userId" label="用户ID"/>
        <el-table-column prop="houseId" label="房源ID"/>
        <el-table-column prop="createdAt" label="收藏时间"/>
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
    <el-dialog v-model="data.formVisible" title="收藏信息" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="100px"
               style="padding: 20px 30px 20px 0px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="data.form.userId" placeholder="请输入用户ID"/>
        </el-form-item>
        <el-form-item label="房源ID" prop="houseId">
          <el-input v-model="data.form.houseId" placeholder="请输入房源ID"/>
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
import {reactive, ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import request from '@/utils/request.js'
import {ElMessage, ElMessageBox} from 'element-plus'

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || '{}'),
  userId: null,
  houseId: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    userId: [{required: true, message: '请输入用户ID', trigger: 'blur'}],
    houseId: [{required: true, message: '请输入房源ID', trigger: 'blur'}]
  },
  rows: []
})

const formRef = ref()

// 分页查询
const load = () => {
  request.get('/favorite/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.userId,
      houseId: data.houseId
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
  data.userId = null
  data.houseId = null
  load()
}

// 新增
const handleAdd = () => {
  data.formVisible = true
  data.form = {
    createdAt: new Date().toISOString()
  }
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 保存
const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      const url = data.form.id ? '/favorite/update' : '/favorite/add'
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

// 删除
const Del = (id) => {
  ElMessageBox.confirm('确定删除该记录吗?', '提示', {type: 'warning'}).then(() => {
    request.delete('/favorite/delete/' + id).then(res => {
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
    request.delete('/favorite/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  })
}
</script>

<style scoped>
</style>