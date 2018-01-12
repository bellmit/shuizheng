package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2017-4-14.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "app/recordSave",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class SaveSurveyRecordQuestionParams extends RequestParams {

    public String dcblnrid;
    public String ajid;
    public String wsid;
    public String wt;
    public String hd;
    public String sx;

    public SaveSurveyRecordQuestionParams() {
    }
}
