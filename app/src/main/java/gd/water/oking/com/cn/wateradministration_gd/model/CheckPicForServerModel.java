package gd.water.oking.com.cn.wateradministration_gd.model;

import org.xutils.common.Callback;
import org.xutils.x;

import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionRecordFilePathParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;

/**
 * Created by Administrator on 2018/2/2.
 */

public class CheckPicForServerModel {
    private OkingCallBack.CheckPicForServer mCheckPicForServer;
    private MissionLog mLog;
    public CheckPicForServerModel(MissionLog log, OkingCallBack.CheckPicForServer checkPicForServer) {
        mLog = log;
        mCheckPicForServer = checkPicForServer;
    }
    public void checkedPicForServer(){
        //搜索服务器已存在的图片
        final GetMissionRecordFilePathParams params = new GetMissionRecordFilePathParams();
        params.log_id = mLog.getId();
        params.type = 0;//日志照片
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mCheckPicForServer.checkedSucc(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mCheckPicForServer.checkedFail(ex);

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
