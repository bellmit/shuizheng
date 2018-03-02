package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.vondear.rxtools.RxNetUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PicSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.TagAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.VideoSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.MyFlowTagLayout;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.MissionLog;
import gd.water.oking.com.cn.wateradministration_gd.bean.Point;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.GetMissionRecordFilePathParams;
import gd.water.oking.com.cn.wateradministration_gd.http.UpdateMissionRecordStateParams;
import gd.water.oking.com.cn.wateradministration_gd.http.UpdateMissionStateParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.main.ShootActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.VideoRecordActivity;
import gd.water.oking.com.cn.wateradministration_gd.model.CheckPicForServerModel;
import gd.water.oking.com.cn.wateradministration_gd.model.GetJobLogModel;
import gd.water.oking.com.cn.wateradministration_gd.model.UploadJobLogForPicModel;
import gd.water.oking.com.cn.wateradministration_gd.model.UploadJobLogTextModel;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class MissionRecordFragment extends BaseFragment implements AMap.OnMyLocationChangeListener {
    private OkingCallBack.MyCallBack mMyCallBack;
    private static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;
    private static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;

    SimpleDateFormat videosdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private Mission mission;
    private int picSize;
    private int picComPostion = 0;
    private int veodSize;
    private int veodComPosion = 0;
    private MissionLog log;
    private PicSimpleAdapter picadapter;
    private VideoSimpleAdapter videoadapter;
    private File picStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_pic");
    private File videoStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_video");
    private MapView mapView;
    private boolean isArcgis = false;
    private List<String> partList = new ArrayList<String>();
    private String parts = "";
    private Disposable mPicDisposable;
    private Disposable mVideoDisposable;
    private boolean swisopen = false;
    private boolean uploadLogPic, uploadSignPic, uploadLogVideo;
    private int uploadLogPicCount, uploadLogSignCount, uploadLogVideoCount;

    private EditText summaryEditText, leaderSummaryEditText, part_other_editText;
    private TextView type_nature, member_textView, equipment_textView, area_TextView;
    private Spinner planSpinner, itemSpinner;
    private GridView picGridView;
    private GridView videoGridView;
    private Button uploadBtn;
    private Button editEquipmentBtn;
    private MyFlowTagLayout partFlowTagLayout;
    private TagAdapter partTagAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_MISSIONLOG_UI_LIST:
                    if (picadapter != null) {
                        picadapter.notifyDataSetChanged();
                    }

                    if (videoadapter != null) {
                        videoadapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private BroadcastReceiver mNetWokReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connected = RxNetUtils.isConnected(MyApp.getApplictaion());
            if (connected) {
                RxToast.error(MyApp.getApplictaion(), "连接", Toast.LENGTH_SHORT).show();

            } else {
                if (mRxDialogLoading != null) {
                    RxToast.error(MyApp.getApplictaion(), "网络断开了~~请检查网络再进行提交数据", Toast.LENGTH_SHORT).show();
                    mRxDialogLoading.cancel();
                }
                if (mPicDisposable != null && mPicDisposable.isDisposed()) {
                    mPicDisposable.dispose();
                }

            }
        }
    };
    //    private File mDataPicFile;
    private SimpleDateFormat mSdf;
    private RxDialogLoading mRxDialogLoading;
    private TextView mTv_tasktype;
    private int mSelePlanPos;
    private int mSeleMattersPos;
    private Switch mSw;
    private boolean mCanSaveComplete;
    private Bundle mSavedInstanceState;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private  SharedPreferences mSp;
    public MissionRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_MISSIONLOG_UI_LIST));
        MyApp.getApplictaion().registerReceiver(mNetWokReceiver, new IntentFilter("oking.network"));
        final View rootView = inflater.inflate(R.layout.fragment_mission_record2, container, false);
        mSavedInstanceState = savedInstanceState;
        mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return rootView;
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        MyApp.getApplictaion().unregisterReceiver(mNetWokReceiver);
        mapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void initView(View rootView) {
        mSp = MyApp.getApplictaion().getSharedPreferences("fileLocation", Context.MODE_PRIVATE);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(mSavedInstanceState);
        ImageButton backBtn = rootView.findViewById(R.id.back_button);
        mAMap = mapView.getMap();
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
//        mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mAMap.setMinZoomLevel(10);
        mAMap.setMaxZoomLevel(20);
        setUpMap();
        mAMap.setOnMyLocationChangeListener(this);

        mSw = rootView.findViewById(R.id.sw);
        mTv_tasktype = (TextView) rootView.findViewById(R.id.tv_tasktype);
        if (mission != null) {
            if ("0".equals(mission.getTypeoftask())) {
                mTv_tasktype.setText("河道管理");
            } else if ("1".equals(mission.getTypeoftask())) {
                mTv_tasktype.setText("河道采砂");
            } else if ("2".equals(mission.getTypeoftask())) {
                mTv_tasktype.setText("水资源管理");
            } else if ("3".equals(mission.getTypeoftask())) {
                mTv_tasktype.setText("水土保持管理");
            } else if ("4".equals(mission.getTypeoftask())) {
                mTv_tasktype.setText("水利工程管理");
            }
        } else {
            return;
        }


        mSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                swisopen = isChecked;
                if (isChecked) {

                    summaryEditText.setEnabled(false);
                    summaryEditText.setText("");
                    leaderSummaryEditText.setEnabled(false);
                    leaderSummaryEditText.setText("");
                } else {
                    summaryEditText.setEnabled(true);
                    leaderSummaryEditText.setEnabled(true);
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mission.getStatus() == 5) {
                    MissionRecordFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
                } else {
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                    rxDialogSureCancel.setContent("未保存日志，是否保存？");
                    rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSureCancel.cancel();
                            localSaveRecord();
                            MissionRecordFragment.this.getParentFragment().getChildFragmentManager().popBackStack();

                        }
                    });
                    rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSureCancel.cancel();
                            MissionRecordFragment.this.getParentFragment().getChildFragmentManager().popBackStack();

                        }
                    });
                    rxDialogSureCancel.show();
                }
            }
        });

        part_other_editText = (EditText) rootView.findViewById(R.id.part_other_editText);
        part_other_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                parts = "";
                for (int i = 0; i < partFlowTagLayout.getChildCount(); i++) {
                    if (partFlowTagLayout.getChildAt(i).isSelected()) {
                        parts += ((TextView) (partFlowTagLayout.getChildAt(i).findViewById(R.id.tv_tag))).getText().toString() + ",";
                    }
                }
                if (!"".equals(part_other_editText.getText().toString())) {
                    parts += part_other_editText.getText().toString() + ",";
                }
                if (!"".equals(parts)) {
                    parts = parts.substring(0, parts.length() - 1);
                }
            }
        });
        equipment_textView = (TextView) rootView.findViewById(R.id.equipment_textView);
        summaryEditText = (EditText) rootView.findViewById(R.id.summary_editText);
        area_TextView = (TextView) rootView.findViewById(R.id.area_TextView);
        leaderSummaryEditText = (EditText) rootView.findViewById(R.id.leader_summary_editText);

        type_nature = (TextView) rootView.findViewById(R.id.type_nature);
        member_textView = (TextView) rootView.findViewById(R.id.member_textView);

        planSpinner = (Spinner) rootView.findViewById(R.id.plan_spinner);
        String[] planArray = getResources().getStringArray(R.array.spinner_plan);
        SpinnerArrayAdapter planArrayAdapter = new SpinnerArrayAdapter(planArray);
        planSpinner.setAdapter(planArrayAdapter);
        planSpinner.setSelection(log.getPlan());
        planSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelePlanPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        itemSpinner = (Spinner) rootView.findViewById(R.id.item_spinner);
        String[] mattersArray = getResources().getStringArray(R.array.spinner_matters);
        SpinnerArrayAdapter mattersArrayAdapter = new SpinnerArrayAdapter(mattersArray);
        itemSpinner.setAdapter(mattersArrayAdapter);
        itemSpinner.setSelection(log.getItem());
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSeleMattersPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        videoGridView = (GridView) rootView.findViewById(R.id.video_gridView);
        picGridView = (GridView) rootView.findViewById(R.id.pic_gridView);


        partFlowTagLayout = (MyFlowTagLayout) rootView.findViewById(R.id.part_flow_layout);
        partFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        partTagAdapter = new TagAdapter(MissionRecordFragment.this.getContext());
        partFlowTagLayout.setAdapter(partTagAdapter);
        partList = Arrays.asList("公安", "海事", "环保", "航道", "交通", "国土", "城管", "纪检");
        partTagAdapter.onlyAddAll(partList);
        partFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    ((TextView) (parent.getChildAt(i).findViewById(R.id.tv_tag))).setTextColor(getResources().getColor(R.color.colorMain4));
                }

                parts = "";
                for (int i = 0; i < selectedList.size(); i++) {
                    parts += partList.get(selectedList.get(i)).toString() + ",";
                    ((TextView) (parent.getChildAt(selectedList.get(i)).findViewById(R.id.tv_tag))).setTextColor(getResources().getColor(R.color.colorMain6));
                }
                if (!"".equals(part_other_editText.getText().toString())) {
                    parts += part_other_editText.getText().toString() + ",";
                }
                if (!"".equals(parts)) {
                    parts = parts.substring(0, parts.length() - 1);
                }
            }
        });

        editEquipmentBtn = (Button) rootView.findViewById(R.id.edit_Equipment_Btn);
        editEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = MissionRecordFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                SetEquipmentFragment f = new SetEquipmentFragment();
                f.setData_textView(equipment_textView);

                ft.add(R.id.fragment_root, f).commit();
            }
        });

        final Button signBtn = (Button) rootView.findViewById(R.id.sign_btn);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localSaveRecord();

                FragmentTransaction ft = MissionRecordFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                MemberSignFragment f = new MemberSignFragment();
                f.setLog(log);
                ft.replace(R.id.fragment_root, f).commit();
            }
        });

        uploadBtn = (Button) rootView.findViewById(R.id.upload_button);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            httpSaveRecord();
                localSaveRecord();
                MissionRecordFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        final Button completeMissionBtn = (Button) rootView.findViewById(R.id.complete_mission_button);
        completeMissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                rxDialogSureCancel.setContent("完成巡查后将停止记录巡查定位，是否继续？");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxToast.warning(MyApp.getApplictaion(), "请去签名!!", Toast.LENGTH_SHORT, true).show();

                        //任务结束时间记录到本地
                        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);
                        if (sharedPreferences.getLong(mission.getId(), 0) == 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            mission.setExecute_end_time(System.currentTimeMillis());
                            editor.putLong(mission.getId(), mission.getExecute_end_time());
                            editor.commit();

                            mission.setStatus(MainActivity.Mission_Completed);

                            completeMissionBtn.setVisibility(View.GONE);
                            signBtn.setVisibility(View.VISIBLE);
                        }

                        if (DefaultContants.ISHTTPLOGIN) {
                            UpdateMissionStateParams params = new UpdateMissionStateParams();
                            params.id = mission.getId();
                            params.status = 4;

                            SharedPreferences sharedPreferencesStart = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                            SharedPreferences sharedPreferencesEnd = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);

                            if (mission.getExecute_start_time() == null) {
                                params.execute_start_time = mSdf.format(sharedPreferencesStart.getLong(mission.getId(), 0));
                            } else {
                                params.execute_start_time = mSdf.format(mission.getExecute_start_time());
                            }

                            if (mission.getExecute_end_time() == null) {
                                params.execute_end_time = mSdf.format(sharedPreferencesEnd.getLong(mission.getId(), 0));
                            } else {
                                params.execute_end_time = mSdf.format(mission.getExecute_end_time());
                            }

                        }

                        localSaveRecord();
                        rxDialogSureCancel.cancel();

                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();

            }
        });

        Button reportMissionBtn = (Button) rootView.findViewById(R.id.report_mission_button);
        reportMissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                rxDialogSureCancel.setContent("上报任务后不能修改日志，是否继续？");
                rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCanSaveComplete = true;

                        if (log.getMembers().size() == 0) {
                            mCanSaveComplete = false;
                        }

                        if (mission.getStatus() != MainActivity.Mission_Completed &&
                                mission.getStatus() != 9) {
                            RxToast.warning(MyApp.getApplictaion(), "任务未完成，不能上报", Toast.LENGTH_SHORT, true).show();

                            return;
                        }

                        Observable.fromArray(log.getMembers()).flatMap(new Function<ArrayList<Member>, Observable<Member>>() {

                            @Override
                            public Observable<Member> apply(ArrayList<Member> memberArrayList) throws Exception {
                                return Observable.fromIterable(memberArrayList);
                            }
                        }).subscribe(new Consumer<Member>() {
                            @Override
                            public void accept(Member member) throws Exception {
                                if (member.getSignPic() == null) {
                                    mCanSaveComplete = false;
                                } else {
                                    File file = new File(FileUtil.PraseUritoPath(getContext(), member.getSignPic()));
                                    if (!file.exists()) {
                                        mCanSaveComplete = false;
                                    }
                                }
                            }
                        });


                        if (mCanSaveComplete) {
                            if (!swisopen && TextUtils.isEmpty(summaryEditText.getText().toString().trim())) {
                                RxToast.warning(MyApp.getApplictaion(), "巡查情况未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();

                                return;
                            }

                            if (!swisopen && TextUtils.isEmpty(leaderSummaryEditText.getText().toString().trim())) {
                                RxToast.warning(MyApp.getApplictaion(), "处理结果未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                                return;
                            }

                            if (mRxDialogLoading == null) {

                                mRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogInterface.cancel();
                                    }
                                });
                            }
                            rxDialogSureCancel.cancel();
                            mRxDialogLoading.setLoadingText("上传数据中...图片："+picComPostion+"/" + log.getPhotoUriList().size() + "视频："+veodComPosion+"/" + log.getVideoUriList().size());
                            mRxDialogLoading.show();
                            //先保存数据
                            localSaveRecord();
                            //上传数据
                            httpSaveRecord();

                        } else {
                            RxToast.warning(MyApp.getApplictaion(), "存在成员未签名，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                            return;
                        }

                    }
                });
                rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSureCancel.cancel();
                    }
                });
                rxDialogSureCancel.show();

            }
        });

        if (log != null) {
            picadapter = new PicSimpleAdapter(log.getPhotoUriList(), this, mission.getStatus() != 5 && mission.getStatus() != 9, mission.getTypename());//mission.getPhotoList()为图片uri的list
            picadapter.setOnClickListener(new PicSimpleAdapter.OnClickListener() {
                @Override
                public void onAddPic() {


                    Intent intent = new Intent(getActivity(), ShootActivity.class);
                    MissionRecordFragment.this.startActivityForResult(intent, PHOTO_FROM_CAMERA);


                }

                @Override
                public void onLongItemClick(final PicSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                    rxDialogSureCancel.setContent("是否删除原图片？");
                    rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = data.get(position);
                            String path = null;

                            path = FileUtil.PraseUritoPath(getContext(), uri);

                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();

                            }

                            log.getPhotoUriList().remove(position);

                            picadapter.notifyDataSetChanged();


                            String logStr = DataUtil.toJson(log);

                            //记录日志记录
                            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(log.getTask_id(), logStr);
                            editor.commit();
                            rxDialogSureCancel.cancel();

                        }
                    });
                    rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();

                }
            });
            picGridView.setAdapter(picadapter);

            videoadapter = new VideoSimpleAdapter(log.getVideoUriList(), this, mission.getStatus() != 5 && mission.getStatus() != 9, mission.getTypename());
            videoadapter.setOnClickListener(new VideoSimpleAdapter.OnClickListener() {
                @Override
                public void onAddVideo() {

                    Intent intent = new Intent();
                    if (!videoStorageDir.exists()) {
                        videoStorageDir.mkdirs();
                    }


                    intent.setClass(getActivity(), VideoRecordActivity.class);
                    MissionRecordFragment.this.startActivityForResult(intent, VIDEO_FROM_CAMERA);

                }

                @Override
                public void onLongItemClick(final VideoSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {

                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(getContext());//提示弹窗
                    rxDialogSureCancel.setContent("是否删除原视频？");
                    rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = data.get(position);
                            String path = null;

                            path = FileUtil.PraseUritoPath(getContext(), uri);

                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();
                            }
                            log.getVideoUriList().remove(position);

                            videoadapter.notifyDataSetChanged();


                            String logStr = DataUtil.toJson(log);

                            //记录日志记录
                            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(log.getTask_id(), logStr);
                            editor.commit();
                            rxDialogSureCancel.cancel();

                        }
                    });
                    rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();

                }
            });
            videoGridView.setAdapter(videoadapter);

            //设置界面数据
            type_nature.setText(mission.getTypename());
            area_TextView.setText(mission.getRwqyms());
            summaryEditText.setText(log.getPatrol());
            leaderSummaryEditText.setText(log.getDzyj());
            equipment_textView.setText(log.getEquipment());
            //签名
            String memberStr = "";
            if (log.getMembers() != null) {
                for (int i = 0; i < log.getMembers().size(); i++) {
                    Member member = log.getMembers().get(i);

                    if (member.getUsername().equals(mission.getReceiver_name())) {
                        memberStr += "<font color=\"#A1A8AE\">任务负责人：</font>";
                        if (member.getSignPic() == null || !new File(FileUtil.PraseUritoPath(getContext(), member.getSignPic())).exists()) {
                            memberStr += "<font color=\"#A1A8AE\">" + mission.getReceiver_name() + "&emsp;&emsp;</font>";
                        } else {
                            memberStr += "<font color=\"#98CF60\">" + mission.getReceiver_name() + "&emsp;&emsp;</font>";
                        }
                        memberStr += "<font color=\"#A1A8AE\">队员：</font>";
                        continue;
                    }

                    if (member.getSignPic() == null || !new File(FileUtil.PraseUritoPath(getContext(), member.getSignPic())).exists()) {
                        //未签名
                        memberStr += "<font color=\"#A1A8AE\">" + member.getUsername() + "&emsp;</font>";
                    } else {
                        //已签名
                        memberStr += "<font color=\"#98CF60\">" + member.getUsername() + "&emsp;</font>";
                    }
                }
            }
            member_textView.setText(Html.fromHtml(memberStr));

            //联合执法部门
            if (log.getOther_part() != null) {
                String[] partsArray = log.getOther_part().split(",");
                String partStr = "";
                List<String> selectData = new ArrayList<>();
                parts = "";
                for (int i = 0; i < partsArray.length; i++) {
                    boolean isCheckbox = false;
                    for (int j = 0; j < partList.size(); j++) {
                        if (partsArray[i].equals(partList.get(j).toString())) {
                            selectData.add(partsArray[i]);
                            parts += partsArray[i] + ",";
                            isCheckbox = true;
                            break;
                        }
                    }

                    if (!isCheckbox) {
                        partStr += partsArray[i] + ",";
                    }
                }
                if (!"".equals(part_other_editText.getText().toString())) {
                    parts += part_other_editText.getText().toString() + ",";
                }
                if (!"".equals(parts)) {
                    parts = parts.substring(0, parts.length() - 1);
                }

                partTagAdapter.setmSelectDataList(selectData);

                if (!"".equals(partStr)) {
                    part_other_editText.setText(partStr.substring(0, partStr.length() - 1));
                }
            }

