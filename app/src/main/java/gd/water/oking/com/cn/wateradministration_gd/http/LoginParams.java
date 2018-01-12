package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;


@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "login/loginByPad",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
    public class LoginParams extends RequestParams {
    public String account;
    public String password;


    public LoginParams() {

        // this.setMultipart(true); // 使用multipart表单
        // this.setAsJsonContent(true); // 请求body将参数转换为json形式发送
    }

    //public long timestamp = System.currentTimeMillis();
    //public File uploadFile; // 上传文件
    //public List<File> files; // 上传文件数组
}