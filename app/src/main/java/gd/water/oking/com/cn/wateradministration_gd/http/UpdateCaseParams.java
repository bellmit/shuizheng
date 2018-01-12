package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2016-12-20.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "caseController/updCaseByParameter",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class UpdateCaseParams extends RequestParams {

    public String option = "upd";

    public String id;
    public String ajlx;
    public String wfsx;
    public Long fxsj;
    public String zb;
    public String fxr;
    public String zt;
    public String ajly;
    public String zsd;
    public String jgd;
    public String ssd;
    public String whjgfsd;
    public String dwid;
    public String grid;
    public String aqjy;
    public String cbryj;
    public String szjcdwfzryj;
    public String fzjgfzryj;
    public String zgldyj;
    public String spjd;
}
