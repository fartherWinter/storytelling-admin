<template>
  <div class="video-conference-container">
    <div class="conference-header">
      <h2>{{ $t('conference.title') }}</h2>
      <div class="room-info">
        <span>{{ $t('conference.roomId') }}: {{ roomId }}</span>
        <el-button type="primary" size="small" @click="copyRoomLink">
          {{ $t('conference.copyLink') }}
        </el-button>
      </div>
    </div>

    <div class="conference-main">
      <div class="video-grid">
        <div v-for="peer in peers" :key="peer.id" class="video-container">
          <video
            :id="`video-${peer.id}`"
            :ref="el => { if(el) videoRefs[peer.id] = el }"
            autoplay
            playsinline
            :muted="peer.id === localPeerId"
          ></video>
          <div class="peer-info">
            <span>{{ peer.displayName }}</span>
            <div class="peer-status">
              <el-icon v-if="!peer.audioEnabled"><Mute /></el-icon>
              <el-icon v-if="!peer.videoEnabled"><VideoCamera /></el-icon>
            </div>
          </div>
        </div>
      </div>

      <div class="conference-sidebar">
        <div class="participants-list">
          <h3>{{ $t('conference.participants') }} ({{ peers.length }})</h3>
          <ul>
            <li v-for="peer in peers" :key="peer.id" :class="{ 'is-local': peer.id === localPeerId }">
              <span>{{ peer.displayName }}</span>
              <div class="peer-controls">
                <el-icon v-if="!peer.audioEnabled"><Mute /></el-icon>
                <el-icon v-if="!peer.videoEnabled"><VideoCamera /></el-icon>
              </div>
            </li>
          </ul>
        </div>
        <div class="chat-panel">
          <h3>{{ $t('conference.chat') }}</h3>
          <div class="chat-messages" ref="chatMessagesContainer">
            <div v-for="(message, index) in chatMessages" :key="index" class="chat-message" :class="{ 'is-local': message.senderId === localPeerId }">
              <div class="message-sender">{{ message.senderName }}</div>
              <div class="message-content">{{ message.content }}</div>
              <div class="message-time">{{ formatTime(message.timestamp) }}</div>
            </div>
          </div>
          <div class="chat-input">
            <el-input
              v-model="chatInput"
              :placeholder="$t('conference.typeMessage')"
              @keyup.enter="sendChatMessage"
            ></el-input>
            <el-button type="primary" @click="sendChatMessage">
              {{ $t('conference.send') }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div class="conference-controls">
      <el-button 
        :type="audioEnabled ? 'info' : 'danger'"
        circle
        @click="toggleAudio"
      >
        <el-icon><Microphone v-if="audioEnabled" /><Mute v-else /></el-icon>
      </el-button>
      <el-button 
        :type="videoEnabled ? 'info' : 'danger'"
        circle
        @click="toggleVideo"
      >
        <el-icon><VideoCamera v-if="videoEnabled" /><VideoCameraSlash v-else /></el-icon>
      </el-button>
      <el-button 
        type="primary"
        circle
        @click="toggleScreenSharing"
      >
        <el-icon><Monitor /></el-icon>
      </el-button>
      <el-button 
        type="danger"
        circle
        @click="leaveConference"
      >
        <el-icon><SwitchButton /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Microphone, Mute, VideoCamera, VideoCameraSlash, Monitor, SwitchButton } from '@element-plus/icons-vue'
import { useConferenceStore } from '@/stores/conference'

