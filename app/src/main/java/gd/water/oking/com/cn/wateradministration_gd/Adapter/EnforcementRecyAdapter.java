package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.EnforcementProcedure;

/**
 * Created by Administrator on 2018/2/11.
 */

public class EnforcementRecyAdapter extends BaseQuickAdapter<EnforcementProcedure, BaseViewHolder> {


    private List<EnforcementProcedure> mData;

    public EnforcementRecyAdapter(int layoutResId, @Nullable List<EnforcementProcedure> data) {
        super(layoutResId, data);
        this.mData = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, EnforcementProcedure item) {
        if (helper.getLayoutPosition() == mData.size() - 1) {
            helper.setBackgroundColor(R.id.v_below_title,Color.TRANSPARENT);
        }

        if (helper.getLayoutPosition() == 1) {
            helper.setVisible(R.id.tv_tag,true);
            helper.setText(R.id.tv_tag,item.tag);
        }

        if (item.isok){
            helper.setBackgroundColor(R.id.v_below_title,Color.GREEN);
            helper.setImageResource(R.id.iv_title,R.mipmap.complted);
        }

        helper.setText(R.id.tv_title, item.title);

    }
}

