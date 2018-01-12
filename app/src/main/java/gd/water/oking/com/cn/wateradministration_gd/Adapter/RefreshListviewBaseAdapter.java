package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public abstract class RefreshListviewBaseAdapter extends BaseAdapter {
    private List<?> mList;

    public RefreshListviewBaseAdapter(List<?> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
