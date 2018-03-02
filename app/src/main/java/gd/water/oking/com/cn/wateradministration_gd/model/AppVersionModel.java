package gd.water.oking.com.cn.wateradministration_gd.model;


import com.vondear.rxtools.RxDeviceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2018/2/1.
 */

public class AppVersionModel {
    private OkingCallBack.AppVersionCallBck mAppVersionCallBck;

    public AppVersionModel(OkingCallBack.AppVersionCallBck appVersionCallBck) {
        mAppVersionCallBck = appVersionCallBck;
    }

    public void reqAppVersion(){
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/cs/cscx");
        params.addBodyParameter("lx", "app_version");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String value = jsonObject.getString("VALUE");
                        String bz = jsonObject.getString("BZ");
                        if (!value.equals(RxDeviceUtils.getAppVersionName(MyApp.getApplictaion()))) {

                            mAppVersionCallBck.reqSucc(bz);


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mAppVersionCallBck.reqFail(e);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mAppVersionCallBck.reqFail(ex);

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
