package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;

/**
 * Created by zhao on 2016/9/12.
 */
public class CaseListAdapter extends BaseQuickAdapter<Case, BaseViewHolder> {

    private boolean isShowState;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public CaseListAdapter(int layoutResId, @Nullable List<Case> data,  boolean isShowState) {
        super(layoutResId, data);
        this.isShowState = isShowState;
    }


    @Override
    protected void convert(BaseViewHolder helper, Case item) {

        helper.setText(R.id.case_name_tv,item.getAJMC());
        helper.setText(R.id.case_Type_tv,item.getAJLX());
        if (item.getAJLY() == null || "".equals(item.getAJLY())) {
            helper.setText(R.id.case_source_tv,"无");
        } else {
            helper.setText(R.id.case_source_tv,item.getAJLY());
        }
        helper.setText(R.id.case_dateTime_tv,dateFormat.format(item.getSLRQ()));
        helper.setText(R.id.case_dept_tv,item.getZFBM());


        switch (item.getSLXX_ZT()) {
            case "SL":
                helper.setText(R.id.case_state_tv,"受理");
                break;
            case "CBBDCQZ":
                helper.setText(R.id.case_state_tv,"承办并调查取证");
                break;
            case "ZB":
                helper.setText(R.id.case_state_tv,"转办");
                break;
            case "LA":
                helper.setText(R.id.case_state_tv,"立案");
                break;
            case "AJSC":
                helper.setText(R.id.case_state_tv,"案件审查");
                break;
            case "BYCF":
                helper.setText(R.id.case_state_tv,"不予处罚");
                break;
            case "WSZL":
                helper.setText(R.id.case_state_tv,"完善资料");
                break;
            case "YS":
                helper.setText(R.id.case_state_tv,"移送");
                break;
            case "CFGZHTZ":
                helper.setText(R.id.case_state_tv,"处罚告知或听证");
                break;
            case "TZ":
                helper.setText(R.id.case_state_tv,"听证");
                break;
            case "FH":
                helper.setText(R.id.case_state_tv,"复核");
                break;
            case "CFJD":
                helper.setText(R.id.case_state_tv,"处罚决定");
                break;
            case "ZX":
                helper.setText(R.id.case_state_tv,"执行");
                break;
            case "JABGD":
                helper.setText(R.id.case_state_tv,"结案并归档");
                break;
            default:
                helper.setText(R.id.case_state_tv,"");
                break;
        }

        if (!isShowState) {
            helper.setVisible(R.id.state_btn,false);
        } else {
            helper.setVisible(R.id.state_btn,true)
                    .addOnClickListener(R.id.state_btn)
                    .setText(R.id.state_btn,"选择");
        }


    }



}
