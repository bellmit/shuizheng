package gd.water.oking.com.cn.wateradministration_gd.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.amap.api.navi.AMapNavi;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.vondear.rxtools.RxUtils;

import org.xutils.x;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gd.water.oking.com.cn.wateradministration_gd.receiver.CallReceiver;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by pc on 16/10/14.
 */

public class MyApp extends Application{
    public static String sessionID = "";
//    public static Typeface typeFace;
    public static LocationService locationService;
    public static LocalSqlite localSqlite;
    private static ExecutorService mExecutorService = new ThreadPoolExecutor(4, 8,
            1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));  ;
    private static MyApp mMyApplication;
    private CallReceiver callReceiver;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName(android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = "gd.water.oking.com.cn.wateradministration_gd".equals(processName);
            if (defaultProcess) {
                RxUtils.init(this);
//                setTypeface();
                MyExceptionHandler exceptionHandler = new MyExceptionHandler();
                mMyApplication= this;
                exceptionHandler.init();
                EMOptions options = new EMOptions();
                //        options.setAcceptInvitationAlways(false);
                options.setAutoLogin(false);
                options.setRequireAck(true);
                options.setRequireDeliveryAck(true);
                EaseUI.getInstance().init(this, options);

                IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
                if(callReceiver == null){
                    callReceiver = new CallReceiver();
                }

                //register incoming call receiver
                registerReceiver(callReceiver, callFilter);

                x.Ext.init(this);
//                x.Ext.setDebug(BuildConfig.DEBUG);

                //创建本地数据库
                localSqlite = new LocalSqlite(getApplicationContext());


                //设置高德地图apikey
                AMapNavi.setApiKey(this, "a6d6b52cc353af3cfdadf636fd4b9d9b");

//        refWatcher = LeakCanary.install(this);



            }

//            LeakCanary.install(this);
        }






    }


    @Override
    public void onTerminate() {
        localSqlite.close();



        super.onTerminate();
    }


    public String getProcessName(int pid) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //在运行的进程的
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;

    }

    public static Application getApplictaion(){
        return mMyApplication;
    }


    //获取全局线程池对象
    public static ExecutorService getGlobalThreadPool() {
        return mExecutorService;
    }






}
