package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by Administrator on 2017/12/18.
 */

public class PuttedForwardAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PuttedForwardAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv, item);
    }
}
