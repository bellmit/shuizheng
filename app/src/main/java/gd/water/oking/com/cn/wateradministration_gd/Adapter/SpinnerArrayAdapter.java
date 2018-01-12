package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/10/20.
 */

public class SpinnerArrayAdapter extends ArrayAdapter<String> {
    private String [] mStringArray;
    public SpinnerArrayAdapter(String[] stringArray) {
        super(MyApp.getApplictaion(), R.layout.sp_item_auto, stringArray);
        mStringArray=stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(MyApp.getApplictaion());
            convertView = inflater.inflate(R.layout.spinner_item, parent,false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(mStringArray[position]);
//        tv.setTextSize(5f);
//        tv.setTextColor(Color.argb(255,161,168,174));

        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(MyApp.getApplictaion());
            convertView = inflater.inflate(R.layout.sp_item_auto, parent, false);
//            AutoUtils.autoSize(convertView);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(mStringArray[position]);
//        tv.setTextSize(AutoUtils.getPercentWidthSize(21));
//        tv.setTextColor(Color.argb(255,161,168,174));
        return convertView;
    }

}