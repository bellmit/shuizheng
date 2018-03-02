package gd.water.oking.com.cn.wateradministration_gd.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;

import gd.water.oking.com.cn.wateradministration_gd.AmapLocationAidlInterface;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;


public class IRemoteLocationService extends Service {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    private String[] result = new String[8];
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String mTime;
    private double mLatitude;
    private double mLongitude;
    private float mAccuracy;
    private float mSpeed;
    private double mAltitude;
    private int mSatellites;
    private int mGpsAccuracyStatus;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat mNotificationManager;
    private Notification mNotification;
    private Intent intent = new Intent(MainActivity.UPDATE_GPS_STATE_UI);
    private RemoteViews mRemoteViews;

    public IRemoteLocationService() {
    }

    private AmapLocationAidlInterface.Stub iBinder = new AmapLocationAidlInterface.Stub() {

        @Override
        public String[] getLocation() throws RemoteException {
            return result;
        }

        @Override
        public void refreshNotification() throws RemoteException {
            doRefreshNotification();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        getAmapLocation();
        return iBinder;
    }


    private void getAmapLocation() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
//                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        aMapLocation.getLatitude();//获取纬度
//                        aMapLocation.getLongitude();//获取经度
//                        aMapLocation.getAccuracy();//获取精度信息

                        int locationType = aMapLocation.getLocationType();
                        switch (locationType) {
                            case 1:
                                result[0] = "GPS定位";
                                break;
                            case 2:
                                result[0] = "返回上次定位";
                                break;
                            case 5:
                                result[0] = "WIFI定位";
                                break;
                            case 6:
                                result[0] = "基站定位";
                                break;
                            default:
                                result[0] = "缓存定位";
                                break;
                        }
                        mTime = df.format(System.currentTimeMillis());
                        mLatitude = aMapLocation.getLatitude();
                        mLongitude = aMapLocation.getLongitude();
                        mAccuracy = aMapLocation.getAccuracy();
                        mSpeed = aMapLocation.getSpeed();
                        mAltitude = aMapLocation.getAltitude();
                        mSatellites = aMapLocation.getSatellites();
                        result[1] = mLatitude + "";//获取纬度
                        result[2] = mLongitude + "";//获取经度
                        result[3] = mTime;//定位时间
                        result[4] = mAccuracy + ""; //获取精度
                        result[5] = mSpeed + ""; //获取速度
                        result[6] = mAltitude + "";  //获取海拔
                        result[7] = mSatellites + ""; //卫星个数


                        mGpsAccuracyStatus = aMapLocation.getGpsAccuracyStatus();


                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    }
                }
            }
        });
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

    }

    private void doRefreshNotification() {
        if (!mlocationClient.isStarted()) {
            getAmapLocation();
        }

        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(getApplicationContext());
        }
        mBuilder.setContentTitle("广东水政定位:").
                setContentText("当前在用卫星数：" + mSatellites).
                setSmallIcon(R.drawable.login_logo);
        mBuilder.setSubText("最新定位时间：" + mTime);

        mNotification = mBuilder.build();
        mNotification.flags |= Notification.FLAG_NO_CLEAR;
        if (mRemoteViews==null){

            mRemoteViews = new RemoteViews(getPackageName(), R.layout.gpsstate_item_layout);
        }

        mRemoteViews.setTextViewText(R.id.latitude_textView, "纬度：" + mLatitude);
        mRemoteViews.setTextViewText(R.id.longitude_textView, "经度：" + mLongitude);
        mRemoteViews.setTextViewText(R.id.altitude_textView, "海拔：" + mAltitude);
        mRemoteViews.setTextViewText(R.id.speed_textView, "速度：" + mSpeed + "m/s");
        mRemoteViews.setTextViewText(R.id.accuracy_textView, "精度：±" + mAccuracy + "m");
        mRemoteViews.setTextViewText(R.id.dateTime_textView, "定位时间：" + mTime);

        mNotification.bigContentView = mRemoteViews;

        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());


        mNotificationManager.notify(10000, mNotification);

        // 更新gps界面

        intent.putExtra("latitude", mLatitude);
        intent.putExtra("longitude", mLongitude);
        intent.putExtra("altitude", mAltitude);
        intent.putExtra("speed", mSpeed);
        intent.putExtra("accuracy", mAccuracy);
        intent.putExtra("dateTime", mTime);

        intent.putExtra("useCount", mSatellites);
        intent.putExtra("SignalLevel", mGpsAccuracyStatus);
        sendBroadcast(intent);
    }
}
