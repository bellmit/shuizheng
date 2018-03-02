package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.MultipleItem;

/**
 * Created by Administrator on 2018/2/6.
 */

public class PunishmentRecyAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {


    public PunishmentRecyAdapter(@Nullable List<MultipleItem> data) {
        super(data);
        addItemType(1, R.layout.administrative_punishment_item1);
        addItemType(2, R.layout.administrative_punishment_item2);
        addItemType(3, R.layout.administrative_punishment_item3);
        addItemType(4, R.layout.administrative_punishment_item4);
        addItemType(5, R.layout.administrative_punishment_item5);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (item.getItemType()) {

            case 1:
                helper.addOnClickListener(R.id.iv_accept)
                        .addOnClickListener(R.id.iv_complaint);
                break;
            case 2:
                helper.addOnClickListener(R.id.iv_investigation);
                break;
            case 3:
                helper.addOnClickListener(R.id.iv_record)
                        .addOnClickListener(R.id.iv_supplementary_evidence);
                break;

            case 4:
                helper.addOnClickListener(R.id.iv_not_punishable)
                        .addOnClickListener(R.id.iv_case_review)
                        .addOnClickListener(R.id.iv_transfer);
                break;
            case 5:
                helper.addOnClickListener(R.id.iv_perfect_information)
                        .addOnClickListener(R.id.iv_group_discussion)
                        .addOnClickListener(R.id.iv_punishment_inform)
                        .addOnClickListener(R.id.iv_hearing)
                        .addOnClickListener(R.id.iv_review)
                        .addOnClickListener(R.id.iv_penalty_decision)
                        .addOnClickListener(R.id.iv_perform)
                        .addOnClickListener(R.id.iv_transfer2)
                        .addOnClickListener(R.id.iv_case_and_file);
                break;
            default:
        }

    }
}
