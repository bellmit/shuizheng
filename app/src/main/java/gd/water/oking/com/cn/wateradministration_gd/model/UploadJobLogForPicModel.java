package gd.water.oking.com.cn.wateradministration_gd.model;

import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

/**
 * Created by Administrator on 2018/2/2.
 */

public class UploadJobLogForPicModel {
    private OkingCallBack.UploadJobLogForPic mUploadJobLogForPic;
    private MissionLog mLog;
    private Uri mUri;
    private SimpleDateFormat mSdf;
    private  SharedPreferences mSp;

    public UploadJobLogForPicModel(SharedPreferences sp,MissionLog log, Uri uri, SimpleDateFormat sdf, OkingCallBack.UploadJobLogForPic uploadJobLogForPic) {
        mLog = log;
        mUri = uri;
        mSdf = sdf;
        mUploadJobLogForPic = uploadJobLogForPic;
         mSp= sp;
    }

    public void uploadJobLogForPic() {
        //上传图片

        RequestParams upPicParams = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/mobile/uploadfile");
        // 使用multipart表单上传文件
        upPicParams.setMultipart(true);
        // 加到url里的参数, http://xxxx/s?wd=xUtils
        upPicParams.addQueryStringParameter("logId", mLog.getId());
        upPicParams.addQueryStringParameter("type", "0");
        upPicParams.addQueryStringParameter("smallImg", "");
        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.


        File file = new File(FileUtil.PraseUritoPath(MyApp.getApplictaion(), mUri));
        upPicParams.addBodyParameter(
                "files", file, null); // 如果文件没有扩展名, 最好设置contentType参数.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = file.getName().split("\\.")[0];
        String  ext;
        try {
            Point location = new Point();
            String locationstr = mSp.getString(filename, "");
            if (!TextUtils.isEmpty(locationstr)) {
                String[] split = locationstr.split(",");
                if(!split[0].equals("null")){

                    location.setLatitude(Double.parseDouble(split[0]));
                    location.setLongitude(Double.parseDouble(split[1]));
                    location.setDatetime(sdf.parse(filename).getTime());
                }

                ext = new Gson().toJson(location);
            }else {
                    Map<String, String> map = new HashMap<>();
                    map.put("datetime", mSdf.format(sdf.parse(filename)));
                  ext = new Gson().toJson(map);
            }

//            Point location = FileUtil.getLastLocationFromFile(sdf.parse(filename).getTime(), 3 * 60 * 1000);


            upPicParams.addBodyParameter("ext", ext);
        } catch (ParseException e) {
            e.printStackTrace();
            mUploadJobLogForPic.uploadPositionFail(e);

        }

        x.http().post(upPicParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mUploadJobLogForPic.uploadSucc(result);


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mUploadJobLogForPic.uploadFail(ex);

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