//            handler.post(updateLocationRunnable);//启动更新定位线程

            signBtn.setVisibility(View.INVISIBLE);

            if (mission.getStatus() == MainActivity.Mission_Completed) {//任务已完成，不能再完成
                completeMissionBtn.setVisibility(View.GONE);

                signBtn.setVisibility(View.VISIBLE);
            }

            if (mission.getStatus() == 5) {//任务已上报，不能再上报
                uploadBtn.setVisibility(View.GONE);
                completeMissionBtn.setVisibility(View.GONE);
                reportMissionBtn.setVisibility(View.GONE);

                planSpinner.setClickable(false);
                itemSpinner.setClickable(false);

//                for (int i = 0; i < partList.size(); i++) {
//                    partList.get(i).setEnabled(false);
//                }
                partFlowTagLayout.setEnabled(false);

                if (!"".equals(part_other_editText.getText().toString())) {
                    part_other_editText.setEnabled(false);
                } else {
                    part_other_editText.setVisibility(View.GONE);
                }
                editEquipmentBtn.setVisibility(View.GONE);

//                summaryEditText.setEnabled(false);
//                leaderSummaryEditText.setEnabled(false);
                summaryEditText.setFocusable(false);
                leaderSummaryEditText.setFocusable(false);

                signBtn.setVisibility(View.VISIBLE);
            }

            if (mission.getStatus() == 9) {//任务被退回修改
                uploadBtn.setVisibility(View.GONE);
                completeMissionBtn.setVisibility(View.GONE);

                planSpinner.setEnabled(false);
                itemSpinner.setEnabled(false);
//                for (int i = 0; i < partList.size(); i++) {
//                    partList.get(i).setEnabled(false);
//                }
                partFlowTagLayout.setEnabled(false);
                if (!"".equals(part_other_editText.getText().toString())) {
                    part_other_editText.setEnabled(false);
                } else {
                    part_other_editText.setVisibility(View.GONE);
                }
                editEquipmentBtn.setVisibility(View.GONE);

                signBtn.setVisibility(View.VISIBLE);
            }

            equipment_textView.setHeight(equipment_textView.getLineCount() * equipment_textView.getLineHeight());

