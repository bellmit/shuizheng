package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

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

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.Utils;

/**
 * 预立案
 */
public class CaseInAdvanceFragment extends BaseFragment implements View.OnClickListener, OnDateSetListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mBt_ok;
    private EditText mEt_cause_action;  //案由
    private EditText mEt_scene;         //案发地点
    private EditText mEt_other;         //其他
    private EditText mEt_situation;     //当事人基本情况
    private EditText mEt_case_brief;    //案情简要
    private EditText mEt_opinion;       //承办人意见
    private RxDialogLoading mRxDialogLoading;
    private Button mBt_time;
    private TimePickerDialog mBeginDialogAll;
    private CheckBox mCb_examination_revealed;      //检查发现
    private CheckBox mCb_superior_specified;        //上级指定
    private CheckBox mCb_department_transfer;       //部门移送
    private CheckBox mCb_masses_report;             //群众举报
    private CheckBox mCb_media_disclosure;
    private CheckBox mCb_other;
    private String sourceCaseType = "";             //案件来源类型  0检查发现；1上级指定；2部门移送；3群众举报；4媒体披露；5其他';
    private String sourceCase = "";                      //案件来源内容

    public CaseInAdvanceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CaseInAdvanceFragment newInstance(String param1, String param2) {
        CaseInAdvanceFragment fragment = new CaseInAdvanceFragment();
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
        return inflater.inflate(R.layout.fragment_case_in_advance, container, false);
    }

    @Override
    public void initView(View rootView) {
        mBt_ok = rootView.findViewById(R.id.bt_ok);
        mEt_cause_action = rootView.findViewById(R.id.et_cause_action);
        mBt_time = rootView.findViewById(R.id.bt_time);
        mEt_scene = rootView.findViewById(R.id.et_scene);
        mEt_other = rootView.findViewById(R.id.et_other);
        mEt_situation = rootView.findViewById(R.id.et_situation);
        mEt_case_brief = rootView.findViewById(R.id.et_case_brief);
        mEt_opinion = rootView.findViewById(R.id.et_opinion);
        mCb_examination_revealed = rootView.findViewById(R.id.cb_examination_revealed);
        mCb_superior_specified = rootView.findViewById(R.id.cb_superior_specified);
        mCb_department_transfer = rootView.findViewById(R.id.cb_department_transfer);
        mCb_masses_report = rootView.findViewById(R.id.cb_masses_report);
        mCb_media_disclosure = rootView.findViewById(R.id.cb_media_disclosure);
        mCb_other = rootView.findViewById(R.id.cb_other);
        initData();
        setListener();
    }

    private void setListener() {
        mBt_ok.setOnClickListener(this);
        mBt_time.setOnClickListener(this);
        mCb_examination_revealed.setOnCheckedChangeListener(this);
        mCb_superior_specified.setOnCheckedChangeListener(this);
        mCb_department_transfer.setOnCheckedChangeListener(this);
        mCb_masses_report.setOnCheckedChangeListener(this);
        mCb_media_disclosure.setOnCheckedChangeListener(this);
        mCb_other.setOnCheckedChangeListener(this);
    }

    private void initData() {
        Utils.setEditTextInhibitInputSpace(mEt_cause_action);
        Utils.setEditTextInhibitInputSpeChat(mEt_cause_action);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_time:
                mBt_time.setText("选择");
                initBeginWheelYearMonthDayDialog();
                mBeginDialogAll.show(getFragmentManager(), "time");
                break;
            case R.id.bt_ok:
                String causeAction = mEt_cause_action.getText().toString().trim();
                String time = mBt_time.getText().toString().trim();
                String scene = mEt_scene.getText().toString().trim();
                String other = mEt_other.getText().toString().trim();
                String situation = mEt_situation.getText().toString().trim();
                String caseBrief = mEt_case_brief.getText().toString().trim();
                String opinion = mEt_opinion.getText().toString().trim();
                if (mCb_examination_revealed.isChecked()) {
                    sourceCaseType = "0";
                    sourceCase = "检查发现";
                } else if (mCb_superior_specified.isChecked()) {
                    sourceCaseType = "1";
                    sourceCase = "上级指定";
                } else if (mCb_department_transfer.isChecked()) {
                    sourceCaseType = "2";
                    sourceCase = "部门移送";
                } else if (mCb_masses_report.isChecked()) {
                    sourceCaseType = "3";
                    sourceCase = "群众举报";
                } else if (mCb_media_disclosure.isChecked()) {
                    sourceCaseType = "4";
                    sourceCase = "媒体披露";
                } else if (mCb_other.isChecked()) {
                    sourceCaseType = "5";
                    sourceCase = other;
                } else {
                    sourceCaseType = "";
                    sourceCase = "";
                }

                if (time.equals("选择")) {
                    RxToast.warning(MyApp.getApplictaion(), "请选择案发时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(causeAction) && !TextUtils.isEmpty(scene)
                        && !TextUtils.isEmpty(situation) && !TextUtils.isEmpty(caseBrief)
                        && !TextUtils.isEmpty(opinion) && !TextUtils.isEmpty(sourceCase)) {
                    if (sourceCaseType.equals("5") && TextUtils.isEmpty(other)) {
                        RxToast.warning(MyApp.getApplictaion(), "提交内容不能有空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sourceCase = other;

                    if (mRxDialogLoading == null) {

                        mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                        mRxDialogLoading.setLoadingText("提交数据中...");
                    }
                    mRxDialogLoading.show();
                    RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/yla/addYlaForAndroid");
                    params.addBodyParameter("ay", causeAction);
                    params.addBodyParameter("afsj", time);
                    params.addBodyParameter("afdd", scene);
                    params.addBodyParameter("ajlylx", sourceCaseType);
                    params.addBodyParameter("ajly", sourceCase);
                    params.addBodyParameter("dsrqk", situation);
                    params.addBodyParameter("aqjy", caseBrief);
                    params.addBodyParameter("cbryj", opinion);
                    params.addBodyParameter("cjrid", DefaultContants.CURRENTUSER.getUserId());
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            mRxDialogLoading.cancel();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    RxToast.success(MyApp.getApplictaion(), "数据提交成功", Toast.LENGTH_LONG).show();
                                    mEt_cause_action.setText("");
                                    mBt_time.setText("选择");
                                    mEt_scene.setText("");
                                    mEt_other.setText("");
                                    mEt_situation.setText("");
                                    mEt_case_brief.setText("");
                                    mEt_opinion.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                RxToast.error(MyApp.getApplictaion(),"服务器内部错误",Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            mRxDialogLoading.cancel();
                            RxToast.error(MyApp.getApplictaion(), "数据提交失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                } else {
                    RxToast.warning(MyApp.getApplictaion(), "提交内容不能有空", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


    private void initBeginWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        mBeginDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择时间")
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
        mBt_time.setText(sf.format(millseconds));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_examination_revealed:
                if (ischecked) {

                    mEt_other.setText("");
                    mEt_other.setEnabled(false);
                    mCb_superior_specified.setChecked(false);
                    mCb_department_transfer.setChecked(false);
                    mCb_masses_report.setChecked(false);
                    mCb_media_disclosure.setChecked(false);
                    mCb_other.setChecked(false);
                }
                break;
            case R.id.cb_superior_specified:
                if (ischecked) {

                    mEt_other.setText("");
                    mEt_other.setEnabled(false);
                    mCb_examination_revealed.setChecked(false);
                    mCb_department_transfer.setChecked(false);
                    mCb_masses_report.setChecked(false);
                    mCb_media_disclosure.setChecked(false);
                    mCb_other.setChecked(false);
                }
                break;
            case R.id.cb_department_transfer:
                if (ischecked) {

                    mEt_other.setText("");
                    mEt_other.setEnabled(false);
                    mCb_superior_specified.setChecked(false);
                    mCb_examination_revealed.setChecked(false);
                    mCb_masses_report.setChecked(false);
                    mCb_media_disclosure.setChecked(false);
                    mCb_other.setChecked(false);
                }
                break;
            case R.id.cb_masses_report:
                if (ischecked) {

                    mEt_other.setText("");
                    mEt_other.setEnabled(false);
                    mCb_superior_specified.setChecked(false);
                    mCb_examination_revealed.setChecked(false);
                    mCb_department_transfer.setChecked(false);
                    mCb_media_disclosure.setChecked(false);
                    mCb_other.setChecked(false);
                }
                break;
            case R.id.cb_media_disclosure:
                if (ischecked) {

                    mEt_other.setText("");
                    mEt_other.setEnabled(false);
                    mCb_superior_specified.setChecked(false);
                    mCb_examination_revealed.setChecked(false);
                    mCb_department_transfer.setChecked(false);
                    mCb_masses_report.setChecked(false);
                    mCb_other.setChecked(false);
                }
                break;
            case R.id.cb_other:
                if (ischecked) {
                    mEt_other.setText("");
                    mEt_other.setEnabled(true);
                    mCb_superior_specified.setChecked(false);
                    mCb_examination_revealed.setChecked(false);
                    mCb_department_transfer.setChecked(false);
                    mCb_media_disclosure.setChecked(false);
                    mCb_masses_report.setChecked(false);
                }
                break;
            default:
                break;
        }
    }
}
