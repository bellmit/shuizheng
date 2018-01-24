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
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.UpcomingAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Upcoming;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubUpcomingFragment extends BaseFragment {

    private ListView upcoming_listView;
    private UpcomingAdapter adapter;

    private ArrayList<Upcoming> upcomings = new ArrayList<>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_MISSION_LIST:
                    setUpcomingData();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private MissionFragment mMissionFragment;
    private ArrangeMissionFragment mArrangeMissionFragment;
    private MissionReportFragment mMissionReportFragment;

    public SubUpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_MISSION_LIST));
        return inflater.inflate(R.layout.fragment_sub_upcoming, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        setUpcomingData();

        upcoming_listView = (ListView) rootView.findViewById(R.id.upcoming_listView);
        adapter = new UpcomingAdapter(getContext(), upcomings);
        upcoming_listView.setAdapter(adapter);
        upcoming_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fm = SubUpcomingFragment.this.getParentFragment().getParentFragment().getChildFragmentManager();

                switch (upcomings.get(position).getRwlx()) {
                    case "任务安排接受":
                        if (mArrangeMissionFragment==null){

                            mArrangeMissionFragment = new ArrangeMissionFragment();
                        }

                        mArrangeMissionFragment.setDefaultTaskID(upcomings.get(position).getTaskId());
                        fm.beginTransaction().replace(R.id.fragment_root, mArrangeMissionFragment).commit();
                        break;
                    case "任务执行":
                        if (mMissionFragment==null){

                            mMissionFragment = new MissionFragment();
                        }

                        mMissionFragment.setDefaultTaskID(upcomings.get(position).getTaskId());
                        fm.beginTransaction().replace(R.id.fragment_root, mMissionFragment).commit();
                        break;
                    case "任务上报":
                        if (mMissionReportFragment==null){

                            mMissionReportFragment = new MissionReportFragment();
                        }

                        mMissionReportFragment.setDefaultTaskID(upcomings.get(position).getTaskId());
                        fm.beginTransaction().replace(R.id.fragment_root, mMissionReportFragment).commit();
                        break;
                }
            }
        });
    }

    private void setUpcomingData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();

        upcomings.clear();
        for (int i = 0; i < MainActivity.missionList.size(); i++) {
            if (MainActivity.missionList.get(i).getApproved_time()!=null){
                String jjcd = "0".equals(MainActivity.missionList.get(i).getJjcd()) ? "特急" : ("1".equals(MainActivity.missionList.get(i).getJjcd()) ? "紧急" : "一般");
                if (MainActivity.missionList.get(i).getStatus() == 2 && MainActivity.missionList.get(i).getEnd_time() > date.getTime()) {
                    Upcoming upcoming = new Upcoming(MainActivity.missionList.get(i).getId(), jjcd, "任务安排接受",
                            MainActivity.missionList.get(i).getTask_name(),
                            MainActivity.missionList.get(i).getFbr(), simpleDateFormat.format(new Date(MainActivity.missionList.get(i).getApproved_time())));
                    upcomings.add(upcoming);
                }
                if ((MainActivity.missionList.get(i).getStatus() == 3 ||
                        MainActivity.missionList.get(i).getStatus() == 4) &&
                        MainActivity.missionList.get(i).getEnd_time() > date.getTime()) {


                    Upcoming upcoming = new Upcoming(MainActivity.missionList.get(i).getId(), jjcd, "任务执行",
                            MainActivity.missionList.get(i).getTask_name(),
                            MainActivity.missionList.get(i).getFbr(),
                            simpleDateFormat.format(new Date(MainActivity.missionList.get(i).getApproved_time())));
                    upcomings.add(upcoming);
                }

                if (MainActivity.missionList.get(i).getStatus() == MainActivity.Mission_Completed ||
                        MainActivity.missionList.get(i).getStatus() == 9) {
                    Upcoming upcoming = new Upcoming(MainActivity.missionList.get(i).getId(), jjcd, "任务上报",
                            MainActivity.missionList.get(i).getTask_name(),
                            MainActivity.missionList.get(i).getFbr(),
                            simpleDateFormat.format(new Date(MainActivity.missionList.get(i).getApproved_time())));
                    upcomings.add(upcoming);
                }
            }

        }
    }
}
