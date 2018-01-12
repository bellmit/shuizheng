package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/11/23.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "publicController/addUserSite",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class SetLocationParams extends RequestParams {

    public   long LOGINTIME ;
    public String USER_ID;
    public String DEV_ID;
    public String COORDINATE;
    public String TIME;

    public SetLocationParams() {
    }
}
