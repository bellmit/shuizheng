package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.IllegalWorkersAdpter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * 违法人员查询
 */
public class IllegalWorkersFragment extends BaseFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PullToRefreshListView mLv_rf;
    private List mNetData=new ArrayList();


    public IllegalWorkersFragment() {
        // Required empty public constructor
    }

    public static IllegalWorkersFragment newInstance(String param1, String param2) {
        IllegalWorkersFragment fragment = new IllegalWorkersFragment();
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
        return inflater.inflate(R.layout.fragment_illegal_workers, container, false);
    }

    @Override
    public void initView(View rootView) {
        mLv_rf = rootView.findViewById(R.id.lv_rf);
        getNetData();
        IllegalWorkersAdpter illegalWorkersAdpter = new IllegalWorkersAdpter(mNetData,R.layout.item_view);
//        RefreshListDataMode refreshListDataMode = new RefreshListDataMode(mLv_rf,true, PullToRefreshBase.Mode.BOTH,illegalWorkersAdpter,this);
//        refreshListDataMode.setOnRefreshListener();
    }


    public void getMoreData() {

    }

    public void getNetData() {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        objects.add("4");
        objects.add("5");


    }
}
