package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Contants;
import gd.water.oking.com.cn.wateradministration_gd.bean.MapTaskInfo;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 区域历史执法记录查询
 */
public class RegionalHistoryEnforcementFragment extends BaseFragment implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle mSavedInstanceState;
    private AMap mAMap;
    private MapView mMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private RelativeLayout mRl;

    private Point mPoint;
    private Point mScreenPosition;
    private Circle mCircle;
    private ArrayList<LatLng> mLatLngs;
    private boolean mIsShowToast=true;
    private TextView mTv_taskname;
    private TextView mTv_state;
    private TextView mTv_pelease_people;
    private TextView mTv_description;
    private TextView mTv_approver;
    private TextView mTv_are;
    private TextView mTv_time;
    public RegionalHistoryEnforcementFragment() {
        // Required empty public constructor
    }

    public static RegionalHistoryEnforcementFragment newInstance(String param1, String param2) {
        RegionalHistoryEnforcementFragment fragment = new RegionalHistoryEnforcementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMap.onDestroy();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        return inflater.inflate(R.layout.fragment_regional_history_enforcement, container, false);
    }

    @Override
    public void initView(View rootView) {
        mMap = rootView.findViewById(R.id.map);
        mRl = rootView.findViewById(R.id.rl);
        mTv_taskname = rootView.findViewById(R.id.tv_taskname);
        mTv_state = rootView.findViewById(R.id.tv_state);
        mTv_pelease_people = rootView.findViewById(R.id.tv_pelease_people);
        mTv_approver = rootView.findViewById(R.id.tv_approver);
        mTv_description = rootView.findViewById(R.id.tv_description);
        mTv_are = rootView.findViewById(R.id.tv_are);
        mTv_time = rootView.findViewById(R.id.tv_time);
        mMap.onCreate(mSavedInstanceState);// 此方法必须重写
        mAMap = mMap.getMap();
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mAMap.setMinZoomLevel(10);
        mAMap.setMaxZoomLevel(20);
        setUpMap();

        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnCameraChangeListener(this);

//        setMapCustomStyleFile();
        //设置SDK 自带定位消息监听
        mAMap.setOnMyLocationChangeListener(this);

        mLatLngs = new ArrayList<>();
        mLatLngs.add(Contants.PAD_1);
        mLatLngs.add(Contants.PAD_2);
        mLatLngs.add(Contants.PAD_3);
        mLatLngs.add(Contants.PAD_4);
        mLatLngs.add(Contants.PAD_5);
        mLatLngs.add(Contants.PAD_6);
        mLatLngs.add(Contants.PAD_7);
        mLatLngs.add(Contants.PAD_8);
        mLatLngs.add(Contants.PAD_9);
    }

    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 定位的类型为跟随模式LOCATION_TYPE_FOLLOW;  定位的类型为只定位模式模式LOCATION_TYPE_SHOW
        mAMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        mAMap.setMapCustomEnable(true); //开启自定义样式


    }

    @Override
    public void onMapLoaded() {
        mAMap.setOnMarkerClickListener(this);
        addMarkersToMap();// 往地图上添加marker
    }

    private void addMarkersToMap() {
        MarkerOptions markerOption0 = new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(false).period(10);
        Marker marker = mAMap.addMarker(markerOption0);
        LatLng latLng = mAMap.getCameraPosition().target;
        mScreenPosition = mAMap.getProjection().toScreenLocation(latLng);
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(mScreenPosition.x, mScreenPosition.y);
    }

    //地图移动监听
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    //地图移动完成监听
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        toGeoLocation();
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                changeCamera(
                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                lng, 18, 30, 30)));
                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mRl.setVisibility(View.VISIBLE);
        MapTaskInfo mapTaskInfo = (MapTaskInfo) marker.getObject();
        mTv_taskname.setText(mapTaskInfo.getTaskName());
        mTv_approver.setText(mapTaskInfo.getTaskApprover());
        mTv_are.setText(mapTaskInfo.getTaskAre());
        mTv_description.setText(mapTaskInfo.getTaskDescription());
        mTv_state.setText(mapTaskInfo.getTaskState());
        mTv_pelease_people.setText(mapTaskInfo.getTaskPeleasePeople());
        mTv_time.setText(mapTaskInfo.getTaskTime());
        return false;
    }


    private void toGeoLocation() {
        mIsShowToast=true;
        mPoint = new Point(mScreenPosition.x, mScreenPosition.y);
        LatLng mLatlng = mAMap.getProjection().fromScreenLocation(mPoint);

        // 绘制一个圆形
        if (mCircle != null) {
            mCircle.remove();
            mCircle = null;
        }
        mCircle = mAMap.addCircle(new CircleOptions().center(mLatlng)
                .radius(1000).strokeColor(Color.argb(20, 1, 1, 1))
                .fillColor(Color.argb(20, 1, 1, 1)).strokeWidth(3));
        //圆形范围内是否有任务
        for(int i=0;i<mLatLngs.size();i++){

            if (mCircle.contains(mLatLngs.get(i))){
                if (mIsShowToast){
                    RxToast.success(MyApp.getApplictaion(),"当前范围内有任务记录",Toast.LENGTH_SHORT).show();
                    mIsShowToast = false;
                }
                MarkerOptions markerOptions = new MarkerOptions().anchor(0.5f, 0.5f)

                        .position(mLatLngs.get(i)).title("巡查任务"+i)
                        .snippet("经纬度："+mLatLngs.get(i)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                        .draggable(false).period(10);
                Marker marker = mAMap.addMarker(markerOptions);
                MapTaskInfo mapTaskInfo = new MapTaskInfo();
                mapTaskInfo.setTaskName("任务名称：巡查任务"+i);
                mapTaskInfo.setTaskState("任务状态：已审核待执行");
                mapTaskInfo.setTaskApprover("审批人：Dev2");
                mapTaskInfo.setTaskAre("巡查区域：珠江上游");
                mapTaskInfo.setTaskDescription("任务描述：违法采砂巡查");
                mapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
                mapTaskInfo.setTaskTime("发布时间：2017年12月14日");
                marker.setObject(mapTaskInfo);
            }
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        mAMap.moveCamera(update);

    }
}
