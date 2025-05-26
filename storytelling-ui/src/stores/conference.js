import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 视频会议状态管理
 */
export const useConferenceStore = defineStore('conference', () => {
  // 状态
  const activeRooms = ref([])
  const currentRoomId = ref(null)
  const conferenceSettings = ref({
    defaultAudioEnabled: true,
    defaultVideoEnabled: true,
    preferredResolution: 'hd', // sd, hd, fullHd
    notificationSound: true,
    autoJoinAudio: true,
    echoCancellation: true,
    noiseSuppression: true,
    autoGainControl: true
  })

  // 计算属性
  const currentRoom = computed(() => {
    return activeRooms.value.find(room => room.id === currentRoomId.value) || null
  })

  // 动作
  function createRoom(roomData) {
    const room = {
      id: roomData.id || `room-${Date.now()}`,
      name: roomData.name || '新会议',
      createdAt: new Date(),
      participants: [],
      isLocked: false,
      ...roomData
    }
    
    activeRooms.value.push(room)
    return room.id
  }

  function joinRoom(roomId) {
    currentRoomId.value = roomId
    return currentRoom.value
  }

  function leaveRoom() {
    currentRoomId.value = null
  }

  function updateRoomParticipants(roomId, participants) {
    const room = activeRooms.value.find(r => r.id === roomId)
    if (room) {
      room.participants = participants
    }
  }

  function updateSettings(settings) {
    conferenceSettings.value = {
      ...conferenceSettings.value,
      ...settings
    }
  }

  return {
    // 状态
    activeRooms,
    currentRoomId,
    conferenceSettings,
    // 计算属性
    currentRoom,
    // 动作
    createRoom,
    joinRoom,
    leaveRoom,
    updateRoomParticipants,
    updateSettings
  }
})