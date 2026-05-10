import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: false,
        headers: {
          'Access-Control-Allow-Origin': '*'
        },
        configure: (proxy) => {
          proxy.on('error', (err) => console.error('proxy error', err))
          proxy.on('proxyReq', (proxyReq, req) => {
            if (req.headers['authorization']) {
              proxyReq.setHeader('Authorization', req.headers['authorization'])
            }
            console.log('Proxying:', req.method, req.url, '| auth:', !!req.headers['authorization'])
          })
        }
      },
      '/auth': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})