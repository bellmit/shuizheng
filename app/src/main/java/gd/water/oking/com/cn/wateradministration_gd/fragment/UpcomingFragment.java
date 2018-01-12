package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.chart.PieData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.MissionListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.DountChartMissionView;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.MyCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends BaseFragment {
    private DataChangeReceiver mDataChangeReceiver = new DataChangeReceiver();
    private MyCallBack mMyCallBack;
    private ArrayList<PieData> pieDatas = new ArrayList<>();
    private ArrayList<Mission> missionList = new ArrayList<>();
    private MissionListAdapter missionListAdapter;
    private Date selectDate;
    private Button upcoming_tabBtn;

    private ImageView temp_imageView;
    private TextView temp_textView, temp2_textView, tempUpt_textView, count1_textView, count2_textView, calendar_title;
    private ListView mission_listView;
    private CompactCalendarView materialcalendarview2;
    private DountChartMissionView dountChartMissionView;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_MISSION_LIST:

                    setDisplayData();

                    getMissionData(selectDate);
                    break;
                case MainActivity.UPDATE_TEMP_UI:

                    setTempView();
                    break;
                default:
                    break;
            }
        }
    };
    private TextView mTv_time;
    private PatrolLogManagementFragment mPatrolLogManagementFragment;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_MISSION_LIST));
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_TEMP_UI));
        MyApp.getApplictaion().registerReceiver(mDataChangeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        MyApp.getApplictaion().unregisterReceiver(mDataChangeReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        mTv_time = (TextView) rootView.findViewById(R.id.tv_time);
        mTv_time.setText(DataUtil.getSystemTime());
        upcoming_tabBtn = (Button) rootView.findViewById(R.id.upcoming_tabBtn);
        upcoming_tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upcoming_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                upcoming_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));

                BaseFragment f = new SubUpcomingFragment();

                FragmentTransaction ft = UpcomingFragment.this.getChildFragmentManager().beginTransaction();
                ft.replace(R.id.upcoming_fragment_root, f).commit();
            }
        });
        upcoming_tabBtn.callOnClick();

        temp_imageView = (ImageView) rootView.findViewById(R.id.temp_imageView);
        temp_textView = (TextView) rootView.findViewById(R.id.temp_textView);
        temp2_textView = (TextView) rootView.findViewById(R.id.temp2_textView);
        tempUpt_textView = (TextView) rootView.findViewById(R.id.tempUpdt_textView);

        ((LinearLayout) temp_imageView.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyCallBack.refreshTemp();
            }
        });

        setTempView();

        count1_textView = (TextView) rootView.findViewById(R.id.count1_textView);
        count2_textView = (TextView) rootView.findViewById(R.id.count2_textView);
        dountChartMissionView = new DountChartMissionView(getContext());
        ((FrameLayout) rootView.findViewById(R.id.chart_layout)).addView(dountChartMissionView);

        setDisplayData();

        mission_listView = (ListView) rootView.findViewById(R.id.mission_listView);
        missionListAdapter = new MissionListAdapter(getContext(), missionList);
        missionListAdapter.setOnItemClickListener(new MissionListAdapter.OnItemClickListener() {
            @Override
            public void onItemBtnClick(int position) {
                Mission mission = missionList.get(position);
                FragmentManager fm = UpcomingFragment.this.getParentFragment().getChildFragmentManager();
                switch (mission.getStatus()) {

                    case 2:
                        ArrangeMissionFragment arrangeMissionFragment = new ArrangeMissionFragment();
                        arrangeMissionFragment.setDefaultTaskID(missionList.get(position).getId());
                        fm.beginTransaction().replace(R.id.fragment_root, arrangeMissionFragment).commit();
                        break;
                    case 3:
                    case 4:
                    case 5:
                        if (mPatrolLogManagementFragment==null){

                            mPatrolLogManagementFragment = PatrolLogManagementFragment.newInstance(mission, null);
                        }
                        fm.beginTransaction().replace(R.id.fragment_root, mPatrolLogManagementFragment).commit();
                        break;
                    case 9:
                        MissionFragment missionFragment = new MissionFragment();
                        missionFragment.setDefaultTaskID(missionList.get(position).getId());
                        fm.beginTransaction().replace(R.id.fragment_root, missionFragment).commit();
                        break;
                    case MainActivity.Mission_Completed:
                        MissionReportFragment missionReportFragment = new MissionReportFragment();
                        missionReportFragment.setDefaultTaskID(missionList.get(position).getId());
                        fm.beginTransaction().replace(R.id.fragment_root, missionReportFragment).commit();
                        break;
                    default:
                        break;
                }
            }
        });
        mission_listView.setAdapter(missionListAdapter);


        calendar_title = (TextView) rootView.findViewById(R.id.calendar_title);

        materialcalendarview2 = (CompactCalendarView) rootView.findViewById(R.id.materialcalendarview2);
        materialcalendarview2.setFirstDayOfWeek(Calendar.SUNDAY);
        materialcalendarview2.setLocale(TimeZone.getDefault(), Locale.getDefault());
        materialcalendarview2.setUseThreeLetterAbbreviation(true);
        materialcalendarview2.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selectDate = dateClicked;
                getMissionData(selectDate);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendar_title.setText(new SimpleDateFormat("yyyy年MM月").format(firstDayOfNewMonth));
                selectDate = firstDayOfNewMonth;
                getMissionData(selectDate);
            }
        });

        selectDate = new Date();
        materialcalendarview2.setCurrentDate(selectDate);
        calendar_title.setText(new SimpleDateFormat("yyyy年MM月").format(selectDate));
        getMissionData(selectDate);
    }

    private void setDisplayData() {
        int mouthMissionCount = 0;
        int mouthMissionCompleteCount = 0;
        for (int i = 0; i < MainActivity.missionList.size(); i++) {
            Mission mission = MainActivity.missionList.get(i);
            Calendar c1 = Calendar.getInstance();
            c1.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), 1, 0, 0, 0);
            Calendar c2 = Calendar.getInstance();
            c2.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.getActualMaximum(Calendar.DATE), 23, 59, 59);
            if (mission.getStatus() >= 2 &&
                    mission.getEnd_time() >= c1.getTimeInMillis() &&
                    mission.getBegin_time() <= c2.getTimeInMillis()) {
                mouthMissionCount += 1;
                if (mission.getStatus() == 5) {
                    mouthMissionCompleteCount += 1;
                }
            }
        }

        if (mouthMissionCount != 0) {
            pieDatas.clear();
            pieDatas.add(new PieData("本月已完成任务数", mouthMissionCompleteCount * 100 / mouthMissionCount, getResources().getColor(R.color.colorMain5)));
            pieDatas.add(new PieData("本月未完成任务数", 100 - mouthMissionCompleteCount * 100 / mouthMissionCount, getResources().getColor(R.color.colorMain7)));

        } else {
            pieDatas.clear();
            pieDatas.add(new PieData("本月已完成任务数", 100, getResources().getColor(R.color.colorMain5)));
            pieDatas.add(new PieData("本月未完成任务数", 0, getResources().getColor(R.color.colorMain7)));

        }

        count1_textView.setText(mouthMissionCompleteCount + "");
        count2_textView.setText((mouthMissionCount - mouthMissionCompleteCount) + "");

        dountChartMissionView.setChartData(pieDatas);
        dountChartMissionView.invalidate();
    }

    private void getMissionData(Date date) {
        if (date != null) {
            missionList.clear();
            for (int i = 0; i < MainActivity.missionList.size(); i++) {
                if ((MainActivity.missionList.get(i).getStatus() == 3 ||
                        MainActivity.missionList.get(i).getStatus() == 4 ||
                        MainActivity.missionList.get(i).getStatus() == 9 ||
                        MainActivity.missionList.get(i).getStatus() == MainActivity.Mission_Completed ||
                        MainActivity.missionList.get(i).getStatus() == 5) &&
                        MainActivity.missionList.get(i).getEnd_time() > date.getTime() &&
                        MainActivity.missionList.get(i).getBegin_time() <= date.getTime()) {
                    missionList.add(MainActivity.missionList.get(i));
                }
            }

            missionListAdapter.notifyDataSetChanged();
        }
    }

    private void setTempView() {
        if (!TextUtils.isEmpty(MainActivity.tempStr)) {
            try {
                JSONArray jsonArray = new JSONObject(MainActivity.tempStr).getJSONArray("results");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String city = jsonObject.getJSONObject("location").getString("name");
                String tempText = jsonObject.getJSONObject("now").getString("text");
                String tempCode = jsonObject.getJSONObject("now").getString("code");
                String temp = jsonObject.getJSONObject("now").getString("temperature") + "°C";
                String tempTime = jsonObject.getString("last_update");

                temp_imageView.setImageResource(R.drawable.class.getField("w" + tempCode).getInt(new R.drawable()));
                temp_textView.setText(temp);
                temp2_textView.setText(city + "  " + tempText);
                tempUpt_textView.setText("发布时间：" + tempTime.substring(11, 16));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    //监听系统时间广播
    public class DataChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            mTv_time.setText(DataUtil.getSystemTime());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mMyCallBack = (MyCallBack) context;
        }
    }
}
