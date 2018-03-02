package gd.water.oking.com.cn.wateradministration_gd.main;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vondear.rxtools.RxDeviceUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.SetLocationParams;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationService extends Service {

    private static final int cacheSize = 5;
    private final double EARTH_RADIUS = 6378137.0;
    private LocationManager locationManager;
    private File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/location");
    private Location newLocation = null;
    private ArrayList<Location> locationArrayList = new ArrayList<>();
    private ArrayList<Location> cacheLonList = new ArrayList<>();
    private Location lastLocation = null;//最后基准点
    private Subscription mSubscription;
    private GpsStatus.Listener gl;

    public LocationService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();

        if (mSubscription!=null){
            mSubscription.cancel();
            mSubscription=null;
        }
        Flowable.interval(0, 1, TimeUnit.MINUTES)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription=s;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //上传最新位置
                        if (newLocation != null) {
                            if (DefaultContants.CURRENTUSER != null && !"".equals(DefaultContants.CURRENTUSER.getUserId()) &&
                                    DefaultContants.ISHTTPLOGIN) {
                                SetLocationParams params = new SetLocationParams();
                                params.USER_ID = DefaultContants.CURRENTUSER.getUserId();
                                params.DEV_ID = RxDeviceUtils.getIMEI(MyApp.getApplictaion());
                                Point mLocation = new Point();
                                mLocation.setLatitude(newLocation.getLatitude());
                                mLocation.setLongitude(newLocation.getLongitude());
                                mLocation.setDatetime(newLocation.getTime());
                                params.COORDINATE = new Gson().toJson(mLocation);
                                params.TIME = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString();
                                params.LOGINTIME = MyApp.getApplictaion().getSharedPreferences("logintime", Context.MODE_PRIVATE).getLong("logintime", 0);

                                Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

                                    @Override
                                    public void onSuccess(String result) {

                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {

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

                        newLocation = null;

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        return super.onStartCommand(intent, flags, startId);
    }


    private void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        // 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(true);
        // 设置是否需要方位信息
        criteria.setBearingRequired(false);
        // 设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(provider, 2000, 1.0f, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (locationArrayList.size() != 4) {
                    locationArrayList.add(location);
                    return;
                }

                locationArrayList.add(location);
                Location aLocation = getBestLocation(locationArrayList);
                locationArrayList.clear();
                if (aLocation == null) {
                    return;
                }
                cacheLonList.add(aLocation);
                if (cacheLonList.size() >= cacheSize) {
                    writeToLogFile();
                }

                //校对时间
                SystemClock.setCurrentTimeMillis(aLocation.getTime());
                //记录最新定位
                newLocation = aLocation;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        gl = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {

                switch (event) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        Log.i("GpsStatus", "第一次定位");
                        break;
                    case GpsStatus.GPS_EVENT_STARTED:
                        Log.i("GpsStatus", "定位开始");
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        Log.i("GpsStatus", "定位结束");
                        break;
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                        //获取当前状态  
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                        //获取卫星颗数的默认最大值  
                        int maxSatellites = gpsStatus.getMaxSatellites();
                        //创建一个迭代器保存所有卫星   
                        Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                        int count = 0;
                        int useCount = 0;
                        while (iters.hasNext() && count <= maxSatellites) {
                            GpsSatellite s = iters.next();
                            count++;
                            if (s.usedInFix()) {
                                useCount++;
                            }
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                        //NotificationManagerCompat notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                        builder.setContentTitle("广东水政定位:").
                                setContentText("当前在用卫星数：" + useCount + "    采样点数：" + locationArrayList.size()).
                                setSmallIcon(R.drawable.login_logo);
                        if (newLocation != null) {
                            builder.setSubText("最新定位时间：" + sdf.format(newLocation.getTime()));
                        }

                        Notification notification = builder.build();
                        notification.flags |= Notification.FLAG_NO_CLEAR;
                        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.gpsstate_item_layout);

                        if (newLocation != null) {
                            remoteViews.setTextViewText(R.id.latitude_textView, "经度：" + newLocation.getLatitude());
                            remoteViews.setTextViewText(R.id.longitude_textView, "纬度：" + newLocation.getLongitude());
                            remoteViews.setTextViewText(R.id.altitude_textView, "海拔：" + newLocation.getAltitude());
                            remoteViews.setTextViewText(R.id.speed_textView, "速度：" + newLocation.getSpeed() + "m/s");
                            remoteViews.setTextViewText(R.id.accuracy_textView, "精度：±" + newLocation.getAccuracy() + "m");
                            remoteViews.setTextViewText(R.id.dateTime_textView, "定位时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(newLocation.getTime())));
                        }

                        notification.bigContentView = remoteViews;

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(10000, notification);

                        // 更新gps界面
                        Intent intent = new Intent(MainActivity.UPDATE_GPS_STATE_UI);
                        if (newLocation != null) {
                            intent.putExtra("latitude", newLocation.getLatitude());
                            intent.putExtra("longitude", newLocation.getLongitude());
                            intent.putExtra("altitude", newLocation.getAltitude());
                            intent.putExtra("speed", newLocation.getSpeed());
                            intent.putExtra("accuracy", newLocation.getAccuracy());
                            intent.putExtra("dateTime", newLocation.getTime());
                        }

                        intent.putExtra("useCount", useCount);
                        switch (useCount) {
                            case 0:
                                intent.putExtra("SignalLevel", 0);
                                break;
                            case 3:
                            case 4:
                            case 5:
                                intent.putExtra("SignalLevel", 1);
                                break;
                            case 6:
                            case 7:
                            case 8:
                                intent.putExtra("SignalLevel", 2);
                                break;
                            default:
                                intent.putExtra("SignalLevel", 3);
                                break;
                        }
                        sendBroadcast(intent);

                        //Log.i("GpsStatus", "卫星状态改变");
                        break;
                    default:
                        break;
                }
            }
        };

        locationManager.addGpsStatusListener(gl);
    }

    private void writeToLogFile() {
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        File file = new File(mediaStorageDir, sdf.format(new Date()) + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fw = new FileWriter(file, true);
            long timeS = System.currentTimeMillis();
            Log.i("WriteFile", "onLocationWrite: >>>>Start@" + timeS);

            for (int i = 0; i < cacheLonList.size(); i++) {
                Location aLocation = cacheLonList.get(i);


                fw.write(aLocation.getLatitude() + "," + aLocation.getLongitude() + "," + aLocation.getTime() + "\n");

            }
            fw.flush();
            fw.close();
            cacheLonList.clear();
            Log.i("WriteFile", "onLocationWrite: >>>>End>>" + (System.currentTimeMillis() - timeS));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

        locationManager.removeGpsStatusListener(gl);
        if (mSubscription!=null){

            mSubscription.cancel();
            mSubscription=null;
        }
        writeToLogFile();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    private Location getBestLocation(ArrayList<Location> locationArrayList) {

        float limit1 = 35;//35m/s = 126km/h
        float limit2 = 20;//20m/s = 74km/h

        if (locationArrayList.size() <= 2) {
            return locationArrayList.get(0);
        }

        try {
            ArrayList<Location> removeList = new ArrayList<>();

            float SpeedSum = 0;

            for (int i = 0; i < locationArrayList.size() - 2; i++) {
                Location a = locationArrayList.get(i);
                Location b = locationArrayList.get(i + 1);

                SpeedSum += gps2m(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude()) /
                        (b.getTime() - a.getTime()) / 1000;
            }

            //平均速度
            float avgSpeed = SpeedSum / (locationArrayList.size() - 1);

            for (int i = 0; i < locationArrayList.size() - 1; i++) {
                //即时速度大于上限
                if (locationArrayList.get(i).getSpeed() > limit1) {
                    removeList.add(locationArrayList.get(i));
                    continue;
                }

                if (locationArrayList.get(i).getAccuracy() > 50) {
                    removeList.add(locationArrayList.get(i));
                    continue;
                }

                //即时速度超出平均速度的10%（当速度大于74km/h）
                if ((locationArrayList.get(i).getSpeed() > limit2) &&
                        avgSpeed * 1.1 < locationArrayList.get(i).getSpeed()) {
                    removeList.add(locationArrayList.get(i));
                    continue;
                }
            }

            for (int i = 0; i < locationArrayList.size() - 2; i++) {

                Location a = null;
                if (i == 0) {
                    if (lastLocation == null) {
                        continue;
                    }
                    a = lastLocation;
                } else {
                    a = locationArrayList.get(i - 1);
                }
                Location b = locationArrayList.get(i);

                double gpsLength = gps2m(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
                double runLength = (a.getSpeed() + b.getSpeed()) * (b.getTime() - a.getTime()) / 1000 / 2;

                if (b.getTime() - a.getTime() < 10000) {
                    //10s内运动距离比理论距离大10%
                    if (gpsLength > runLength * 1.1) {
                        removeList.add(locationArrayList.get(i));
                        continue;
                    }
                } else if (b.getTime() - a.getTime() < 30000) {
                    //10s-60s内运动距离比理论距离大50%
                    if (gpsLength > runLength * 1.5) {
                        removeList.add(locationArrayList.get(i));
                        continue;
                    }
                } else {
                    if (gpsLength > runLength * 2) {
                        removeList.add(locationArrayList.get(i));
                        continue;
                    }
                }
            }

            for (int i = 0; i < locationArrayList.size() - 3; i++) {
                Location a = locationArrayList.get(i);
                Location b = locationArrayList.get(i + 1);
                Location c = locationArrayList.get(i + 2);

                double abSpeed = gps2m(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude()) /
                        (b.getTime() - a.getTime()) / 1000;

                double bcSpeed = gps2m(b.getLatitude(), b.getLongitude(), c.getLatitude(), c.getLongitude()) /
                        (c.getTime() - b.getTime()) / 1000;

                //平均速度大于上限
                if (abSpeed >= limit1 && bcSpeed >= limit1) {
                    removeList.add(locationArrayList.get(i + 1));
                    continue;
                }

                //拐点??
//                float abBearing = b.getBearing() - a.getBearing();
//                float bcBearing = c.getBearing() - b.getBearing();
//
//                if (Math.abs(abBearing)>=180 && Math.abs(bcBearing)>=180){
//                    removeList.add(locationArrayList.get(i + 1));
//                    continue;
//                }
            }

            Location mlocation = locationArrayList.get(0);
            String a = "";
            for (int i = 0; i < locationArrayList.size(); i++) {
                if (mlocation.getAccuracy() >= locationArrayList.get(0).getAccuracy()) {
                    mlocation = locationArrayList.get(i);
                }

                a += locationArrayList.get(i).getAccuracy() + ",";
            }

//            Toast.makeText(getApplicationContext(), a + ">>>>>>    " + locationArrayList.size() + "/" + removeList.size(), Toast.LENGTH_LONG).show();

            //去除不及格取样点
            for (int i = 0; i < removeList.size() - 1; i++) {
                locationArrayList.remove(removeList.get(i));
            }

//            if (locationArrayList.size() == 0 && mlocation.getTime() - newLocation.getTime() > 60000) {
//                locationArrayList.add(mlocation);
//            }

            lastLocation = locationArrayList.get(locationArrayList.size() - 1);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

//        Toast.makeText(getApplicationContext(), locationArrayList.size() + "", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), ((int) Math.floor(locationArrayList.size() / 2)) + "", Toast.LENGTH_SHORT).show();
        if (locationArrayList.size() > 0) {
            Location nLocation = locationArrayList.get(0);
            for (int i = 0; i < locationArrayList.size(); i++) {
                if (nLocation.getAccuracy() >= locationArrayList.get(0).getAccuracy()) {
                    nLocation = locationArrayList.get(i);
                }
            }
            return nLocation;
        } else {
            return null;
        }
    }

    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

}
