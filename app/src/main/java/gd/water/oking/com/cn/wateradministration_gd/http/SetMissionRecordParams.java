package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016/10/27.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "taskLog/addAndupdateTaskLogDatas",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class SetMissionRecordParams extends RequestParams {

    public int mode;
    public String id;
    public String task_id;
    public String name;
    public String post;
    public String id_card;
    public String other_part;
    public int other_person;
    public String weather;
    public String equipment;
    public String addr;
    public String time;
    public int plan;
    public int item;
    public int type;
    public String area;
    public String route;
    public String patrol;
    public int status;
    public String deal;
    public String result;
    public int examine_status;
    public String dzyj;
    public String tbr;
    public String tbrid;
    public String whetherComplete;

    public SetMissionRecordParams() {
    }
}
