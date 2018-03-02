package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Contants;
import gd.water.oking.com.cn.wateradministration_gd.bean.MapTaskInfo;
import gd.water.oking.com.cn.wateradministration_gd.bean.MarkerOptionInfo;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 地图任务展示
 */
public class MapTaskFragment extends BaseFragment implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AMap mAMap;
    private MapView mMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    //    private ArrayList<MarkerOptions> mMarkerOptionlst;
    private Bundle mSavedInstanceState;
    private TextView mTv_taskname;
    private TextView mTv_state;
    private TextView mTv_pelease_people;
    private TextView mTv_description;
    private TextView mTv_approver;
    private TextView mTv_are;
    private TextView mTv_time;
    private RelativeLayout mRl;
    private MapTaskInfo mMapTaskInfo;
    /**
     * 坐标点数组数据
     */
    private double[] coords = {23.234459161444516, 112.8114729970563,
            23.234319354496318, 112.81167560190744,
            23.234143304271104, 112.81183571779263,
            23.23406709077123, 112.8119377172778,
            23.234389527100365, 112.81139311095282,
            23.234579713087772, 112.81178969973902,
            23.234461254138907, 112.81211917466369,
            23.234393684494975, 112.81204051025058,
            23.234472937512454, 112.81183888058642,
            23.234417046858468, 112.81169039231924,
            23.234451402907357, 112.81182280178054,
            23.23444164300824, 112.81188143146471,
            23.233781894128345, 112.81219338185511,
            23.232668125027857, 112.81269356482568,
            23.231336492320402, 112.81308264111598,
            23.23049192144717, 112.81312998837963,
            23.229134181063408, 112.81306578150506,
            23.227345011397247, 112.81259071417007,
            23.22606218590359, 112.81206327557257,
            23.225277248596555, 112.81168521465942,
            23.223841704097623, 112.81102242813,
            23.22232165313181, 112.81045982779115,
            23.22135671328482, 112.81009792637622,
            23.22057759605435, 112.80985722393275,
            23.219195510664616, 112.8094532905785,
            23.217934251573208, 112.80856677258035,
            23.217723834430824, 112.80788874030586,
            23.217208541288986, 112.8062859113857,
            23.216617195583087, 112.80490123600151,
            23.216244923286347, 112.8037920566501,
            23.216214825937595, 112.8036822335851,
            23.216240864201108, 112.80308326186321,
            23.216561696197353, 112.80298505094613,
            23.21666802119845, 112.80299788588151,
            23.21660299254844, 112.80295671854384,
            23.216587449342082, 112.80291121348183,
            23.216536010931407, 112.80292541928308,
            23.21655288466756, 112.80289272134782,
            23.21656185939853, 112.80291387370337,
            23.216567711943092, 112.80303607347932,
            23.21656576432231, 112.80281126704615,
            23.216548859305966, 112.8028756340516,
            23.216560810869986, 112.80287287837952,
            23.216512825189554, 112.8029994698979,
            23.2164972735513, 112.8029827300431,
            23.216556558279773, 112.80300587298373,
            23.216542121893962, 112.80298447202641,
            23.21657134091864, 112.80296936329047,
            23.21666146870437, 112.80304111362766,
            23.216999723403138, 112.80312374386828,
            23.21705398685992, 112.8030470444334,
            23.21706461808483, 112.80313672365068,
            23.217012588402625, 112.80315316356526,
            23.2168313048019, 112.80305463862841,
            23.2167554983609, 112.80306345319501,
            23.216602888791414, 112.80301918640765,
            23.216509678493743, 112.80299845538765,
            23.216546848353424, 112.80297377942023,
            23.216549260217256, 112.8029782801787,
            23.216663859954945, 112.80281348053288,
            23.2167726386821, 112.80287719166486,
            23.217033284695884, 112.80299029312822,
            23.21699655899932, 112.80302525694945,
            23.216970615358697, 112.80302333280613,
            23.21699988979337, 112.80297215126866,
            23.217061488966465, 112.8029607024895,
            23.216987921251214, 112.80294501144814,
            23.21701841958066, 112.80291597092118,
            23.21707470677381, 112.80290923674184,
            23.21698909476372, 112.80290775064714,
            23.21696538729771, 112.80290312866707,
            23.216927695817933, 112.80293250504201,


    };
    private ListView mLv_task;
    private ArrayList<MapTaskInfo> mMapTaskInfos;
    private TextView mTv_lng;
    private ArrayList<MarkerOptions> mMarkerOptionlst;
    private boolean mIsOnClick = true;

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
        mLv_task = rootView.findViewById(R.id.lv_task);
        mTv_lng = rootView.findViewById(R.id.tv_lng);
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

        initData();
        initListeber();
    }

    private void initListeber() {
        mLv_task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mAMap.clear();
                MapTaskInfo mapTaskInfo = mMapTaskInfos.get(position);
                mRl.setVisibility(View.VISIBLE);
                mTv_taskname.setText(mapTaskInfo.getTaskName());
                mTv_approver.setText(mapTaskInfo.getTaskApprover());
                mTv_are.setText(mapTaskInfo.getTaskAre());
                mTv_description.setText(mapTaskInfo.getTaskDescription());
                mTv_state.setText(mapTaskInfo.getTaskState());
                mTv_pelease_people.setText(mapTaskInfo.getTaskPeleasePeople());
                mTv_time.setText(mapTaskInfo.getTaskTime());
                mTv_lng.setText("经纬度：" + mapTaskInfo.getTaskLatLng().toString());
                addMarkersToMap(mapTaskInfo);
                addPolylineInPlayGround(mapTaskInfo);
            }
        });
    }

    private void initData() {

        mMapTaskInfos = new ArrayList<>();
        mMapTaskInfo = new MapTaskInfo();
        ArrayList<LatLng> piclatLngs = new ArrayList<>();
        piclatLngs.add(new LatLng(23.217723834430824, 112.80788874030586));
        piclatLngs.add(new LatLng(23.225277248596555, 112.81168521465942));
        piclatLngs.add(new LatLng(23.22057759605435, 112.80985722393275));
        mMapTaskInfo.setPicLatLngs(piclatLngs);
        ArrayList<LatLng> recordScreenLngs = new ArrayList<>();
        recordScreenLngs.add(new LatLng(23.21666802119845, 112.80299788588151));
        recordScreenLngs.add(new LatLng(23.21656576432231, 112.80281126704615));
        recordScreenLngs.add(new LatLng(23.217012588402625, 112.80315316356526));
        mMapTaskInfo.setRecordScreenLatLngs(recordScreenLngs);
        mMapTaskInfo.setTaskName("任务名称：巡查任务1");
        mMapTaskInfo.setTaskState("任务状态：已完成");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江上游");
        mMapTaskInfo.setTaskDescription("任务描述：违法采砂巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月14日");
        mMapTaskInfo.setTaskLatLng(Contants.PAD_5);
        mMapTaskInfo.setMoveCenterLatLng(Contants.MOVECENTER);
        mMapTaskInfo.setStartLatLng(Contants.STARTLATLNG);
        mMapTaskInfo.setEndLatLng(Contants.ENDLATLNG);
        mMapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setPicLatLngs(piclatLngs);
        mMapTaskInfo.setRecordScreenLatLngs(recordScreenLngs);
        mMapTaskInfo.setTaskName("任务名称：巡查任务2");
        mMapTaskInfo.setTaskState("任务状态：已完成");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江中游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月11日");
        mMapTaskInfo.setTaskLatLng(Contants.PAD_6);
        mMapTaskInfo.setMoveCenterLatLng(Contants.MOVECENTER);
        mMapTaskInfo.setStartLatLng(Contants.STARTLATLNG);
        mMapTaskInfo.setEndLatLng(Contants.ENDLATLNG);
        mMapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setPicLatLngs(piclatLngs);
        mMapTaskInfo.setRecordScreenLatLngs(recordScreenLngs);
        mMapTaskInfo.setTaskName("任务名称：巡查任务3");
        mMapTaskInfo.setTaskState("任务状态：已完成");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江中游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月15日");
        mMapTaskInfo.setTaskLatLng(Contants.PAD_7);
        mMapTaskInfo.setMoveCenterLatLng(Contants.MOVECENTER);
        mMapTaskInfo.setStartLatLng(Contants.STARTLATLNG);
        mMapTaskInfo.setEndLatLng(Contants.ENDLATLNG);
        mMapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setPicLatLngs(piclatLngs);
        mMapTaskInfo.setRecordScreenLatLngs(recordScreenLngs);
        mMapTaskInfo.setTaskName("任务名称：巡查任务4");
        mMapTaskInfo.setTaskState("任务状态：已完成");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江干流");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月15日");
        mMapTaskInfo.setTaskLatLng(Contants.PAD_8);
        mMapTaskInfo.setMoveCenterLatLng(Contants.MOVECENTER);
        mMapTaskInfo.setStartLatLng(Contants.STARTLATLNG);
        mMapTaskInfo.setEndLatLng(Contants.ENDLATLNG);
        mMapTaskInfos.add(mMapTaskInfo);
        mMapTaskInfo = new MapTaskInfo();
        mMapTaskInfo.setPicLatLngs(piclatLngs);
        mMapTaskInfo.setRecordScreenLatLngs(recordScreenLngs);
        mMapTaskInfo.setTaskName("任务名称：巡查任务5");
        mMapTaskInfo.setTaskState("任务状态：已完成");
        mMapTaskInfo.setTaskApprover("审批人：Dev2");
        mMapTaskInfo.setTaskAre("巡查区域：珠江下游");
        mMapTaskInfo.setTaskDescription("任务描述：日常违法巡查");
        mMapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
        mMapTaskInfo.setTaskTime("发布时间：2017年12月13日");
        mMapTaskInfo.setTaskLatLng(Contants.PAD_9);
        mMapTaskInfo.setMoveCenterLatLng(Contants.MOVECENTER);
        mMapTaskInfo.setStartLatLng(Contants.STARTLATLNG);
        mMapTaskInfo.setEndLatLng(Contants.ENDLATLNG);
        mMapTaskInfos.add(mMapTaskInfo);

        mLv_task.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mMapTaskInfos.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View contentView, ViewGroup viewGroup) {
                if (contentView == null) {
                    contentView = View.inflate(getActivity(), R.layout.maptask_item, null);
                }
                TextView tv = contentView.findViewById(R.id.tv);
                String taskName = mMapTaskInfos.get(position).getTaskName();
                tv.setText(taskName);

                return contentView;
            }
        });
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
//        addMarkersToMap();// 往地图上添加marker
        mAMap.setOnMarkerClickListener(this);
    }

    private void addMarkersToMap(MapTaskInfo mapTaskInfo) {

        ArrayList<MarkerOptionInfo>markerOptionInfos = new ArrayList<>();

        //起点
        mMarkerOptionlst = new ArrayList<MarkerOptions>();
        MarkerOptions startMarkerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(mapTaskInfo.getStartLatLng())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start))
                .draggable(false).period(10);

        mMarkerOptionlst.add(startMarkerOption);
        MarkerOptionInfo startMarkerOptionInfo = new MarkerOptionInfo();
        startMarkerOptionInfo.setOptionType(Contants.STARTADNEDD);
        markerOptionInfos.add(startMarkerOptionInfo);
        //终点
        MarkerOptions endMarkerOption = new MarkerOptions().anchor(0.5f, 0.5f)
                .position(mapTaskInfo.getEndLatLng())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end))
                .draggable(false).period(10);

        mMarkerOptionlst.add(endMarkerOption);
        MarkerOptionInfo endMarkerOptionInfo = new MarkerOptionInfo();
        endMarkerOptionInfo.setOptionType(Contants.STARTADNEDD);
        markerOptionInfos.add(endMarkerOptionInfo);



        //图片
        ArrayList<LatLng> picLatLngs = mapTaskInfo.getPicLatLngs();
        for (LatLng lng:picLatLngs) {
            MarkerOptions picOptions = new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(lng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.taking_picture))
                    .draggable(false).period(10);
            mMarkerOptionlst.add(picOptions);
            MarkerOptionInfo markerOptionInfo = new MarkerOptionInfo();
            markerOptionInfo.setOptionType(Contants.PICTYPE);
            markerOptionInfos.add(markerOptionInfo);
        }

        //视频
        ArrayList<LatLng> recordScreenLatLngs = mapTaskInfo.getRecordScreenLatLngs();
        for (LatLng lng:recordScreenLatLngs){
            MarkerOptions recordScreenOptions = new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(lng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.record_screen))
                    .draggable(false).period(10);
            mMarkerOptionlst.add(recordScreenOptions);
            MarkerOptionInfo markerOptionInfo = new MarkerOptionInfo();
            markerOptionInfo.setOptionType(Contants.RECORDSCREENTYPE);
            markerOptionInfos.add(markerOptionInfo);
        }

        ArrayList<Marker> markers = mAMap.addMarkers(mMarkerOptionlst, true);

        for (int i = 0; i < markers.size(); i++) {
            Marker marker = markers.get(i);
            marker.setObject(markerOptionInfos.get(i));
        }
