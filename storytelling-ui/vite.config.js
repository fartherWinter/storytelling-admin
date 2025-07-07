import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/',
  plugins: [vue()],
  optimizeDeps: {
    include: ['vue-i18n']
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/admin': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/workflow': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    assetsDir: 'assets',
    rollupOptions: {
      input: path.resolve(__dirname, 'index.html'),
      output: {
        manualChunks: undefined
      }
    }
  }
})