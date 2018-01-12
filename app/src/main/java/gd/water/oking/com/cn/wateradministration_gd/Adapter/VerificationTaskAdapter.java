package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;

/**
 * Created by Administrator on 2018/1/8.
 */

public class VerificationTaskAdapter extends BaseAdapter{
    private int mLayout;
    private int mPos=-1;
    private Activity mActivity;
    private Mission mMission;
    public VerificationTaskAdapter(Activity mActivity, Mission mission, int layout) {
        this.mActivity = mActivity;
        this.mLayout = layout;
        this.mMission = mission;
    }

    @Override
    public int getCount() {
        return 1;
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        if (contentView==null){
            contentView = View.inflate(mActivity,mLayout,null);
            AutoUtils.auto(contentView);
        }
        TextView tv = contentView.findViewById(R.id.tv);
        tv.setText(mMission.getTask_name());
        if (mPos ==position){
            contentView.setBackgroundColor(Color.DKGRAY);
        }else {
            contentView.setBackgroundColor(Color.TRANSPARENT);
        }
        return contentView;
    }
}
