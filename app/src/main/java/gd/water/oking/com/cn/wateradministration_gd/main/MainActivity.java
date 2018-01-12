package gd.water.oking.com.cn.wateradministration_gd.main;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.RxLocationUtils;
import com.vondear.rxtools.RxPermissionTool;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Area;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.bean.Question;
import gd.water.oking.com.cn.wateradministration_gd.fragment.LoginFragment;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.GetCaseListParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionAreaParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionListParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionMemberParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetQuestionParams;
import gd.water.oking.com.cn.wateradministration_gd.http.GetTemperatureParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.MyCallBack;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;
import io.reactivex.Flowable;

public class MainActivity extends AutoLayoutActivity implements MyCallBack {

    public static final String UPDATE_MISSION_LIST = "oking.updateMissionList";
    public static final String UPDATE_CASE_LIST = "oking.updateCaseList";
    public static final String UPDATE_MISSIONLOG_UI_LIST = "oking.updateMissionLogUIList";
    public static final String UPDATE_CASEFILE_UI_LIST = "oking.updateCaseFileUIList";
    public static final String START_ALARM_UI_LIST = "oking.startAlarmUIList";
    public static final String STOP_ALARM_UI_LIST = "oking.stopAlarmUIList";
    public static final String UPDATE_GPS_STATE_UI = "oking.updategpsstate";
    public static final String UPDATE_SIGNAL_UI = "oking.updatesignal";
    public static final String NewMission = "oking.newmission";
    public static final String CALL_CASE_MANAGER = "oking.callCaseManager";
    public static final String UPDATE_TEMP_UI = "oking.updatetempui";
    public static final String UPDATE_MISSION_GET = "oking.missionGet";

    public static final int Mission_Completed = 100;
    public static final String Area = "广东省";

    public static ArrayList<Mission> missionList = new ArrayList<>();
    public static ArrayList<Case> caseList = new ArrayList<>();
    public static String tempStr = "";
    public static String selectCaseId = "";
    public static int arrangeNum, inspectNum, reportNum;
    private Subscription mSubscription;
    private PhoneStateListener mylistener;

    private long missionTime = 0;
    private Comparator<Mission> comparator = new Comparator<Mission>() {
        @Override
        public int compare(Mission o1, Mission o2) {

            if (o1.getStatus() == 4 && o2.getStatus() != 4) {
                return -1;
            } else if (o2.getStatus() == 4 && o1.getStatus() != 4) {
                return 1;
            } else if (o1.getStatus() == MainActivity.Mission_Completed && o2.getStatus() != MainActivity.Mission_Completed) {
                return -1;
            } else if (o2.getStatus() == MainActivity.Mission_Completed && o1.getStatus() != MainActivity.Mission_Completed) {
                return 1;
            } else if (o1.getStatus() != o2.getStatus()) {
                return o1.getStatus() - o2.getStatus();
            } else if (o2.getEnd_time().equals(o1.getEnd_time())) {
                return (int) (o2.getEnd_time() - o1.getEnd_time());
            } else {
                return 1;
            }
        }
    };
    private long mMTime;
    private GetMissionListRecever mGetMissionListRecever;
    private Callback.Cancelable mGetCaseCancelable;
    private Callback.Cancelable mGetTemperatureCancelable;
    private Callback.Cancelable mGetMissionCancelable;
    private Callback.Cancelable mGetQuestionCancelable;
    private Callback.Cancelable mGetMissionMemberCancelable;
    private Callback.Cancelable mGetMissionAreaCancelable;
    private long mRefreshTemEndTime;
    private Intent mIntent;


