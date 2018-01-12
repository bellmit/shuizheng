package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBean;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuAdapter extends BaseAdapter{
    private ArrayList<MenuBean> menus;
    private int mPosition=-1;

    public MenuAdapter(ArrayList<MenuBean> menus) {
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {

            view = View.inflate(MyApp.getApplictaion(), R.layout.menu_item, null);
            AutoUtils.auto(view);
        }
        TextView tv1 = (TextView) view.findViewById(R.id.tv);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);

        iv.setImageResource(menus.get(i).getIcon());
        tv1.setText(menus.get(i).getTitle());

        if (mPosition==i){
            view.setBackgroundColor(Color.DKGRAY);
        }else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        return view;
    }

    public void setSelectItem(int position){
        mPosition = position;
    }
}
