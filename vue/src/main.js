import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/global.css'

// 引入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 引入ElementPlus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用ElementPlus并设置中文
app.use(ElementPlus, {
  locale: zhCn,
})

// 使用路由
app.use(router)

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('Global Error:', err, info)
}

// 挂载应用
app.mount('#app')

