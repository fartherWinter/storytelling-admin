<template>
  <div class="erp-login-container">
    <el-card class="login-card">
      <h2 class="login-title">ERP系统登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-row gutter="8">
            <el-col :span="14">
              <el-input v-model="loginForm.code" placeholder="请输入验证码" maxlength="6" />
            </el-col>
            <el-col :span="10">
              <img :src="captchaImg" @click="getCaptcha" style="height:32px;cursor:pointer;vertical-align:middle;" :alt="'点击刷新验证码'" />
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import service from '@/utils/request'
import { setToken } from '@/utils/auth'

const router = useRouter()
const loginForm = ref({ username: '', password: '', code: '', uuid: '' })
const loginFormRef = ref(null)
const captchaImg = ref('')
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

function getCaptcha() {
  service.post('/sys/login/captchaImage', {}, { responseType: 'json' }).then(res => {
    if (res.data) {
      captchaImg.value = 'data:image/jpeg;base64,' + res.data.img
      loginForm.value.uuid = res.data.uuid
    }
  })
}
onMounted(() => {
  getCaptcha()
})

function handleLogin() {
  loginFormRef.value.validate(valid => {
    if (valid) {
      service.post('/sys/login/login', {
        userName: loginForm.value.username,
        passWord: loginForm.value.password,
        code: loginForm.value.code,
        uuid: loginForm.value.uuid
      }).then(res => {
        if (res.code === '200') {
          setToken(res.data.token)
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error(res.msg || '用户名、密码或验证码错误')
          getCaptcha()
        }
      }).catch(() => {
        ElMessage.error('登录失败')
        getCaptcha()
      })
    }
  })
}
</script>

<style scoped>
.erp-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f5f7fa;
}
.login-card {
  width: 360px;
  padding: 40px 30px 20px 30px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #409EFF;
}
</style>