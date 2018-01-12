package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.PatrolLogGson;

/**
 * Created by Administrator on 2017/12/19.
 */

public class PatrolLogManagementAdapter extends RefreshListviewBaseAdapter{
    private int mLayout;
    private int mPos=-1;
    private List<?> mList;
    private Activity mActivity;
    public PatrolLogManagementAdapter(Activity mActivity,List<?> list,int layout) {
        super(list);
        this.mActivity = mActivity;
        this.mLayout = layout;
        this.mList = list;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        if (contentView==null){
            contentView = View.inflate(mActivity,mLayout,null);
            AutoUtils.auto(contentView);
        }
        TextView tv = contentView.findViewById(R.id.tv);
        List<PatrolLogGson.RowsBean> mRows = (List<PatrolLogGson.RowsBean>) mList;
        tv.setText(mRows.get(position).getTASK_NAME());
        if (mPos ==position){
            contentView.setBackgroundColor(Color.DKGRAY);
        }else {
            contentView.setBackgroundColor(Color.TRANSPARENT);
        }
        return contentView;
    }

    public void setSelecte(int pos){
        mPos = pos;
        notifyDataSetChanged();
    }

}
