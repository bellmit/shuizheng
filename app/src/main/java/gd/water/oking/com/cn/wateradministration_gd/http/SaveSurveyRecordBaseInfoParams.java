package gd.water.oking.com.cn.wateradministration_gd.http;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by zhao on 2017-4-12.
 */
@HttpRequest(
        host = DefaultContants.SERVER_HOST,
        path = "app/investigationSave",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)

public class SaveSurveyRecordBaseInfoParams extends RequestParams {

    public String xh;
    public String ajid;
    public String wsid;
    public String bdcr;
    public int xb = 0;
    public String nl;
    public String lxdh;
    public String sfzhm;
    public String zw;
    public String bdcdwmc;
    public String frmc;
    public String frzw;
    public String dz;
    public String xdz;
    public String dcsj;
    public String ks;
    public String js;
    public String sj1;
    public String sj2;
    public String dcdd;
    public String dcry1;
    public String dw1;
    public String zfzh1;
    public String dcry2;
    public String dw2;
    public String zfzh2;
    public String jlr;
    public String jlrdw;
    public String dcry1userid;
    public String dcry2userid;
    public String jlruserid;
    public String ay;

    public SaveSurveyRecordBaseInfoParams() {
    }
}
