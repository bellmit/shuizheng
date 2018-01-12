package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2017-5-2.
 */
@HttpRequest(
        host = "https://api.seniverse.com",
        path = "v3/weather/now.json",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class GetTemperatureParams extends RequestParams {

    public String key;
    public String location;
    public String language = "zh-Hans";
    public int start = 0;
    public int hours = 1;
}
