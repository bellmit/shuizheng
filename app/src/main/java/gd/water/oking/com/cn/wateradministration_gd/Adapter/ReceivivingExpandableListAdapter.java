package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.UpdateMissionStateParams;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

import static gd.water.oking.com.cn.wateradministration_gd.main.MainActivity.Mission_Completed;

/**
 * Created by Administrator on 2017/8/11.
 */

public class ReceivivingExpandableListAdapter extends AutoCompleteAdapter {
    private List<Mission> missionList;
    private View.OnClickListener ivGoToChildClickListener;
    private DateFormat mDateFormat;
    private OnBtnClickListener onItemClickListener;
    private boolean dis = true;
//    private final Handler mHandler;

    public ReceivivingExpandableListAdapter(List<Mission> missionList,
                                            View.OnClickListener ivGoToChildClickListener) {
        this.missionList = missionList;
        this.ivGoToChildClickListener = ivGoToChildClickListener;
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        mHandler = new Handler(){
//
//            @Override
//            public void handleMessage(Message msg) {
//                notifyDataSetChanged();
//                super.handleMessage(msg);
//            }
//        };
    }

    @Override
    public int getGroupCount() {    //组的数量
        return missionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {    //某组中子项数量
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {     //某组
        return missionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {  //某子项
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApp.getApplictaion()).inflate(R.layout.item_elv_group, null);
            groupHold = new GroupHold();
            groupHold.tv_taskname = (TextView) convertView.findViewById(R.id.list_item_missionTitle);
            groupHold.ivGoToChildLv = (ImageView) convertView.findViewById(R.id.iv_goToChildLV);
            groupHold.tv_entime  = (TextView) convertView.findViewById(R.id.list_item_missionDate);
            groupHold.tv_state  = (TextView) convertView.findViewById(R.id.list_item_missionState);


            convertView.setTag(groupHold);
            AutoUtils.auto(convertView);
        } else {
            groupHold = (GroupHold) convertView.getTag();

        }

        String groupName = missionList.get(groupPosition).getTask_name();
        String endTime = "结束时间:" + mDateFormat.format(missionList.get(groupPosition).getEnd_time());
        groupHold.tv_taskname.setText(groupName);
        groupHold.tv_entime.setText(endTime);

        switch (missionList.get(groupPosition).getStatus()) {
            case 0:

            case 1:

            case 2:
                groupHold.tv_state.setText("未安排人员");
                groupHold.tv_state.setTextColor(MyApp.getApplictaion().getResources().getColor(R.color.colorMain8));
                break;
            case 3:
                groupHold.tv_state.setText("已安排，待执行");
                groupHold.tv_state.setTextColor(MyApp.getApplictaion().getResources().getColor(R.color.colorMain7));
                break;
            case 4:
                groupHold.tv_state.setText("巡查中");
                groupHold.tv_state.setTextColor(MyApp.getApplictaion().getResources().getColor(R.color.colorMain5));
                break;
            case Mission_Completed:
                groupHold.tv_state.setText("巡查结束");
                groupHold.tv_state.setTextColor(MyApp.getApplictaion().getResources().getColor(R.color.colorMain3));
                break;
            case 5:
                groupHold.tv_state.setText("已上报");
                groupHold.tv_state.setTextColor(MyApp.getApplictaion().getResources().getColor(R.color.colorMain4));
                break;
            case 9:
                groupHold.tv_state.setText("退回修改");
                groupHold.tv_state.setTextColor(Color.GRAY);
                break;
        }




        //取消默认的groupIndicator后根据方法中传入的isExpand判断组是否展开并动态自定义指示器
        if (isExpanded) {   //如果组展开
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_down);
        } else {
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_right);
        }

        //setTag() 方法接收的类型是object ，所以可将position和converView先封装在Map中。Bundle中无法封装view,所以不用bundle
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("groupPosition", groupPosition);
        tagMap.put("isExpanded", isExpanded);
        groupHold.ivGoToChildLv.setTag(tagMap);

        //图标的点击事件
        groupHold.ivGoToChildLv.setOnClickListener(ivGoToChildClickListener);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        final ChildHold childHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApp.getApplictaion()).inflate(R.layout.item_elv_child, null);
            childHold = new ChildHold();
            childHold.tv_sued_people = (TextView) convertView.findViewById(R.id.publisher_tv);
            childHold.tv_approver_people = (TextView) convertView.findViewById(R.id.approved_person_tv);
            childHold.tv_statime = (TextView) convertView.findViewById(R.id.begin_time_tv);
            childHold.tv_type = (TextView) convertView.findViewById(R.id.mission_type_tv);
            childHold.tv_members = (TextView) convertView.findViewById(R.id.list_item_missionMember);   //成员
            childHold.tv_patrol_area = (TextView) convertView.findViewById(R.id.list_item_missionDetail);
            childHold.rl = (RelativeLayout) convertView.findViewById(R.id.rl_01);
            childHold.list_item_missionRecord = (Button) convertView.findViewById(R.id.list_item_missionRecord);

