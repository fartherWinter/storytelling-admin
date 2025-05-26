/**
 * 视频会议路由配置
 */
import Layout from '@/layout/index.vue'

export default [
  {
    path: '/conference',
    component: Layout,
    redirect: '/conference/create',
    name: 'Conference',
    meta: { title: '视频会议', icon: 'video-camera' },
    children: [
      {
        path: 'create',
        name: 'CreateConference',
        component: () => import('@/views/conference/CreateConference.vue'),
        meta: { title: '创建会议', icon: 'plus' }
      },
      {
        path: ':roomId',
        name: 'VideoConference',
        component: () => import('@/views/conference/VideoConference.vue'),
        meta: { title: '视频会议', activeMenu: '/conference/create' },
        props: true,
        hidden: true
      }
    ]
  }
]