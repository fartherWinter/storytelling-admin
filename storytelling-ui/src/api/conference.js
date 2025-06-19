import request from '@/utils/request'

// ========================= 会议管理 API =========================

/**
 * 获取会议列表（分页）
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getConferenceList(params) {
  return request({
    url: '/conference/list',
    method: 'get',
    params
  })
}

/**
 * 获取会议详情
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function getConferenceById(id) {
  return request({
    url: `/conference/${id}`,
    method: 'get'
  })
}

/**
 * 创建会议
 * @param {Object} data 会议信息
 * @returns {Promise}
 */
export function createConference(data) {
  return request({
    url: '/conference',
    method: 'post',
    data
  })
}

/**
 * 更新会议
 * @param {Number} id 会议ID
 * @param {Object} data 会议信息
 * @returns {Promise}
 */
export function updateConference(id, data) {
  return request({
    url: `/conference/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除会议
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function deleteConference(id) {
  return request({
    url: `/conference/${id}`,
    method: 'delete'
  })
}

/**
 * 取消会议
 * @param {Number} id 会议ID
 * @param {String} reason 取消原因
 * @returns {Promise}
 */
export function cancelConference(id, reason) {
  return request({
    url: `/conference/${id}/cancel`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 开始会议
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function startConference(id) {
  return request({
    url: `/conference/${id}/start`,
    method: 'post'
  })
}

/**
 * 结束会议
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function endConference(id) {
  return request({
    url: `/conference/${id}/end`,
    method: 'post'
  })
}

/**
 * 获取会议参与者列表
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function getConferenceParticipants(id) {
  return request({
    url: `/conference/${id}/participants`,
    method: 'get'
  })
}

/**
 * 添加会议参与者
 * @param {Number} id 会议ID
 * @param {Array} userIds 用户ID数组
 * @returns {Promise}
 */
export function addConferenceParticipants(id, userIds) {
  return request({
    url: `/conference/${id}/participants`,
    method: 'post',
    data: { userIds }
  })
}

/**
 * 移除会议参与者
 * @param {Number} id 会议ID
 * @param {Number} userId 用户ID
 * @returns {Promise}
 */
export function removeConferenceParticipant(id, userId) {
  return request({
    url: `/conference/${id}/participants/${userId}`,
    method: 'delete'
  })
}

/**
 * 确认参会
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function confirmAttendance(id) {
  return request({
    url: `/conference/${id}/confirm`,
    method: 'post'
  })
}

/**
 * 拒绝参会
 * @param {Number} id 会议ID
 * @param {String} reason 拒绝原因
 * @returns {Promise}
 */
export function declineAttendance(id, reason) {
  return request({
    url: `/conference/${id}/decline`,
    method: 'post',
    data: { reason }
  })
}

/**
 * 获取我的会议列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getMyConferences(params) {
  return request({
    url: '/conference/my/list',
    method: 'get',
    params
  })
}

/**
 * 获取今日会议
 * @returns {Promise}
 */
export function getTodayConferences() {
  return request({
    url: '/conference/today',
    method: 'get'
  })
}

/**
 * 获取即将开始的会议
 * @returns {Promise}
 */
export function getUpcomingConferences() {
  return request({
    url: '/conference/upcoming',
    method: 'get'
  })
}

/**
 * 获取会议室列表
 * @returns {Promise}
 */
export function getConferenceRooms() {
  return request({
    url: '/conference/rooms',
    method: 'get'
  })
}

/**
 * 检查会议室可用性
 * @param {Number} roomId 会议室ID
 * @param {String} startTime 开始时间
 * @param {String} endTime 结束时间
 * @returns {Promise}
 */
export function checkRoomAvailability(roomId, startTime, endTime) {
  return request({
    url: `/conference/rooms/${roomId}/availability`,
    method: 'get',
    params: { startTime, endTime }
  })
}

/**
 * 预订会议室
 * @param {Number} roomId 会议室ID
 * @param {Object} data 预订信息
 * @returns {Promise}
 */
export function bookConferenceRoom(roomId, data) {
  return request({
    url: `/conference/rooms/${roomId}/book`,
    method: 'post',
    data
  })
}

/**
 * 取消会议室预订
 * @param {Number} roomId 会议室ID
 * @param {Number} bookingId 预订ID
 * @returns {Promise}
 */
export function cancelRoomBooking(roomId, bookingId) {
  return request({
    url: `/conference/rooms/${roomId}/bookings/${bookingId}/cancel`,
    method: 'post'
  })
}

/**
 * 获取会议纪要
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function getConferenceMinutes(id) {
  return request({
    url: `/conference/${id}/minutes`,
    method: 'get'
  })
}

/**
 * 保存会议纪要
 * @param {Number} id 会议ID
 * @param {Object} data 会议纪要
 * @returns {Promise}
 */
export function saveConferenceMinutes(id, data) {
  return request({
    url: `/conference/${id}/minutes`,
    method: 'post',
    data
  })
}

/**
 * 获取会议录音/录像
 * @param {Number} id 会议ID
 * @returns {Promise}
 */
export function getConferenceRecordings(id) {
  return request({
    url: `/conference/${id}/recordings`,
    method: 'get'
  })
}

/**
 * 上传会议录音/录像
 * @param {Number} id 会议ID
 * @param {FormData} formData 文件数据
 * @returns {Promise}
 */
export function uploadConferenceRecording(id, formData) {
  return request({
    url: `/conference/${id}/recordings`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 发送会议邀请
 * @param {Number} id 会议ID
 * @param {Array} emails 邮箱地址数组
 * @returns {Promise}
 */
export function sendConferenceInvitation(id, emails) {
  return request({
    url: `/conference/${id}/invite`,
    method: 'post',
    data: { emails }
  })
}

/**
 * 获取会议统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getConferenceStatistics(params) {
  return request({
    url: '/conference/statistics',
    method: 'get',
    params
  })
}

/**
 * 导出会议列表
 * @param {Object} params 导出参数
 * @returns {Promise}
 */
export function exportConferenceList(params) {
  return request({
    url: '/conference/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

/**
 * 批量删除会议
 * @param {Array} ids 会议ID数组
 * @returns {Promise}
 */
export function batchDeleteConferences(ids) {
  return request({
    url: '/conference/batch/delete',
    method: 'post',
    data: { ids }
  })
}

/**
 * 获取会议模板列表
 * @returns {Promise}
 */
export function getConferenceTemplates() {
  return request({
    url: '/conference/templates',
    method: 'get'
  })
}

/**
 * 根据模板创建会议
 * @param {Number} templateId 模板ID
 * @param {Object} data 会议数据
 * @returns {Promise}
 */
export function createConferenceFromTemplate(templateId, data) {
  return request({
    url: `/conference/template/${templateId}/create`,
    method: 'post',
    data
  })
}

/**
 * 设置会议提醒
 * @param {Number} id 会议ID
 * @param {Object} data 提醒设置
 * @returns {Promise}
 */
export function setConferenceReminder(id, data) {
  return request({
    url: `/conference/${id}/reminder`,
    method: 'post',
    data
  })
}

/**
 * 获取会议日历
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getConferenceCalendar(params) {
  return request({
    url: '/conference/calendar',
    method: 'get',
    params
  })
}

/**
 * 同步到外部日历
 * @param {Number} id 会议ID
 * @param {String} calendarType 日历类型（google/outlook/apple）
 * @returns {Promise}
 */
export function syncToExternalCalendar(id, calendarType) {
  return request({
    url: `/conference/${id}/sync/${calendarType}`,
    method: 'post'
  })
}