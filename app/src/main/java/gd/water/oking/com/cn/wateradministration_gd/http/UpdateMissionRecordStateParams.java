package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/10/27.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskLog/updateTaskLogStatus",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class UpdateMissionRecordStateParams extends RequestParams {

    public String id;
    public byte status;

    public UpdateMissionRecordStateParams() {
    }
}
