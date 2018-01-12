package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.annotation.HttpResponse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wyouflf on 15/11/5.
 * json 返回值示例, 如果它作为Callback的泛型,
 * 那么xUtils将自动调用JsonResponseParser将字符串转换为BaiduResponse.
 *
 * @HttpResponse 注解 和 ResponseParser接口仅适合做json, xml等文本类型数据的解析,
 * 如果需要其他类型的解析可参考:
 * {@link org.xutils.http.loader.LoaderFactory}
 * 和 {@link org.xutils.common.Callback.PrepareCallback}.
 * LoaderFactory提供PrepareCallback第一个泛型参数类型的自动转换,
 * 第二个泛型参数需要在prepare方法中实现.
 * (LoaderFactory中已经默认提供了部分常用类型的转换实现, 其他类型需要自己注册.)
 */
@HttpResponse(parser = JsonResponseParser.class)
public class MapResponse  {
    // some properties

    private Map<String, Object> map=new HashMap<>();


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "MapResponse{" +
                "map=" + map +
                '}';
    }

     public int size() {
        return map.size();
    }

     public boolean isEmpty() {
        if (map == null || map.size() <= 0)
            return true;
        else
            return false;
    }

     public boolean containsKey(String key) {
        return map.containsKey(key);
    }

     public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

     public Object get(String key) {
        return map.get(key);
    }

     public Object put(String key, Object value) {
        return map.put(key,value);
    }

     public Object remove(Object key) {
        return map.remove(key);
    }

     public void putAll(Map m) {
    m.putAll(m);
    }

     public void clear() {
        map.clear();
    }

     public Set keySet() {
        return map.keySet();
    }

     public Collection values() {
        return map.values();
    }

     public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
