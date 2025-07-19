import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { URL as NodeURL } from 'node:url'

export default defineConfig(({ mode }) => {
  // 載入 .env 檔案
  const env = loadEnv(mode, process.cwd(), '')
  
  // 從 VITE_FRONT_URL 擷取 hostname
  const frontUrl = env.VITE_FRONT_URL
  const hostname = new NodeURL(frontUrl).hostname

  return {
    plugins: [
      vue(),
      vueDevTools(),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      },
    },
    server: {
      host: '0.0.0.0',
      port: 4173,
      allowedHosts: [hostname], // 使用 .env 的前端網址主機名
    },
  }
})
