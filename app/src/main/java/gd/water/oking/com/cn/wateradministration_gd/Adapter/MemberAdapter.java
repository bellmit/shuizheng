package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;

/**
 * Created by zhao on 2017-3-30.
 */

public class MemberAdapter extends BaseAdapter {

    private ArrayList<Member> memberArrayList;
    private Activity context;
    private List<Member> checkNames = new ArrayList<Member>();
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox

    public MemberAdapter(Activity context, ArrayList<Member> memberArrayList) {
        this.context = context;
        this.memberArrayList = memberArrayList;
    }

    @Override
    public int getCount() {
        return memberArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.can_select_item, null);
            holder.name_tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Member member = memberArrayList.get(position);
        if (!(member == null)) {
            holder.name_tv.setText(member.getUsername());
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        map.put(position,true);


                    } else {
                        map.remove(position);


                    }

                }
            });


            if(map!=null&&map.containsKey(position)){
                holder.cb.setChecked(true);
            }else {
                holder.cb.setChecked(false);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView name_tv;
        CheckBox cb;
    }

    public List<Member> getCheckName() {

        checkNames.clear();
        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {

            int key = iter.next();

                checkNames.add(memberArrayList.get(key));


        }
        return checkNames;
    }

}
