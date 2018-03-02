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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
