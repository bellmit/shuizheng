package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;

/**
 * Created by zhao on 2016/9/12.
 */
public class CaseListAdapter extends BaseAdapter {

    private Context context;
    private List<Case> caseList;
    private boolean isShowState;
    private String button_text;
    private OnButtonClick onButtonClick;

    public CaseListAdapter(Context context, List<Case> caseList, boolean isShowState, String button_text) {
        this.context = context;
        this.caseList = caseList;
        this.isShowState = isShowState;
        this.button_text = button_text;
    }

    @Override
    public int getCount() {
        return caseList.size();
    }

    @Override
    public Object getItem(int i) {
        return caseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item_case, null);
            holder.case_name_tv = (TextView) view.findViewById(R.id.case_name_tv);
            holder.case_Type_tv = (TextView) view.findViewById(R.id.case_Type_tv);
            holder.case_source_tv = (TextView) view.findViewById(R.id.case_source_tv);
            holder.case_dateTime_tv = (TextView) view.findViewById(R.id.case_dateTime_tv);
            holder.case_dept_tv = (TextView) view.findViewById(R.id.case_dept_tv);
            holder.case_state_tv = (TextView) view.findViewById(R.id.case_state_tv);
            holder.case_location_tv = (TextView) view.findViewById(R.id.case_location_tv);
            holder.state_button = (Button) view.findViewById(R.id.state_btn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (i < 0 || i >= caseList.size()) {
            return null;
        }

        Case aCase = caseList.get(i);
        if (aCase != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            holder.case_name_tv.setText(aCase.getAJMC());
            holder.case_dateTime_tv.setText(dateFormat.format(aCase.getSLRQ()));
            if (aCase.getAJLY() == null || "".equals(aCase.getAJLY())) {
                holder.case_source_tv.setText("无");
            } else {
                holder.case_source_tv.setText(aCase.getAJLY());
            }
            holder.case_Type_tv.setText(aCase.getAJLX());
            holder.case_dept_tv.setText(aCase.getZFBM());
            holder.case_location_tv.setText(aCase.getAFDD());
            holder.state_button.setText(button_text);
            if (onButtonClick != null) {
                holder.state_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick.onButtonClick(i);
                    }
                });
            }

            switch (aCase.getSLXX_ZT()) {
                case "SL":
                    holder.case_state_tv.setText("受理");
                    break;
                case "CBBDCQZ":
                    holder.case_state_tv.setText("承办并调查取证");
                    break;
                case "ZB":
                    holder.case_state_tv.setText("转办");
                    break;
                case "LA":
                    holder.case_state_tv.setText("立案");
                    break;
                case "AJSC":
                    holder.case_state_tv.setText("案件审查");
                    break;
                case "BYCF":
                    holder.case_state_tv.setText("不予处罚");
                    break;
                case "WSZL":
                    holder.case_state_tv.setText("完善资料");
                    break;
                case "YS":
                    holder.case_state_tv.setText("移送");
                    break;
                case "CFGZHTZ":
                    holder.case_state_tv.setText("处罚告知或听证");
                    break;
                case "TZ":
                    holder.case_state_tv.setText("听证");
                    break;
                case "FH":
                    holder.case_state_tv.setText("复核");
                    break;
                case "CFJD":
                    holder.case_state_tv.setText("处罚决定");
                    break;
                case "ZX":
                    holder.case_state_tv.setText("执行");
                    break;
                case "JABGD":
                    holder.case_state_tv.setText("结案并归档");
                    break;
                default:
                    holder.case_state_tv.setText("");
                    break;
            }

            if (!isShowState) {
                holder.state_button.setVisibility(View.GONE);
            } else {
                holder.state_button.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    public void setDataList(List<Case> caseList) {
        this.caseList = caseList;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface OnButtonClick {
        void onButtonClick(int position);
    }

    private static class ViewHolder {
        TextView case_Type_tv;
        TextView case_name_tv;
        TextView case_source_tv;
        TextView case_dept_tv;
        TextView case_dateTime_tv;
        TextView case_state_tv;
        TextView case_location_tv;
        Button state_button;
    }
}
