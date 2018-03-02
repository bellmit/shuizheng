package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MultipleItem;

/**
 * Created by Administrator on 2018/2/9.
 */

public class PunishmentProceduresResouceAdapter extends BaseQuickAdapter<MultipleItem.Item, BaseViewHolder> {

    public PunishmentProceduresResouceAdapter(int layoutResId, @Nullable List<MultipleItem.Item> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem.Item item) {
        helper.setText(R.id.tv,item.getTitle());

    }
}
