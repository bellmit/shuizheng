package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.CaseListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.DividerItemDecoration;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.OkingCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class CaseManagerFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OkingCallBack.MyCallBack mMyCallBack;

    private CaseListAdapter adapter;
    private int btn_position;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_CASE_LIST:

                    if (adapter != null) {
                        adapter.setNewData(MainActivity.caseList);
                    }

                    break;
                default:
                    break;
            }
        }
    };
    private List<Case> mCaseList;


    public CaseManagerFragment() {

    }

    public static CaseManagerFragment newInstance(String param1, String param2) {
        CaseManagerFragment fragment = new CaseManagerFragment();
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
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_CASE_LIST));
        return inflater.inflate(R.layout.fragment_case_manager, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        mCaseList = new ArrayList<>();
        for (Case aCase : MainActivity.caseList) {
            mCaseList.add(aCase);
        }
        mMyCallBack.refreshCase();

        final RecyclerView case_ry = rootView.findViewById(R.id.case_ry);
        case_ry.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        case_ry.addItemDecoration(new DividerItemDecoration(MyApp.getApplictaion(), 1)); case_ry.setItemAnimator(new DefaultItemAnimator());

        final ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.pb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                case_ry.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        }, 500);

        if (mCaseList !=null&& mCaseList.size()>0){
        }else {
            return;
        }
        adapter = new CaseListAdapter(R.layout.list_item_case, mCaseList,true);
        case_ry.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                MainActivity.selectCaseId = mCaseList.get(position).getAJID();

                SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("selectCaseId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectCaseId", MainActivity.selectCaseId);
                editor.commit();

//                if (listItemClickOpenFragment != null) {
//                    if (listItemClickOpenFragment instanceof BaseCaseFragment) {
//                        ((BaseCaseFragment) listItemClickOpenFragment).setMyCase(caseList.get(position));
//
//                    } else if (listItemClickOpenFragment instanceof WebViewFragment) {
//                        String url = ((WebViewFragment) listItemClickOpenFragment).getUrlStr();
//                        url += "ajid=" + caseList.get(position).getAJID();
//                        if (DefaultContants.CURRENTUSER != null) {
//                            url += "&deptid=" + DefaultContants.CURRENTUSER.getDeptId();
//                        }
//
//                        ((WebViewFragment) listItemClickOpenFragment).setUrlStr(url);
//
//                    }

                    getCaseLocalFile(mCaseList.get(position));

                    CaseDealFragment dealFragment = CaseDealFragment.newInstance(mCaseList.get(position),null);
                    dealFragment.setBtn_position(btn_position);


                    FragmentManager fm = CaseManagerFragment.this.getParentFragment().getChildFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_root, dealFragment).commit();
                }

//            }
        });
//        if (!"".equals(MainActivity.selectCaseId)) {
//
//            Case selectCase = null;
//
//            //找出对应案件
//            for (int i = 0; i < caseList.size(); i++) {
//                if (caseList.get(i).getAJID().equals(MainActivity.selectCaseId)) {
//                    selectCase = caseList.get(i);
//                    break;
//                }
//            }
//
////            if (selectCase != null && listItemClickOpenFragment != null) {
////                if (listItemClickOpenFragment instanceof BaseCaseFragment) {
////                    ((BaseCaseFragment) listItemClickOpenFragment).setMyCase(selectCase);
////
////
////                } else if (listItemClickOpenFragment instanceof WebViewFragment) {
////                    String url = ((WebViewFragment) listItemClickOpenFragment).getUrlStr();
////                    url += "ajid=" + selectCase.getAJID();
////                    url += "&deptid=" + DefaultContants.CURRENTUSER.getDeptId();
////
////                    ((WebViewFragment) listItemClickOpenFragment).setUrlStr(url);
////
////                }
//
//                getCaseLocalFile(selectCase);
//
//                CaseDealFragment dealFragment = new CaseDealFragment();
//                dealFragment.setMycase(selectCase);
//                dealFragment.setBtn_position(btn_position);
//
//
//                FragmentManager fm = CaseManagerFragment.this.getParentFragment().getChildFragmentManager();
//                fm.beginTransaction().replace(R.id.fragment_root, dealFragment).commit();
//            }
//        }
    }

    private void getCaseLocalFile(final Case myCase) {

        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("case", MODE_PRIVATE);
        String jsonStr = sharedPreferences.getString(myCase.getAJID(), "");
        if (!"".equals(jsonStr)) {

            Case aCase = DataUtil.praseJson(jsonStr, new TypeToken<Case>() {
            });

            if (aCase != null) {

                //获取本地证据
                myCase.getEvidenceList().clear();
                for (int i = 0; i < aCase.getEvidenceList().size(); i++) {
                    myCase.getEvidenceList().add(aCase.getEvidenceList().get(i));
                }

                //获取本地调查笔录
                myCase.getSurveyRecordList().clear();
                for (int i = 0; i < aCase.getSurveyRecordList().size(); i++) {
                    myCase.getSurveyRecordList().add(aCase.getSurveyRecordList().get(i));
                }
            }
        } else {

        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mMyCallBack = (OkingCallBack.MyCallBack) context;
        }
    }


}
