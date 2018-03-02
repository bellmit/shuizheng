package gd.water.oking.com.cn.wateradministration_gd.model;

import android.content.Context;
import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.vondear.rxtools.RxDeviceUtils;

import org.xutils.common.Callback;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import gd.water.oking.com.cn.wateradministration_gd.bean.Contants;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.SetLocationParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2018/2/8.
 */

public class UploadLocationToServerModel {
    private OkingCallBack.UploadLocationToServer mUploadLocationToServer;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public UploadLocationToServerModel(OkingCallBack.UploadLocationToServer uploadLocationToServer) {
        mUploadLocationToServer = uploadLocationToServer;
    }

    public void upploadLocationToServer(){

            SetLocationParams params = new SetLocationParams();
            params.USER_ID = DefaultContants.CURRENTUSER.getUserId();
            params.DEV_ID = RxDeviceUtils.getIMEI(MyApp.getApplictaion());
            Point mLocation = new Point();
            mLocation.setLatitude(Double.parseDouble(Contants.LOCATIONRESULT[1]));
            mLocation.setLongitude(Double.parseDouble(Contants.LOCATIONRESULT[2]));

            try {
                mLocation.setDatetime(sdf.parse(Contants.LOCATIONRESULT[3]).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            params.COORDINATE = new Gson().toJson(mLocation);
            params.TIME = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
            params.LOGINTIME = MyApp.getApplictaion().getSharedPreferences("logintime", Context.MODE_PRIVATE).getLong("logintime", 0);

            Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mUploadLocationToServer.uploadSucc(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    mUploadLocationToServer.uploadFail(ex);
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
