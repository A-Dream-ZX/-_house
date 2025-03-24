<template>
  <div>
    <!-- 查询区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.username"
                placeholder="请输入账号查询"
                :prefix-icon="Search"/>
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.name"
                placeholder="请输入名称查询"
                :prefix-icon="Search"/>

      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="primary" @click="rest">重 置</el-button>

    </div>

    <!-- 操作按钮区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="primary" @click="deleteBatch">批量删除</el-button>
      <!-- <el-button type="primary">批量导入</el-button>
      <el-button type="primary">批量导出</el-button> -->
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

        <el-table-column label="头像">
          <template #default="scope">
            <el-image
                v-if="scope.row.avatar"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]"
                :preview-teleported="true"
                style="width: 50px;
                height: 50px;
                border-radius: 50%;
                display: block;
                "/>
          </template>
        </el-table-column>

        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>

        <el-table-column label="操作" width="100">
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
    <!-- 对话框区域-->
    <el-dialog v-model="data.formVisible" title="用户信息" width="400px" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px"
               style="padding: 20px 30px 20px 0px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>

        <el-form-item label="头像" prop="avatar">
          <el-upload
              action="http://localhost:8080/files/upload"
              list-type="picture"
              :headers="{token:data.user.token}"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>


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
import {reactive, ref} from 'vue';
import {Search} from '@element-plus/icons-vue';
import request from '@/utils/request.js';
import {ElMessage, ElMessageBox} from 'element-plus';

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || {}),
  name: null,
  username: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
    ],
    name: [
      {required: true, message: '请输入名称', trigger: 'blur'},
    ],
    phone: [
      {required: true, message: '请输入电话', trigger: 'blur'},
    ],
    email: [
      {required: true, message: '请输入邮箱', trigger: 'blur'},
    ]
  },
  rows: [],
})

const formRef = ref()

// 分页查询
const load = () => {
  request.get('/user/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      username: data.username
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
const rest = () => {
  data.name = null
  data.username = null
  load()
}

// 新增
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}
const add = () => {
  //formRef 是表单的引用
  formRef.value.validate(valid => {
    if (valid) { // 表单校验通过
      request.post('/user/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('新增成功')
          data.formVisible = false // 关闭对话框
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
  data.form = JSON.parse(JSON.stringify(row)) // 深拷贝
  data.formVisible = true // 打开对话框
}
const update = () => {
  //formRef 是表单的引用
  formRef.value.validate(valid => {
    if (valid) { // 表单校验通过
      request.put('/user/update', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功')
          data.formVisible = false // 关闭对话框
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

// 保存事件
const save = () => {
  if (data.form.id) {
    update()
  } else {
    add()
  }
}

// 删除
const Del = (id) => {
  ElMessageBox.confirm('确定删除该记录吗?', '提示', {type: 'warning'}).then(res => {
    request.delete('user/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
  })
}

// 批量删除
const handleSelectionChange = (rows) => {  //  rows 是实际选择的数组
  data.rows = rows
}

const deleteBatch = () => {  //  rows 是实际选择的数组
  if (data.rows.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  ElMessageBox.confirm('确定删除所选记录吗?', '提示', {type: 'warning'}).then(res => {
    request.delete('user/deleteBatch', {data: data.rows}).then(res => {
      if (res.code === '200') {
        ElMessage.success('全部删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
  })
}

// 头像上传
const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data
}

</script>

<style scoped>



</style>