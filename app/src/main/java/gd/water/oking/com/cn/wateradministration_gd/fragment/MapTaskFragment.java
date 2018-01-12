package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Contants;
import gd.water.oking.com.cn.wateradministration_gd.bean.MapTaskInfo;

/**
 * 地图任务展示
 */
public class MapTaskFragment extends BaseFragment implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener ,AMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Marker mCenterMarker;
    private AMap mAMap;
    private MapView mMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private ArrayList<MarkerOptions> mMarkerOptionlst;
    private  Bundle mSavedInstanceState;
    private TextView mTv_taskname;
    private TextView mTv_state;
    private TextView mTv_pelease_people;
    private TextView mTv_description;
    private TextView mTv_approver;
    private TextView mTv_are;
    private TextView mTv_time;
    private RelativeLayout mRl;
    private MapTaskInfo mMapTaskInfo;

    public MapTaskFragment() {
        // Required empty public constructor
    }

    public static MapTaskFragment newInstance(String param1, String param2) {
        MapTaskFragment fragment = new MapTaskFragment();
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


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        return inflater.inflate(R.layout.fragment_map_task, container, false);
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

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
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
    public void onMapLoaded() {
        mAMap.setOnMarkerClickListener(this);
        addMarkersToMap();// 往地图上添加marker
    }

    private void addMarkersToMap() {
        mMarkerOptionlst = new ArrayList<MarkerOptions>();
//        MarkerOptions markerOption0 = new MarkerOptions().anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .draggable(false).period(10);
//
//        mMarkerOptionlst.add(markerOption0);



        MarkerOptions markerOption5 = new MarkerOptions().anchor(0.5f, 0.5f)

                .position(Contants.PAD_5).title("巡查任务1")
                .snippet("经纬度："+Contants.PAD_5).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false).period(10);
        mMarkerOptionlst.add(markerOption5);

        MarkerOptions markerOption6 = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(Contants.PAD_6).title("巡查任务2")
                .snippet("经纬度："+Contants.PAD_6).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false).period(10);
        mMarkerOptionlst.add(markerOption6);

        MarkerOptions markerOption7 = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(Contants.PAD_7).title("巡查任务3")
                .snippet("经纬度："+Contants.PAD_7).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false).period(10);
        mMarkerOptionlst.add(markerOption7);

        MarkerOptions markerOption8 = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(Contants.PAD_8).title("巡查任务4")
                .snippet("经纬度："+Contants.PAD_8).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false
                ).period(10);
        mMarkerOptionlst.add(markerOption8);

        MarkerOptions markerOption9 = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(Contants.PAD_9).title("巡查任务5")
                .snippet("经纬度："+Contants.PAD_8).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false
                ).period(10);
        mMarkerOptionlst.add(markerOption9);
        ArrayList<Marker> markers = mAMap.addMarkers(mMarkerOptionlst, true);
        ArrayList<MapTaskInfo> mapTaskInfos = new ArrayList<>();
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setTaskName("任务名称：巡查任务1");
        mMapTaskInfo.setTaskState("任务状态：已审核待执行");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江上游");
        mMapTaskInfo.setTaskDescription("任务描述：违法采砂巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月14日");
        mapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setTaskName("任务名称：巡查任务2");
        mMapTaskInfo.setTaskState("任务状态：待审核");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江中游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月11日");
        mapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setTaskName("任务名称：巡查任务3");
        mMapTaskInfo.setTaskState("任务状态：待审核");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江中游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月15日");
        mapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setTaskName("任务名称：巡查任务4");
        mMapTaskInfo.setTaskState("任务状态：正在执行");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江干流");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月15日");
        mapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setTaskName("任务名称：巡查任务5");
        mMapTaskInfo.setTaskState("任务状态：正在执行");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江下游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月13日");
        mapTaskInfos.add(mMapTaskInfo);
        for (int i=0;i<markers.size();i++){
            Marker marker = markers.get(i);
            marker.setObject(mapTaskInfos.get(i));
        }
//        mCenterMarker = markers.get(0);
//        LatLng latLng = mAMap.getCameraPosition().target;
//        Point screenPosition = mAMap.getProjection().toScreenLocation(latLng);
//        //设置Marker在屏幕上,不跟随地图移动
//        mCenterMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMyLocationChange(Location location) {

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
}