//        mCenterMarker = markers.get(0);
//        LatLng latLng = mAMap.getCameraPosition().target;
//        Point screenPosition = mAMap.getProjection().toScreenLocation(latLng);
        //设置Marker在屏幕上,不跟随地图移动
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

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        mRl.setVisibility(View.VISIBLE);
//        MapTaskInfo mapTaskInfo = (MapTaskInfo) marker.getObject();
//        mTv_taskname.setText(mapTaskInfo.getTaskName());
//        mTv_approver.setText(mapTaskInfo.getTaskApprover());
//        mTv_are.setText(mapTaskInfo.getTaskAre());
//        mTv_description.setText(mapTaskInfo.getTaskDescription());
//        mTv_state.setText(mapTaskInfo.getTaskState());
//        mTv_pelease_people.setText(mapTaskInfo.getTaskPeleasePeople());
//        mTv_time.setText(mapTaskInfo.getTaskTime());
//
//        changeCamera(
//                CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                        new LatLng(23.216240864201108,112.80308326186321), 18, 30, 30)));
//        addPolylineInPlayGround();
////        startMove();
//        return false;
//    }


    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs() {
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < coords.length; i += 2) {
            points.add(new LatLng(coords[i], coords[i + 1]));
        }
        return points;
    }


    /**
     * 个性化定制的信息窗口视图的类
     * 如果要定制化渲染这个信息窗口，需要重载getInfoWindow(Marker)方法。
     * 如果只是需要替换信息窗口的内容，则需要重载getInfoContents(Marker)方法。
     */
