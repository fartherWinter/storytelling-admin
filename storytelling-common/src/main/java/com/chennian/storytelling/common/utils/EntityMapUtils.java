package com.chennian.storytelling.common.utils;



import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体转换工具类
 *
 * @author by chennian
 * @date 2023/11/13 14:28
 */
public class EntityMapUtils {
    /**
     * 将实体类转换为Map
     *
     * @param obj 实体类对象
     * @return Map
     */
    public static Map<String, Object> entityToMap(Object obj) {
        Map<String, Object> map = new HashMap<>(16);
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return removeMapKey(map);
    }

    /**
     * 将Map转换为实体类
     *
     * @param map   Map对象
     * @param clazz 实体类的Class对象
     * @return 实体类对象
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 去除所有value为空的项
     *
     * @param param
     * @return
     */
    public static Map<String, Object> removeMapKey(Map param) {
        Set set = param.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            Object value = (Object) param.get(obj);
            if (value == null || value.equals("") || value.equals("null") || obj.toString().length() == 0) {
                iterator.remove();
            }
        }
        return param;
    }

    /**
     * 参数字典排序
     */
    private static String sortParam(Map<String, Object> params) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        String ret = "";
        for (Map.Entry<String, Object> entry : infoIds) {
            ret += entry.getKey();
            ret += "=";
            ret += entry.getValue();
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * 获取param格式的请求后缀
     */
    public static String getParam(Map<String, Object> params) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        String ret = "?";
        for (Map.Entry<String, Object> entry : infoIds) {
            ret += entry.getKey();
            ret += "=";
            ret += entry.getValue();
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }




    public static String getSignWanBang(Map<String, Object> params) {
        String stringA = sortParam(params);
        return stringA;
    }
}
