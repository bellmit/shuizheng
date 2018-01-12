package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2017-3-30.
 */

@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "app/getwxuser",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class GetMissionCanSelectMemberParams extends RequestParams {

    public String lx = "getwxuser";
    public String dept_id;
    public String task_id;
    public String dzid;//登录人id
}
