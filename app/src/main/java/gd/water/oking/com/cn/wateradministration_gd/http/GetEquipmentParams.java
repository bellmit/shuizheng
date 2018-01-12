package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/11/24.
 */

@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "equiPmentController/getSelectorForAndroid",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class GetEquipmentParams extends RequestParams {

    public String USER_ID;
    public String TYPE;
    public String VALUE;
    public String REMARKS;
    public String LY;

    public GetEquipmentParams() {
    }
}
