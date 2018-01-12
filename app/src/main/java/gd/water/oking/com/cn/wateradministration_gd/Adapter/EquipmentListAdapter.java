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
import gd.water.oking.com.cn.wateradministration_gd.bean.Equipment;

/**
 * Created by zhao on 2016/11/24.
 */

public class EquipmentListAdapter extends BaseAdapter {

    private ArrayList<Equipment> equipmentArrayList;
    private List<Equipment> checkItems = new ArrayList<Equipment>();
    private Map<Integer, Boolean> map = new HashMap<>();// 存放已被选中的CheckBox
    private Activity mActivity;

    public EquipmentListAdapter( Activity mActivity,ArrayList<Equipment> equipmentArrayList) {
        this.mActivity = mActivity;
        this.equipmentArrayList = equipmentArrayList;
    }

    @Override
    public int getCount() {
        return equipmentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return equipmentArrayList.get(position);
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.equipment_item_layout, null);

            holder.type_textView = (TextView) convertView.findViewById(R.id.type_textView);
            holder.value_textView = (TextView) convertView.findViewById(R.id.value_textView);
            holder.tv_attribute = (TextView) convertView.findViewById(R.id.tv_attribute);
            holder.remarks_textView = (TextView) convertView.findViewById(R.id.remarks_textView);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            holder.tv_mc = (TextView) convertView.findViewById(R.id.tv_mc);
            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final Equipment equipment = equipmentArrayList.get(position);
        holder.value_textView.setText(equipment.getValue());
        holder.type_textView.setText(equipment.getMc1());

        String ly = equipment.getLy();
        switch (ly){
            case "0":
                holder.tv_attribute.setText("自有");
                break;
            case "1":
                holder.tv_attribute.setText("租借");
                break;

            default:
                break;
        }
        holder.remarks_textView.setText(equipment.getRemarks());

        holder.tv_mc.setText(equipment.getMc2());
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    map.put(position, true);


                } else {
                    map.remove(position);


                }

            }
        });


        if (map != null && map.containsKey(position)) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }

        return convertView;
    }

    private static class ViewHolder {
        CheckBox cb;
        TextView type_textView;
        TextView value_textView;
        TextView tv_attribute;
        TextView tv_mc;
        TextView remarks_textView;
    }

    public List<Equipment> getCheckItem() {

        checkItems.clear();
        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {

            int key = iter.next();

            checkItems.add(equipmentArrayList.get(key));


        }
        return checkItems;
    }

    public void setData(ArrayList<Equipment> aList){
        equipmentArrayList = aList;
        notifyDataSetChanged();
    }
}