//    AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {
//
//        // 个性化Marker的InfoWindow 视图
//        // 如果这个方法返回null，则将会使用默认的信息窗口风格，内容将会调用getInfoContents(Marker)方法获取
//        @Override
//        public View getInfoWindow(Marker marker) {
//
//            return getInfoWindowView(marker);
//        }
//
//        // 这个方法只有在getInfoWindow(Marker)返回null 时才会被调用
//        // 定制化的view 做这个信息窗口的内容，如果返回null 将以默认内容渲染
//        @Override
//        public View getInfoContents(Marker marker) {
//
//            return getInfoWindowView(marker);
//        }
//    };

//    LinearLayout infoWindowLayout;
//    TextView title;
//    TextView snippet;
    private Polyline mPolyline;

    /**
     * 自定义View并且绑定数据方法
     *
     * @param marker 点击的Marker对象
     * @return 返回自定义窗口的视图
     */
//    private View getInfoWindowView(Marker marker) {
//        if (infoWindowLayout == null) {
//            infoWindowLayout = new LinearLayout(getActivity());
//            infoWindowLayout.setOrientation(LinearLayout.VERTICAL);
//            title = new TextView(getActivity());
//            snippet = new TextView(getActivity());
//            title.setTextColor(Color.BLACK);
//            snippet.setTextColor(Color.BLACK);
//            infoWindowLayout.setBackgroundResource(R.drawable.infowindow_bg);
//
//            infoWindowLayout.addView(title);
//            infoWindowLayout.addView(snippet);
//        }
//
//        return infoWindowLayout;
//    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        mAMap.moveCamera(update);

    }

    /**
     * 开始移动
     */
