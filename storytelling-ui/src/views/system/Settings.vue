<template>
  <div class="settings-container">
    <div class="page-header">
      <h2>系统设置</h2>
      <p>配置系统基础参数和功能选项</p>
    </div>
    
    <div class="content-area">
      <el-row :gutter="20">
        <!-- 基础设置 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>基础设置</span>
            </template>
            
            <el-form :model="basicSettings" label-width="120px">
              <el-form-item label="系统名称">
                <el-input v-model="basicSettings.systemName" placeholder="请输入系统名称" />
              </el-form-item>
              
              <el-form-item label="系统版本">
                <el-input v-model="basicSettings.version" placeholder="请输入系统版本" />
              </el-form-item>
              
              <el-form-item label="公司名称">
                <el-input v-model="basicSettings.companyName" placeholder="请输入公司名称" />
              </el-form-item>
              
              <el-form-item label="联系邮箱">
                <el-input v-model="basicSettings.contactEmail" placeholder="请输入联系邮箱" />
              </el-form-item>
              
              <el-form-item label="系统描述">
                <el-input 
                  v-model="basicSettings.description" 
                  type="textarea" 
                  :rows="3"
                  placeholder="请输入系统描述"
                />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveBasicSettings">保存设置</el-button>
                <el-button @click="resetBasicSettings">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        
        <!-- 安全设置 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>安全设置</span>
            </template>
            
            <el-form :model="securitySettings" label-width="120px">
              <el-form-item label="密码强度">
                <el-select v-model="securitySettings.passwordStrength" placeholder="选择密码强度">
                  <el-option label="低" value="low" />
                  <el-option label="中" value="medium" />
                  <el-option label="高" value="high" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="会话超时">
                <el-input-number 
                  v-model="securitySettings.sessionTimeout" 
                  :min="5" 
                  :max="480"
                  controls-position="right"
                />
                <span style="margin-left: 10px; color: #909399;">分钟</span>
              </el-form-item>
              
              <el-form-item label="登录失败限制">
                <el-input-number 
                  v-model="securitySettings.maxLoginAttempts" 
                  :min="3" 
                  :max="10"
                  controls-position="right"
                />
                <span style="margin-left: 10px; color: #909399;">次</span>
              </el-form-item>
              
              <el-form-item label="启用双因子认证">
                <el-switch v-model="securitySettings.twoFactorAuth" />
              </el-form-item>
              
              <el-form-item label="启用IP白名单">
                <el-switch v-model="securitySettings.ipWhitelist" />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveSecuritySettings">保存设置</el-button>
                <el-button @click="resetSecuritySettings">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 邮件设置 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>邮件设置</span>
            </template>
            
            <el-form :model="emailSettings" label-width="120px">
              <el-form-item label="SMTP服务器">
                <el-input v-model="emailSettings.smtpServer" placeholder="请输入SMTP服务器地址" />
              </el-form-item>
              
              <el-form-item label="端口">
                <el-input-number 
                  v-model="emailSettings.port" 
                  :min="1" 
                  :max="65535"
                  controls-position="right"
                  style="width: 100%;"
                />
              </el-form-item>
              
              <el-form-item label="用户名">
                <el-input v-model="emailSettings.username" placeholder="请输入邮箱用户名" />
              </el-form-item>
              
              <el-form-item label="密码">
                <el-input 
                  v-model="emailSettings.password" 
                  type="password" 
                  placeholder="请输入邮箱密码"
                  show-password
                />
              </el-form-item>
              
              <el-form-item label="启用SSL">
                <el-switch v-model="emailSettings.ssl" />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveEmailSettings">保存设置</el-button>
                <el-button @click="testEmail">测试连接</el-button>
                <el-button @click="resetEmailSettings">重置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
        
        <!-- 系统维护 -->
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>系统维护</span>
            </template>
            
            <div class="maintenance-section">
              <div class="maintenance-item">
                <div class="item-info">
                  <h4>数据备份</h4>
                  <p>定期备份系统数据，确保数据安全</p>
                </div>
                <el-button type="primary" @click="backupData">立即备份</el-button>
              </div>
              
              <el-divider />
              
              <div class="maintenance-item">
                <div class="item-info">
                  <h4>清理日志</h4>
                  <p>清理系统运行日志，释放存储空间</p>
                </div>
                <el-button type="warning" @click="clearLogs">清理日志</el-button>
              </div>
              
              <el-divider />
              
              <div class="maintenance-item">
                <div class="item-info">
                  <h4>系统重启</h4>
                  <p>重启系统服务，应用最新配置</p>
                </div>
                <el-button type="danger" @click="restartSystem">重启系统</el-button>
              </div>
              
              <el-divider />
              
              <div class="maintenance-item">
                <div class="item-info">
                  <h4>系统信息</h4>
                  <p>查看系统运行状态和资源使用情况</p>
                </div>
                <el-button @click="viewSystemInfo">查看信息</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const basicSettings = ref({
  systemName: 'ERP管理系统',
  version: '1.0.0',
  companyName: '示例公司',
  contactEmail: 'admin@example.com',
  description: '企业资源规划管理系统，提供完整的业务流程管理解决方案'
})

