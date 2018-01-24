package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

import gd.water.oking.com.cn.wateradministration_gd.Adapter.AskAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.ProblemAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.NoScrollListView;
import gd.water.oking.com.cn.wateradministration_gd.bean.AnswBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.ProblemBean;
import gd.water.oking.com.cn.wateradministration_gd.db.LawDao;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 调查笔录
 */
public class WrittenRecordFragment extends BaseFragment implements View.OnClickListener, OnDateSetListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PopupWindow mPopupWindow;
    private View mInflate;
    private TextView mTv_problem;
    private ProblemAdapter mProblemAdapter;
    private Spinner mSp_type;
    private ListView mLv_questions;
    private NoScrollListView mLv_ask;
    private TextView mTv_typename;
    private ArrayList<ProblemBean> mProblemContent;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String mCasetypeName;
    private AskAdapter mAskAdapter;
    private View mContentView;
    private Button mBt_save;
    private String mCasetype;
    private Button mBt_select_begintime;
    private Button mBt_select_endtime;
    private TimePickerDialog mBeginDialogAll;
    private TimePickerDialog mEndDialogAll;
    private long mBeginMillseconds = 0;
    private TextInputEditText mTet_content;         //询问内容
    private Spinner mSp_city;                      //询问城市
    private Spinner mSp_county;                    //询问县区
    private EditText mEt_addrdetail;                //询问详细地址
    private TextInputEditText mTet_ask_person;          //询问人
    private TextInputEditText mTet_ask_enforcement_number;      //询问人执法编号
    private TextInputEditText mTet_recorder;                    //记录人
    private TextInputEditText mTet_recorder_number;             //记录人执法编号
    private RadioButton mRb_illegal_person;                     //违法行为人
    private RadioButton mRb_victim;                             //受害人
    private RadioButton mRb_other;                              //第三方
    private String mBxwrtype;                                   //被询问人类型
    private TextInputEditText mTet_asking_people;               //被询问人姓名
    private TextInputEditText mTet_asking_idcard;                //被询问人身份证号码
    private TextInputEditText mTet_asking_position;                //被询问人职务
    private TextInputEditText mTet_asking_work_units;               //被询问人工作单位
    private Spinner mSp_city2;
    private Spinner mSp_county2;
    private EditText mEt_addrdetail2;
    private EditText mEt_enforcement_name;
    private ArrayList<AnswBean> mAnswBeans;
    private Gson mGson;
    private int[] countyIds = {R.array.lv_county_01, R.array.lv_county_02, R.array.lv_county_03
            , R.array.lv_county_04, R.array.lv_county_05, 0, R.array.lv_county_07
            , R.array.lv_county_08, R.array.lv_county_09, R.array.lv_county_10
            , R.array.lv_county_11, R.array.lv_county_12, R.array.lv_county_13
            , R.array.lv_county_14, R.array.lv_county_15, 0, 0, R.array.lv_county_18
            , R.array.lv_county_19, R.array.lv_county_20, R.array.lv_county_21};
    private RxDialogLoading mRxDialogLoading;
    private EditText mEt_town;


    public WrittenRecordFragment() {
        // Required empty public constructor
    }

    public static WrittenRecordFragment newInstance(String param1, String param2) {
        WrittenRecordFragment fragment = new WrittenRecordFragment();
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
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_written_record, container, false);
        }
        return mInflate;
    }

    @Override
    public void initView(View rootView) {
        mTv_problem = rootView.findViewById(R.id.tv_problem);
        mSp_type = rootView.findViewById(R.id.sp_type);
        mLv_ask = rootView.findViewById(R.id.lv_ask);
        mBt_save = rootView.findViewById(R.id.bt_save);
        mBt_select_begintime = rootView.findViewById(R.id.bt_select_begintime);
        mBt_select_endtime = rootView.findViewById(R.id.bt_select_endtime);
        mTet_content = rootView.findViewById(R.id.tet_content);
        mSp_city = rootView.findViewById(R.id.sp_city);
        mSp_county = rootView.findViewById(R.id.sp_county);
        mEt_addrdetail = rootView.findViewById(R.id.et_addrdetail);
        mTet_ask_person = rootView.findViewById(R.id.tet_ask_person);
        mTet_ask_enforcement_number = rootView.findViewById(R.id.tet_ask_enforcement_number);
        mTet_recorder = rootView.findViewById(R.id.tet_recorder);
        mTet_recorder_number = rootView.findViewById(R.id.tet_recorder_number);
        mRb_illegal_person = rootView.findViewById(R.id.rb_illegal_person);
        mRb_victim = rootView.findViewById(R.id.rb_victim);
        mRb_other = rootView.findViewById(R.id.rb_other);
        mTet_asking_people = rootView.findViewById(R.id.tet_asking_people);
        mTet_asking_idcard = rootView.findViewById(R.id.tet_asking_idcard);
        mTet_asking_position = rootView.findViewById(R.id.tet_asking_position);
        mTet_asking_work_units = rootView.findViewById(R.id.tet_asking_work_units);
        mSp_city2 = rootView.findViewById(R.id.sp_city2);
        mSp_county2 = rootView.findViewById(R.id.sp_county2);
        mEt_town = rootView.findViewById(R.id.et_town);
        mEt_addrdetail2 = rootView.findViewById(R.id.et_addrdetail2);
        mEt_enforcement_name = rootView.findViewById(R.id.et_enforcement_name);
        initData();
        setListener();
    }

    private void setListener() {
        mTv_problem.setOnClickListener(this);
        mBt_save.setOnClickListener(this);
        mBt_select_begintime.setOnClickListener(this);
        mBt_select_endtime.setOnClickListener(this);
        mRb_illegal_person.setOnCheckedChangeListener(this);
        mRb_victim.setOnCheckedChangeListener(this);
        mRb_other.setOnCheckedChangeListener(this);
    }

    private void initData() {


        initSpinnerData();
        initPopupWindow();


    }

    private void initSpinnerData() {
        final String[] caseTypeArray = getResources().getStringArray(R.array.spinner_case_type);
        SpinnerArrayAdapter caseTypeAdapter = new SpinnerArrayAdapter(caseTypeArray);
        mSp_type.setAdapter(caseTypeAdapter);

        mSp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCasetypeName = caseTypeArray[i];
                if (mCasetypeName.equals("无河道采砂许可证案")) {
                    mCasetype = "1";
                } else if (mCasetypeName.equals("水土保持方案违法案")) {
                    mCasetype = "2";
                }
                mProblemContent = LawDao.getProblemContent(mCasetypeName);
                if (mAskAdapter == null) {

                    mAskAdapter = new AskAdapter(getActivity(), mProblemContent);
                    mLv_ask.setAdapter(mAskAdapter);
                } else {

                    mAskAdapter.setDatas(mProblemContent);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        citySpinnerData(mSp_city, mSp_county);
        citySpinnerData(mSp_city2, mSp_county2);


    }

    private void citySpinnerData(Spinner citySpinner, final Spinner countySpinner) {
        final String[] cityArray = getResources().getStringArray(R.array.lv_city);
        SpinnerArrayAdapter cityAdapter = new SpinnerArrayAdapter(cityArray);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int countyId = countyIds[position];
                if (countyId == 0) {
                    String[] countyArray = {"无"};
                    SpinnerArrayAdapter countyAdapter = new SpinnerArrayAdapter(countyArray);
                    countySpinner.setAdapter(countyAdapter);
                } else {

                    String[] countyArray = getResources().getStringArray(countyIds[position]);
                    SpinnerArrayAdapter countyAdapter = new SpinnerArrayAdapter(countyArray);
                    countySpinner.setAdapter(countyAdapter);
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
            case R.id.tv_problem:
                showPopWindow();
                break;
            case R.id.bt_save:    //保存

                savaData();
                break;
            case R.id.bt_select_begintime:
                if (mBeginDialogAll == null) {
                    initBeginWheelYearMonthDayDialog();
                }
                mBt_select_endtime.setText("选择");
                mBeginDialogAll.show(getFragmentManager(), "beginTime");

                break;
            case R.id.bt_select_endtime:
                if (mEndDialogAll==null){

                    initEndWheelYearMonthDayDialog();
                }
                mEndDialogAll.show(getFragmentManager(), "endTime");
                break;
            default:
                break;
        }
    }

    private void savaData() {

        String begintime = mBt_select_begintime.getText().toString().trim();
        String endtime = mBt_select_endtime.getText().toString().trim();
        if (begintime.equals("选择") || endtime.equals("选择")) {
            RxToast.warning(MyApp.getApplictaion(), "请选择笔录时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String askContent = mTet_content.getText().toString().trim();
        String city = mSp_city.getSelectedItem().toString().trim();
        String county = mSp_county.getSelectedItem().toString().trim();
        String addrdetail = mEt_addrdetail.getText().toString().trim();
        String askPerson = mTet_ask_person.getText().toString().trim();
        String askEnforcementNumber = mTet_ask_enforcement_number.getText().toString().trim();
        final String recorder = mTet_recorder.getText().toString().trim();
        String recorderNumber = mTet_recorder_number.getText().toString().trim();
        String askingPeople = mTet_asking_people.getText().toString().trim();
        String askingIdcard = mTet_asking_idcard.getText().toString().trim();
        String askingPosition = mTet_asking_position.getText().toString().trim();
        String askingWorkUnits = mTet_asking_work_units.getText().toString().trim();
        String city2 = mSp_city2.getSelectedItem().toString().trim();
        String county2 = mSp_county2.getSelectedItem().toString().trim();
        String town = mEt_town.getText().toString().trim();
        String addrdetail2 = mEt_addrdetail2.getText().toString().trim();
        String enforcementName = mEt_enforcement_name.getText().toString().trim();

        if (mAnswBeans == null) {
            mAnswBeans = new ArrayList<>();
        }
        for (int i = 0; i < mProblemContent.size(); i++) {
            RelativeLayout layout = (RelativeLayout) mLv_ask.getChildAt(i);// 获得子item的layout
            TextView tv_ask = (TextView) layout.findViewById(R.id.tv_ask_content);
            EditText et_answer = (EditText) layout.findViewById(R.id.et_answer_content);// 从layout中获得控件,根据其id
            String ask = tv_ask.getText().toString().trim();
            String answ = et_answer.getText().toString().trim();
            if (TextUtils.isEmpty(answ)) {
                RxToast.warning(MyApp.getApplictaion(), "问题笔录不能有空", Toast.LENGTH_SHORT).show();
                return;
            }

            AnswBean answBean = new AnswBean(ask, answ, i);
            mAnswBeans.add(answBean);

        }

        if (TextUtils.isEmpty(askContent)) {
            RxToast.warning("询问内容不能为空");
            return;
        }


        if (TextUtils.isEmpty(addrdetail)) {
            RxToast.warning("询问地点不能为空");
            return;
        }

        if (TextUtils.isEmpty(askPerson)) {
            RxToast.warning("询问人不能为空");
            return;
        }

        if (TextUtils.isEmpty(askEnforcementNumber)) {
            RxToast.warning("询问人执法编号不能为空");
            return;
        }

        if (TextUtils.isEmpty(recorder)) {
            RxToast.warning("记录人不能为空");
            return;
        }

        if (TextUtils.isEmpty(recorderNumber)) {
            RxToast.warning("记录人执法编号不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingPeople)) {
            RxToast.warning("被询问人姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingIdcard)) {
            RxToast.warning("被询问人身份证号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingPosition)) {
            RxToast.warning("被询问人职务不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingWorkUnits)) {
            RxToast.warning("被询问人工作单位不能为空");
            return;
        }

        if (TextUtils.isEmpty(town)) {
            RxToast.warning("被询问人住址不能为空");
            return;
        }

        if (TextUtils.isEmpty(addrdetail2)) {
            RxToast.warning("被询问人住址不能为空");
            return;
        }

        if (TextUtils.isEmpty(enforcementName)) {
            RxToast.warning("执法人员姓名不能为空");
            return;
        }

        String address1;
        if (county.equals("无")) {
            address1 = "广东省" + city + "市" + addrdetail;
        } else {
            address1 = "广东省" + city + "市" + county + "县" + addrdetail;

        }
        String address2;
        if (county2.equals("无")) {
            address2 = "广东省" + city2 + "市" + town + addrdetail2;
        } else {
            address2 = "广东省" + city2 + "市" + county2 + "县" + town + "镇" + addrdetail2;

        }


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
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/dcblxx/addDcblForAN");
        params.addBodyParameter("ajlx", mCasetypeName);
        params.addBodyParameter("xwnr", askContent);
        params.addBodyParameter("blbegindate", begintime);
        params.addBodyParameter("blenddate", endtime);
        params.addBodyParameter("xwdd", address1);
        params.addBodyParameter("xwrname", askPerson);
        params.addBodyParameter("xwrcode", askEnforcementNumber);
        params.addBodyParameter("jlrname", recorder);
        params.addBodyParameter("jlrcode", recorderNumber);
        params.addBodyParameter("bxwrtype", mBxwrtype);
        params.addBodyParameter("bxwrname", askingPeople);
        params.addBodyParameter("bxwridcode", askingIdcard);
        params.addBodyParameter("bxwrposition", askingPosition);
        params.addBodyParameter("bxwrdept", askingPosition);
        params.addBodyParameter("bxwraddr", address2);
        params.addBodyParameter("zfryintroduction", "我是" + enforcementName + "执法人员，现在我需要问一些问题，你要如实回答。");
        params.addBodyParameter("jllrrid", DefaultContants.CURRENTUSER.getUserId());
        if (mGson == null) {
            mGson = new Gson();

        }
        params.addBodyParameter("dcblnr", mGson.toJson(mAnswBeans));
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                mRxDialogLoading.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        RxToast.success(MyApp.getApplictaion(), "数据提交成功", Toast.LENGTH_SHORT).show();
                        mTet_content.setText("");
                        mEt_addrdetail.setText("");
                        mTet_ask_person.setText("");
                        mTet_ask_enforcement_number.setText("");
                        mTet_recorder.setText("");
                        mTet_recorder_number.setText("");
                        mTet_asking_people.setText("");
                        mTet_asking_idcard.setText("");
                        mTet_asking_position.setText("");
                        mTet_asking_work_units.setText("");
                        mEt_addrdetail2.setText("");
                        mEt_enforcement_name.setText("");

                        for (ProblemBean problemBean : mProblemContent) {
                            problemBean.setContent("");
                        }

                        mAskAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    RxToast.error(MyApp.getApplictaion(), "数据提交失败,服务器错误", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mRxDialogLoading.cancel();
                RxToast.error(MyApp.getApplictaion(), "数据提交失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

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


    private void initPopupWindow() {
        //要在布局中显示的布局
        if (mContentView == null) {

            mContentView = LayoutInflater.from(getActivity()).inflate(R.layout.questions_pupwindow, null, false);
            mLv_questions = mContentView.findViewById(R.id.lv_questions);
            final TextView tv_add = mContentView.findViewById(R.id.tv_add);
            final TextView tv_back = mContentView.findViewById(R.id.tv_back);
            mTv_typename = mContentView.findViewById(R.id.tv_typename);
            final TextView tv_tag1 = mContentView.findViewById(R.id.tv_tag1);
            final EditText et_ask_content = mContentView.findViewById(R.id.et_ask_content);
            final Button bt_add = mContentView.findViewById(R.id.bt_add);
            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_add.setVisibility(View.GONE);
                    tv_back.setVisibility(View.VISIBLE);
                    mLv_questions.setVisibility(View.GONE);
                    mTv_typename.setVisibility(View.VISIBLE);
                    tv_tag1.setVisibility(View.VISIBLE);
                    et_ask_content.setVisibility(View.VISIBLE);
                    bt_add.setVisibility(View.VISIBLE);
                }
            });

            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_add.setVisibility(View.VISIBLE);
                    tv_back.setVisibility(View.GONE);
                    mLv_questions.setVisibility(View.VISIBLE);
                    mTv_typename.setVisibility(View.GONE);
                    tv_tag1.setVisibility(View.GONE);
                    et_ask_content.setVisibility(View.GONE);
                    bt_add.setVisibility(View.GONE);
                }
            });

            bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ask_content = et_ask_content.getText().toString().trim();
                    if (!TextUtils.isEmpty(ask_content)) {
                        ProblemBean problemBean = new ProblemBean();

                        problemBean.setAsk(ask_content);
                        problemBean.setTypename(mCasetypeName);
                        problemBean.setType(mCasetype);
                        long state = LawDao.insetProblemContent(problemBean);
                        if (state > 0) {
                            RxToast.success(MyApp.getApplictaion(), "问题新增成功", Toast.LENGTH_SHORT).show();
                            problemBean.setRowid((int) state);
                            mProblemContent.add(problemBean);
                            mAskAdapter.setDatas(mProblemContent);
                        } else {
                            RxToast.error(MyApp.getApplictaion(), "问题新增失败", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            });
        }
        if (mPopupWindow == null) {
            //实例化PopupWindow并设置宽高
            mPopupWindow = new PopupWindow(mContentView, LinearLayout.LayoutParams.MATCH_PARENT, 400);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            //设置可以点击
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(true);
        }
    }

    private void showPopWindow() {
        mProblemContent = LawDao.getProblemContent(mCasetypeName);
        if (mProblemAdapter == null) {

            mProblemAdapter = new ProblemAdapter(getActivity(), WrittenRecordFragment.this);
        }
        mTv_typename.setText("案件类型：" + mCasetypeName);
        mProblemAdapter.setDatas(mProblemContent);
        mLv_questions.setAdapter(mProblemAdapter);
        mPopupWindow.showAtLocation(mInflate, Gravity.BOTTOM, 20, 20);
    }

    public void notyAskData() {
        mAskAdapter.setDatas(mProblemContent);
        mAskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mInflate != null) {
            ((ViewGroup) mInflate.getParent()).removeView(mInflate);
        }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        switch (compoundButton.getId()) {
            case R.id.rb_illegal_person:
                if (ischecked) {

                    mBxwrtype = "0";
                }
                break;
            case R.id.rb_victim:
                if (ischecked) {

                    mBxwrtype = "1";
                }
                break;
            case R.id.rb_other:
                if (ischecked) {

                    mBxwrtype = "3";
                }
                break;
            default:
                break;

        }
    }
}
