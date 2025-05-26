<template>
  <div class="create-conference-container">
    <el-card class="conference-card">
      <template #header>
        <div class="card-header">
          <h2>{{ $t('conference.createOrJoin') }}</h2>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="$t('conference.createNew')" name="create">
          <el-form :model="createForm" label-position="top">
            <el-form-item :label="$t('conference.roomName')">
              <el-input v-model="createForm.name" :placeholder="$t('conference.enterRoomName')"></el-input>
            </el-form-item>
            
            <el-form-item :label="$t('conference.description')">
              <el-input 
                v-model="createForm.description" 
                type="textarea" 
                :placeholder="$t('conference.enterDescription')"
              ></el-input>
            </el-form-item>
            
            <el-form-item>
              <el-checkbox v-model="createForm.isPrivate">{{ $t('conference.privateRoom') }}</el-checkbox>
              <el-tooltip :content="$t('conference.privateRoomTip')" placement="top">
                <el-icon class="info-icon"><InfoFilled /></el-icon>
              </el-tooltip>
            </el-form-item>
            
            <el-form-item v-if="createForm.isPrivate">
              <el-input 
                v-model="createForm.password" 
                type="password" 
                :placeholder="$t('conference.enterPassword')"
                show-password
              ></el-input>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="createConference" :loading="loading">
                {{ $t('conference.createAndJoin') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane :label="$t('conference.joinExisting')" name="join">
          <el-form :model="joinForm" label-position="top">
            <el-form-item :label="$t('conference.roomId')">
              <el-input v-model="joinForm.roomId" :placeholder="$t('conference.enterRoomId')"></el-input>
            </el-form-item>
            
            <el-form-item :label="$t('conference.displayName')">
              <el-input v-model="joinForm.displayName" :placeholder="$t('conference.enterYourName')"></el-input>
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="joinConference" :loading="loading">
                {{ $t('conference.join') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <el-card class="recent-rooms-card" v-if="recentRooms.length > 0">
      <template #header>
        <div class="card-header">
          <h3>{{ $t('conference.recentRooms') }}</h3>
        </div>
      </template>
      
      <el-table :data="recentRooms" style="width: 100%">
        <el-table-column prop="name" :label="$t('conference.roomName')" width="180">
          <template #default="scope">
            <el-link type="primary" @click="quickJoin(scope.row)">{{ scope.row.name }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" :label="$t('conference.createdAt')" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="participants" :label="$t('conference.participants')">
          <template #default="scope">
            {{ scope.row.participants?.length || 0 }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('conference.actions')" width="120">
          <template #default="scope">
            <el-button size="small" @click="quickJoin(scope.row)">{{ $t('conference.join') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog
      v-model="passwordDialogVisible"
      :title="$t('conference.enterRoomPassword')"
      width="30%"
    >
      <el-form>
        <el-form-item :label="$t('conference.password')">
          <el-input 
            v-model="passwordInput" 
            type="password" 
            show-password
            @keyup.enter="confirmJoinWithPassword"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="confirmJoinWithPassword">
            {{ $t('conference.join') }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { InfoFilled } from '@element-plus/icons-vue'
import { useConferenceStore } from '@/stores/conference'
import axios from 'axios'

export default {
  name: 'CreateConference',
  components: {
    InfoFilled
  },
  setup() {
    const router = useRouter()
    const conferenceStore = useConferenceStore()
    
    // 状态变量
    const activeTab = ref('create')
    const loading = ref(false)
    const passwordDialogVisible = ref(false)
    const passwordInput = ref('')
    const selectedRoom = ref(null)
    
    // 表单数据
    const createForm = reactive({
      name: '',
      description: '',
      isPrivate: false,
      password: ''
    })
    
    const joinForm = reactive({
      roomId: '',
      displayName: localStorage.getItem('conferenceDisplayName') || ''
    })
    
    // 最近的房间列表
    const recentRooms = computed(() => conferenceStore.activeRooms)
    
    // 初始化
    onMounted(() => {
      // 从本地存储加载显示名称
      if (joinForm.displayName) {
        localStorage.setItem('conferenceDisplayName', joinForm.displayName)
      }
      
      // 加载最近的房间列表
      loadRecentRooms()
    })
    
    // 加载最近的房间列表
    const loadRecentRooms = async () => {
      try {
        const response = await axios.get('/api/conference/rooms/recent')
        if (response.data && Array.isArray(response.data)) {
          response.data.forEach(room => {
            if (!conferenceStore.activeRooms.some(r => r.id === room.id)) {
              conferenceStore.activeRooms.push(room)
            }
          })
        }
      } catch (error) {
        console.error('加载最近房间失败:', error)
      }
    }
    
    // 创建会议
    const createConference = async () => {
      if (!createForm.name.trim()) {
        ElMessage.warning('请输入会议室名称')
        return
      }
      
      if (createForm.isPrivate && !createForm.password) {
        ElMessage.warning('私密会议室需要设置密码')
        return
      }
      
      loading.value = true
      
      try {
        // 调用API创建会议室
        const response = await axios.post('/api/conference/rooms', {
          name: createForm.name,
          description: createForm.description,
          isPrivate: createForm.isPrivate,
          password: createForm.isPrivate ? createForm.password : undefined
        })
        
        const roomId = response.data.roomId
        
        // 保存到状态管理
        conferenceStore.createRoom({
          id: roomId,
          name: createForm.name,
          description: createForm.description,
          isPrivate: createForm.isPrivate,
          createdAt: new Date()
        })
        
        // 保存显示名称
        if (joinForm.displayName) {
          localStorage.setItem('conferenceDisplayName', joinForm.displayName)
        }
        
        // 跳转到会议室
        router.push(`/conference/${roomId}`)
      } catch (error) {
        console.error('创建会议室失败:', error)
        ElMessage.error('创建会议室失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 加入会议
    const joinConference = async () => {
      if (!joinForm.roomId.trim()) {
        ElMessage.warning('请输入会议室ID')
        return
      }
      
      if (!joinForm.displayName.trim()) {
        ElMessage.warning('请输入您的显示名称')
        return
      }
      
      loading.value = true
      
      try {
        // 检查房间是否存在并是否需要密码
        const response = await axios.get(`/api/conference/rooms/${joinForm.roomId}/check`)
        
        if (response.data.exists) {
          if (response.data.isPrivate) {
            // 如果是私密房间，显示密码输入对话框
            passwordDialogVisible.value = true
            selectedRoom.value = {
              id: joinForm.roomId,
              name: response.data.name || joinForm.roomId
            }
          } else {
            // 直接加入公开房间
            proceedToJoinRoom(joinForm.roomId)
          }
        } else {
          ElMessage.error('会议室不存在或已结束')
        }
      } catch (error) {
        console.error('检查会议室失败:', error)
        ElMessage.error('无法加入会议室，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 确认使用密码加入
    const confirmJoinWithPassword = async () => {
      if (!passwordInput.value) {
        ElMessage.warning('请输入密码')
        return
      }
      
      loading.value = true
      
      try {
        // 验证密码
        const response = await axios.post(`/api/conference/rooms/${selectedRoom.value.id}/verify-password`, {
          password: passwordInput.value
        })
        
        if (response.data.valid) {
          passwordDialogVisible.value = false
          proceedToJoinRoom(selectedRoom.value.id)
        } else {
          ElMessage.error('密码错误')
        }
      } catch (error) {
        console.error('验证密码失败:', error)
        ElMessage.error('验证密码失败，请稍后重试')
      } finally {
        loading.value = false
      }
    }
    
    // 快速加入最近的房间
    const quickJoin = (room) => {
      if (room.isPrivate) {
        passwordDialogVisible.value = true
        selectedRoom.value = room
      } else {
        proceedToJoinRoom(room.id)
      }
    }
    
    // 执行加入房间
    const proceedToJoinRoom = (roomId) => {
      // 保存显示名称
      if (joinForm.displayName) {
        localStorage.setItem('conferenceDisplayName', joinForm.displayName)
      }
      
      // 加入房间
      conferenceStore.joinRoom(roomId)
      
      // 跳转到会议室
      router.push(`/conference/${roomId}`)
    }
    
    // 格式化日期
    const formatDate = (dateString) => {
      const date = new Date(dateString)
      return date.toLocaleString()
    }
    
    return {
      activeTab,
      createForm,
      joinForm,
      loading,
      recentRooms,
      passwordDialogVisible,
      passwordInput,
      createConference,
      joinConference,
      quickJoin,
      confirmJoinWithPassword,
      formatDate
    }
  }
}
</script>

<style scoped>
.create-conference-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.conference-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2, .card-header h3 {
  margin: 0;
}

.info-icon {
  margin-left: 5px;
  color: #909399;
  cursor: help;
}

.recent-rooms-card {
  margin-top: 30px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>