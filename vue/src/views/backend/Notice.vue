<template>
  <div>
    <!-- 查询区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load"
                style="width: 260px; margin-right: 10px;"
                v-model="data.title"
                placeholder="请输入"
                :prefix-icon="Search"/>

      <el-button type="primary" @click="load">查 询</el-button>
      <el-button type="primary" @click="rest">重 置</el-button>

    </div>

    <!-- 操作按钮区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-button type="primary" @click="handleAdd">新 增</el-button>

    </div>
    <!-- 表格区域 -->
    <div class="card" style="margin-bottom: 5px;">
      <el-table
          :data="data.tableData"
          style="width: 100%"
          :header-cell-style="{ color: '#333', backgroundColor: '#fff' }"
      >
        <el-table-column type="selection" width="55"/>

        <el-table-column prop="title" label="公告标题"/>
        <el-table-column prop="content" label="公告内容"/>
        <el-table-column prop="time" label="发布时间"/>

        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="Del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="data.formVisible" title="系统公告" width="400px" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px"
               style="padding: 20px 30px 20px 0px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入标题"/>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input type="textarea" :rows="4" v-model="data.form.content" autocomplete="off" placeholder="请输入内容"/>
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
import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || {}),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tablrData: [],
  form: {},
  formVisible: false,
  rules: {
    title: [
      {required: true, message: '请输入标题', trigger: 'blur'},
    ],
    content: [
      {required: true, message: '请输入内容', trigger: 'blur'},
    ],

  },

})

const load = () => {
  request.get('/notice/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }

  })
}

load()


// 新增
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}
const add = () => {
  //formRef 是表单的引用
  formRef.value.validate(valid => {
    if (valid) { // 表单校验通过
      request.post('/notice/add', data.form).then(res => {
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

// 重置
const rest = () => {
  data.title = null
  load()
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
      request.put('/notice/update', data.form).then(res => {
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
    request.delete('notice/delete/' + id).then(res => {
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


</script>

<style scoped>

</style>