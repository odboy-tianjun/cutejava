package cn.odboy.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * Json工具
 *
 * @author odboy
 * @date 2024-10-01
 */
public class JsonUtil {
    public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> kClass, Class<V> vClass) {
        return JSON.parseObject(jsonStr, new TypeReference<Map<K, V>>() {
        });
    }

    public static <V> List<V> toList(String jsonStr, Class<V> vClass) {
        return JSON.parseObject(jsonStr, new TypeReference<List<V>>() {
        });
    }

    public static void main(String[] args) {
        String mapStr = "{\"key1\":\"value1\", \"key2\":\"value2\"}";
        System.err.println(JsonUtil.toMap(mapStr, String.class, String.class));
        String listStr = "[\"value1\", \"value2\"]";
        System.err.println(JsonUtil.toList(listStr, String.class));
    }
}
