package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/11/3.
 */

public class PupViewAdapter extends BaseAdapter{
    private String[] datas;

    @Override
    public int getCount() {
        return datas.length;
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
        TextView textView = new TextView(MyApp.getApplictaion());
        textView.setGravity(Gravity.LEFT);
        textView.setText(datas[position]);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(10);
        textView.setPadding(10,10,10,10);
        return textView;
    }

    public void setDatas(String[]datas){
        this.datas = datas;
        notifyDataSetChanged();
    }
}
