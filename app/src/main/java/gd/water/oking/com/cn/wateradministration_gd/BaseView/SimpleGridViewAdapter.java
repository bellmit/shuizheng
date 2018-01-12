package gd.water.oking.com.cn.wateradministration_gd.BaseView;

        import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/10/9.
 */

public abstract class SimpleGridViewAdapter extends BaseAdapter {

    private ArrayList<Uri> uriArrayList;

    public SimpleGridViewAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @Override
    public int getCount() {
        return uriArrayList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return uriArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
