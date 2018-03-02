package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MenuBean;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuAdapter extends BaseQuickAdapter<MenuBean, BaseViewHolder> {


    public MenuAdapter(int layoutResId, @Nullable List<MenuBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuBean item) {
        helper.setImageResource(R.id.iv,item.getIcon());
        helper.setText(R.id.tv, item.getTitle());
    }



}
