package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/10/26.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskPublish/updatePublishStatus",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class UpdateMissionStateParams extends RequestParams {
    public String id;
    public String execute_start_time;
    public String execute_end_time;
    public int status;

    public UpdateMissionStateParams(){}
}