export default {
  name: 'VideoConference',
  components: {
    Microphone,
    Mute,
    VideoCamera,
    VideoCameraSlash,
    Monitor,
    SwitchButton
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const conferenceStore = useConferenceStore()
    
    // 状态变量
    const roomId = ref(route.params.roomId || 'default-room')
    const localPeerId = ref('')
    const peers = ref([])
    const videoRefs = reactive({})
    const audioEnabled = ref(true)
    const videoEnabled = ref(true)
    const isScreenSharing = ref(false)
    const chatMessages = ref([])
    const chatInput = ref('')
    const chatMessagesContainer = ref(null)
    
    // WebRTC相关变量
    let socket = null
    let localStream = null
    let screenStream = null
    let rtcPeerConnections = {}
    
    // 初始化会议
    onMounted(async () => {
      try {
        // 初始化WebSocket连接
        initializeSocket()
        
        // 获取本地媒体流
        await getLocalMedia()
        
        // 加入房间
        joinRoom()
        
        // 添加窗口关闭事件监听
        window.addEventListener('beforeunload', handleBeforeUnload)
      } catch (error) {
        console.error('初始化会议失败:', error)
        ElMessage.error('初始化会议失败，请检查摄像头和麦克风权限')
      }
    })
    
    // 组件销毁前清理资源
    onBeforeUnmount(() => {
      leaveRoom()
      window.removeEventListener('beforeunload', handleBeforeUnload)
    })
    
    // 初始化WebSocket连接
    const initializeSocket = () => {
      const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
      const wsUrl = `${protocol}//${window.location.host}/api/conference/ws/${roomId.value}`
      
      socket = new WebSocket(wsUrl)
      
      socket.onopen = () => {
        console.log('WebSocket连接已建立')
      }
      
      socket.onmessage = (event) => {
        const message = JSON.parse(event.data)
        handleSignalingMessage(message)
      }
      
      socket.onerror = (error) => {
        console.error('WebSocket错误:', error)
        ElMessage.error('连接服务器失败，请稍后重试')
      }
      
      socket.onclose = () => {
        console.log('WebSocket连接已关闭')
      }
    }
    
    // 处理信令消息
    const handleSignalingMessage = async (message) => {
      switch (message.type) {
        case 'room_created':
          localPeerId.value = message.peerId
          break
          
        case 'room_joined':
          localPeerId.value = message.peerId
          break
          
        case 'new_peer':
          createPeerConnection(message.peerId, message.displayName, false)
          sendOffer(message.peerId)
          break
          
        case 'peer_left':
          removePeer(message.peerId)
          break
          
        case 'offer':
          await handleOffer(message)
          break
          
        case 'answer':
          await handleAnswer(message)
          break
          
        case 'ice_candidate':
          handleIceCandidate(message)
          break
          
        case 'chat_message':
          handleChatMessage(message)
          break
      }
    }
    
    // 获取本地媒体流
    const getLocalMedia = async () => {
      try {
        localStream = await navigator.mediaDevices.getUserMedia({
          audio: true,
          video: {
            width: { ideal: 1280 },
            height: { ideal: 720 }
          }
        })
        
        // 添加本地视频流
        peers.value.push({
          id: 'local',
          displayName: '我',
          audioEnabled: true,
          videoEnabled: true,
          isLocal: true
        })
        
        // 等待DOM更新后设置视频源
        setTimeout(() => {
          if (videoRefs['local']) {
            videoRefs['local'].srcObject = localStream
          }
        }, 0)
      } catch (error) {
        console.error('获取媒体设备失败:', error)
        throw error
      }
    }
    
    // 加入房间
    const joinRoom = () => {
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify({
          type: 'join',
          roomId: roomId.value,
          displayName: '用户' + Math.floor(Math.random() * 1000)
        }))
      } else {
        setTimeout(joinRoom, 1000) // 如果连接未就绪，稍后重试
      }
    }
    
    // 创建对等连接
    const createPeerConnection = (peerId, displayName, isInitiator) => {
      const peerConnection = new RTCPeerConnection({
        iceServers: [
          { urls: 'stun:stun.l.google.com:19302' },
          { urls: 'stun:stun1.l.google.com:19302' }
        ]
      })
      
      // 添加本地流轨道到连接
      localStream.getTracks().forEach(track => {
        peerConnection.addTrack(track, localStream)
      })
      
      // 处理ICE候选
      peerConnection.onicecandidate = (event) => {
        if (event.candidate) {
          socket.send(JSON.stringify({
            type: 'ice_candidate',
            candidate: event.candidate,
            targetPeerId: peerId
          }))
        }
      }
      
      // 处理远程流
      peerConnection.ontrack = (event) => {
        // 检查是否已存在该对等方
        const existingPeer = peers.value.find(p => p.id === peerId)
        
        if (!existingPeer) {
          peers.value.push({
            id: peerId,
            displayName: displayName,
            audioEnabled: true,
            videoEnabled: true,
            isLocal: false
          })
        }
        
        // 等待DOM更新后设置视频源
        setTimeout(() => {
          if (videoRefs[peerId]) {
            videoRefs[peerId].srcObject = event.streams[0]
          }
        }, 0)
      }
      
      // 存储连接
      rtcPeerConnections[peerId] = peerConnection
      
      return peerConnection
    }
    
    // 发送offer
    const sendOffer = async (peerId) => {
      const peerConnection = rtcPeerConnections[peerId]
      
      try {
        const offer = await peerConnection.createOffer()
        await peerConnection.setLocalDescription(offer)
        
        socket.send(JSON.stringify({
          type: 'offer',
          offer: offer,
          targetPeerId: peerId
        }))
      } catch (error) {
        console.error('创建offer失败:', error)
      }
    }
    
    // 处理offer
    const handleOffer = async (message) => {
      const { offer, peerId, displayName } = message
      
      // 创建对等连接（如果不存在）
      if (!rtcPeerConnections[peerId]) {
        createPeerConnection(peerId, displayName, false)
      }
      
      const peerConnection = rtcPeerConnections[peerId]
      
      try {
        await peerConnection.setRemoteDescription(new RTCSessionDescription(offer))
        const answer = await peerConnection.createAnswer()
        await peerConnection.setLocalDescription(answer)
        
        socket.send(JSON.stringify({
          type: 'answer',
          answer: answer,
          targetPeerId: peerId
        }))
      } catch (error) {
        console.error('处理offer失败:', error)
      }
    }
    
    // 处理answer
    const handleAnswer = async (message) => {
      const { answer, peerId } = message
      const peerConnection = rtcPeerConnections[peerId]
      
      if (peerConnection) {
        try {
          await peerConnection.setRemoteDescription(new RTCSessionDescription(answer))
        } catch (error) {
          console.error('处理answer失败:', error)
        }
      }
    }
    
    // 处理ICE候选
    const handleIceCandidate = (message) => {
      const { candidate, peerId } = message
      const peerConnection = rtcPeerConnections[peerId]
      
      if (peerConnection) {
        try {
          peerConnection.addIceCandidate(new RTCIceCandidate(candidate))
        } catch (error) {
          console.error('添加ICE候选失败:', error)
        }
      }
    }
    
    // 移除对等方
    const removePeer = (peerId) => {
      // 关闭并删除连接
      if (rtcPeerConnections[peerId]) {
        rtcPeerConnections[peerId].close()
        delete rtcPeerConnections[peerId]
      }
      
      // 从列表中移除
      peers.value = peers.value.filter(peer => peer.id !== peerId)
    }
    
    // 切换音频
    const toggleAudio = () => {
      if (localStream) {
        const audioTrack = localStream.getAudioTracks()[0]
        if (audioTrack) {
          audioTrack.enabled = !audioTrack.enabled
          audioEnabled.value = audioTrack.enabled
          
          // 通知其他参与者
          socket.send(JSON.stringify({
            type: 'media_state_change',
            mediaType: 'audio',
            enabled: audioTrack.enabled
          }))
        }
      }
    }
    
    // 切换视频
    const toggleVideo = () => {
      if (localStream) {
        const videoTrack = localStream.getVideoTracks()[0]
        if (videoTrack) {
          videoTrack.enabled = !videoTrack.enabled
          videoEnabled.value = videoTrack.enabled
          
          // 通知其他参与者
          socket.send(JSON.stringify({
            type: 'media_state_change',
            mediaType: 'video',
            enabled: videoTrack.enabled
          }))
        }
      }
    }
    
    // 切换屏幕共享
    const toggleScreenSharing = async () => {
      try {
        if (!isScreenSharing.value) {
          // 开始屏幕共享
          screenStream = await navigator.mediaDevices.getDisplayMedia({
            video: true
          })
          
          // 替换所有连接中的视频轨道
          const videoTrack = screenStream.getVideoTracks()[0]
          
          Object.values(rtcPeerConnections).forEach(pc => {
            const sender = pc.getSenders().find(s => s.track && s.track.kind === 'video')
            if (sender) {
              sender.replaceTrack(videoTrack)
            }
          })
          
          // 更新本地视频
          if (videoRefs['local']) {
            videoRefs['local'].srcObject = screenStream
          }
          
          // 监听屏幕共享结束事件
          videoTrack.onended = () => {
            stopScreenSharing()
          }
          
          isScreenSharing.value = true
        } else {
          // 停止屏幕共享
          stopScreenSharing()
        }
      } catch (error) {
        console.error('屏幕共享失败:', error)
        ElMessage.error('屏幕共享失败')
      }
    }
    
    // 停止屏幕共享
    const stopScreenSharing = () => {
      if (screenStream) {
        screenStream.getTracks().forEach(track => track.stop())
        screenStream = null
        
        // 恢复摄像头视频
        const videoTrack = localStream.getVideoTracks()[0]
        
        Object.values(rtcPeerConnections).forEach(pc => {
          const sender = pc.getSenders().find(s => s.track && s.track.kind === 'video')
          if (sender && videoTrack) {
            sender.replaceTrack(videoTrack)
          }
        })
        
        // 更新本地视频
        if (videoRefs['local']) {
          videoRefs['local'].srcObject = localStream
        }
        
        isScreenSharing.value = false
      }
    }
    
    // 发送聊天消息
    const sendChatMessage = () => {
      if (chatInput.value.trim() && socket) {
        const message = {
          type: 'chat_message',
          content: chatInput.value.trim(),
          roomId: roomId.value
        }
        
        socket.send(JSON.stringify(message))
        
        // 添加到本地消息列表
        chatMessages.value.push({
          senderId: localPeerId.value,
          senderName: '我',
          content: chatInput.value.trim(),
          timestamp: new Date()
        })
        
        // 清空输入
        chatInput.value = ''
        
        // 滚动到底部
        scrollChatToBottom()
      }
    }
    
    // 处理聊天消息
    const handleChatMessage = (message) => {
      chatMessages.value.push({
        senderId: message.senderId,
        senderName: message.senderName,
        content: message.content,
        timestamp: new Date(message.timestamp)
      })
      
      // 滚动到底部
      scrollChatToBottom()
    }
    
    // 滚动聊天到底部
    const scrollChatToBottom = () => {
      if (chatMessagesContainer.value) {
        setTimeout(() => {
          chatMessagesContainer.value.scrollTop = chatMessagesContainer.value.scrollHeight
        }, 50)
      }
    }
    
    // 格式化时间
    const formatTime = (timestamp) => {
      const date = new Date(timestamp)
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }
    
    // 复制房间链接
    const copyRoomLink = () => {
      const link = `${window.location.origin}/conference/${roomId.value}`
      navigator.clipboard.writeText(link).then(() => {
        ElMessage.success('会议链接已复制到剪贴板')
      }).catch(err => {
        console.error('复制失败:', err)
        ElMessage.error('复制失败')
      })
    }
    
    // 离开会议
    const leaveConference = () => {
      ElMessageBox.confirm(
        '确定要离开会议吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      ).then(() => {
        leaveRoom()
        router.push('/dashboard')
      }).catch(() => {})
    }
    
    // 离开房间
    const leaveRoom = () => {
      // 通知服务器
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify({
          type: 'leave',
          roomId: roomId.value
        }))
      }
      
      // 关闭所有连接
      Object.values(rtcPeerConnections).forEach(pc => pc.close())
      rtcPeerConnections = {}
      
      // 停止所有媒体流
      if (localStream) {
        localStream.getTracks().forEach(track => track.stop())
        localStream = null
      }
      
      if (screenStream) {
        screenStream.getTracks().forEach(track => track.stop())
        screenStream = null
      }
      
      // 关闭WebSocket连接
      if (socket) {
        socket.close()
        socket = null
      }
    }
    
    // 窗口关闭前处理
    const handleBeforeUnload = () => {
      leaveRoom()
    }
    
    return {
      roomId,
      localPeerId,
      peers,
      videoRefs,
      audioEnabled,
      videoEnabled,
      isScreenSharing,
      chatMessages,
      chatInput,
      toggleAudio,
      toggleVideo,
      toggleScreenSharing,
      sendChatMessage,
      formatTime,
      copyRoomLink,
      leaveConference
    }
  }
}
</script>

