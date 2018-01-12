package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/10/26.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskPublish/getTaskPublishDatas",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class GetMissionListParams extends RequestParams {
    public String nonsort = "-1";
    public String id;
    public String assignment;
    public String receiver;
    public String task_name;
    public String publisher;
    public String beginTime,endTime;
    public int classify;

    public GetMissionListParams(){
      //  super(DefaultContants.ServerHost+"/taskPublish/getTaskPublishDatas");

    }
}
