package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.AllEmergencyTaskBean;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/10/10.
 */

public class EmergencyAllTaskAdapter extends BaseAdapter {
    private ArrayList<AllEmergencyTaskBean> allEmergencyTaskBeens;
    private final SimpleDateFormat mSf;

    public EmergencyAllTaskAdapter(ArrayList<AllEmergencyTaskBean> allEmergencyTaskBeens) {

        this.allEmergencyTaskBeens = allEmergencyTaskBeens;
        mSf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    }

    @Override
    public int getCount() {
        return allEmergencyTaskBeens.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        AllEmergencyTaskBean allEmergencyTaskBean = allEmergencyTaskBeens.get(position);
        if (view==null){
            viewHolder = new ViewHolder();
            view = View.inflate(MyApp.getApplictaion(), R.layout.emergencyalltask_item,null);
            viewHolder.tv_taskname = (TextView) view.findViewById(R.id.tv_taskname);
            viewHolder.tv_fbdw = (TextView) view.findViewById(R.id.tv_fbdw);
            viewHolder.tv_taskid = (TextView) view.findViewById(R.id.tv_taskid);
            viewHolder.tv_fbr = (TextView) view.findViewById(R.id.tv_fbr);
            viewHolder.tv_begintime = (TextView) view.findViewById(R.id.tv_begintime);
            viewHolder.tv_endtime = (TextView) view.findViewById(R.id.tv_endtime);
            viewHolder.tv_spr = (TextView) view.findViewById(R.id.tv_spr);
            viewHolder.tv_tasktype = (TextView) view.findViewById(R.id.tv_tasktype);
            viewHolder.tv_xsly = (TextView) view.findViewById(R.id.tv_xsly);
            viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.tv_taskname.setText("任务名称："+allEmergencyTaskBean.getTASK_NAME());
        viewHolder.tv_fbdw.setText("发布单位："+allEmergencyTaskBean.getFBDW());
        viewHolder.tv_taskid.setText("任务ID号："+allEmergencyTaskBean.getID());
        viewHolder.tv_fbr.setText("发起人："+allEmergencyTaskBean.getFBR());
        viewHolder.tv_begintime.setText("开始时间："+mSf.format(new Date(allEmergencyTaskBean.getBEGIN_TIME())));
        viewHolder.tv_endtime.setText("结束时间："+mSf.format(new Date(allEmergencyTaskBean.getEND_TIME())));
        viewHolder.tv_spr.setText("审批人："+allEmergencyTaskBean.getSPR());
        viewHolder.tv_tasktype.setText("任务类型："+allEmergencyTaskBean.getRWLX());
        viewHolder.tv_xsly.setText("线索来源："+allEmergencyTaskBean.getRWLY());
        viewHolder.tv_state.setText("任务状态："+allEmergencyTaskBean.getSTATUS());
        return view;
    }

    class ViewHolder{
        TextView tv_taskname;
        TextView tv_fbdw;
        TextView tv_taskid;
        TextView tv_fbr;
        TextView tv_begintime;
        TextView tv_endtime;
        TextView tv_spr;
        TextView tv_tasktype;
        TextView tv_xsly;
        TextView tv_state;
    }
}
