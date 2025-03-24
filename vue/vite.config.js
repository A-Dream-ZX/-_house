import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


import AutoImport from 'unplugin-auto-import/vite' //自动导入vue中的组件
import Components from 'unplugin-vue-components/vite' //自动导入ui组件
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers' //自动导入element-plus




// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),


    //element-plus自动导入
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [
        // 配置element-plus采用sass样式
        ElementPlusResolver({importStyle: 'scss'})
      ],
    }),
  ],
  css:{
    preprocessorOptions:{
      scss:{
        additionalData: `@use "@/assets/css/index.scss" as *;`
      }
    }

  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