<style scoped>
.video-conference-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #f5f7fa;
}

.conference-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
}

.room-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.conference-main {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.video-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  grid-auto-rows: 1fr;
  gap: 10px;
  padding: 10px;
  overflow: auto;
}

.video-container {
  position: relative;
  background-color: #000;
  border-radius: 8px;
  overflow: hidden;
}

.video-container video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.peer-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: rgba(0, 0, 0, 0.5);
  color: #fff;
}

.peer-status {
  display: flex;
  gap: 8px;
}

.conference-sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  border-left: 1px solid #e6e6e6;
  background-color: #fff;
}

.participants-list {
  padding: 15px;
  border-bottom: 1px solid #e6e6e6;
}

.participants-list h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.participants-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.participants-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.participants-list li.is-local {
  font-weight: bold;
}

.peer-controls {
  display: flex;
  gap: 8px;
}

.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 15px;
}

.chat-panel h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px 0;
}

.chat-message {
  margin-bottom: 10px;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #f0f0f0;
  max-width: 80%;
}

.chat-message.is-local {
  background-color: #e1f3ff;
  margin-left: auto;
}

.message-sender {
  font-weight: bold;
  margin-bottom: 4px;
}

.message-time {
  font-size: 12px;
  color: #999;
  text-align: right;
  margin-top: 4px;
}

.chat-input {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.conference-controls {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 15px;
  background-color: #fff;
  border-top: 1px solid #e6e6e6;
}

.conference-controls .el-button {
  width: 50px;
  height: 50px;
}
</style>