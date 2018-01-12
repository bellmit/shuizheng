package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Upcoming;

/**
 * Created by zhao on 2017-5-5.
 */

public class UpcomingAdapter extends BaseAdapter {

    private ArrayList<Upcoming> upcomings;
    private Context context;

    public UpcomingAdapter(Context context, ArrayList<Upcoming> upcomings) {
        this.context = context;
        this.upcomings = upcomings;
    }

    @Override
    public int getCount() {
        return upcomings.size();
    }

    @Override
    public Object getItem(int position) {
        return upcomings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_upcoming, null);
            holder.jjcd_tv = (TextView) convertView.findViewById(R.id.jjcd_tv);
            holder.rwlx_tv = (TextView) convertView.findViewById(R.id.rwlx_tv);
            holder.rwnr_tv = (TextView) convertView.findViewById(R.id.rwnr_tv);
            holder.fqr_tv = (TextView) convertView.findViewById(R.id.fqr_tv);
            holder.fqsj_tv = (TextView) convertView.findViewById(R.id.fqsj_tv);

            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (upcomings.get(position) != null) {
            Upcoming upcoming = upcomings.get(position);

            switch (upcoming.getJjcd()){
                case "一般":
                    holder.jjcd_tv.setBackgroundColor(context.getResources().getColor(R.color.colorMain3));
                    break;
                case "紧急":
                    holder.jjcd_tv.setBackgroundColor(context.getResources().getColor(R.color.colorMain5));
                    break;
                case "特急":
                    holder.jjcd_tv.setBackgroundColor(context.getResources().getColor(R.color.colorMain7));
                    break;
                default:
                    holder.jjcd_tv.setBackgroundColor(context.getResources().getColor(R.color.colorMain3));
            }
            holder.rwlx_tv.setText(upcoming.getRwlx());
            holder.rwnr_tv.setText(upcoming.getRwnr());
            holder.fqr_tv.setText(upcoming.getFqr());
            holder.fqsj_tv.setText(upcoming.getFqsj());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView jjcd_tv;
        TextView rwlx_tv;
        TextView rwnr_tv;
        TextView fqr_tv;
        TextView fqsj_tv;
    }
}
