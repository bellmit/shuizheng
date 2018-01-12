package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseDealFragment extends BaseFragment {

    private BaseFragment dealFragment;
    private Case mycase;
    private TextView ajlx_tv, ajly_tv, ajmc_tv, slsj_tv, dsr_tv, afdd_tv, aqjy_tv, ajzt_tv;
    private Button change_btn;
    private int btn_position;
    private SimpleDateFormat mSimpleDateFormat;

    public CaseDealFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_case_deal, container, false);
    }

    @Override
    public void initView(View rootView) {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ajlx_tv = (TextView) rootView.findViewById(R.id.ajlx_tv);
        ajly_tv = (TextView) rootView.findViewById(R.id.ajly_tv);
        ajmc_tv = (TextView) rootView.findViewById(R.id.ajmc_tv);
        slsj_tv = (TextView) rootView.findViewById(R.id.slsj_tv);
        dsr_tv = (TextView) rootView.findViewById(R.id.dsr_tv);
        afdd_tv = (TextView) rootView.findViewById(R.id.afdd_tv);
        aqjy_tv = (TextView) rootView.findViewById(R.id.aqjy_tv);
        ajzt_tv = (TextView) rootView.findViewById(R.id.ajzt_tv);

        change_btn = (Button) rootView.findViewById(R.id.change_btn);

        if (mycase != null) {
            ajlx_tv.setText(mycase.getAJLX());
            ajly_tv.setText(mycase.getAJLY());
            ajmc_tv.setText(mycase.getAJMC());

            slsj_tv.setText(mSimpleDateFormat.format(new Date(mycase.getSLRQ())));
            dsr_tv.setText(mycase.getDSRQK());
            afdd_tv.setText(mycase.getAFDD());
            aqjy_tv.setText(mycase.getAQJY());
            ajzt_tv.setText(mycase.getZT());

            switch (mycase.getSLXX_ZT()) {
                case "SL":
                    ajzt_tv.setText("受理");
                    break;
                case "CBBDCQZ":
                    ajzt_tv.setText("承办并调查取证");
                    break;
                case "ZB":
                    ajzt_tv.setText("转办");
                    break;
                case "LA":
                    ajzt_tv.setText("立案");
                    break;
                case "AJSC":
                    ajzt_tv.setText("案件审查");
                    break;
                case "BYCF":
                    ajzt_tv.setText("不予处罚");
                    break;
                case "WSZL":
                    ajzt_tv.setText("完善资料");
                    break;
                case "YS":
                    ajzt_tv.setText("移送");
                    break;
                case "CFGZHTZ":
                    ajzt_tv.setText("处罚告知或听证");
                    break;
                case "TZ":
                    ajzt_tv.setText("听证");
                    break;
                case "FH":
                    ajzt_tv.setText("复核");
                    break;
                case "CFJD":
                    ajzt_tv.setText("处罚决定");
                    break;
                case "ZX":
                    ajzt_tv.setText("执行");
                    break;
                case "JABGD":
                    ajzt_tv.setText("结案并归档");
                    break;
                default:
                    ajzt_tv.setText("");
                    break;
            }
        }

        if (dealFragment != null) {
            if (CaseDealFragment.this.getChildFragmentManager().findFragmentById(R.id.sub_fragment_root) != dealFragment) {

                CaseDealFragment.this.getChildFragmentManager().beginTransaction().replace(R.id.sub_fragment_root, dealFragment).commit();

            }
        }

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectCaseId = "";

                SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("selectCaseId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectCaseId", MainActivity.selectCaseId);
                editor.commit();

                Intent intent = new Intent(MainActivity.CALL_CASE_MANAGER);
//                intent.putExtra("btn_position", btn_position);
                MyApp.getApplictaion().sendBroadcast(intent);
            }
        });
    }

    public void setDealFragment(BaseFragment dealFragment) {
        this.dealFragment = dealFragment;
    }

    public void setMycase(Case mycase) {
        this.mycase = mycase;
    }

    public void setBtn_position(int btn_position) {
        this.btn_position = btn_position;
    }
}