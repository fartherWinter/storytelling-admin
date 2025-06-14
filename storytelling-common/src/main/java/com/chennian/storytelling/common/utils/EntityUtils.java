package com.chennian.storytelling.common.utils;

import com.chennian.storytelling.common.constant.SystemConstants;

import java.util.Date;
import java.util.UUID;

/**
 * 实体工具类 - 统一处理实体字段的默认值设置
 *
 * @author by chennian
 * @date 2024/01/01
 */
public class EntityUtils {
    
    /**
     * 获取当前用户ID
     * 优先从SecurityUtils获取，如果获取失败则返回默认管理员用户
     */
    public static String getCurrentUserId() {
        try {
            // 尝试从SecurityUtils获取当前用户
            // 这里需要根据实际的SecurityUtils实现来调整
            // Long userId = SecurityUtils.getUserId();
            // return userId != null ? userId.toString() : SystemConstants.DEFAULT_ADMIN_USER;
            
            // 暂时返回默认管理员用户，后续可以根据实际情况调整
            return SystemConstants.DEFAULT_ADMIN_USER;
        } catch (Exception e) {
            // 如果获取失败，返回默认管理员用户
            return SystemConstants.DEFAULT_ADMIN_USER;
        }
    }
    
    /**
     * 获取当前用户名
     * 优先从SecurityUtils获取，如果获取失败则返回默认管理员用户
     */
    public static String getCurrentUserName() {
        try {
            // 尝试从SecurityUtils获取当前用户名
            // String userName = SecurityUtils.getUserName();
            // return StringUtils.isNotEmpty(userName) ? userName : SystemConstants.DEFAULT_ADMIN_USER;
            
            // 暂时返回默认管理员用户，后续可以根据实际情况调整
            return SystemConstants.DEFAULT_ADMIN_USER;
        } catch (Exception e) {
            // 如果获取失败，返回默认管理员用户
            return SystemConstants.DEFAULT_ADMIN_USER;
        }
    }
    
    /**
     * 生成默认ID
     * @param prefix ID前缀
     * @return 生成的ID
     */
    public static String generateDefaultId(String prefix) {
        return prefix + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 获取当前时间
     * @return 当前时间
     */
    public static Date getCurrentTime() {
        return new Date();
    }
    
    /**
     * 设置创建信息
     * 用于新增实体时设置创建时间和创建人
     */
    public static void setCreateInfo(Object entity) {
        if (entity == null) {
            return;
        }
        
        Date currentTime = getCurrentTime();
        String currentUser = getCurrentUserName();
        
        try {
            // 使用反射设置创建时间
            if (hasMethod(entity, "setCreatedTime")) {
                entity.getClass().getMethod("setCreatedTime", Date.class).invoke(entity, currentTime);
            }
            
            // 使用反射设置创建人
            if (hasMethod(entity, "setCreatedBy")) {
                entity.getClass().getMethod("setCreatedBy", String.class).invoke(entity, currentUser);
            }
            
            // 同时设置更新时间和更新人
            setUpdateInfo(entity);
            
        } catch (Exception e) {
            // 忽略反射异常，不影响主要业务逻辑
        }
    }
    
    /**
     * 设置更新信息
     * 用于更新实体时设置更新时间和更新人
     */
    public static void setUpdateInfo(Object entity) {
        if (entity == null) {
            return;
        }
        
        Date currentTime = getCurrentTime();
        String currentUser = getCurrentUserName();
        
        try {
            // 使用反射设置更新时间
            if (hasMethod(entity, "setUpdatedTime")) {
                entity.getClass().getMethod("setUpdatedTime", Date.class).invoke(entity, currentTime);
            }
            
            // 使用反射设置更新人
            if (hasMethod(entity, "setUpdatedBy")) {
                entity.getClass().getMethod("setUpdatedBy", String.class).invoke(entity, currentUser);
            }
            
        } catch (Exception e) {
            // 忽略反射异常，不影响主要业务逻辑
        }
    }
    
    /**
     * 检查对象是否有指定的方法
     */
    private static boolean hasMethod(Object obj, String methodName) {
        try {
            if (methodName.equals("setCreatedTime") || methodName.equals("setUpdatedTime")) {
                obj.getClass().getMethod(methodName, Date.class);
            } else if (methodName.equals("setCreatedBy") || methodName.equals("setUpdatedBy")) {
                obj.getClass().getMethod(methodName, String.class);
            }
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}