//            showAnimation();
        }

    }

    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 定位的类型为跟随模式LOCATION_TYPE_FOLLOW;  定位的类型为只定位模式模式LOCATION_TYPE_SHOW
        mAMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        mAMap.setMapCustomEnable(true); //开启自定义样式
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void setLog(MissionLog log) {
        this.log = log;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(MissionRecordFragment.this.getActivity().getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }

                    String path = FileUtil.PraseUritoPath(getContext(), uri);
                    FileUtil.compressImage(path);

                    log.getPhotoUriList().add(uri);
                    picadapter.notifyDataSetChanged();

                    localSaveRecord();
                    break;
                case PHOTO_FROM_CAMERA:
                    ArrayList<String> picpaths = data.getStringArrayListExtra("picpaths");
                    if (picpaths != null && picpaths.size() > 0) {
                        for (String s : picpaths) {
                            log.getPhotoUriList().add(Uri.parse("file://" + s));
                        }
                        picadapter.notifyDataSetChanged();
                    }


                    //通知系统扫描文件
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(picStorageDir));
                    MissionRecordFragment.this.getContext().sendBroadcast(intent);

                    localSaveRecord();
                    break;
                case VIDEO_FROM_CAMERA:
                    if (data != null) {
                        Uri videouri = data.getData();

                        log.getVideoUriList().add(videouri);
                        videoadapter.notifyDataSetChanged();


                        //通知系统扫描文件
                        Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent2.setData(videouri);
                        MissionRecordFragment.this.getContext().sendBroadcast(intent2);

                        localSaveRecord();
                    }

                    break;
                case VIDEO_FROM_GALLERY:
                    Uri videoUri = data.getData();
                    log.getVideoUriList().add(videoUri);
                    videoadapter.notifyDataSetChanged();

                    localSaveRecord();
                    break;

                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setMapLocation() {

//        Point p = FileUtil.getLastLocationFromFile(System.currentTimeMillis(), 30 * 60 * 1000);


//        if (p != null) {
//            String urlMap = DefaultContants.SERVER_HOST + "/arcgis/xcdgl/xclog.jsp?x=" + p.getLongitude() + "&y=" + p.getLatitude() + "&level=3";
//            DefaultContants.syncCookie(urlMap);
//            mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
//                    .setAgentWebParent(mRl_web, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
//                    .useDefaultIndicator()//
//                    .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
//                        @Override
//                        public void onReceivedTitle(WebView webView, String s) {
//
//                        }
//                    }) //设置 Web 页面的 title 回调
//                    .createAgentWeb()//
//                    .ready()
//                    .go(urlMap);
//
//
//        }

//        if ((isArcgis && DefaultContants.ISHTTPLOGIN) || mapView == null) {
//            return;
//        }
//
//        mapView.setVisibility(View.VISIBLE);
////        mRl_web.setVisibility(View.GONE);
//        if (p != null) {
//            CoordinateConverter converter = new CoordinateConverter(getContext());
//            converter.from(CoordinateConverter.CoordType.GPS);
//            LatLng sourceLatLng = new LatLng(p.getLatitude(), p.getLongitude());
//            converter.coord(sourceLatLng);
//            LatLng desLatLng = converter.convert();
//            mapView.getMap().moveCamera(CameraUpdateFactory.newLatLng(desLatLng));
//
//            mapView.getMap().clear();
//
//            Marker marker = mapView.getMap().addMarker(new MarkerOptions()
//                    .position(desLatLng)
//                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                            .decodeResource(getResources(), R.drawable.location)))
//                    .draggable(true));
//        } else {
//            mapView.getMap().clear();
//        }
    }

    //检查是否所有图片已上传
    private void checkChangeState() {
        if (uploadLogPic && uploadSignPic && uploadLogVideo) {
            httpCompleteMission();

            if (mRxDialogLoading != null) {
                mRxDialogLoading.cancel();
            }
        }
    }

    //
    private void uploadPic() {

        new CheckPicForServerModel(log, new OkingCallBack.CheckPicForServer() {
            @Override
            public void checkedSucc(String result) {
                ArrayList<Uri> photoUriList = new ArrayList<>();
                try {
                    JSONArray paths = new JSONArray(result);
                    for (int i = 0; i < paths.length(); i++) {
                        String[] p = paths.getJSONObject(i).getString("path").split("/");
                        String fileName = p[p.length - 1];

                        if (!picStorageDir.exists()) {
                            picStorageDir.mkdirs();
                        }
                        File file = new File(picStorageDir, fileName);
                        photoUriList.add(Uri.fromFile(file));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(MyApp.getApplictaion(), "服务器内部错误", Toast.LENGTH_SHORT, true).show();

                }

                final ArrayList<Uri> photoList = new ArrayList<>();
                for (int i = 0; i < log.getPhotoUriList().size(); i++) {
                    boolean isUpload = true;

                    for (int j = 0; j < photoUriList.size(); j++) {
                        String path1 = FileUtil.PraseUritoPath(getContext(), photoUriList.get(j));
                        String path2 = FileUtil.PraseUritoPath(getContext(), log.getPhotoUriList().get(i));
                        if (path1.equals(path2)) {
                            isUpload = false;
                            break;
                        }
                    }

                    if (isUpload) {
                        photoList.add(log.getPhotoUriList().get(i));
                    }
                }

                uploadLogPicCount = 0;
                if (photoList.size() > 0) {
                    picSize = photoList.size();

                    Observable.fromArray(photoList).flatMap(new Function<ArrayList<Uri>, Observable<Uri>>() {

                        @Override
                        public Observable<Uri> apply(ArrayList<Uri> uris) throws Exception {
                            return Observable.fromIterable(uris);
                        }
                    }).subscribeOn(Schedulers.io()).subscribe(new Observer<Uri>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mPicDisposable = d;
                        }

                        @Override
                        public void onNext(Uri uri) {
                            new UploadJobLogForPicModel(mSp,log, uri, mSdf, new OkingCallBack.UploadJobLogForPic() {
                                @Override
                                public void uploadSucc(String result) {
                                    picComPostion++;
                                    mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + log.getPhotoUriList().size() + "视频：" + veodComPosion + "/" + log.getVideoUriList().size());
                                    try {
                                        JSONObject object = new JSONObject(result);
                                        int code = object.getInt("code");
                                        if (code == 200) {
                                            uploadLogPicCount += 1;
                                            if (uploadLogPicCount == photoList.size()) {
                                                uploadLogPic = true;
                                                checkChangeState();
                                            }
                                        } else {
                                            if (mRxDialogLoading != null) {
                                                mRxDialogLoading.cancel();
                                            }
                                            RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！1", Toast.LENGTH_SHORT, true).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        if (mRxDialogLoading != null) {
                                            mRxDialogLoading.cancel();
                                        }
                                        RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！2", Toast.LENGTH_SHORT, true).show();

                                    }
                                }

                                @Override
                                public void uploadFail(Throwable ex) {
                                    mPicDisposable.dispose();
                                    if (mRxDialogLoading != null) {
                                        mRxDialogLoading.cancel();
                                    }
                                    RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！3", Toast.LENGTH_SHORT, true).show();

                                }

                                @Override
                                public void uploadPositionFail(Throwable ex) {
                                    RxToast.error(MyApp.getApplictaion(), "位置数据解析异常", Toast.LENGTH_SHORT, true).show();

                                }
                            }).uploadJobLogForPic();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

                } else {
                    uploadLogPic = true;
                    checkChangeState();
                }

            }

            @Override
            public void checkedFail(Throwable ex) {
                if (mRxDialogLoading != null) {
                    mRxDialogLoading.cancel();
                }
                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！4", Toast.LENGTH_SHORT, true).show();

            }
        }).checkedPicForServer();
    }

    private void uploadVideo() {
        final GetMissionRecordFilePathParams params3 = new GetMissionRecordFilePathParams();
        params3.log_id = log.getId();
        params3.type = 2;//巡查视频
        Callback.Cancelable cancelable3 = x.http().post(params3, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i("GetMisRecordFilePath", "onSuccess>>>>>>" + result);

                ArrayList<Uri> videoUriList = new ArrayList<>();
                try {
                    JSONArray paths = new JSONArray(result);
                    for (int i = 0; i < paths.length(); i++) {
                        String[] p = paths.getJSONObject(i).getString("path").split("/");
                        String fileName = p[p.length - 1];

                        if (!videoStorageDir.exists()) {
                            videoStorageDir.mkdirs();
                        }
                        File file = new File(videoStorageDir, fileName);
                        videoUriList.add(Uri.fromFile(file));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(MyApp.getApplictaion(), "检索信息失败！", Toast.LENGTH_SHORT, true).show();

                }

                final ArrayList<Uri> videoList = new ArrayList<>();
                for (int i = 0; i < log.getVideoUriList().size(); i++) {
                    boolean isUpload = true;

                    for (int j = 0; j < videoUriList.size(); j++) {
                        String path1 = FileUtil.PraseUritoPath(getContext(), videoUriList.get(j));
                        String path2 = FileUtil.PraseUritoPath(getContext(), log.getVideoUriList().get(i));
                        if (path1.equals(path2)) {
                            isUpload = false;
                            break;
                        }
                    }

                    if (isUpload) {
                        videoList.add(log.getVideoUriList().get(i));
                    }
                }

                uploadLogVideoCount = 0;
                if (videoList.size() > 0) {
                    veodSize = videoList.size();
                    Observable.fromArray(videoList).flatMap(new Function<ArrayList<Uri>, Observable<Uri>>() {

                        @Override
                        public Observable<Uri> apply(ArrayList<Uri> uris) throws Exception {
                            return Observable.fromIterable(uris);
                        }
                    }).subscribeOn(Schedulers.io())
                            .subscribe(new Observer<Uri>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    mVideoDisposable = d;
                                }

                                @Override
                                public void onNext(Uri value) {
                                    //上传视频封面图片

                                    RequestParams upVideoPicParams = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/mobile/uploadfile");
                                    // 使用multipart表单上传文件
                                    upVideoPicParams.setMultipart(true);
                                    // 加到url里的参数, http://xxxx/s?wd=xUtils
                                    upVideoPicParams.addQueryStringParameter("logId", log.getId());
                                    upVideoPicParams.addQueryStringParameter("type", "3");
                                    upVideoPicParams.addQueryStringParameter("smallImg", "");
                                    // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.


                                    final File videoFile = new File(FileUtil.PraseUritoPath(getContext(), value));
                                    Bitmap bitmap = null;
                                    bitmap = ThumbnailUtils.createVideoThumbnail(videoFile.getPath(),
                                            MediaStore.Images.Thumbnails.MICRO_KIND);
                                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 1000, 569, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                                    final File bitmapFile = new File(videoFile.getParent(), videoFile.getName().split("\\.")[0] + ".jpg");
                                    if (!bitmapFile.exists()) {
                                        try {
                                            bitmapFile.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            if (mRxDialogLoading != null) {
                                                mRxDialogLoading.cancel();
                                            }
                                            RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！5", Toast.LENGTH_SHORT, true).show();
                                        }
                                    }
                                    try {
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(bitmapFile));
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                        bos.flush();
                                        bos.close();
                                        bitmap.recycle();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    upVideoPicParams.addBodyParameter(
                                            "files", bitmapFile, null); // 如果文件没有扩展名, 最好设置contentType参数.

                                    x.http().post(upVideoPicParams, new ProgressCallback<String>() {
                                        @Override
                                        public void onSuccess(String result) {

                                            try {
                                                JSONObject object = new JSONObject(result);
                                                int code = object.getInt("code");
                                                if (code == 200) {
                                                    if (bitmapFile.exists()) {
                                                        bitmapFile.delete();
                                                    }
                                                    String path = object.getString("path");
                                                    //上传视频

                                                    RequestParams upVideoParams = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/mobile/uploadfile");
                                                    // 使用multipart表单上传文件
                                                    upVideoParams.setMultipart(true);
                                                    // 加到url里的参数, http://xxxx/s?wd=xUtils
                                                    upVideoParams.addQueryStringParameter("logId", log.getId());
                                                    upVideoParams.addQueryStringParameter("type", "2");
                                                    upVideoParams.addQueryStringParameter("smallImg", path);
                                                    // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.

                                                    upVideoParams.addBodyParameter(
                                                            "files", videoFile, null);

                                                    String filename = videoFile.getName().split("\\.")[0];

                                                    String  ext = null;

                                                    try {
                                                        Point location = new Point();
                                                        String locationstr = mSp.getString(filename, "");
                                                        if (!TextUtils.isEmpty(locationstr)) {
                                                            String[] split = locationstr.split(",");
                                                            if (!split[0].equals("null")){
                                                                location.setLatitude(Double.parseDouble(split[0]));
                                                                location.setLongitude(Double.parseDouble(split[1]));
                                                                location.setDatetime(videosdf.parse(filename).getTime());
                                                                ext = new Gson().toJson(location);
                                                            }

                                                        }else {
                                                            Map<String, String> map = new HashMap<>();
                                                            map.put("datetime", mSdf.format(videosdf.parse(filename)));
                                                            ext = new Gson().toJson(map);
                                                        }


                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                        if (mRxDialogLoading != null) {
                                                            mRxDialogLoading.cancel();
                                                        }
                                                        RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！6", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                    upVideoParams.addBodyParameter("ext", ext);
                                                    x.http().post(upVideoParams, new Callback.CommonCallback<String>() {
                                                        @Override
                                                        public void onSuccess(String result) {
                                                            Log.i("UploadVideo", "onSuccess>>>>>>" + result);
                                                            try {
                                                                JSONObject object = new JSONObject(result);
                                                                int code = object.getInt("code");
                                                                if (code == 200) {
                                                                    veodComPosion++;
                                                                    mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + log.getPhotoUriList().size() + "视频：" + veodComPosion + "/" + log.getVideoUriList().size());


                                                                    uploadLogVideoCount += 1;
                                                                    if (uploadLogVideoCount == videoList.size()) {
                                                                        uploadLogVideo = true;
                                                                        checkChangeState();
                                                                    }
                                                                } else {
                                                                    if (mRxDialogLoading != null) {
                                                                        mRxDialogLoading.cancel();
                                                                    }
                                                                    RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！7", Toast.LENGTH_SHORT, true).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                if (mRxDialogLoading != null) {
                                                                    mRxDialogLoading.cancel();
                                                                }
                                                                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！8", Toast.LENGTH_SHORT, true).show();
                                                            }

                                                        }

                                                        @Override
                                                        public void onError(Throwable ex, boolean isOnCallback) {
                                                            Log.i("UploadVideo", "onError>>>>>>" + ex.toString());
                                                            if (mRxDialogLoading != null) {
                                                                mRxDialogLoading.cancel();
                                                            }
                                                            mVideoDisposable.dispose();
                                                            RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！9", Toast.LENGTH_SHORT, true).show();
                                                        }

                                                        @Override
                                                        public void onCancelled(CancelledException cex) {

                                                        }

                                                        @Override
                                                        public void onFinished() {

                                                        }
                                                    });
                                                } else {
                                                    if (mRxDialogLoading != null) {
                                                        mRxDialogLoading.cancel();
                                                    }
                                                    RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！10", Toast.LENGTH_SHORT, true).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                if (mRxDialogLoading != null) {
                                                    mRxDialogLoading.cancel();
                                                }
                                                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！11", Toast.LENGTH_SHORT, true).show();
                                            }

                                        }

                                        @Override
                                        public void onError(Throwable ex, boolean isOnCallback) {
                                            if (mRxDialogLoading != null) {
                                                mRxDialogLoading.cancel();
                                            }
                                            mVideoDisposable.dispose();
                                            RxToast.error(getContext(), "当前4G网络不稳定，上传失败，请稍后重试！12", Toast.LENGTH_SHORT, true).show();

                                        }

                                        @Override
                                        public void onCancelled(CancelledException cex) {

                                        }

                                        @Override
                                        public void onFinished() {

                                        }

                                        @Override
                                        public void onWaiting() {

                                        }

                                        @Override
                                        public void onStarted() {

                                        }

                                        @Override
                                        public void onLoading(long total, long current, boolean isDownloading) {

                                        }
                                    });

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                } else {
                    uploadLogVideo = true;
                    checkChangeState();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (mRxDialogLoading != null) {
                    mRxDialogLoading.cancel();
                }
                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！13", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void uploadSignedPic() {
        //先去服务器查询图片
        final GetMissionRecordFilePathParams params = new GetMissionRecordFilePathParams();
        params.log_id = log.getId();
        params.type = 1;//签名图片
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray paths = new JSONArray(result);
                    ArrayList<String> useridList = new ArrayList<>();
                    for (int i = 0; i < paths.length(); i++) {
                        String userid = paths.getJSONObject(i).getString("userid");
                        useridList.add(userid);
                    }

                    final ArrayList<Member> memberList = new ArrayList<>();
                    for (int i = 0; i < log.getMembers().size(); i++) {
                        boolean isUpload = true;

                        for (int j = 0; j < useridList.size(); j++) {
                            if (useridList.get(j).equals(log.getMembers().get(i).getUserid())) {
                                isUpload = false;
                                break;
                            }
                        }

                        if (isUpload) {
                            memberList.add(log.getMembers().get(i));
                        }
                    }

                    uploadLogSignCount = 0;
                    if (memberList.size() > 0) {

                        for (int i = 0; i < memberList.size(); i++) {
                            if (log.getMembers().get(i).getSignPic() != null) {

                                //上传图片
                                RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskLog/mobile/uploadfile");
                                // 加到url里的参数, http://xxxx/s?wd=xUtils
                                params.addQueryStringParameter("logId", log.getId());
                                params.addQueryStringParameter("type", "1");
                                params.addQueryStringParameter("smallImg", "");
                                // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.

                                // 使用multipart表单上传文件
                                params.setMultipart(true);
                                params.addQueryStringParameter("user_id", memberList.get(i).getUserid());
                                params.addBodyParameter(
                                        "files",
                                        new File(FileUtil.PraseUritoPath(getContext(), memberList.get(i).getSignPic())),
                                        null); // 如果文件没有扩展名, 最好设置contentType参数.

                                x.http().post(params, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        try {
                                            JSONObject object = new JSONObject(result);
                                            int code = object.getInt("code");
                                            if (code == 200) {
                                                uploadLogSignCount += 1;
                                                if (uploadLogSignCount == memberList.size()) {
                                                    uploadSignPic = true;
                                                    checkChangeState();
                                                }
                                            } else {
                                                if (mRxDialogLoading != null) {
                                                    mRxDialogLoading.cancel();
                                                }
                                                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！14", Toast.LENGTH_SHORT, true).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            if (mRxDialogLoading != null) {
                                                mRxDialogLoading.cancel();
                                            }
                                            RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！15", Toast.LENGTH_SHORT, true).show();
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        Log.i("UploadSignPic", "onError>>>>>>" + ex.toString());
                                        if (mRxDialogLoading != null) {
                                            mRxDialogLoading.cancel();
                                        }
                                        RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！16", Toast.LENGTH_SHORT, true).show();
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
                    } else {
                        uploadSignPic = true;
                        checkChangeState();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！17", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (mRxDialogLoading != null) {
                    mRxDialogLoading.cancel();
                }
                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！18", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //提交任务日志文本资料
    private void httpSaveRecordOnSuccess(String logResult) {

        String area = area_TextView.getText().toString();
        String summary = summaryEditText.getText().toString();
        String leaderSummary = leaderSummaryEditText.getText().toString();
        String equipment = equipment_textView.getText().toString();
        new UploadJobLogTextModel(logResult, log, mission, mSdf.format(System.currentTimeMillis()), swisopen, mSelePlanPos, mSeleMattersPos,
                area, summary, leaderSummary, parts, equipment, new OkingCallBack.UploadJobLogForText() {
            @Override
            public void uploadSucc(String result) {
                uploadLogPic = false;
                uploadSignPic = false;
                uploadLogVideo = false;


                if (picGridView.getAdapter().getCount() > 1) {
                    //上传巡查日志的图片
                    uploadPic();

                } else {
                    uploadLogPic = true;
                }


                //上传签名图片
                uploadSignedPic();


                //上传巡查视频
                if (videoGridView.getAdapter().getCount() > 1) {
                    uploadVideo();
                } else {
                    uploadLogVideo = true;
                }
            }

            @Override
            public void uploadFail(Throwable ex) {

                if (mRxDialogLoading != null) {
                    mRxDialogLoading.cancel();
                }
                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！19", Toast.LENGTH_SHORT, true).show();

            }
        }).uploadJobLogForText();


    }

    private void httpSaveRecord() {

        new GetJobLogModel(mission, new OkingCallBack.GetJobLog() {
            @Override
            public void getSucc(String result) {
                httpSaveRecordOnSuccess(result);
            }

            @Override
            public void getFail(Throwable ex) {
                if (mRxDialogLoading != null) {
                    mRxDialogLoading.cancel();
                }
                RxToast.error(MyApp.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！20", Toast.LENGTH_SHORT, true).show();

            }
        }).getJobLog();


    }

    private void httpCompleteMission() {
        UpdateMissionRecordStateParams uspparams = new UpdateMissionRecordStateParams();
        uspparams.id = log.getId();
        uspparams.status = 1;
        x.http().post(uspparams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                UpdateMissionStateParams params = new UpdateMissionStateParams();
                params.id = mission.getId();
                params.status = 5;
                SharedPreferences sharedPreferencesStart = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                SharedPreferences sharedPreferencesEnd = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);

                if (mission.getExecute_start_time() == null) {
                    params.execute_start_time = mSdf.format(sharedPreferencesStart.getLong(mission.getId(), 0));
                } else {
                    params.execute_start_time = mSdf.format(mission.getExecute_start_time());
                }

                if (mission.getExecute_end_time() == null) {
                    params.execute_end_time = mSdf.format(sharedPreferencesEnd.getLong(mission.getId(), 0));
                } else {
                    params.execute_end_time = mSdf.format(mission.getExecute_end_time());
                }

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {

                        try {
                            JSONObject object = new JSONObject(result);
                            int code = object.getInt("code");
                            if (code == 0) {
                                //删除本地缓存文件
                                SharedPreferences sharedPreferences1 = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                editor1.remove(mission.getId());
                                editor1.commit();

                                SharedPreferences sharedPreferences2 = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                                editor2.remove(mission.getId());
                                editor2.commit();

                                SharedPreferences sharedPreferences3 = MyApp.getApplictaion().getSharedPreferences("missionEndTime", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                                editor3.remove(mission.getId());
                                editor3.commit();

                                mission.setStatus(5);
                                MissionRecordFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
                                mMyCallBack.refreshMission();
                            } else {
                                Toast.makeText(getContext(), object.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void localSaveRecord() {

        log.setPlan(mSelePlanPos);
        log.setItem(mSeleMattersPos);
        log.setArea(area_TextView.getText().toString());
        log.setPatrol(summaryEditText.getText().toString());
        log.setDzyj(leaderSummaryEditText.getText().toString());
        log.setEquipment(equipment_textView.getText().toString());
        log.setOther_part(parts);

        ArrayList<Uri> photoList = log.getPhotoUriList();
        for (int i = 0; i < photoList.size(); i++) {
            File file = new File(FileUtil.PraseUritoPath(getContext(), photoList.get(i)));
            if (!file.exists()) {
                photoList.remove(i);
            }
        }

        ArrayList<Uri> videoList = log.getVideoUriList();
        for (int i = 0; i < videoList.size(); i++) {
            File file = new File(FileUtil.PraseUritoPath(getContext(), videoList.get(i)));
            if (!file.exists()) {
                videoList.remove(i);
            }
        }


        String logStr = DataUtil.toJson(log);

        //记录日志记录
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("logRecord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(log.getTask_id(), logStr);
        editor.commit();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mMyCallBack = (OkingCallBack.MyCallBack) context;
        }
    }

    private LatLng mLng;

    @Override
    public void onMyLocationChange(Location location) {

        if (location != null) {
            mLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */

                changeCamera(
                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                mLng, 18, 30, 30)));
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }

    }

    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.moveCamera(cameraUpdate);
    }
}