//    public void startMove() {
//        if (mPolyline == null) {
//            RxToast.error(MyApp.getApplictaion(), "请先设置路线", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 读取轨迹点
//        List<LatLng> points = readLatLngs();
//        // 构建 轨迹的显示区域
//        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
//        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//
//        // 实例 SmoothMoveMarker 对象
//        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAMap);
//        // 设置 平滑移动的 图标
//        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));
//
//        // 取轨迹点的第一个点 作为 平滑移动的启动
//        LatLng drivePoint = points.get(0);
//
//        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
//        points.set(pair.first, drivePoint);
//        List<LatLng> subList = points.subList(pair.first, points.size());
//
//        // 设置轨迹点
//        smoothMarker.setPoints(subList);
//        // 设置平滑移动的总时间  单位  秒
//        smoothMarker.setTotalDuration(40);
//
//        // 设置  自定义的InfoWindow 适配器
//        mAMap.setInfoWindowAdapter(infoWindowAdapter);
//        // 显示 infowindow
//        smoothMarker.getMarker().showInfoWindow();
//
//        // 设置移动的监听事件  返回 距终点的距离  单位 米
//        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
//            @Override
//            public void move(final double distance) {
//
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (infoWindowLayout != null && title != null) {
//
//                            title.setText("距离终点还有： " + (int) distance + "米");
//                        }
//                    }
//                });
//
//            }
//        });
//
//        // 开始移动
//        smoothMarker.startSmoothMove();
//
//    }


    /**
     * 添加轨迹线
     *
     * @param mapTaskInfo
     */
    private void addPolylineInPlayGround(MapTaskInfo mapTaskInfo) {
        List<LatLng> list = readLatLngs();
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));


//        changeCamera(
//                CameraUpdateFactory.newCameraPosition(new CameraPosition(mapTaskInfo.getMoveCenterLatLng(), 18, 30, 30)));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mIsOnClick){

            mIsOnClick = false;
            MarkerOptionInfo markerOptionInfo = (MarkerOptionInfo) marker.getObject();
            if (markerOptionInfo.getOptionType()==Contants.PICTYPE){
                Intent intent = new Intent(getActivity(), EaseShowBigImageActivity.class);
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/mission_pic/pic_test.jpg");

                if (file.exists()) {
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra("uri", uri);
                    getActivity().startActivity(intent);
                }else {
                    RxToast.error(MyApp.getApplictaion(),"图片加载出错",Toast.LENGTH_SHORT).show();
                }
            }else if(markerOptionInfo.getOptionType()==Contants.RECORDSCREENTYPE){

                Uri uri = Uri.parse("file:///"+Environment.getExternalStorageDirectory().getPath() + "/oking/mission_video/voce_test.mp4");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"video/mp4");
                getActivity().startActivity(intent);

            }else {

            }

            mIsOnClick = true;
        }


        return false;
    }
}
