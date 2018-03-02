package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.AutoCompleteAdapter;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;

/**
 * Created by zhao on 2016/9/7.
 */
public class MissionListAdapter extends AutoCompleteAdapter<Mission> {

    private Context context;
    private List<Mission> missionList;
    private OnItemClickListener onItemClickListener;

    public MissionListAdapter(Context context, List<Mission> missionList) {
        this.context = context;
        this.missionList = missionList;
    }

    @Override
    public int getCount() {
        return missionList.size();
    }

    @Override
    public Object getItem(int i) {
        return missionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item_mission2, null);
            holder.tv_title = (TextView) view.findViewById(R.id.title_tv);
            holder.tv_detail = (TextView) view.findViewById(R.id.detail_tv);
            holder.tv_date = (TextView) view.findViewById(R.id.datetime_tv);
            holder.tv_state = (TextView) view.findViewById(R.id.state_tv);
            holder.btn_deal = (Button) view.findViewById(R.id.deal_btn);
            holder.btn_state = (Button) view.findViewById(R.id.state_btn);

            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

            Mission mission = missionList.get(position);

            holder.tv_title.setText(mission.getTask_name());
            holder.tv_date.setText("任务时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(mission.getBegin_time())) + "～" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(mission.getEnd_time())));
            holder.tv_detail.setText("核查区域：" + mission.getRwqyms());

            switch (mission.getJjcd()) {
                case "2":
                    holder.btn_state.setBackground(context.getResources().getDrawable(R.drawable.mission_state_bg3));
                    break;
                case "1":
                    holder.btn_state.setBackground(context.getResources().getDrawable(R.drawable.mission_state_bg2));
                    break;
                case "0":
                    holder.btn_state.setBackground(context.getResources().getDrawable(R.drawable.mission_state_bg1));
                    break;
                default:
                    holder.btn_state.setBackground(context.getResources().getDrawable(R.drawable.mission_state_bg3));
            }

            switch (mission.getStatus()) {
                case 1://已发布
                    holder.tv_state.setText("状        态：待审核");
                    break;
                case 2://已审核
                    holder.tv_state.setText("状        态：待安排人员");
                    break;
                case 3://已确认
                    holder.tv_state.setText("状        态：待执行");
                    break;
                case 4://已开始
                    holder.tv_state.setText("状        态：正在进行");
                    break;
                case 5://已完成
                    holder.tv_state.setText("状        态：已完成");
                    holder.btn_deal.setText("查 看");
                    break;
                case 9://退回修改
                    holder.tv_state.setText("状        态：日志退回修改");
                    break;
                case MainActivity.Mission_Completed://巡查结束
                    holder.tv_state.setText("状        态：巡查结束，待上传");
                    break;
                default:
                    break;
            }

            holder.btn_deal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemBtnClick(position);
                    }
                }
            });

        return view;
    }

    @Override
    public void setDataList(ArrayList<Mission> dataList) {
        this.missionList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemBtnClick(int position);
    }

    private static class ViewHolder {
        TextView tv_title;
        TextView tv_date;
        TextView tv_detail;
        TextView tv_state;
        Button btn_deal;
        Button btn_state;
    }

}


