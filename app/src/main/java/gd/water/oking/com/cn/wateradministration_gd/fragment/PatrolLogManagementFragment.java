package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vondear.rxtools.view.RxToast;

import org.apache.commons.lang3.StringUtils;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PatrolLogManagementAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.VerificationTaskAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.PatrolLogGson;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 巡查日志管理
 */
public class PatrolLogManagementFragment extends BaseFragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam2;
    private AMap mAMap;
    private MapView mMap;
    private Bundle mSavedInstanceState;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private List<PatrolLogGson.RowsBean> mRows;
    private PullToRefreshListView mRlv;
    private int offset = 10;
    private PatrolLogManagementAdapter mPatrolLogManagementAdapter;
    private TextView mTv_taskname;
    private TextView mTv_dep;
    private TextView mTv_taskid;
    private TextView mTv_dy;
    private TextView mTv_equipment;
    private TextView mTv_begintime;
    private TextView mTv_endtime;
    private TextView mTv_spld;
    private TextView mTv_tasktype;
    private TextView mTv_tasksource;
    private TextView mTv_taskplan;
    private TextView mTv_area;
    private TextView mTv_describe;
    private TextView mTv_examine;
    private long mTimeMillis;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private FragmentActivity mActivity;
    private Mission mMission;

    public PatrolLogManagementFragment() {
        // Required empty public constructor
    }

    public static PatrolLogManagementFragment newInstance(Mission param1, String param2) {
        PatrolLogManagementFragment fragment = new PatrolLogManagementFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMission = (Mission) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        return inflater.inflate(R.layout.fragment_patrol_log_management, container, false);
    }

    @Override
    public void initView(View rootView) {
        mMap = rootView.findViewById(R.id.map);
        mMap.onCreate(mSavedInstanceState);// 此方法必须重写
        mAMap = mMap.getMap();
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
//        mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mAMap.setMinZoomLevel(10);
        mAMap.setMaxZoomLevel(20);
        setUpMap();



        mRlv = rootView.findViewById(R.id.rlv);
        mTv_taskname = rootView.findViewById(R.id.tv_taskname);
        mTv_dep = rootView.findViewById(R.id.tv_dep);
        mTv_taskid = rootView.findViewById(R.id.tv_taskid);
        mTv_dy = rootView.findViewById(R.id.tv_dy);
        mTv_equipment = rootView.findViewById(R.id.tv_equipment);
        mTv_examine = rootView.findViewById(R.id.tv_examine);
        mTv_begintime = rootView.findViewById(R.id.tv_begintime);
        mTv_endtime = rootView.findViewById(R.id.tv_endtime);
        mTv_spld = rootView.findViewById(R.id.tv_spld);
        mTv_tasktype = rootView.findViewById(R.id.tv_tasktype);
        mTv_tasksource = rootView.findViewById(R.id.tv_tasksource);
        mTv_taskplan = rootView.findViewById(R.id.tv_taskplan);
        mTv_area = rootView.findViewById(R.id.tv_area);
        mTv_describe = rootView.findViewById(R.id.tv_describe);


        if (mMission!=null){
            mRlv.setScrollingWhileRefreshingEnabled(false);
            VerificationTaskAdapter verificationTaskAdapter = new VerificationTaskAdapter(getActivity(), mMission, R.layout.putted_forward_item);
            mRlv.setAdapter(verificationTaskAdapter);
        }else {
            mRlv.setScrollingWhileRefreshingEnabled(true);
            //mPullRefreshListView.getMode();//得到模式
            //上下都可以刷新的模式。这里有两个选择：Mode.PULL_FROM_START，Mode.BOTH，PULL_FROM_END
            mRlv.setMode(PullToRefreshBase.Mode.BOTH);
            mRlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    //设置下拉时显示的日期和时间
                    String label = DateUtils.formatDateTime(MyApp.getApplictaion(), System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                    // 更新显示的label
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                    long timeMillis = System.currentTimeMillis();
                    if (timeMillis - mTimeMillis > 5000) {
                        //获取数据
                        getNetData();

                        mRlv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRlv.onRefreshComplete();

                            }
                        }, 500);

                    } else {
//                    解决调用onRefreshComplete无效（时间太短）
                        mRlv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRlv.onRefreshComplete();

                            }
                        }, 1000);
                        RxToast.warning(MyApp.getApplictaion(), "数据刷新太频繁了,请稍后再试", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//获取更多数据
                    getMoreData();
                    mRlv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRlv.onRefreshComplete();

                        }
                    }, 500);
                }
            });





            getNetData();
        }


        setListener();

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

    private void setListener() {
        mRlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mMission!=null){
                    mTv_taskname.setText("任务名称：" + mMission.getTask_name());
                    mTv_taskid.setText("部门：" + mMission.getFbdw());
                    ArrayList<Member> members = mMission.getMembers();
                    String username="";
                    for (Member m:members){
                        if (!TextUtils.isEmpty(username)){

                            username= username+","+m.getUsername();
                        }else {
                            username= username+m.getUsername();
                        }
                    }
                    mTv_dy.setText("执法人员：" +username);
                    mTv_equipment.setText("任务编号：" + mMission.getId());
//                    mTv_equipment.setText("执法装备：" + rowsBean.getEQUIPMENT());
                    mTv_examine.setText("任务进展：已完成");
                    formatter.format(mMission.getExecute_start_time());
                    mTv_begintime.setText("开始时间：" +  formatter.format(mMission.getExecute_start_time()));
                    mTv_endtime.setText("结束时间：" + formatter.format(mMission.getExecute_end_time()));
                    mTv_spld.setText("审批领导：" + mMission.getApproved_person_name());
                    mTv_tasktype.setText("任务类型：" + mMission.getTypename());
//                    mTv_tasksource.setText("任务来源：" + rowsBean.getRWLY());
//                    mTv_taskplan.setText("任务计划：" + rowsBean.getPLANKEY());
                    mTv_area.setText("巡查区域：" + mMission.getRwqyms());
//                    mTv_describe.setText("巡查描述：" + mMission.getRwqyms());

                }else {
                    mPatrolLogManagementAdapter.setSelecte(position - 1);
                    PatrolLogGson.RowsBean rowsBean = mRows.get(position - 1);
                    String coordinate = rowsBean.getCOORDINATE();
                    if (coordinate!=null){
                        mMap.setVisibility(View.VISIBLE);
                        String s1 = StringUtils.substringBetween(coordinate, "[[[", "],");
                        String[] split = StringUtils.split(s1,",");
                        LatLng gps = convert(new LatLng(Double.parseDouble(split[1]), Double.parseDouble(split[0])), CoordinateConverter.CoordType.valueOf("GPS"));
                        addMarkersToMap(gps,rowsBean.getTASK_NAME());// 往地图上添加marker
                    }else {
                        mMap.setVisibility(View.GONE);
                    }
                    mTv_taskname.setText("任务名称：" + rowsBean.getTASK_NAME());
                    mTv_dep.setText("部门：" + rowsBean.getFBDW());
                    mTv_taskid.setText("任务编号：" + rowsBean.getTASK_ID());
                    mTv_dy.setText("执法人员及执法证号：" + rowsBean.getDY());
                    mTv_equipment.setText("执法装备：" + rowsBean.getEQUIPMENT());
                    mTv_examine.setText("任务进展：" + rowsBean.getEXAMINEKEY());
                    mTv_begintime.setText("开始时间：" + rowsBean.getBEGIN_TIME());
                    mTv_endtime.setText("结束时间：" + rowsBean.getEND_TIME());
                    mTv_spld.setText("审批领导：" + rowsBean.getLDQM());
                    mTv_tasktype.setText("任务类型：" + rowsBean.getRWLX());
                    mTv_tasksource.setText("任务来源：" + rowsBean.getRWLY());
                    mTv_taskplan.setText("任务计划：" + rowsBean.getPLANKEY());
                    mTv_area.setText("巡查区域：" + rowsBean.getAREA());
                    mTv_describe.setText("巡查描述：" + rowsBean.getPATROL());
                }


            }
        });
    }

    public void getMoreData() {
        offset += 10;
        //http://192.168.0.104:8080/gdWater/taskLog/getLogPageForAndroid?limit=10&offset=0
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/getLogPageForAndroid?limit=10&offset=" + offset);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Gson gson = new Gson();
                    PatrolLogGson patrolLogGson = gson.fromJson(result, PatrolLogGson.class);
                    List<PatrolLogGson.RowsBean> rows = patrolLogGson.getRows();
                    if (rows!=null&&rows.size() > 0) {
                        mRows.addAll(rows);
                        RxToast.success(MyApp.getApplictaion(), "加载更多成功", Toast.LENGTH_SHORT).show();
                        mPatrolLogManagementAdapter.notifyDataSetChanged();

                    } else {
                        RxToast.warning(MyApp.getApplictaion(), "服务器无更多数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    RxToast.warning(MyApp.getApplictaion(), "服务器无更多数据", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                RxToast.error(MyApp.getApplictaion(), "数据获取失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void getNetData() {
        offset = 10;
        //http://192.168.0.104:8080/gdWater/taskLog/getLogPageForAndroid?limit=10&offset=0
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/getLogPageForAndroid?limit=10&offset=0");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (!TextUtils.isEmpty(result)) {

                    mTimeMillis = System.currentTimeMillis();
                    Gson gson = new Gson();
                    PatrolLogGson patrolLogGson = gson.fromJson(result, PatrolLogGson.class);
                    mRows = patrolLogGson.getRows();

                    if (mRows!=null&&mRows.size()>0){
                        RxToast.success(MyApp.getApplictaion(), "数据获取成功", Toast.LENGTH_SHORT).show();
                        if (mPatrolLogManagementAdapter == null) {
                            mPatrolLogManagementAdapter = new PatrolLogManagementAdapter(mActivity,mRows, R.layout.putted_forward_item);
                            mRlv.setAdapter(mPatrolLogManagementAdapter);
                        } else {
                            mPatrolLogManagementAdapter.notifyDataSetChanged();
                        }
                    }else {
                        RxToast.warning(MyApp.getApplictaion(),"无记录",Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                RxToast.error(MyApp.getApplictaion(), "数据获取失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 根据类型 转换 坐标
     */
    private LatLng convert(LatLng sourceLatLng, CoordinateConverter.CoordType coord ) {
        CoordinateConverter converter  = new CoordinateConverter(MyApp.getApplictaion());
        // CoordType.GPS 待转换坐标类型
        converter.from(coord);
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }


    private void addMarkersToMap(LatLng gps, String taskName) {
        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)

                .position(gps).title("任务名称："+taskName)
                .snippet("经纬度："+gps).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                .draggable(false).period(10);
        mAMap.clear();
        Marker marker = mAMap.addMarker(markerOption);
        marker.showInfoWindow();
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        gps, 18, 30, 30)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.moveCamera(cameraUpdate);
    }
}
