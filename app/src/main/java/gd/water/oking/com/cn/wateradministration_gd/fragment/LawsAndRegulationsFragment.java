package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.LawsAndRegulationAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawBean;
import gd.water.oking.com.cn.wateradministration_gd.db.LawDao;

/**
 * 法律法规库
 */
public class LawsAndRegulationsFragment extends BaseFragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ListView mLv;


    public LawsAndRegulationsFragment() {
        // Required empty public constructor
    }

    public static LawsAndRegulationsFragment newInstance(String param1, String param2) {
        LawsAndRegulationsFragment fragment = new LawsAndRegulationsFragment();
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
        return inflater.inflate(R.layout.fragment_laws_and_regulations, container, false);
    }

    @Override
    public void initView(View rootView) {
        mLv = rootView.findViewById(R.id.lv);
        initData();
    }

    private void initData() {
        List<LawBean> laws = LawDao.getLaw();
        LawsAndRegulationAdapter lawsAndRegulationAdapter = new LawsAndRegulationAdapter(getActivity(),this,laws);
        mLv.setAdapter(lawsAndRegulationAdapter);

    }


}
