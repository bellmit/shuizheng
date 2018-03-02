package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;

/**
 * Created by Administrator on 2018/2/24.
 */

public class DocumentaryEvidenceListRecyAdapter extends BaseQuickAdapter<Evidence, BaseViewHolder> {
    public DocumentaryEvidenceListRecyAdapter(int layoutResId, @Nullable List<Evidence> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Evidence item) {

        helper.setText(R.id.zjmc_tv, item.getZJMC());
        helper.setText(R.id.cjdd_tv, item.getCJDD());
        helper.setText(R.id.zjnr_tv, item.getZJNR());
        helper.addOnClickListener(R.id.upload_button);
        helper.addOnClickListener(R.id.delete_button);
        helper.addOnClickListener(R.id.edit_button);;

        if (item.isUpload()) {
            helper.setVisible(R.id.upload_button, false);
            helper.setVisible(R.id.delete_button, false);
            helper.setText(R.id.edit_button, "查看");
        }

    }
}
