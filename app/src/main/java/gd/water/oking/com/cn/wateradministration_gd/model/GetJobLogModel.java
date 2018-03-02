package gd.water.oking.com.cn.wateradministration_gd.model;

import org.xutils.common.Callback;
import org.xutils.x;

import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionRecordParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;

/**
 * Created by Administrator on 2018/2/3.
 */

public class GetJobLogModel {
    private OkingCallBack.GetJobLog mGetJobLog;
    private Mission mMission;

    public GetJobLogModel(Mission mission, OkingCallBack.GetJobLog getJobLog) {
        mMission = mission;
        mGetJobLog = getJobLog;
    }

    public void getJobLog(){
        //先从服务器获取任务日志
        GetMissionRecordParams params = new GetMissionRecordParams();
        params.task_id = mMission.getId();

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                mGetJobLog.getSucc(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mGetJobLog.getFail(ex);
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
