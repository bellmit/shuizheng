package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/10/27.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskLog/getTaskLogDatas",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class GetMissionRecordParams extends RequestParams {

    public String nonsort = "-1";
    public String limit;
    public String offset;
    public String task_id;
    public String name;
    public String userId;
    public String beginTime,endTime;

    public GetMissionRecordParams() {
    }
}
