package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import gd.water.oking.com.cn.wateradministration_gd.Adapter.InspectTaskListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.InspectTaskBean;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

import static gd.water.oking.com.cn.wateradministration_gd.R.id.tv;

/**
 * 巡查任务列表
 */
public class InspectTaskListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogLoading mRxDialogLoading;
    private InspectTaskListAdapter mAdapter;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ArrayList<InspectTaskBean> mInspectTaskBeens;
//    private RxDialogSureCancel mRxDialogSureCancel1;
//    private RxDialogLoading mRxDialogLoading1;

    public InspectTaskListFragment() {
        // Required empty public constructor
    }

    public static InspectTaskListFragment newInstance(String param1, String param2) {
        InspectTaskListFragment fragment = new InspectTaskListFragment();
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
        return inflater.inflate(R.layout.fragment_inspect_task_list, container, false);
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


    //获取巡查任务列表
    private void getNetData() {
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/rwfb");
        params.addBodyParameter("lx", "getData");
        params.addBodyParameter("USERID", DefaultContants.CURRENTUSER.getUserId());
        params.addBodyParameter("deptId", DefaultContants.CURRENTUSER.getDeptId());
        params.addBodyParameter("fbrid", DefaultContants.CURRENTUSER.getUserId());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mPullToRefreshListView.onRefreshComplete();
                mTimeMillis = System.currentTimeMillis();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    mInspectTaskBeens = new ArrayList<InspectTaskBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        InspectTaskBean inspectTaskBean = new InspectTaskBean();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        inspectTaskBean.setAPPROVED_PERSON(jsonObject.getString("APPROVED_PERSON"));
                        inspectTaskBean.setBEGIN_TIME(jsonObject.getLong("BEGIN_TIME"));
                        inspectTaskBean.setCREATE_TIME(jsonObject.getLong("CREATE_TIME"));
                        inspectTaskBean.setDELIVERY_TIME(jsonObject.getLong("DELIVERY_TIME"));
                        inspectTaskBean.setDEPT_ID(jsonObject.getString("DEPT_ID"));
                        inspectTaskBean.setEND_TIME(jsonObject.getLong("END_TIME"));
                        inspectTaskBean.setFBDW(jsonObject.getString("FBDW"));
                        inspectTaskBean.setFBR(jsonObject.getString("FBR"));
                        inspectTaskBean.setFBRID(jsonObject.getString("FBRID"));
                        inspectTaskBean.setFID(jsonObject.getString("FID"));
                        String taskid = jsonObject.getString("ID");

                        inspectTaskBean.setID(taskid);
                        if ("0".equals(jsonObject.getString("JJCD"))) {

                            inspectTaskBean.setJJCD("特急");
                        } else if ("1".equals(jsonObject.getString("JJCD"))) {
                            inspectTaskBean.setJJCD("紧急");
                        } else if ("2".equals(jsonObject.getString("JJCD"))) {
                            inspectTaskBean.setJJCD("一般");
                        }

                        inspectTaskBean.setJSDW(jsonObject.getString("JSDW"));
                        inspectTaskBean.setJSR(jsonObject.getString("JSR"));
                        inspectTaskBean.setJSRID(jsonObject.getString("JSRID"));
                        inspectTaskBean.setPUBLISHER(jsonObject.getString("PUBLISHER"));
                        inspectTaskBean.setRECEIVER(jsonObject.getString("RECEIVER"));

                        if ("0".equals(jsonObject.getString("RWLX"))) {

                            inspectTaskBean.setRWLX("日常执法");
                        } else if ("1".equals(jsonObject.getString("RWLX"))) {
                            inspectTaskBean.setRWLX("联合执法");
                        } else if ("2".equals(jsonObject.getString("RWLX"))) {
                            inspectTaskBean.setRWLX("专项执法");
                        } else if ("3".equals(jsonObject.getString("RWLX"))) {
                            inspectTaskBean.setRWLX("目标核查");
                        }

                        if ("0".equals(jsonObject.getString("RWLY"))) {

                            inspectTaskBean.setRWLY("上级交办");
                        } else if ("1".equals(jsonObject.getString("RWLY"))) {
                            inspectTaskBean.setRWLY("部门移送");
                        } else if ("2".equals(jsonObject.getString("RWLY"))) {
                            inspectTaskBean.setRWLY("系统报警");
                        } else if ("3".equals(jsonObject.getString("RWLY"))) {
                            inspectTaskBean.setRWLY("日常巡查");
                        } else if ("4".equals(jsonObject.getString("RWLY"))) {
                            inspectTaskBean.setRWLY("媒体披露");
                        } else if ("5".equals(jsonObject.getString("RWLY"))) {
                            inspectTaskBean.setRWLY("群众举报");
                        }

                        inspectTaskBean.setRWMC(jsonObject.getString("RWMC"));
                        inspectTaskBean.setRWMS(jsonObject.getString("RWMS"));
                        inspectTaskBean.setRWQYMS(jsonObject.getString("RWQYMS"));
                        inspectTaskBean.setSPR(jsonObject.getString("SPR"));
                        inspectTaskBean.setSPRID(jsonObject.getString("SPRID"));
                        if ("0".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("未发布");
                        } else if ("1".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("已发布待审核");
                        } else if ("2".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("审核通过");
                        } else if ("3".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("接收并已分配队员");
                        } else if ("4".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("任务开始");
                        } else if ("5".equals(jsonObject.getString("STATUS"))) {
                            inspectTaskBean.setSTATUS("任务完成");
                        }


                        if ("0".equals(jsonObject.getString("TYPEOFTASK"))) {
                            inspectTaskBean.setTYPEOFTASK("河道管理");
                        } else if ("1".equals(jsonObject.getString("TYPEOFTASK"))) {
                            inspectTaskBean.setTYPEOFTASK("河道采砂");
                        } else if ("2".equals(jsonObject.getString("TYPEOFTASK"))) {
                            inspectTaskBean.setTYPEOFTASK("水资源管理");
                        } else if ("3".equals(jsonObject.getString("TYPEOFTASK"))) {
                            inspectTaskBean.setTYPEOFTASK("水土保持管理");
                        } else if ("4".equals(jsonObject.getString("TYPEOFTASK"))) {
                            inspectTaskBean.setTYPEOFTASK("水利工程管理");
                        }

                        inspectTaskBean.setTASK_NAME(jsonObject.getString("TASK_NAME"));


                        mInspectTaskBeens.add(inspectTaskBean);


                    }
                    mAdapter = new InspectTaskListAdapter(mInspectTaskBeens);
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
                //发布任务
                PatrolsToReleaseFragment mPatrolsToReleaseFragment = PatrolsToReleaseFragment.newInstance(null, null);

                getFragmentManager().beginTransaction().replace(R.id.fragment_root, mPatrolsToReleaseFragment).commit();
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private AlertDialog mAlertDialog;
            private RxDialogLoading mRxDialogLoading;
            private RxDialogSureCancel mRxDialogSureCancel;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final InspectTaskBean inspectTaskBean = mInspectTaskBeens.get(i - 1);



                if (inspectTaskBean.getFBRID().equals(DefaultContants.CURRENTUSER.getUserId()) && "未发布".equals(inspectTaskBean.getSTATUS())) {
                    //发布撤回的任务
                    PatrolsToReleaseFragment mWithdrawPatrolsToReleaseFragment = PatrolsToReleaseFragment.newInstance(inspectTaskBean, null);

                    getFragmentManager().beginTransaction().replace(R.id.fragment_root, mWithdrawPatrolsToReleaseFragment).commit();

                    return;

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View inflate = View.inflate(getActivity(), R.layout.inspect_task_list_dialog, null);
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
                Button bt_back = (Button) inflate.findViewById(R.id.bt_back);
                if (inspectTaskBean.getFBRID().equals(DefaultContants.CURRENTUSER.getUserId()) && "已发布待审核".equals(inspectTaskBean.getSTATUS())) {
                    bt_back.setVisibility(View.VISIBLE);
                    bt_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mRxDialogSureCancel == null) {

                                mRxDialogSureCancel = new RxDialogSureCancel(getContext());
                                mRxDialogSureCancel.setContent("确定撤回任务吗？");

                            }
                            mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mRxDialogSureCancel.cancel();
                                    //撤回
                                    withDraw();
                                }
                            });
                            mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mRxDialogSureCancel.cancel();
                                }
                            });
                            mRxDialogSureCancel.show();
                        }

                        private void withDraw() {
                            if (mRxDialogLoading == null) {
                                mRxDialogLoading = new RxDialogLoading(getContext(), false, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogInterface.cancel();
                                    }
                                });
                                mRxDialogLoading.setLoadingText("正在撤回,请稍等...");
                            }
                            mRxDialogLoading.show();
                            RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/withDraw");
                            params.addBodyParameter("id", inspectTaskBean.getID());
                            params.addBodyParameter("sprid", inspectTaskBean.getSPRID());
                            params.addBodyParameter("fbrid", inspectTaskBean.getFBRID());
                            x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    mRxDialogLoading.cancel();
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        int code = jsonObject.getInt("code");
                                        mAlertDialog.dismiss();
                                        if (code==400){

                                            mInspectTaskBeens.get(i - 1).setSTATUS("未发布");
                                            mAdapter.notifyDataSetChanged();
                                            RxToast.success(MyApp.getApplictaion(), "撤回成功！", Toast.LENGTH_LONG).show();

                                        }else if (code==100){

                                            RxToast.error(MyApp.getApplictaion(), "撤回失败！该任务已经通过审核", Toast.LENGTH_LONG).show();
                                            getNetData();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        RxToast.error(MyApp.getApplictaion(), "撤回失败！" + e.getMessage(), Toast.LENGTH_LONG).show();

                                    }



                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {
                                    RxToast.error(MyApp.getApplictaion(), "撤回失败！" + ex.getMessage(), Toast.LENGTH_LONG).show();
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
                    });
                }


                tv_taskname.setText(inspectTaskBean.getRWMC());
                tv_tasktype.setText(inspectTaskBean.getTYPEOFTASK());
                publisher_tv.setText(inspectTaskBean.getFBR());
                tv_approver.setText(inspectTaskBean.getSPR());
                tv_souce.setText(inspectTaskBean.getRWLY());
                tv_tasknature.setText(inspectTaskBean.getRWLX());
                tv_begintime.setText(sf.format(new Date(inspectTaskBean.getBEGIN_TIME())));
                tv_endtime.setText(sf.format(new Date(inspectTaskBean.getEND_TIME())));
                list_item_missionMember.setText(inspectTaskBean.getJSR());
                list_item_missionDetail.setText(inspectTaskBean.getRWQYMS());
                tv_description.setText(inspectTaskBean.getRWMS());
                mAlertDialog = builder.create();
                mAlertDialog.setView(inflate);
                mAlertDialog.show();


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
        //只有未审核的才能删除
        if ("已发布待审核".equals(mInspectTaskBeens.get(position - 1).getSTATUS())) {
            if (mInspectTaskBeens.get(position - 1).getFBRID().equals(DefaultContants.CURRENTUSER.getUserId())) {
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
                final String emId = mInspectTaskBeens.get(position - 1).getID();
                RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/taskPublish/contractorDeleteForAndroid");
                params.addBodyParameter("id", emId);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        mRxDialogLoading.cancel();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int code = jsonObject.getInt("code");
                            if (code == 400) {

                                mInspectTaskBeens.remove(position - 1);
                                mAdapter.notifyDataSetChanged();
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
            } else {
                RxToast.warning(MyApp.getApplictaion(), "您无权删除他人发布的任务！", Toast.LENGTH_LONG).show();
            }

        } else {
            RxToast.warning(MyApp.getApplictaion(), "该任务已经通过审核不能进行删除！", Toast.LENGTH_LONG).show();
        }

    }
}
