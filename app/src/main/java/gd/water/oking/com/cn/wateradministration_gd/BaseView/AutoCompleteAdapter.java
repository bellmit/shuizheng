package gd.water.oking.com.cn.wateradministration_gd.BaseView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/10/13.
 */

public abstract class AutoCompleteAdapter<T> extends BaseAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public abstract void setDataList(ArrayList<T> dataList);
}