    private static void execshell(String command) {

        try {
            Runtime.getRuntime().exec(command);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        getCurrentNetDBM(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {

        stopCurrentNetDBM(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorMain9));
        setContentView(R.layout.activity_main);
        init();
        //检测是否为平板
//        if (isTabletDevice(this)) {
//            //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//
//        } else {
////            RxToast.error(MyApp.getApplictaion(), "不支持在手机上运行", Toast.LENGTH_SHORT).show();
////            finish();
////            System.exit(0);
//        }


        //请求权限
        requestPm();


        if (!RxLocationUtils.isGpsEnabled(MyApp.getApplictaion())) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.startActivityForResult(intent, 0); //此为设置完成后返回到获取界面Intent GPSIntent = new Intent();
        }

        if (!Utils.isServiceWork(this,"gd.water.oking.com.cn.wateradministration_gd.main.LocationService")) {
            mIntent = new Intent(getApplicationContext(), LocationService.class);

            startService(mIntent);
        }
    }

    private void requestPm() {
        RxPermissionTool.with(this)
                .addPermission(Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.WAKE_LOCK)
                .addPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .addPermission(Manifest.permission.INTERNET)
                .addPermission(Manifest.permission.RECORD_AUDIO)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .initPermission();
    }

    private void getTempData() {
        if (System.currentTimeMillis()-mRefreshTemEndTime>10000){
            GetTemperatureParams params = new GetTemperatureParams();
            params.key = "zjd6pbelugqzxt1n";
//            params.location = "佛山";
            Point p = FileUtil.getLastLocationFromFile(System.currentTimeMillis(), 30 * 60 * 1000);//30min内定位
            if (p != null) {
                params.location = p.getLatitude() + ":" + p.getLongitude();
            } else {
                params.location = "ip";
            }
            params.hours = 3;
            mGetTemperatureCancelable = x.http().get(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mRefreshTemEndTime = System.currentTimeMillis();
                    Log.i("TempResult", result);
                    tempStr = result;

                    Intent intent = new Intent(UPDATE_TEMP_UI);
                    sendBroadcast(intent);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("TempResult", "onError: " + ex.toString());
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

    /**
     * 判断是否平板设备
     *
     * @return true:平板,false:手机
     */
//    private boolean isTabletDevice(Context context) {
//        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
//                Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }

    private void init() {
        MyApp.getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                copyDB();
            }
        });

        IntentFilter intentFilter = new IntentFilter(UPDATE_MISSION_GET);
        mGetMissionListRecever = new GetMissionListRecever();
        MyApp.getApplictaion().registerReceiver(mGetMissionListRecever, intentFilter);


        getSupportFragmentManager().beginTransaction().replace(R.id.activity_root, new LoginFragment()).commit();


        //还原已选择的案件id
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("selectCaseId", Context.MODE_PRIVATE);
        selectCaseId = sharedPreferences.getString("selectCaseId", "");
    }

    /**
     * copy数据库
     */
    private void copyDB() {
        //创建一个文件 /data/data/包名/files/address.db
        File file = new File(getFilesDir(), "law.db");
        if (file.exists() && file.length() > 0) {
//            System.out.println("数据库已经拷贝了");
        } else {
            try {
                AssetManager am = getAssets();
                InputStream is = am.open("law.db");
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER)) {
            finish();
        }
    }


    private void getHttpAlarm() {
        int count = 0;
        for (int i = 0; i < missionList.size(); i++) {
            if (missionList.get(i).getStatus() == 2 && missionList.get(i).getEnd_time() > System.currentTimeMillis()) {
                count++;
            }
        }

        if (count > 0) {
            Intent startIntent = new Intent(START_ALARM_UI_LIST);
            sendBroadcast(startIntent);
        } else {
            Intent stopIntent = new Intent(MainActivity.STOP_ALARM_UI_LIST);
            sendBroadcast(stopIntent);
        }
    }

    private void getHttpMissionList() {
        if (DefaultContants.ISHTTPLOGIN) {
            final GetMissionListParams params = new GetMissionListParams();

            params.receiver = DefaultContants.CURRENTUSER.getUserId();
            params.classify = 2;


            mGetMissionCancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(final String result) {

                            if (missionList != null) {


                                MyApp.getGlobalThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        setMissionList(result);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {

                            Collections.sort(missionList, comparator);
                            sendBroadcast(new Intent(UPDATE_MISSION_LIST));

                            getMissionTypeNum();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    }

            );
        } else {
            missionList.clear();

            MyApp.getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = MyApp.localSqlite.select(LocalSqlite.MISSION_TABLE,
                            new String[]{"jsonStr"},
                            "receiver = ?",
                            new String[]{DefaultContants.CURRENTUSER.getUserId()}, null, null, null);
                    while (cursor.moveToNext()) {
                        String jsonStr = cursor.getString(cursor.getColumnIndex("jsonStr"));

                        final Mission mission = DataUtil.praseJson(jsonStr, new TypeToken<Mission>() {
                        });
                        if (mission != null) {
                            missionList.add(mission);
                        }

                        //判断书否存有任务开始结束时间
                        Long execute_start_time = null, execute_end_time = null;
                        SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                        if (sharedPreferences1.getLong(mission.getId(), 0) != 0) {
                            execute_start_time = sharedPreferences1.getLong(mission.getId(), 0);
                        }
                        SharedPreferences sharedPreferences2 = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);
                        if (sharedPreferences2.getLong(mission.getId(), 0) != 0) {
                            execute_end_time = sharedPreferences2.getLong(mission.getId(), 0);
                        }
                        if (execute_end_time != null) {
                            mission.setStatus(Mission_Completed);
                        } else if (execute_start_time != null) {
                            mission.setStatus(4);
                        }

                        getMember(mission);

                        getArea(mission);

                    }

                    Collections.sort(missionList, comparator);
                    sendBroadcast(new Intent(UPDATE_MISSION_LIST));

                    getMissionTypeNum();

                }
            });
        }
    }

    private void getHttpCaseList() {

        if (DefaultContants.ISHTTPLOGIN) {
            GetCaseListParams params = new GetCaseListParams();
            params.uid = DefaultContants.CURRENTUSER.getUserId();
            mGetCaseCancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(final String result) {
                    caseList.clear();
                    MyApp.getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            int deleteCount = MyApp.localSqlite.delete(LocalSqlite.CASE_TABLE,
                                    null, null);

                            ArrayList<Case> cList = DataUtil.praseJson(result.toString(),
                                    new TypeToken<ArrayList<Case>>() {
                                    });

                            if (cList != null) {
                                for (int i = 0; i < cList.size(); i++) {
                                    Case aCase = cList.get(i);

                                    caseList.add(aCase);
                                    aCase.insertDB(MyApp.localSqlite);
                                }
                            }

                            Intent uptIntent = new Intent(MainActivity.UPDATE_CASE_LIST);
                            sendBroadcast(uptIntent);

                        }
                    });
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("GetCaseList", "onError>>> " + ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            caseList.clear();

            MyApp.getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = MyApp.localSqlite.select(LocalSqlite.CASE_TABLE,
                            new String[]{"jsonStr"},
                            null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        String jsonStr = cursor.getString(cursor.getColumnIndex("jsonStr"));

                        Case aCase = DataUtil.praseJson(jsonStr, new TypeToken<Case>() {
                        });
                        if (aCase != null) {
                            caseList.add(aCase);
                        }
                    }
                }
            });

        }
    }

    private void getHttpQuestionList() {
        if (DefaultContants.ISHTTPLOGIN) {
            GetQuestionParams params = new GetQuestionParams();
            params.wtlx = "DCBL";
            mGetQuestionCancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(final String result) {
                    Log.i("GetQuestion", "onSuccess>>> " + result);

                    MyApp.getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            int deleteCount = MyApp.localSqlite.delete(LocalSqlite.QUESTION_TABLE,
                                    null, null);

                            final ArrayList<Question> qList = DataUtil.praseJson(result.toString(),
                                    new TypeToken<ArrayList<Question>>() {
                                    });


                            if (qList != null) {
                                for (int i = 0; i < qList.size(); i++) {
                                    Question question = qList.get(i);
                                    question.insertDB(MyApp.localSqlite);
                                }
                            }

                        }
                    });
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("GetQuestion", "onError>>> " + ex.toString());
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

    private void getMember(final Mission mission) {

        //请求成员列表
        GetMissionMemberParams params = new GetMissionMemberParams();
        params.task_id = mission.getId();
        mGetMissionMemberCancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    int count = object.getInt("total");
                    JSONArray missionJSONArray = object.getJSONArray("rows");

                    mission.getMembers().clear();
                    MyApp.localSqlite.delete(LocalSqlite.MEMBER_TABLE, "task_id = ?",
                            new String[]{mission.getId()});

                    final ArrayList<Member> mbList = DataUtil.praseJson(missionJSONArray.toString(),
                            new TypeToken<ArrayList<Member>>() {
                            });

                    Member leader = new Member();
                    leader.setUsername(mission.getReceiver_name());
                    leader.setUserid(mission.getReceiver());
                    leader.setPost("任务负责人");
                    mission.getMembers().add(leader);

                    if (mbList != null) {
                        for (int i = 0; i < mbList.size(); i++) {
                            Member member = mbList.get(i);
                            member.setPost("组员");
                            mission.getMembers().add(member);
                            member.insertDB(MyApp.localSqlite);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                missionTime = mMTime;
                //missionTime保存到SharedPreferences
                SharedPreferences saveSharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionTime", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveSharedPreferences.edit();
                editor.putLong("missionTime", missionTime);
                editor.commit();
                //Log.i("missionTime", missionTime + "");
                Collections.sort(missionList, comparator);
                getMissionTypeNum();
                sendBroadcast(new Intent(UPDATE_MISSION_LIST));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


                mission.getMembers().clear();

                Member leader = new Member();
                leader.setUsername(mission.getReceiver_name());
                leader.setUserid(mission.getReceiver());
                leader.setPost("任务负责人");
                mission.getMembers().add(leader);

                Cursor cursor = MyApp.localSqlite.select(LocalSqlite.MEMBER_TABLE,
                        new String[]{"task_id", "userid", "uName", "jsonStr"},
                        "task_id=?",
                        new String[]{mission.getId()},
                        null, null, null);
                while (cursor.moveToNext()) {
                    String jsonStr = cursor.getString(cursor.getColumnIndex("jsonStr"));

                    Member member = DataUtil.praseJson(jsonStr, new TypeToken<Member>() {
                    });
                    if (member != null) {
                        member.setPost("组员");
                        mission.getMembers().add(member);
                    }

                }
                missionTime = mMTime;
                //missionTime保存到SharedPreferences
                SharedPreferences saveSharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionTime", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saveSharedPreferences.edit();
                editor.putLong("missionTime", missionTime);
                editor.commit();
                //Log.i("missionTime", missionTime + "");
                Collections.sort(missionList, comparator);
                getMissionTypeNum();
                sendBroadcast(new Intent(UPDATE_MISSION_LIST));
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getArea(final Mission mission) {

        GetMissionAreaParams params = new GetMissionAreaParams();
        params.busiTaskId = mission.getId();
        mGetMissionAreaCancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i("GetMissionArea", "onSuccess>>>>>>" + result);

                if (result == null || "".equals(result) || "[]".equals(result)) {
                    return;
                }

                try {
                    JSONObject object = new JSONArray(result).getJSONObject(0);
                    String coordinate = object.getString("coordinate");
                    String type = object.getString("area_type");
                    String id = object.getString("id");

                    final Area a = new Area();
                    a.setArea_type(type);
                    a.setCoordinate(coordinate);
                    a.setTaskid(mission.getId());
                    a.setId(id);

                    final Cursor c = MyApp.localSqlite.select(LocalSqlite.AREA_TABLE, new String[]{"aid"}, "aid=?", new String[]{id}, null, null, null);
                    if (c.moveToNext()) {
                        a.updateDB(MyApp.localSqlite);
                    } else {
                        a.insertDB(MyApp.localSqlite);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("GetMissionArea", "onError: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public void getCurrentNetDBM(Context context) {

        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        mylistener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                String signalInfo = signalStrength.toString();
                String[] params = signalInfo.split(" ");

                int dbm = 0;
                String state = "4G";
                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络 最佳范围   >-90dBm 越大越好
                    dbm = Integer.parseInt(params[9]);
                    state = "4G";
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    //3G网络最佳范围  >-90dBm  越大越好  ps:中国移动3G获取不到  返回的无效dbm值是正数（85dbm）
                    //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方

                    state = "3G";

                    if (ActivityCompat.checkSelfPermission(MyApp.getApplictaion(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String imsi = tm.getSubscriberId();
                    if (imsi != null) {
                        //因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 //中国移动
                        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                            dbm = 0;
                        }
                        //中国联通
                        else if (imsi.startsWith("46001")) {
                            dbm = signalStrength.getCdmaDbm();
                        }
                        //中国电信
                        else if (imsi.startsWith("46003")) {
                            dbm = signalStrength.getEvdoDbm();
                        }
                    }
                } else {
                    //2G网络最佳范围>-90dBm 越大越好
                    int asu = signalStrength.getGsmSignalStrength();
                    dbm = -113 + 2 * asu;

                    state = "2G";
                }


                Intent intent = new Intent(MainActivity.UPDATE_SIGNAL_UI);
                intent.putExtra("state", state);
                if (-97 <= dbm) {
                    intent.putExtra("SignalLevel", 5);
                } else if (-105 <= dbm && dbm <= -97) {
                    intent.putExtra("SignalLevel", 4);
                } else if (-110 <= dbm && dbm <= -105) {
                    intent.putExtra("SignalLevel", 3);
                } else if (-120 <= dbm && dbm <= -110) {
                    intent.putExtra("SignalLevel", 2);
                } else if (-140 <= dbm && dbm <= -120) {
                    intent.putExtra("SignalLevel", 1);
                } else {
                    intent.putExtra("SignalLevel", 0);
                }

                sendBroadcast(intent);
            }
        };
        //开始监听
        tm.listen(mylistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void stopCurrentNetDBM(Context context) {

        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (mylistener != null) {
            tm.listen(mylistener, PhoneStateListener.LISTEN_NONE);
        }
    }

    private void getMissionTypeNum() {
        arrangeNum = 0;
        inspectNum = 0;
        reportNum = 0;

        for (int i = 0; i < MainActivity.missionList.size(); i++) {
            if (MainActivity.missionList.get(i).getStatus() == 2 &&
                    MainActivity.missionList.get(i).getEnd_time() > System.currentTimeMillis()) {
                arrangeNum += 1;
            }

            if ((MainActivity.missionList.get(i).getStatus() == 3 ||
                    MainActivity.missionList.get(i).getStatus() == 4) &&
                    MainActivity.missionList.get(i).getEnd_time() > System.currentTimeMillis()) {
                inspectNum += 1;
            }

            if (MainActivity.missionList.get(i).getStatus() == MainActivity.Mission_Completed ||
                    MainActivity.missionList.get(i).getStatus() == 9) {
                reportNum += 1;
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(mIntent);
        if (mSubscription!=null){

            mSubscription.cancel();
            mSubscription=null;
        }
        File StorageDir = new File(Environment.getExternalStorageDirectory(), "oking/System_log/");
        if (!StorageDir.exists()) {
            StorageDir.mkdirs();
        }
        File f = new File(StorageDir, new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".txt");
        execshell("logcat -d -v time -f " + f.getPath());

        MyApp.getApplictaion().unregisterReceiver(mGetMissionListRecever);
        if (mGetCaseCancelable != null) {

            mGetCaseCancelable.cancel();
        }

        if (mGetTemperatureCancelable != null) {
            mGetTemperatureCancelable.cancel();
        }

        if (mGetMissionCancelable != null) {
            mGetMissionCancelable.cancel();
        }

        if (mGetQuestionCancelable != null) {
            mGetQuestionCancelable.cancel();
        }

        if (mGetMissionMemberCancelable != null) {
            mGetMissionMemberCancelable.cancel();
        }

        if (mGetMissionAreaCancelable != null) {
            mGetMissionAreaCancelable.cancel();
        }
        super.onDestroy();
    }

    private synchronized void setMissionList(String result) {
        try {
            JSONObject object = new JSONObject(result);
            int count = object.getInt("total");
            JSONArray missionJSONArray = object.getJSONArray("rows");

            missionList.clear();
            int deleteCount = MyApp.localSqlite.delete(LocalSqlite.MISSION_TABLE,
                    "receiver = ?", new String[]{DefaultContants.CURRENTUSER.getUserId()});

            final ArrayList<Mission> mList = DataUtil.praseJson(missionJSONArray.toString(),
                    new TypeToken<ArrayList<Mission>>() {
                    });

            //从SharedPreferences获取missionTime
            SharedPreferences loadSharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionTime", Context.MODE_PRIVATE);
            missionTime = loadSharedPreferences.getLong("missionTime", 0);
            mMTime = missionTime;

            if (mList != null) {

                for (int i = 0; i < mList.size(); i++) {
                    final Mission mission = mList.get(i);

                    if (mission.getStatus() == 2 || mission.getStatus() == 3 || mission.getStatus() == 4 ||
                            mission.getStatus() == 5 || mission.getStatus() == 9) {
                        missionList.add(mission);
                        mission.insertDB(MyApp.localSqlite);

                        //判断是否存有任务开始结束时间
                        Long execute_start_time = null, execute_end_time = null;
                        SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                        if (sharedPreferences1.getLong(mission.getId(), 0) != 0) {
                            execute_start_time = sharedPreferences1.getLong(mission.getId(), 0);
                        }
                        SharedPreferences sharedPreferences2 = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);
                        if (sharedPreferences2.getLong(mission.getId(), 0) != 0) {
                            execute_end_time = sharedPreferences2.getLong(mission.getId(), 0);
                            mission.setExecute_end_time(execute_end_time);
                        }
                        if (execute_end_time != null) {
                            mission.setStatus(Mission_Completed);
                        } else if (execute_start_time != null) {
                            mission.setStatus(4);
                        }

                        if (mMTime == 0) {
                            if (mission.getApproved_time() != null &&
                                    mission.getApproved_time() > mMTime) {
                                mMTime = mission.getApproved_time();
                            }
                        } else {
                            if (mission.getApproved_time() != null &&
                                    mission.getApproved_time() > mMTime) {
                                //发提示
                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                                builder.setContentTitle("新任务").
                                        setContentText(mission.getTask_name()).
                                        setSmallIcon(R.drawable.login_logo).
                                        setTicker("新任务").
                                        setDefaults(Notification.DEFAULT_ALL);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("showfragment", "MissionFragment");
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                builder.setContentIntent(pendingIntent);

                                Notification notification = builder.build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                                //notificationManager.notify((int) (new Date()).getTime(), builder.build());
                                notificationManager.notify(mission.getId(), (int) (new Date()).getTime(), notification);

                                mMTime = mission.getApproved_time();
                            }
                        }

                        getMember(mission);

                        getArea(mission);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Intent intent1 = new Intent(NewMission);
        if (intent.getStringExtra("showfragment") != null) {
            intent1.putExtra("showfragment", intent.getStringExtra("showfragment"));
            sendBroadcast(intent1);
        }
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void refreshMission() {
        getHttpMissionList();
    }

    @Override
    public void refreshTemp() {
        getTempData();
    }

    @Override
    public void refreshCase() {
        getHttpCaseList();
    }

    private class GetMissionListRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mSubscription != null) {
                mSubscription.cancel();
                mSubscription=null;
            }


            Flowable.interval(1, 10, TimeUnit.SECONDS)
                    .onBackpressureDrop()
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onSubscribe(Subscription s) {
                            mSubscription = s;
                            s.request(Long.MAX_VALUE);
                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (DefaultContants.CURRENTUSER != null && !TextUtils.isEmpty(DefaultContants.CURRENTUSER.getUserId())) {

                                getHttpMissionList();
                                getHttpCaseList();
                                getHttpAlarm();
                                getHttpQuestionList();
                                getTempData();
                            }

//                            System.out.println("第" + aLong + "次网络请求");
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void stopGetNetServer() {
        if (mSubscription!=null){

            mSubscription.cancel();
            mSubscription=null;
        }

        if (mGetCaseCancelable != null) {

            mGetCaseCancelable.cancel();
        }

        if (mGetTemperatureCancelable != null) {
            mGetTemperatureCancelable.cancel();
        }

        if (mGetMissionCancelable != null) {
            mGetMissionCancelable.cancel();
        }

        if (mGetQuestionCancelable != null) {
            mGetQuestionCancelable.cancel();
        }

        if (mGetMissionMemberCancelable != null) {
            mGetMissionMemberCancelable.cancel();
        }

        if (mGetMissionAreaCancelable != null) {
            mGetMissionAreaCancelable.cancel();
        }
    }

    /**
     * activity无论分发按键事件、触摸事件或者轨迹球事件都会调用Activity#onUserInteraction()。
     * 如果你想知道用户用某种方式和你正在运行的activity交互，可以重写Activity#onUserInteraction()。
     */

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

    }
}
