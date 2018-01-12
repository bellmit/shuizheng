package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.ApproverBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.InspectTaskBean;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.ApproverPinyinComparator;
import gd.water.oking.com.cn.wateradministration_gd.util.CBRPinyinComparator;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;

/**
 * 巡查任务发布
 */
public class PatrolsToReleaseFragment extends BaseFragment implements View.OnClickListener, OnDateSetListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private InspectTaskBean inspectTaskBean;
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
    private Button mBt_ok;
    private TextView mPublisher_tv;         //申请人
    private RxDialogLoading mRxDialogLoading;
    private EditText mEt_taskname;      //任务名称
    private EditText mList_item_missionDetail;      //巡查区域
    private EditText mEt_description;               //任务描述
    private String[] mSourceArray;
    private String[] mTasknatureArray;
    private String[] mApprovers;
    private String mApproverId;    //选中的审批人ID
    private String mApprover;    //选中的审批人
    private String mSource;     //选中的线索来源
    private String mTasknature;   //选中的任务性质
    private List<ApproverBean.SZJCBean> mSzjc;
    private RxDialogLoading mSubRxDialogLoading;
    private Spinner mSp_recipient;     //接收人
    private Spinner mSp_emergency;      //紧急程度
    private String[] mEmergencyArray;
    private List<ApproverBean.CBRBean> mCbr;
    private ArrayList<String> mRecipients;      //接收人数据源
    private String mSelecRecipient;         //选中的接收人名称
    private String mEmergency;
    private ApproverBean.CBRBean mSelectRecipientsBean;   //选中的接收人
    private String mMcoordinateJson;

    private Spinner mSp_tasktype;       //任务类型
    private String[] mTasktypeArray;
    private String mTasktype;
    private UpcomingFragment mUpcomingFragment;
    private FrameLayout mFl_web;
    private AgentWeb mAgentWeb;

    public PatrolsToReleaseFragment() {
        // Required empty public constructor
    }

    public static PatrolsToReleaseFragment newInstance(InspectTaskBean inspectTaskBean, String param2) {
        PatrolsToReleaseFragment fragment = new PatrolsToReleaseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, inspectTaskBean);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inspectTaskBean = (InspectTaskBean) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patrols_to_release, container, false);
    }

    @Override
    public void onDestroyView() {
        if (mAgentWeb!=null){

            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroyView();

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
        mPublisher_tv = (TextView) rootView.findViewById(R.id.publisher_tv);
        mList_item_missionDetail = (EditText) rootView.findViewById(R.id.list_item_missionDetail);
        mEt_description = (EditText) rootView.findViewById(R.id.et_description);
        mSp_recipient = (Spinner) rootView.findViewById(R.id.sp_recipient);
        mSp_emergency = (Spinner) rootView.findViewById(R.id.sp_emergency);
        mFl_web = (FrameLayout) rootView.findViewById(R.id.fl_web);
        mSp_tasktype = (Spinner) rootView.findViewById(R.id.sp_tasktype);
        initData();
        initLister();
    }

    private void initData() {

        Utils.setEditTextInhibitInputSpace(mEt_taskname);
        Utils.setEditTextInhibitInputSpeChat(mEt_taskname);

        if (inspectTaskBean != null) {
            mEt_taskname.setText(inspectTaskBean.getRWMC());
            mPublisher_tv.setText(inspectTaskBean.getFBR());
            mBt_select_begintime.setText(sf.format(inspectTaskBean.getBEGIN_TIME()));
            mBt_select_endtime.setText(sf.format(inspectTaskBean.getEND_TIME()));
            mList_item_missionDetail.setText(inspectTaskBean.getRWQYMS());
            mEt_description.setText(inspectTaskBean.getRWMS());
        } else {
            mPublisher_tv.setText(DefaultContants.CURRENTUSER.getUserName());

        }
        initSpinner();
        initWebView();
    }

    private void initSpinner() {
        mSourceArray = getResources().getStringArray(R.array.spinner_source);
        SpinnerArrayAdapter sourceArrayAdapter = new SpinnerArrayAdapter(mSourceArray);
        mSp_source.setAdapter(sourceArrayAdapter);

        mTasknatureArray = getResources().getStringArray(R.array.spinner_tasknature);
        SpinnerArrayAdapter tasknatureArrayAdapter = new SpinnerArrayAdapter(mTasknatureArray);
        mSp_tasknature.setAdapter(tasknatureArrayAdapter);

        mEmergencyArray = getResources().getStringArray(R.array.spinner_emergency);
        SpinnerArrayAdapter emergencyArrayAdapter = new SpinnerArrayAdapter(mEmergencyArray);
        mSp_emergency.setAdapter(emergencyArrayAdapter);

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
                Collections.sort(mSzjc,new ApproverPinyinComparator());
                mApprovers= new String[mSzjc.size()];
                for (int i=0;i<mSzjc.size();i++) {
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

        RequestParams jsrParams = new RequestParams(DefaultContants.SERVER_HOST + "/cs/getUserByPosition");
        params.addBodyParameter("lx", "SZJC,CBR");
        x.http().post(jsrParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ApproverBean approverBean = gson.fromJson(result, ApproverBean.class);
                List<ApproverBean.SZJCBean> szjc = approverBean.getSZJC();
                mCbr = approverBean.getCBR();
                mRecipients = new ArrayList<>();

                for (ApproverBean.CBRBean cbrBean:mCbr){
                    mRecipients.add(cbrBean.getUSERNAME());
                }

                for (ApproverBean.SZJCBean szjcBean:szjc){
                    mRecipients.add(szjcBean.getUSERNAME());
                }

                Collections.sort(mRecipients,new CBRPinyinComparator());
                String[] objects = mRecipients.toArray(new String[0]);

                SpinnerArrayAdapter recipientsAdapter = new SpinnerArrayAdapter(objects);
                mSp_recipient.setAdapter(recipientsAdapter);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                RxToast.error(MyApp.getApplictaion(), "数据获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initWebView() {

        long timeMillis = System.currentTimeMillis();
        DefaultContants.syncCookie(DefaultContants.SERVER_HOST + "/arcgis/xcdgl/task_select_area.jsp?rev=99&uuid=" + timeMillis);
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(mFl_web, new FrameLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()//
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView webView, String s) {

                    }
                }) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go(DefaultContants.SERVER_HOST+"/arcgis/xcdgl/task_select_area.jsp?rev=99&uuid=" + timeMillis);

    }

    private void initLister() {
        mBt_select_begintime.setOnClickListener(this);
        mBt_select_endtime.setOnClickListener(this);
        mBt_ok.setOnClickListener(this);
        mSp_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
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
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
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
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
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

        mSp_emergency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
                if ("特急".equals(mEmergencyArray[i])) {
                    mEmergency = "0";
                } else if ("紧急".equals(mEmergencyArray[i])) {
                    mEmergency = "1";
                } else if ("一般".equals(mEmergencyArray[i])) {
                    mEmergency = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSp_recipient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
                mSelecRecipient = mRecipients.get(position);
                mSelectRecipientsBean = mCbr.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSp_tasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView)view;
                tv.setTextSize(12.0f);
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

                //与js交互
                mAgentWeb.getJsEntraceAccess().quickCallJs("getSelectArea", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String coordinateJson) {

                        if ("null".equals(coordinateJson)){
                            mMcoordinateJson=null;
                        }else {
                            String replace = coordinateJson.replace("\\", "");
                            mMcoordinateJson = replace.substring(1,replace.length()-1);
                        }


                        if (inspectTaskBean != null) {
                            submitWithdrawDataToServer();
                        } else {
                            submitDataToServer();
                        }
                    }
                });




                break;

            default:
                break;

        }
    }

    /**
     * 发布撤回的数据
     */
    private void submitWithdrawDataToServer() {
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
        String missionDetail = mList_item_missionDetail.getText().toString().trim();
        String description = mEt_description.getText().toString().trim();
        String beginTime = mBt_select_begintime.getText().toString();
        String endTime = mBt_select_endtime.getText().toString();

        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(missionDetail)
                && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBt_ok.setEnabled(false);
            mSubRxDialogLoading.show();

            RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/contractorUpdateForAndroid");
            params.addBodyParameter("fid", "0");
            params.addBodyParameter("id",inspectTaskBean.getID());
            params.addBodyParameter("rwms", description);
            params.addBodyParameter("rwmc", taskName);
            params.addBodyParameter("fbrid", DefaultContants.CURRENTUSER.getUserId());
            params.addBodyParameter("sjq", beginTime);
            params.addBodyParameter("sjz", endTime);
            params.addBodyParameter("jsrid", mSelectRecipientsBean.getUSERID());
            params.addBodyParameter("rwlx", mTasknature);
            params.addBodyParameter("sprid", mApproverId);
            params.addBodyParameter("zt", "1");
            params.addBodyParameter("deptid", DefaultContants.CURRENTUSER.getDeptId());
            params.addBodyParameter("rwqyms", missionDetail);
            params.addBodyParameter("jjcd", mEmergency);
            params.addBodyParameter("rwly", mSource);
            params.addBodyParameter("jsr", mSelecRecipient);
            params.addBodyParameter("jsdw", mSelectRecipientsBean.getDEPTNAME());
            params.addBodyParameter("fbr", DefaultContants.CURRENTUSER.getUserName());
            params.addBodyParameter("fbdw", DefaultContants.CURRENTUSER.getDeptName());
            params.addBodyParameter("spr", mApprover);
            params.addBodyParameter("rwcd", "0");
            params.addBodyParameter("typeoftask", mTasktype);
            params.addBodyParameter("receiver", mSelectRecipientsBean.getUSERID());
            params.addBodyParameter("coordinateJson", mMcoordinateJson);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    mBt_ok.setEnabled(true);
                    mSubRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code==400){
                              if (mUpcomingFragment == null) {

                                mUpcomingFragment = new UpcomingFragment();
                            }
                            getFragmentManager().beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();
                            RxToast.success(MyApp.getApplictaion(), "巡查任务发布成功", Toast.LENGTH_LONG).show();

                        }else {
                            RxToast.error(MyApp.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RxToast.error(MyApp.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    mBt_ok.setEnabled(true);
                    mSubRxDialogLoading.cancel();
                    RxToast.error(MyApp.getApplictaion(), "巡查任务发布失败"+ex.getMessage(), Toast.LENGTH_LONG).show();
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
        String missionDetail = mList_item_missionDetail.getText().toString().trim();
        String description = mEt_description.getText().toString().trim();
        String beginTime = mBt_select_begintime.getText().toString();
        String endTime = mBt_select_endtime.getText().toString();


        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(missionDetail)
                && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBt_ok.setEnabled(false);
            mSubRxDialogLoading.show();

            RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/contractorInsertForAndroid");
            params.addBodyParameter("fid", "0");
            params.addBodyParameter("rwms", description);
            params.addBodyParameter("rwmc", taskName);
            params.addBodyParameter("fbrid", DefaultContants.CURRENTUSER.getUserId());
            params.addBodyParameter("sjq", beginTime);
            params.addBodyParameter("sjz", endTime);
            params.addBodyParameter("jsrid", mSelectRecipientsBean.getUSERID());
            params.addBodyParameter("rwlx", mTasknature);
            params.addBodyParameter("sprid", mApproverId);
            params.addBodyParameter("zt", "1");
            params.addBodyParameter("deptid", DefaultContants.CURRENTUSER.getDeptId());
            params.addBodyParameter("rwqyms", missionDetail);
            params.addBodyParameter("jjcd", mEmergency);
            params.addBodyParameter("rwly", mSource);
            params.addBodyParameter("jsr", mSelecRecipient);
            params.addBodyParameter("jsdw", mSelectRecipientsBean.getDEPTNAME());
            params.addBodyParameter("fbr", DefaultContants.CURRENTUSER.getUserName());
            params.addBodyParameter("fbdw", DefaultContants.CURRENTUSER.getDeptName());
            params.addBodyParameter("spr", mApprover);
            params.addBodyParameter("rwcd", "0");
            params.addBodyParameter("typeoftask", mTasktype);
            params.addBodyParameter("receiver", mSelectRecipientsBean.getUSERID());
            params.addBodyParameter("coordinateJson", mMcoordinateJson);

            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    mSubRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 400) {
                            if (mUpcomingFragment == null) {

                                mUpcomingFragment = new UpcomingFragment();
                            }
                            getFragmentManager().beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();
                            RxToast.success(MyApp.getApplictaion(), "巡查任务发布成功", Toast.LENGTH_LONG).show();

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
                    RxToast.error(MyApp.getApplictaion(), "巡查任务发布失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
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


    //show出开始时间选取器
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
            mBt_select_begintime.setText(sf.format(millseconds));
        } else {
            mBt_select_endtime.setText(sf.format(millseconds));
        }
    }




}
