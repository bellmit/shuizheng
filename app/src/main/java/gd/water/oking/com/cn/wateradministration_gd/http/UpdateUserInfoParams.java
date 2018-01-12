package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016-12-12.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "publicController/updUsers",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class UpdateUserInfoParams extends RequestParams {

    public String userid;
    public String oldPassword;
    public String account;
    public String password;
    public String phone;
    public byte[] headimg;

    public UpdateUserInfoParams() {

    }
}
