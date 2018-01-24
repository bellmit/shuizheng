package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSure;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.MemberAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.ReceivivingExpandableListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.SearchMissionEditText;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.bean.ReceptionStaffBean;
import gd.water.oking.com.cn.wateradministration_gd.http.AddMissionMemberParams;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.QRMissionParams;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.MyCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.ArrangeMissionPinyinComparator;

/**
 * 任务接收安排
 * A simple {@link Fragment} subclass.
 */
public class ArrangeMissionFragment extends BaseFragment {
    private MyCallBack mMyCallBack;
    private Button qrMission_button;
    private TextView arrange_title_textView;
    private ListView canSelect_listView;
    private MemberAdapter canSelectMemberAdapter;

    private ArrayList<Mission> allMissionList = MainActivity.missionList;
    private ArrayList<Mission> missionList = new ArrayList<>();

    private ArrayList<Member> canSelectMemberList = new ArrayList<>();
//    private ArrayList<Member> SelectedMemberList = new ArrayList<>();

    private ExpandableListView missionListView;
    private ReceivivingExpandableListAdapter missionListAdapter;
    private Mission selectMission;
    private UpcomingFragment mUpcomingFragment;
    private int selectItemIndex = -1;
    private String defaultTaskID = null;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_MISSION_LIST:

                    missionList.clear();
                    for (int i = 0; i < allMissionList.size(); i++) {
                        if (allMissionList.get(i).getStatus() == 2 || allMissionList.get(i).getStatus() == 3) {
                            missionList.add(allMissionList.get(i));
                        }
                    }

                    if (missionListAdapter != null && missionList.size() > 0) {
                        missionListAdapter.setDataList(missionList);
                    }


                    break;
            }
        }
    };
    private Mission mMission;
    private Gson mGson;

    public ArrangeMissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_MISSION_LIST));

        missionList.clear();
        for (int i = 0; i < allMissionList.size(); i++) {
            if (allMissionList.get(i).getStatus() == 2 || allMissionList.get(i).getStatus() == 3) {
                missionList.add(allMissionList.get(i));
            }
        }
        return inflater.inflate(R.layout.fragment_arrange_mission, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        qrMission_button = (Button) rootView.findViewById(R.id.qrMission_button);

        arrange_title_textView = (TextView) rootView.findViewById(R.id.arrange_title_textView);
        canSelect_listView = (ListView) rootView.findViewById(R.id.canSelect_listView);


        canSelectMemberAdapter = new MemberAdapter(getActivity(), canSelectMemberList);



        missionListView = (ExpandableListView) rootView.findViewById(R.id.mission_listView);

        missionListAdapter = new ReceivivingExpandableListAdapter(missionList, new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        missionListView.setAdapter(missionListAdapter);
        //        //默认展开第一个分组
//        elv.expandGroup(0);
//展开某个分组时，并关闭其他分组。注意这里设置的是 ExpandListener
        missionListAdapter.disButton(false);
        missionListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < missionList.size(); i++) {
                    if (i != groupPosition) {
                        missionListView.collapseGroup(i); //收起某个指定的组
                    }
                }
                if (missionList.get(groupPosition).getStatus() == 3) {
                    RxToast.warning(MyApp.getApplictaion(), "该任务已经安排了队员", Toast.LENGTH_LONG).show();
                } else {
                    mMission = missionList.get(groupPosition);
                    if (missionList.size() > groupPosition && groupPosition >= 0 && missionList.get(groupPosition) != null) {
                        httpGetCanSelectMember(missionList.get(groupPosition));

                        selectMission = missionList.get(groupPosition);


                        arrange_title_textView.setText(missionList.get(groupPosition).getTask_name() + " 队员安排");

                        missionListAdapter.notifyDataSetInvalidated();

                    }
                }


            }
        });


        SearchMissionEditText searchMissionEditText = (SearchMissionEditText) rootView.findViewById(R.id.searchMission_editText);
        searchMissionEditText.setDataList(missionList);
        searchMissionEditText.setMissionListView(missionListView);


        qrMission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canSelectMemberAdapter.getCheckName().size() < 1) {
                    RxToast.warning(MyApp.getApplictaion(), "请选择队员", Toast.LENGTH_SHORT).show();
                    return;
                }

                QRMission(selectMission);
                final RxDialogSure rxDialogSure = new RxDialogSure(getContext(), false, null);//提示弹窗
                rxDialogSure.setTitle("任务确认成功！");
                rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogSure.cancel();
                        if (mUpcomingFragment == null) {

                            mUpcomingFragment = new UpcomingFragment();
                        }
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.fragment_root, mUpcomingFragment).commit();
                    }

                });
                rxDialogSure.show();


            }
        });

    }


    private void httpGetCanSelectMember(Mission mission) {

        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/cs/getUserByPosition");
        params.addBodyParameter("lx", "SZJC,CBR");


        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (mGson==null){
                    mGson = new Gson();
                }

                ReceptionStaffBean receptionStaff = mGson.fromJson(result, ReceptionStaffBean.class);
                List<ReceptionStaffBean.SZJCBean> szjc = receptionStaff.getSZJC();
                List<ReceptionStaffBean.CBRBean> cbr = receptionStaff.getCBR();
                ArrayList<Member> mbList =new ArrayList<>();
                for (ReceptionStaffBean.SZJCBean szjcBean:szjc){
                    Member member = new Member();
                    member.setUsername(szjcBean.getUSERNAME());
                    member.setUserid(szjcBean.getUSERID());
                    member.setId(szjcBean.getUSERID());
                    mbList.add(member);
                }

                for (ReceptionStaffBean.CBRBean cbrBean:cbr){
                    Member member = new Member();
                    member.setUsername(cbrBean.getUSERNAME());
                    member.setUserid(cbrBean.getUSERID());
                    member.setId(cbrBean.getUSERID());
                    mbList.add(member);
                }


                canSelectMemberList.clear();

                if (mbList!=null&&mbList.size()>0){
                    for (int i = 0; i < mbList.size(); i++) {
                        Member member = mbList.get(i);

                        canSelectMemberList.add(member);
                    }
                    qrMission_button.setVisibility(View.VISIBLE);
                    Collections.sort(canSelectMemberList,new ArrangeMissionPinyinComparator());
                    canSelect_listView.setAdapter(canSelectMemberAdapter);
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                qrMission_button.setVisibility(View.GONE);
                canSelectMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void addMember(final Member member) {

        member.setTaskid(selectMission.getId());

        AddMissionMemberParams params = new AddMissionMemberParams();
        params.userid = DefaultContants.CURRENTUSER.getUserId();
        params.task_id = member.getTaskid();
        params.userids = member.getUserid();

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

//                canSelectMemberList.remove(member);
//                SelectedMemberList.add(member);
                member.setId(result);

//                canSelectMemberAdapter.notifyDataSetChanged();
//                SelectedMemberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                RxToast.error(MyApp.getApplictaion(), "选择失败", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //确认
    private void QRMission(Mission mission) {
        List<Member> checkName = canSelectMemberAdapter.getCheckName();

        for (Member m : checkName) {
            if (!m.getUsername().equals(mMission.getReceiver_name())) {

                addMember(m);
            }
        }


        QRMissionParams params = new QRMissionParams();
        params.id = mission.getId();
        params.jsrid = DefaultContants.CURRENTUSER.getUserId();

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                mMyCallBack.refreshMission();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                mMyCallBack.refreshMission();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mMyCallBack = (MyCallBack) context;
        }
    }

    public void setDefaultTaskID(String defaultTaskID) {
        this.defaultTaskID = defaultTaskID;
    }
}
