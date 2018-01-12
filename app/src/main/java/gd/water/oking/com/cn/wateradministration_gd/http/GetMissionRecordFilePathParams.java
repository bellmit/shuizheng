package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/11/10.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskLog/mobile/getImagePath",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class GetMissionRecordFilePathParams extends RequestParams {

    public String log_id;
    public String user_id;
    public int type;
    public String ext;

    public GetMissionRecordFilePathParams() {
    }
}
