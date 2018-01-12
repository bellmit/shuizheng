package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.EmergencyAllTaskAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.AllEmergencyTaskBean;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

import static gd.water.oking.com.cn.wateradministration_gd.R.id.tv;

/**
 * 紧急任务列表
 */
public class EmergencyTaskListFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PullToRefreshListView mPullToRefreshListView;
    private TextView mTv;
    private long mTimeMillis;
    private Button mAdd_btn;
    private MissionFragment mMissionFragment;
    private TemporaryEmergencyTaskFragment mTemporaryEmergencyTaskFragment;
    private ArrayList<AllEmergencyTaskBean> mAllEmergencyTaskBeens;
    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogLoading mRxDialogLoading;
    private EmergencyAllTaskAdapter mAdapter;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public EmergencyTaskListFragment() {
        // Required empty public constructor
    }

    public static EmergencyTaskListFragment newInstance(String param1, String param2) {
        EmergencyTaskListFragment fragment = new EmergencyTaskListFragment();
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
        View view = inflater.inflate(R.layout.fragment_emergency_task_list, container, false);
        return view;
    }

    @Override
    public void initView(View rootView) {
        mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.lv);
        mTv = (TextView) rootView.findViewById(tv);
        mAdd_btn = (Button) rootView.findViewById(R.id.add_Btn);
        initData();
        initLister();

    }

    private void initData() {

        //mPullRefreshListView.getMode();//得到模式
        //上下都可以刷新的模式。这里有两个选择：Mode.PULL_FROM_START，Mode.BOTH，PULL_FROM_END
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(MyApp.getApplictaion(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                long timeMillis = System.currentTimeMillis();
                if (timeMillis - mTimeMillis > 5000) {
                    getNetData();
                } else {
//                    解决调用onRefreshComplete无效（时间太短）
                    mPullToRefreshListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();

                        }
                    }, 1000);
                    RxToast.warning(MyApp.getApplictaion(), "数据刷新太频繁了,请稍后再试", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

//                getMoreData();
            }
        });

        mPullToRefreshListView.onRefreshComplete();
        getNetData();

    }

    //获取紧急任务列表
    private void getNetData() {
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/getAllEmergencyTaskForAndroid");
        params.addBodyParameter("USERID", DefaultContants.CURRENTUSER.getUserId());
        params.addBodyParameter("deptId", DefaultContants.CURRENTUSER.getDeptId());
        params.addBodyParameter("fbrid", DefaultContants.CURRENTUSER.getUserId());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                RxToast.success(MyApp.getApplictaion(), "数据获取成功", Toast.LENGTH_SHORT).show();
                mPullToRefreshListView.onRefreshComplete();
                mTimeMillis = System.currentTimeMillis();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    mAllEmergencyTaskBeens = new ArrayList<AllEmergencyTaskBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AllEmergencyTaskBean allEmergencyTaskBean = new AllEmergencyTaskBean();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        allEmergencyTaskBean.setAPPROVED_PERSON(jsonObject.getString("APPROVED_PERSON"));
                        allEmergencyTaskBean.setAPPROVED_TIME(jsonObject.getLong("APPROVED_TIME"));
                        allEmergencyTaskBean.setBEGIN_TIME(jsonObject.getLong("BEGIN_TIME"));
                        allEmergencyTaskBean.setCREATE_TIME(jsonObject.getLong("CREATE_TIME"));
                        allEmergencyTaskBean.setDELIVERY_TIME(jsonObject.getLong("DELIVERY_TIME"));
                        allEmergencyTaskBean.setDEPT_ID(jsonObject.getString("DEPT_ID"));
                        allEmergencyTaskBean.setEND_TIME(jsonObject.getLong("END_TIME"));
                        allEmergencyTaskBean.setFBDW(jsonObject.getString("FBDW"));
                        allEmergencyTaskBean.setFBR(jsonObject.getString("FBR"));
                        allEmergencyTaskBean.setFBRID(jsonObject.getString("FBRID"));
                        allEmergencyTaskBean.setFID(jsonObject.getString("FID"));
                        String taskid = jsonObject.getString("ID");

                        allEmergencyTaskBean.setID(taskid);
                        if ("0".equals(jsonObject.getString("JJCD"))) {

                            allEmergencyTaskBean.setJJCD("特急");
                        } else if ("1".equals(jsonObject.getString("JJCD"))) {
                            allEmergencyTaskBean.setJJCD("紧急");
                        } else if ("2".equals(jsonObject.getString("JJCD"))) {
                            allEmergencyTaskBean.setJJCD("一般");
                        }

                        allEmergencyTaskBean.setJSDW(jsonObject.getString("JSDW"));
                        allEmergencyTaskBean.setJSR(jsonObject.getString("JSR"));
                        allEmergencyTaskBean.setJSRID(jsonObject.getString("JSRID"));
                        allEmergencyTaskBean.setPUBLISHER(jsonObject.getString("PUBLISHER"));
                        allEmergencyTaskBean.setRECEIVER(jsonObject.getString("RECEIVER"));

                        if ("0".equals(jsonObject.getString("RWLX"))) {

                            allEmergencyTaskBean.setRWLX("日常执法");
                        } else if ("1".equals(jsonObject.getString("RWLX"))) {
                            allEmergencyTaskBean.setRWLX("联合执法");
                        } else if ("2".equals(jsonObject.getString("RWLX"))) {
                            allEmergencyTaskBean.setRWLX("专项执法");
                        } else if ("3".equals(jsonObject.getString("RWLX"))) {
                            allEmergencyTaskBean.setRWLX("目标核查");
                        }

                        if ("0".equals(jsonObject.getString("RWLY"))) {

                            allEmergencyTaskBean.setRWLY("上级交办");
                        } else if ("1".equals(jsonObject.getString("RWLY"))) {
                            allEmergencyTaskBean.setRWLY("部门移送");
                        } else if ("2".equals(jsonObject.getString("RWLY"))) {
                            allEmergencyTaskBean.setRWLY("系统报警");
                        } else if ("3".equals(jsonObject.getString("RWLY"))) {
                            allEmergencyTaskBean.setRWLY("日常巡查");
                        } else if ("4".equals(jsonObject.getString("RWLY"))) {
                            allEmergencyTaskBean.setRWLY("媒体披露");
                        } else if ("5".equals(jsonObject.getString("RWLY"))) {
                            allEmergencyTaskBean.setRWLY("群众举报");
                        }

                        allEmergencyTaskBean.setRWMC(jsonObject.getString("RWMC"));
                        allEmergencyTaskBean.setRWMS(jsonObject.getString("RWMS"));
                        allEmergencyTaskBean.setRWQYMS(jsonObject.getString("RWQYMS"));
                        allEmergencyTaskBean.setSPR(jsonObject.getString("SPR"));
                        allEmergencyTaskBean.setSPRID(jsonObject.getString("SPRID"));
                        if ("1".equals(jsonObject.getString("STATUS"))) {
                            allEmergencyTaskBean.setSTATUS("已发布");
                        } else if ("2".equals(jsonObject.getString("STATUS"))) {
                            allEmergencyTaskBean.setSTATUS("审核通过");
                        } else if ("3".equals(jsonObject.getString("STATUS"))) {
                            allEmergencyTaskBean.setSTATUS("接收并已分配队员");
                        } else if ("4".equals(jsonObject.getString("STATUS"))) {
                            allEmergencyTaskBean.setSTATUS("任务开始");
                        } else if ("5".equals(jsonObject.getString("STATUS"))) {
                            allEmergencyTaskBean.setSTATUS("任务完成");
                        }


                        if ("0".equals(jsonObject.getString("TYPEOFTASK"))){
                            allEmergencyTaskBean.setTYPEOFTASK("河道管理");
                        }else if ("1".equals(jsonObject.getString("TYPEOFTASK"))){
                            allEmergencyTaskBean.setTYPEOFTASK("河道采砂");
                        }else if ("2".equals(jsonObject.getString("TYPEOFTASK"))){
                            allEmergencyTaskBean.setTYPEOFTASK("水资源管理");
                        }else if ("3".equals(jsonObject.getString("TYPEOFTASK"))){
                            allEmergencyTaskBean.setTYPEOFTASK("水土保持管理");
                        }else if ("4".equals(jsonObject.getString("TYPEOFTASK"))){
                            allEmergencyTaskBean.setTYPEOFTASK("水利工程管理");
                        }


                        allEmergencyTaskBean.setTASK_NAME(jsonObject.getString("TASK_NAME"));


                        mAllEmergencyTaskBeens.add(allEmergencyTaskBean);


                    }
                    mAdapter = new EmergencyAllTaskAdapter(mAllEmergencyTaskBeens);
                    mPullToRefreshListView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    RxToast.error(MyApp.getApplictaion(), "数据解析失败" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mPullToRefreshListView.onRefreshComplete();
                mPullToRefreshListView.setVisibility(View.GONE);
                mTv.setVisibility(View.VISIBLE);
                RxToast.error(MyApp.getApplictaion(), "获取数据失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();

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
        mAdd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //临时紧急任务
                if (mTemporaryEmergencyTaskFragment == null) {
                    mTemporaryEmergencyTaskFragment = TemporaryEmergencyTaskFragment.newInstance(null, null);

                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_root, mTemporaryEmergencyTaskFragment).commit();
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AllEmergencyTaskBean allEmergencyTaskBean = mAllEmergencyTaskBeens.get(i - 1);
                String fbrid = allEmergencyTaskBean.getFBRID();

                if (fbrid.equals(DefaultContants.CURRENTUSER.getUserId())){
                    String id = allEmergencyTaskBean.getID();

                    if (mMissionFragment == null) {
                        mMissionFragment = new MissionFragment();
                    }
                    mMissionFragment.setDefaultTaskID(id);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_root, mMissionFragment).commit();

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View inflate = View.inflate(getActivity(),R.layout.emergency_task_dialog,null);
                    TextView tv_taskname = (TextView) inflate.findViewById(R.id.tv_taskname);
                    TextView tv_tasktype = (TextView) inflate.findViewById(R.id.tv_tasktype);
                    TextView publisher_tv = (TextView) inflate.findViewById(R.id.publisher_tv);
                    TextView tv_approver = (TextView) inflate.findViewById(R.id.tv_approver);
                    TextView tv_souce = (TextView) inflate.findViewById(R.id.tv_souce);
                    TextView tv_tasknature = (TextView) inflate.findViewById(R.id.tv_tasknature);
                    TextView tv_begintime = (TextView) inflate.findViewById(R.id.tv_begintime);
                    TextView tv_endtime = (TextView) inflate.findViewById(R.id.tv_endtime);
                    TextView list_item_missionMember = (TextView) inflate.findViewById(R.id.list_item_missionMember);
                    TextView list_item_missionDetail = (TextView) inflate.findViewById(R.id.list_item_missionDetail);
                    TextView tv_description = (TextView) inflate.findViewById(R.id.tv_description);
                    tv_taskname.setText(allEmergencyTaskBean.getRWMC());
                    tv_tasktype.setText(allEmergencyTaskBean.getRWLX());
                    publisher_tv.setText(allEmergencyTaskBean.getFBR());
                    tv_approver.setText(allEmergencyTaskBean.getSPR());
                    tv_souce.setText(allEmergencyTaskBean.getRWLY());
                    tv_tasknature.setText(allEmergencyTaskBean.getTYPEOFTASK());
                    tv_begintime.setText(sf.format(new Date(allEmergencyTaskBean.getBEGIN_TIME())));
                    tv_endtime.setText(sf.format(new Date(allEmergencyTaskBean.getEND_TIME())));
                    list_item_missionMember.setText(allEmergencyTaskBean.getJSR());
                    list_item_missionDetail.setText(allEmergencyTaskBean.getRWQYMS());
                    tv_description.setText(allEmergencyTaskBean.getRWMS());
                    builder.setView(inflate);
                    builder.show();

                }

            }
        });


        ListView refreshableView = mPullToRefreshListView.getRefreshableView();
        refreshableView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getContext());
                    mRxDialogSureCancel.setContent("确定删除任务？");
                }
                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogSureCancel.cancel();
                    }
                });
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogSureCancel.cancel();
                        deleteTask(position);
                    }
                });
                mRxDialogSureCancel.show();
                return true;
            }
        });
    }


    private void deleteTask(final int position) {
        if (mAllEmergencyTaskBeens.get(position - 1).getFBRID().equals(DefaultContants.CURRENTUSER.getUserId())){
            if (mRxDialogLoading == null) {
                mRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                });
                mRxDialogLoading.setLoadingText("正在删除,请稍等...");
            }

            mRxDialogLoading.show();
            final String emId = mAllEmergencyTaskBeens.get(position - 1).getID();
            RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/deleteEmergencyReleaseForAndroid");
            params.addBodyParameter("id", emId);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    mRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code==400){

                            mAllEmergencyTaskBeens.remove(position-1);
                            mAdapter.notifyDataSetChanged();

                            ArrayList<Mission> missionList = MainActivity.missionList;
                            for (int i = 0;i<missionList.size();i++){
                                String id = missionList.get(i).getId();
                                if (emId.equals(id)){
                                    MainActivity.missionList.remove(i);
                                    Intent intent = new Intent(MainActivity.UPDATE_MISSION_LIST);
                                    MyApp.getApplictaion().sendBroadcast(intent);
                                }
                            }
                            RxToast.success(MyApp.getApplictaion(), "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RxToast.error(MyApp.getApplictaion(), "删除失败,服务器内部错误", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    mRxDialogLoading.cancel();
                    RxToast.error(MyApp.getApplictaion(), "删除失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else {
            RxToast.warning(MyApp.getApplictaion(),"您无权删除他人发布的任务！",Toast.LENGTH_LONG).show();
        }

    }
}
