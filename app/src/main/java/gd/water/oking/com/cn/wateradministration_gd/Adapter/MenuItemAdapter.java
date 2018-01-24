package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MenuItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MenuItemAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        AutoUtils.autoSize(helper.getConvertView());
        helper.getView(R.id.tv).setBackgroundColor(Color.TRANSPARENT);
        helper.setText(R.id.tv, item);
    }

}