package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.EnforcementRecyAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.EnforcementProcedure;

public class TestFragment2 extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRy;
    private ArrayList<EnforcementProcedure> mEnforcementProcedures;


    public TestFragment2() {
        // Required empty public constructor
    }

    public static TestFragment2 newInstance(String param1, String param2) {
        TestFragment2 fragment = new TestFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_fragment2, container, false);
    }

    @Override
    public void initView(View rootView) {
//        final RecyclerView ry_detail = rootView.findViewById(R.id.ry_detail);
//        ry_detail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        ry_detail.setItemAnimator(new DefaultItemAnimator());
//        ry_detail.addItemDecoration(new DividerItemDecoration(MyApp.getApplictaion(), 1));
//=================================================================================
        mRy = rootView.findViewById(R.id.ry);
        mRy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRy.setItemAnimator(new DefaultItemAnimator());
       initData();
        mRy.setAdapter(new EnforcementRecyAdapter(R.layout.administrative_enforcement_procedures_item,mEnforcementProcedures));
        setListener();
    }

    private void setListener() {

    }

    private void initData() {
        mEnforcementProcedures = new ArrayList<>();
        EnforcementProcedure enforcementProcedure1 = new EnforcementProcedure("责令限期拆除决定书",true,null);
        mEnforcementProcedures.add(enforcementProcedure1);
        EnforcementProcedure enforcementProcedure2 = new EnforcementProcedure("行政强制拆除公告",true,"行政诉讼期限届满之日起三个月内");
        mEnforcementProcedures.add(enforcementProcedure2);
        EnforcementProcedure enforcementProcedure3 = new EnforcementProcedure("履行拆除义务催告书",false,null);
        mEnforcementProcedures.add(enforcementProcedure3);
        EnforcementProcedure enforcementProcedure4 = new EnforcementProcedure("听取并记录当事人陈述、申辩意见",false,null);
        mEnforcementProcedures.add(enforcementProcedure4);
        EnforcementProcedure enforcementProcedure5 = new EnforcementProcedure("复核陈述、申辩意见",false,null);
        mEnforcementProcedures.add(enforcementProcedure5);
        EnforcementProcedure enforcementProcedure6 = new EnforcementProcedure("行政强制执行决定书",false,null);
        mEnforcementProcedures.add(enforcementProcedure6);
        EnforcementProcedure enforcementProcedure7 = new EnforcementProcedure("组织或实验拆除",false,null);
        mEnforcementProcedures.add(enforcementProcedure7);
        EnforcementProcedure enforcementProcedure8 = new EnforcementProcedure("拆除现场笔录",false,null);
        mEnforcementProcedures.add(enforcementProcedure8);
    }

}
