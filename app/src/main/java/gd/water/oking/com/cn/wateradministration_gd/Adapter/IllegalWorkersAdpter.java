package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/12/15.
 */

public class IllegalWorkersAdpter extends RefreshListviewBaseAdapter{
    private int mLayout;
    public IllegalWorkersAdpter(List<?> list, int layout) {
        super(list);
        this.mLayout = layout;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        if (contentView==null){
            contentView = View.inflate(MyApp.getApplictaion(),mLayout,null);
        }
        return contentView;
    }
}