            convertView.setTag(childHold);
            AutoUtils.auto(convertView);
        } else {
            childHold = (ChildHold) convertView.getTag();
        }
        childHold.tv_sued_people.setText(missionList.get(groupPosition).getPublisher_name());
        childHold.tv_approver_people.setText(missionList.get(groupPosition).getApproved_person_name());
        childHold.tv_statime.setText(mDateFormat.format(new Date(missionList.get(groupPosition).getBegin_time())));
        childHold.tv_type.setText(missionList.get(groupPosition).getTypename());

        String memberStr = "";
        if (missionList.get(groupPosition).getMembers() != null) {
            for (int j = 0; j < missionList.get(groupPosition).getMembers().size(); j++) {
                memberStr += missionList.get(groupPosition).getMembers().get(j).getUsername() + ",";
            }
        }
        if (!"".equals(memberStr)) {
            memberStr = memberStr.substring(0, memberStr.length() - 1);
        }

        childHold.tv_members.setText(memberStr);

        if (missionList.get(groupPosition).getRwqyms() != null) {
            childHold.tv_patrol_area.setText(missionList.get(groupPosition).getRwqyms());
        }
        if (!dis){
            childHold.rl.setVisibility(View.GONE);
        }else {
            childHold.rl.setVisibility(View.VISIBLE);
            switch (missionList.get(groupPosition).getStatus()) {
                case 0:

                case 1:

                case 2:
                    childHold.list_item_missionRecord.setText("NONE");
                    childHold.list_item_missionRecord.setEnabled(false);
                    break;
                case 3:
                    childHold.list_item_missionRecord.setEnabled(true);
                    childHold.list_item_missionRecord.setText("开始任务");
                    break;
                case 4:
                    childHold.list_item_missionRecord.setEnabled(true);
                    childHold.list_item_missionRecord.setText("任务日志");
                    break;
                case Mission_Completed:
                    childHold.list_item_missionRecord.setEnabled(true);
                    childHold.list_item_missionRecord.setText("任务日志");
                    break;
                case 5:
                    childHold.list_item_missionRecord.setEnabled(true);
                    childHold.list_item_missionRecord.setText("任务日志");
                    break;
                case 9:
                    childHold.list_item_missionRecord.setEnabled(true);
                    childHold.list_item_missionRecord.setText("任务日志");
                    break;
            }
            childHold.list_item_missionRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("开始任务".equals(childHold.list_item_missionRecord.getText().toString())){


//                        for (Mission mission : missionList) {
//                            if (mission.getStatus() == 4) {
//                                Toast.makeText(MyApp.getApplictaion(), "已有任务正在进行中，不能同时执行两个任务！", Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                        }

                        if (missionList.get(groupPosition).getBegin_time() > System.currentTimeMillis()) {
                            Toast.makeText(MyApp.getApplictaion(), "未到达任务开始时间，不能开始任务！", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date(missionList.get(groupPosition).getEnd_time()));
//                    c.add(android.icu.util.Calendar.DATE, 1);
                        if (c.getTime().getTime() < System.currentTimeMillis()) {
                            Toast.makeText(MyApp.getApplictaion(), "超出任务的预计结束时间，不能开始任务！", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(DefaultContants.ISHTTPLOGIN) {
                            UpdateMissionStateParams params = new UpdateMissionStateParams();
                            params.id = missionList.get(groupPosition).getId();
                            params.status = 4;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            final Date startTime = new Date();
                            params.execute_start_time = sdf.format(startTime);
                            Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    try {
                                        JSONObject object = new JSONObject(result);
                                        int code = object.getInt("code");
                                        if (code == 0) {

                                            missionList.get(groupPosition).setExecute_start_time(startTime.getTime());
                                            missionList.get(groupPosition).setStatus(4);
                                            notifyDataSetChanged();

                                            //任务开始时间记录到本地
                                            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putLong(missionList.get(groupPosition).getId(), missionList.get(groupPosition).getExecute_start_time());
                                            editor.commit();

                                        } else {
                                            Toast.makeText(MyApp.getApplictaion(), object.getString("msg"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(Throwable ex, boolean isOnCallback) {

                                    missionList.get(groupPosition).setExecute_start_time(System.currentTimeMillis());
                                    missionList.get(groupPosition).setStatus(4);

                                    //任务开始时间记录到本地
                                    SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                                    if (sharedPreferences.getLong(missionList.get(groupPosition).getId(), 0) == 0) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong(missionList.get(groupPosition).getId(), missionList.get(groupPosition).getExecute_start_time());
                                        editor.commit();
                                    }

                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(CancelledException cex) {

                                }

                                @Override
                                public void onFinished() {

                                }
                            });
                        }else{
                            missionList.get(groupPosition).setExecute_start_time(System.currentTimeMillis());
                            missionList.get(groupPosition).setStatus(4);

                            //任务开始时间记录到本地
                            SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("missionStartTime", Context.MODE_PRIVATE);
                            if (sharedPreferences.getLong(missionList.get(groupPosition).getId(), 0) == 0) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong(missionList.get(groupPosition).getId(), missionList.get(groupPosition).getExecute_start_time());
                                editor.commit();
                            }
                            notifyDataSetChanged();
                        }

                    }else if("任务日志".equals(childHold.list_item_missionRecord.getText().toString())){
                        if (onItemClickListener!=null){

                            onItemClickListener.onItemRecordBtnClick(groupPosition);

                        }else {
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;    //默认返回false,改成true表示组中的子条目可以被点击选中
    }

    @Override
    public void setDataList(ArrayList dataList) {
        this.missionList = dataList;

        notifyDataSetChanged();

    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    class GroupHold {
        TextView tv_taskname;
        TextView tv_entime;
        TextView tv_state;
        ImageView ivGoToChildLv;
    }

    class ChildHold {
        TextView tv_sued_people;
        TextView tv_approver_people;
        TextView tv_statime;
        TextView tv_type;
        TextView tv_members;
        TextView tv_patrol_area;
        Button list_item_missionRecord;
        RelativeLayout rl;
    }

    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }
    public void setOnBtnClickListener(OnBtnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnBtnClickListener {
        void onItemRecordBtnClick(int position);
    }

    public void disButton(boolean dis){
        this.dis = dis;
    }

}
