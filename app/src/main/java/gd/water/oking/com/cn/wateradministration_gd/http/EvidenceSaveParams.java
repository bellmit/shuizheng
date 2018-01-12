package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2017-4-3.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "app/caseEvidenceSave",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class EvidenceSaveParams extends RequestParams {

    public String zjid;
    public String ajid;
    public String zjlx;
    public String zjmc;
    public String zjly;
    public String zjnr;
    public String sl;
    public String cjsj;
    public String cjr;
    public String cjdd;
    public String jzr;
    public String dw;
    public String bz;
    public String scr;
    public String scsj;
    public String zt;
    public String wsid;
    public String lxmc;
    public String zjlymc;
    public String lrsj;
    public String ys;

    public EvidenceSaveParams() {
    }
}
