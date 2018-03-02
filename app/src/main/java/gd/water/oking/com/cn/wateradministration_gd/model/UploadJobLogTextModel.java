package gd.water.oking.com.cn.wateradministration_gd.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.RxFileUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.SetMissionRecordParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/2.
 */

public class UploadJobLogTextModel {
    private String mLogResult;
    private MissionLog mLog;
    private Mission mMission;
    private String mTime;
    private boolean mSwisopen;
    private int mSelePlanPos;
    private int mSeleMattersPos;
    private String mArea;
    private String mSummary;
    private String mLeaderSummary;
    private String mParts;
    private String mEquipment;
    private OkingCallBack.UploadJobLogForText mUploadJobLog;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private int mDatePoor;
    private long mBeforTime;

    public UploadJobLogTextModel(String logResult, MissionLog log, Mission mission, String time, boolean swisopen, int selePlanPos, int seleMattersPos, String summary, String area, String leaderSummary, String parts, String equipment, OkingCallBack.UploadJobLogForText uploadJobLog) {
        mLogResult = logResult;
        mLog = log;
        mMission = mission;
        mTime = time;
        mSwisopen = swisopen;
        mSelePlanPos = selePlanPos;
        mSeleMattersPos = seleMattersPos;
        mArea = area;
        mSummary = summary;
        mUploadJobLog = uploadJobLog;
        mParts = parts;
        mEquipment = equipment;
        mLeaderSummary = leaderSummary;
    }

    /**
     * 上传任务日志
     */
    public void uploadJobLogForText() {
        final SetMissionRecordParams params = new SetMissionRecordParams();
        JSONObject object = null;
        try {
            object = new JSONObject(mLogResult);
            int count = object.getInt("total");
            if (count > 0) {
                MissionLog oldLog = DataUtil.praseJson(object.getJSONArray("rows").getJSONObject(0).toString(),
                        new TypeToken<MissionLog>() {
                        });
                params.mode = 1;
                mLog.setId(oldLog.getId());
                params.id = mLog.getId();
            } else {
                params.mode = 0;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.task_id = mMission.getId();
        params.name = DefaultContants.CURRENTUSER.getUserId();
        params.time = mTime;
        params.plan = mSelePlanPos;
        params.item = mSeleMattersPos;
        params.type = mMission.getTask_type();
        params.area = mArea;

        if (mSwisopen) {
            params.whetherComplete = "0";
        } else {
            params.whetherComplete = "1";
        }
        params.patrol = mSummary;
        params.dzyj = mLeaderSummary;

        params.status = 0;
        params.other_part = mParts;
        params.examine_status = 0;


        if (!"".equals(mEquipment)) {
            params.equipment = mEquipment;
        }

        if (mMission.getExecute_start_time() == null) {
            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
            mMission.setExecute_start_time(sharedPreferences.getLong(mMission.getId(), System.currentTimeMillis()));
        }

        if (mMission.getExecute_end_time() == null) {

            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);
            mMission.setExecute_end_time(sharedPreferences.getLong(mMission.getId(), System.currentTimeMillis()));
        }

        final long beginTime = mMission.getExecute_start_time();
        final long endTime = mMission.getExecute_end_time();
        mBeforTime = beginTime-24 * 60 * 60 * 1000;
        final String file1 = sdf.format(beginTime);
//        final String file2 = sdf.format(endTime);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                final ArrayList<Point> locationPath = new ArrayList<>();

                mDatePoor = getDatePoor(beginTime, endTime);
                if (mDatePoor < 1) {        //表示在同一天
//                    Log.i("Oking","是同一天");
                    List<String> locationPos = RxFileUtils.readFile2List(Environment.getExternalStorageDirectory() + "/oking/location/" + file1 + ".txt", "UTF-8");
                    if (locationPos != null) {
                        for (String s : locationPos) {
                            String[] items = s.split(",");
                            if (items.length != 3) {
                                continue;
                            }

                            String Latitude = items[0];
                            String Longitude = items[1];
                            String datetime = items[2];

                            if (Long.parseLong(datetime) > beginTime && Long.parseLong(datetime) < endTime) {
                                Point location = new Point();
                                location.setLatitude(Double.valueOf(Latitude));
                                location.setLongitude(Double.valueOf(Longitude));
                                location.setDatetime(Long.valueOf(datetime));
                                locationPath.add(location);
                            }
                        }
                    }


                } else {

                    for (int i = 0; i <=mDatePoor; i++) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/oking/location/" + getAfterData(mBeforTime) + ".txt");

                        if (file.exists()) {
//                            Log.i("Oking","不是同一天"+file.getName());
                            List<String> locationPos = RxFileUtils.readFile2List(file, "UTF-8");
                            for (String s : locationPos) {
                                String[] items = s.split(",");
                                if (items.length != 3) {
                                    continue;
                                }

                                String Latitude = items[0];
                                String Longitude = items[1];
                                String datetime = items[2];

                                if (Long.parseLong(datetime) > beginTime && Long.parseLong(datetime) < endTime) {

                                    Point location = new Point();
                                    location.setLatitude(Double.valueOf(Latitude));
                                    location.setLongitude(Double.valueOf(Longitude));
                                    location.setDatetime(Long.valueOf(datetime));
                                    locationPath.add(location);
                                }
                            }

                        }

                    }
                }

                //筛选一下，不然点集太多
                if (locationPath.size() > 100) {
                    ArrayList<Point> newLocationPath = new ArrayList<>();
                    for (int i = 0; i < locationPath.size(); i = i + 2) {

                        newLocationPath.add(locationPath.get(i));
                    }
                    e.onNext(new Gson().toJson(newLocationPath));
                } else {
                    e.onNext(new Gson().toJson(locationPath));
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
//                Log.i("Oking","长度："+value.length()+value+"轨迹");
                        params.route = value;

                        params.tbr = DefaultContants.CURRENTUSER.getUserName();
                        params.tbrid = DefaultContants.CURRENTUSER.getUserId();

//        params.tbr = mission.getFbr();
//        params.tbrid = mission.getPublisher();

                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {

                                try {
                                    JSONObject object = new JSONObject(result);
                                    int code = object.getInt("code");

                                    if (mLog.getId() == null) {
                                        mLog.setId(object.getString("id"));
                                    }

                                    if (code != 0) {
                                        mUploadJobLog.uploadFail(new Exception("服务器内部错误"));
                                        return;
                                    }

                                    mUploadJobLog.uploadSucc("0");


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mUploadJobLog.uploadFail(e);
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                mUploadJobLog.uploadFail(ex);
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("异常了" + e.getMessage());
                        mUploadJobLog.uploadFail(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public int getDatePoor(long time1, long time2) {

        long nd = 1000 * 24 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = time2 - time1;
        // 计算差多少天
        int day = (int) (diff / nd);
        return day;

    }


    public String getAfterData(long time) {
        //如果需要向后计算日期 -改为+
        Date newDate = new Date(time + 24 * 60 * 60 * 1000);
        mBeforTime = newDate.getTime();
        String dateOk = sdf.format(newDate);
        return dateOk;
    }
}
