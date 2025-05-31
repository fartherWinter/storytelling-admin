<template>
  <div class="partners-container">
    <div class="page-header">
      <h2>合作伙伴管理</h2>
      <p>管理供应链合作伙伴信息</p>
    </div>
    
    <div class="content-area">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>合作伙伴列表</span>
            <el-button type="primary" @click="addPartner">
              <el-icon><Plus /></el-icon>
              添加合作伙伴
            </el-button>
          </div>
        </template>
        
        <el-table :data="partners" style="width: 100%">
          <el-table-column prop="name" label="合作伙伴名称" width="200" />
          <el-table-column prop="type" label="类型" width="120" />
          <el-table-column prop="contact" label="联系人" width="120" />
          <el-table-column prop="phone" label="联系电话" width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '活跃' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="editPartner(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deletePartner(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const partners = ref([
  {
    id: 1,
    name: '供应商A',
    type: '原材料供应商',
    contact: '张三',
    phone: '13800138001',
    status: '活跃'
  },
  {
    id: 2,
    name: '物流公司B',
    type: '物流服务商',
    contact: '李四',
    phone: '13800138002',
    status: '活跃'
  },
  {
    id: 3,
    name: '分销商C',
    type: '销售渠道',
    contact: '王五',
    phone: '13800138003',
    status: '暂停'
  }
])

const addPartner = () => {
  ElMessage.info('添加合作伙伴功能开发中...')
}

const editPartner = (partner) => {
  ElMessage.info(`编辑合作伙伴: ${partner.name}`)
}

const deletePartner = async (partner) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除合作伙伴 "${partner.name}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const index = partners.value.findIndex(p => p.id === partner.id)
    if (index > -1) {
      partners.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch {
    ElMessage.info('已取消删除')
  }
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.partners-container {
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-area {
  background: #f5f5f5;
  min-height: calc(100vh - 200px);
}
</style>