const securitySettings = ref({
  passwordStrength: 'medium',
  sessionTimeout: 30,
  maxLoginAttempts: 5,
  twoFactorAuth: false,
  ipWhitelist: false
})

const emailSettings = ref({
  smtpServer: 'smtp.example.com',
  port: 587,
  username: '',
  password: '',
  ssl: true
})

const saveBasicSettings = () => {
  ElMessage.success('基础设置保存成功')
}

const resetBasicSettings = () => {
  basicSettings.value = {
    systemName: 'ERP管理系统',
    version: '1.0.0',
    companyName: '示例公司',
    contactEmail: 'admin@example.com',
    description: '企业资源规划管理系统，提供完整的业务流程管理解决方案'
  }
  ElMessage.info('基础设置已重置')
}

const saveSecuritySettings = () => {
  ElMessage.success('安全设置保存成功')
}

const resetSecuritySettings = () => {
  securitySettings.value = {
    passwordStrength: 'medium',
    sessionTimeout: 30,
    maxLoginAttempts: 5,
    twoFactorAuth: false,
    ipWhitelist: false
  }
  ElMessage.info('安全设置已重置')
}

const saveEmailSettings = () => {
  ElMessage.success('邮件设置保存成功')
}

const testEmail = () => {
  ElMessage.info('正在测试邮件连接...')
  setTimeout(() => {
    ElMessage.success('邮件连接测试成功')
  }, 2000)
}

const resetEmailSettings = () => {
  emailSettings.value = {
    smtpServer: 'smtp.example.com',
    port: 587,
    username: '',
    password: '',
    ssl: true
  }
  ElMessage.info('邮件设置已重置')
}

const backupData = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要立即备份系统数据吗？',
      '确认备份',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    )
    ElMessage.success('数据备份已开始，请稍候...')
  } catch {
    ElMessage.info('已取消备份')
  }
}

const clearLogs = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清理系统日志吗？此操作不可恢复！',
      '确认清理',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('系统日志清理完成')
  } catch {
    ElMessage.info('已取消清理')
  }
}

const restartSystem = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重启系统吗？重启期间系统将暂时不可用！',
      '确认重启',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    ElMessage.success('系统重启指令已发送')
  } catch {
    ElMessage.info('已取消重启')
  }
}

const viewSystemInfo = () => {
  ElMessage.info('系统信息查看功能开发中...')
}

onMounted(() => {
  // 初始化设置数据
})
</script>

<style scoped>
.settings-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.content-area {
  background: #f5f5f5;
  min-height: calc(100vh - 200px);
}

.maintenance-section {
  padding: 10px 0;
}

.maintenance-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
}

.item-info h4 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 16px;
}

.item-info p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}
</style>