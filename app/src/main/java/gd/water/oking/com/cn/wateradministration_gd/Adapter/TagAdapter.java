package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhl.library.OnInitSelectedPosition;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> mDataList;

    private List<T> mSelectDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);
        AutoUtils.auto(convertView);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tag);
        T t = mDataList.get(position);

        if (t instanceof String) {
            textView.setText((String) t);
        }

        if(isSelectedPosition(position)){
            textView.setTextColor(mContext.getResources().getColor(R.color.colorMain6));
        }else{
            textView.setTextColor(mContext.getResources().getColor(R.color.colorMain4));
        }

        convertView.setEnabled(parent.isEnabled());

        return convertView;
    }

    public void onlyAddAll(List<T> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }

    public void setmSelectDataList(List<T> mSelectDataList) {
        this.mSelectDataList = mSelectDataList;
        notifyDataSetChanged();
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if (mSelectDataList != null) {
            for (int i = 0; i < mSelectDataList.size(); i++) {
                if (mDataList.get(position).equals(mSelectDataList.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
