package gd.water.oking.com.cn.wateradministration_gd.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wyouflf on 15/11/5.
 */
public class JsonResponseParser implements ResponseParser {// 如果实现 InputStreamResponseParser, 可实现自定义流数据转换.

    @Override
    public void checkResponse(UriRequest request) throws Throwable {
        // custom check ?
        // get headers ?
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        // TODO: json to java bean
        if (resultClass == List.class) {
            // 这里只是个示例, 不做json转换.
            List<MapResponse> list = new ArrayList<MapResponse>();
             java.lang.reflect.Type type = new TypeToken<List<Map<String,Object>>>() {
            }.getType();
            try {
                Log.d("xutils",result);
                List<Map<String,Object>> lis  = new Gson().fromJson(result, type);
               for (Map<String,Object> map:lis) {
                   MapResponse response = new MapResponse();
                   response.setMap(map);
                   list.add(response);

               }

            }catch (Exception e){
                e.printStackTrace();
            }
            return list;
         } else {
            // 这里只是个示例, 不做json转换.
            MapResponse response = new MapResponse();
            java.lang.reflect.Type type = new TypeToken<Map<String,Object>>() {
            }.getType();
            try {
                Log.d("xutils",result);
                Map map = new Gson().fromJson(result, type);
                response.setMap(map);
            }catch (Exception e){
                e.printStackTrace();
            }

             return response;
         }

    }
}
