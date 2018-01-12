package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;

/**
 * Created by zhao on 2016/11/16.
 */

public class MissionMemberAdapter extends BaseAdapter {

    private ArrayList<Member> members;
    private Context context;

    public MissionMemberAdapter(Context context, ArrayList<Member> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder= new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.member_sign_item_layout,null);

            viewHolder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            viewHolder.post_tv = (TextView) convertView.findViewById(R.id.post_tv);
            viewHolder.sign_iv = (ImageView) convertView.findViewById(R.id.sign_iv);

            convertView.setTag(viewHolder);
            AutoUtils.auto(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name_tv.setText(members.get(position).getUsername());
        viewHolder.post_tv.setText(members.get(position).getPost());
        viewHolder.sign_iv.setImageURI(members.get(position).getSignPic());

        return convertView;
    }

    private static class ViewHolder {
        TextView name_tv;
        TextView post_tv;
        ImageView sign_iv;
    }
}
