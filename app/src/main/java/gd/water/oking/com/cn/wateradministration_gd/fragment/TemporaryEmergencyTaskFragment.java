package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.EmergencyMemberAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.ApproverBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.EmergencyMember;
import gd.water.oking.com.cn.wateradministration_gd.bean.EmergencyMemberGson;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.ApproverPinyinComparator;
import gd.water.oking.com.cn.wateradministration_gd.util.EmergencyPinyinComparator;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;

import static gd.water.oking.com.cn.wateradministration_gd.R.id.bt_okselect;

/**
 * 临时紧急任务
 */
public class TemporaryEmergencyTaskFragment extends BaseFragment implements View.OnClickListener, OnDateSetListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mSp_approver;   //审批人
    private Spinner mSp_source;     //线索来源
    private Spinner mSp_tasknature;   //任务性质
    private Button mBt_select_begintime;
    private Button mBt_select_endtime;
    private TimePickerDialog mBeginDialogAll;
    private TimePickerDialog mEndDialogAll;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private long mBeginMillseconds = 0;
    private ListView mLv_members;       //队员列表
    private Button mBt_ok;
    private Button mBt_select_members;
    private TextView mPublisher_tv;         //申请人
    private MissionFragment mMissionFragment;
    private Button mBt_okselect;
    private RxDialogLoading mRxDialogLoading;
    private TextView mList_item_missionMember;    //成员
    private EditText mEt_taskname;      //任务名称
    private EditText mList_item_missionDetail;      //巡查区域
    private EditText mEt_description;               //任务描述
    private ArrayList<EmergencyMember> mEmergencyMembers;
    private EmergencyMemberAdapter mEmergencyMemberAdapter;
    private String[] mSourceArray;
    private String[] mTasknatureArray;
    private String[] mApprovers;
    private String mApproverId;    //选中的审批人ID
    private String mApprover;    //选中的审批人
    private String mSource;     //选中的线索来源
    private String mTasknature;   //选中的任务性质
    private List<ApproverBean.SZJCBean> mSzjc;
    private String mMembersid;
    private RxDialogLoading mSubRxDialogLoading;
    private Handler mainHandler;
    private Spinner mSp_tasktype;       //任务类型
    private String[] mTasktypeArray;
    private String mTasktype;
    private UpcomingFragment mUpcomingFragment;

    public TemporaryEmergencyTaskFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TemporaryEmergencyTaskFragment newInstance(String param1, String param2) {
        TemporaryEmergencyTaskFragment fragment = new TemporaryEmergencyTaskFragment();
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
        return inflater.inflate(R.layout.fragment_temporary_emergency_task, container, false);
    }

    @Override
    public void initView(View rootView) {
        mEt_taskname = (EditText) rootView.findViewById(R.id.et_taskname);
        mSp_approver = (Spinner) rootView.findViewById(R.id.sp_approver);
        mSp_source = (Spinner) rootView.findViewById(R.id.sp_source);
        mSp_tasknature = (Spinner) rootView.findViewById(R.id.sp_tasknature);
        mBt_select_begintime = (Button) rootView.findViewById(R.id.bt_select_begintime);
        mBt_select_endtime = (Button) rootView.findViewById(R.id.bt_select_endtime);
        mBt_ok = (Button) rootView.findViewById(R.id.bt_ok);
        mLv_members = (ListView) rootView.findViewById(R.id.lv_members);
        mBt_select_members = (Button) rootView.findViewById(R.id.bt_select_members);
        mPublisher_tv = (TextView) rootView.findViewById(R.id.publisher_tv);
        mBt_okselect = (Button) rootView.findViewById(bt_okselect);
        mList_item_missionMember = (TextView) rootView.findViewById(R.id.list_item_missionMember);
        mList_item_missionDetail = (EditText) rootView.findViewById(R.id.list_item_missionDetail);
        mEt_description = (EditText) rootView.findViewById(R.id.et_description);
        mSp_tasktype = (Spinner) rootView.findViewById(R.id.sp_tasktype);
        initData();
        initLister();

    }

    private void initData() {

        mPublisher_tv.setText(DefaultContants.CURRENTUSER.getUserName());
        RequestParams params2 = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/getMcForAndroid");
        params2.addBodyParameter("deptId", DefaultContants.CURRENTUSER.getDeptId());
        x.http().post(params2, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt("code");
                    if (code == 400) {
                        JSONArray msg = jsonObject.getJSONArray("msg");
                        JSONObject jsonObject1 = msg.getJSONObject(0);
                        String rwmc = jsonObject1.getString("rwmc");
                        mEt_taskname.setText(rwmc);
                        Utils.setEditTextInhibitInputSpace(mEt_taskname);
                        Utils.setEditTextInhibitInputSpeChat(mEt_taskname);
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

        mSourceArray = getResources().getStringArray(R.array.spinner_source);
        SpinnerArrayAdapter sourceArrayAdapter = new SpinnerArrayAdapter(mSourceArray);
        mSp_source.setAdapter(sourceArrayAdapter);

        mTasknatureArray = getResources().getStringArray(R.array.spinner_tasknature);
        SpinnerArrayAdapter tasknatureArrayAdapter = new SpinnerArrayAdapter(mTasknatureArray);
        mSp_tasknature.setAdapter(tasknatureArrayAdapter);

        mTasktypeArray = getResources().getStringArray(R.array.spinner_tasktype);
        SpinnerArrayAdapter tasktypeArrayAdapter = new SpinnerArrayAdapter(mTasktypeArray);
        mSp_tasktype.setAdapter(tasktypeArrayAdapter);


        if (mRxDialogLoading == null) {
            initWaitingDialog();
        }

        mRxDialogLoading.show();
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/cs/getUserByPosition");
        params.addBodyParameter("lx", "SZJC");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mRxDialogLoading.cancel();
                Gson gson = new Gson();
                ApproverBean approverBean = gson.fromJson(result, ApproverBean.class);
                mSzjc = approverBean.getSZJC();
                Collections.sort(mSzjc, new ApproverPinyinComparator());
                mApprovers = new String[mSzjc.size()];
                for (int i = 0; i < mSzjc.size(); i++) {
                    mApprovers[i] = mSzjc.get(i).getUSERNAME();
                }


                SpinnerArrayAdapter approverAdapter = new SpinnerArrayAdapter(mApprovers);
                mSp_approver.setAdapter(approverAdapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                RxToast.error(MyApp.getApplictaion(), "数据获取失败", Toast.LENGTH_SHORT).show();
                mRxDialogLoading.cancel();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void initLister() {
        mBt_select_begintime.setOnClickListener(this);
        mBt_select_endtime.setOnClickListener(this);
        mBt_ok.setOnClickListener(this);
        mBt_select_members.setOnClickListener(this);
        mBt_okselect.setOnClickListener(this);
        mSp_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mApproverId = mSzjc.get(i).getUSERID();
                mApprover = mSzjc.get(i).getUSERNAME();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSp_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("上级交办".equals(mSourceArray[i])) {
                    mSource = "0";
                } else if ("部门移送".equals(mSourceArray[i])) {
                    mSource = "1";
                } else if ("系统报警".equals(mSourceArray[i])) {
                    mSource = "2";
                } else if ("日常巡查".equals(mSourceArray[i])) {
                    mSource = "3";
                } else if ("媒体披露".equals(mSourceArray[i])) {
                    mSource = "4";
                } else if ("群众举报".equals(mSourceArray[i])) {
                    mSource = "5";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSp_tasknature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("日常执法".equals(mTasknatureArray[i])) {
                    mTasknature = "0";
                } else if ("联合执法".equals(mTasknatureArray[i])) {
                    mTasknature = "1";
                } else if ("专项执法".equals(mTasknatureArray[i])) {
                    mTasknature = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("河道管理".equals(mTasktypeArray[i])) {
                    mTasktype = "0";
                } else if ("河道采砂".equals(mTasktypeArray[i])) {
                    mTasktype = "1";
                } else if ("水资源管理".equals(mTasktypeArray[i])) {
                    mTasktype = "2";
                } else if ("水土保持管理".equals(mTasktypeArray[i])) {
                    mTasktype = "3";
                } else if ("水利工程管理".equals(mTasktypeArray[i])) {
                    mTasktype = "4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_select_begintime:     //选择开始时间
                if (mBeginDialogAll == null) {
                    initBeginWheelYearMonthDayDialog();
                }
                mBt_select_endtime.setText("选择");
                mBeginDialogAll.show(getFragmentManager(), "beginTime");
                break;
            case R.id.bt_select_endtime:        //选择结束时间

                initEndWheelYearMonthDayDialog();
                mEndDialogAll.show(getFragmentManager(), "endTime");
                break;

            case R.id.bt_ok:            //提交

                submitDataToServer();

                break;

            case R.id.bt_select_members:    //选择成员
                if (mRxDialogLoading == null) {

                    initWaitingDialog();
                }
                mRxDialogLoading.show();
                httpGetCanSelectMember();
                break;
            case R.id.bt_okselect:          //完成选择队员
                List<EmergencyMember> checkName = mEmergencyMemberAdapter.getCheckName();
                String members = DefaultContants.CURRENTUSER.getUserName();
                mMembersid = "";
                for (EmergencyMember m : checkName) {
                    members = members + "," + m.getUsername();
                    mMembersid = mMembersid + "," + m.getUserId();
                }
                mList_item_missionMember.setText(members);
                mLv_members.setVisibility(View.GONE);
                mBt_okselect.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void submitDataToServer() {
        if (mSubRxDialogLoading == null) {

            mSubRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            mSubRxDialogLoading.setLoadingText("提交数据中,请稍等...");
        }


        String taskName = mEt_taskname.getText().toString().trim();
        String members = mList_item_missionMember.getText().toString().trim();
        String missionDetail = mList_item_missionDetail.getText().toString().trim();
        String description = mEt_description.getText().toString().trim();
        String beginTime = mBt_select_begintime.getText().toString();
        String endTime = mBt_select_endtime.getText().toString();

        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(members)
                && !TextUtils.isEmpty(missionDetail) && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBt_ok.setEnabled(false);
            mSubRxDialogLoading.show();

            RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/insertEmergencyReleaseForAndroid");
            params.addBodyParameter("rwmc", taskName);
            params.addBodyParameter("fid", "0");
            params.addBodyParameter("rwms", description);
            params.addBodyParameter("fbrid", DefaultContants.CURRENTUSER.getUserId());
            params.addBodyParameter("sjq", beginTime);
            params.addBodyParameter("sjz", endTime);
            params.addBodyParameter("jsrid", DefaultContants.CURRENTUSER.getUserId());
            params.addBodyParameter("rwlx", mTasknature);
            params.addBodyParameter("sprid", mApproverId);
            params.addBodyParameter("zt", "3");
            params.addBodyParameter("jjcd", "1");
            params.addBodyParameter("deptid", DefaultContants.CURRENTUSER.getDeptId());
            params.addBodyParameter("rwqyms", missionDetail);
            params.addBodyParameter("rwly", mSource);
            params.addBodyParameter("jsr", DefaultContants.CURRENTUSER.getUserName());
            params.addBodyParameter("jsdw", DefaultContants.CURRENTUSER.getDeptName());
            params.addBodyParameter("fbr", DefaultContants.CURRENTUSER.getUserName());
            params.addBodyParameter("fbdw", DefaultContants.CURRENTUSER.getDeptName());
            params.addBodyParameter("spr", mApprover);
            params.addBodyParameter("rwcd", "1");
            params.addBodyParameter("typeoftask", mTasktype);
            params.addBodyParameter("yxry", mMembersid.substring(1, mMembersid.length()));
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mSubRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 400) {
                            Intent intent = new Intent(MainActivity.UPDATE_MISSION_GET);
                            MyApp.getApplictaion().sendBroadcast(intent);

                            if (mainHandler == null) {

                                mainHandler = new Handler();
                            }
                            mainHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    if (mUpcomingFragment == null) {

                                        mUpcomingFragment = new UpcomingFragment();
                                    }
                                    FragmentManager fm = getFragmentManager();
                                    fm.beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();
                                }
                            }, 100);
                            RxToast.success(MyApp.getApplictaion(), "紧急任务发布成功", Toast.LENGTH_LONG).show();

                        } else {
                            RxToast.error(MyApp.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RxToast.error(MyApp.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                    }
                    mBt_ok.setEnabled(true);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    mBt_ok.setEnabled(true);
                    mSubRxDialogLoading.cancel();
                    RxToast.error(MyApp.getApplictaion(), "紧急任务发布失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        } else {
            RxToast.warning(MyApp.getApplictaion(), "提交内容不能有空", Toast.LENGTH_LONG).show();
        }


    }

    private void initWaitingDialog() {
        mRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.cancel();
            }
        });
        mRxDialogLoading.setLoadingText("获取数据中,请稍等...");

    }

    private void initEndWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;

        if (mBeginMillseconds == 0) {
            mBeginMillseconds = System.currentTimeMillis();
        }
        mEndDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择结束时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(mBeginMillseconds)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();

    }

    private void initBeginWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        mBeginDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String tag = timePickerView.getTag();
        if ("beginTime".equals(tag)) {
            mBeginMillseconds = millseconds;
            mBt_select_begintime.setText(getDateToString(millseconds));
        } else {
            mBt_select_endtime.setText(getDateToString(millseconds));
        }
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }


    private void httpGetCanSelectMember() {

        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/cs/getUserByPosition");
        params.addBodyParameter("lx", "SZJC,CBR");

//        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/getWxryForAndroid");
//        params.addBodyParameter("dz_id", DefaultContants.CURRENTUSER.getUserId());
//        params.addBodyParameter("dept_id", DefaultContants.CURRENTUSER.getDeptId());

        final Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mRxDialogLoading.cancel();
                mLv_members.setVisibility(View.VISIBLE);
                mBt_okselect.setVisibility(View.VISIBLE);


                if (mEmergencyMembers == null) {

                    mEmergencyMembers = new ArrayList<EmergencyMember>();
                }
                mEmergencyMembers.clear();


                Gson gson = new Gson();
                EmergencyMemberGson emergencyMemberGson = gson.fromJson(result, EmergencyMemberGson.class);
                List<EmergencyMemberGson.CBRBean> cbrs = emergencyMemberGson.getCBR();
                for (EmergencyMemberGson.CBRBean cbrBean : cbrs) {


                    if (cbrBean.getUSERNAME().equals(DefaultContants.CURRENTUSER.getUserName())) {
                        continue;
                    }
                    EmergencyMember member = new EmergencyMember();
                    member.setUsername(cbrBean.getUSERNAME());
                    member.setDeptId(cbrBean.getDEPTID());
                    member.setDeptName(cbrBean.getDEPTNAME());
                    member.setRemark(cbrBean.getREMARK());
                    member.setUserId(cbrBean.getUSERID());
                    member.setZfzh(cbrBean.getZFZH());
                    mEmergencyMembers.add(member);
                }
                List<EmergencyMemberGson.SZJCBean> szjcs = emergencyMemberGson.getSZJC();


                for (EmergencyMemberGson.SZJCBean szjcBean : szjcs) {


                    if (szjcBean.getUSERNAME().equals(DefaultContants.CURRENTUSER.getUserName())) {
                        continue;
                    }
                    EmergencyMember member = new EmergencyMember();
                    member.setUsername(szjcBean.getUSERNAME());
                    member.setDeptId(szjcBean.getDEPTID());
                    member.setDeptName(szjcBean.getDEPTNAME());
                    member.setRemark(szjcBean.getREMARK());
                    member.setUserId(szjcBean.getUSERID());
                    member.setZfzh(szjcBean.getZFZH());
                    mEmergencyMembers.add(member);
                }

//=============================================================================
                    /*JSONArray objects = new JSONArray(result);
                    for (int i = 0; i < objects.length(); i++) {

                        String username = objects.getJSONObject(i).getString("USERNAME");


                        if (username.equals(DefaultContants.CURRENTUSER.getUserName())) {
                            continue;
                        }

                        EmergencyMember emergencyMember = new EmergencyMember();
                        emergencyMember.setUsername(username);

                        String deptid = objects.getJSONObject(i).getString("DEPTID");
                        emergencyMember.setDeptId(deptid);
                        String deptname = objects.getJSONObject(i).getString("DEPTNAME");
                        emergencyMember.setDeptName(deptname);
                        String remark = objects.getJSONObject(i).getString("REMARK");
                        emergencyMember.setRemark(remark);
                        String userid = objects.getJSONObject(i).getString("USERID");
                        emergencyMember.setUserId(userid);

                        String zfzh = objects.getJSONObject(i).getString("ZFZH");
                        emergencyMember.setZfzh(zfzh);
                        mEmergencyMembers.add(emergencyMember);
                    }*/

//==========================================================================


                Collections.sort(mEmergencyMembers, new EmergencyPinyinComparator());
                mEmergencyMemberAdapter = new EmergencyMemberAdapter(getActivity(), mEmergencyMembers);
                mLv_members.setAdapter(mEmergencyMemberAdapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLv_members.setVisibility(View.GONE);
                mBt_okselect.setVisibility(View.GONE);
                mRxDialogLoading.cancel();
                mEmergencyMemberAdapter.notifyDataSetChanged();
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
