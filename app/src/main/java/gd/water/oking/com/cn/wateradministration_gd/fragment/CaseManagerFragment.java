package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.CaseListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseCaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.interfaces.MyCallBack;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class CaseManagerFragment extends BaseFragment {
    private MyCallBack mMyCallBack;
    private List<Case> caseList = MainActivity.caseList;
    private CaseListAdapter adapter;
    private BaseFragment listItemClickOpenFragment;
    private int btn_position;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_CASE_LIST:

                    if (adapter != null) {
                        adapter.setDataList(MainActivity.caseList);
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };


    public CaseManagerFragment() {

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

        mMyCallBack.refreshCase();

        final ListView caseListView = (ListView) rootView.findViewById(R.id.case_listView);

        final ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.pb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                caseListView.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        }, 500);
        adapter = new CaseListAdapter(this.getContext(), caseList, true, "选择");
        caseListView.setAdapter(adapter);
        adapter.setOnButtonClick(new CaseListAdapter.OnButtonClick() {

            @Override
            public void onButtonClick(int position) {
                MainActivity.selectCaseId = caseList.get(position).getAJID();

                SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("selectCaseId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selectCaseId", MainActivity.selectCaseId);
                editor.commit();

                if (listItemClickOpenFragment != null) {
                    if (listItemClickOpenFragment instanceof BaseCaseFragment) {
                        ((BaseCaseFragment) listItemClickOpenFragment).setMyCase(caseList.get(position));

                    } else if (listItemClickOpenFragment instanceof WebViewFragment) {
                        String url = ((WebViewFragment) listItemClickOpenFragment).getUrlStr();
                        url += "ajid=" + caseList.get(position).getAJID();
                        if (DefaultContants.CURRENTUSER != null) {
                            url += "&deptid=" + DefaultContants.CURRENTUSER.getDeptId();
                        }

                        ((WebViewFragment) listItemClickOpenFragment).setUrlStr(url);

                    }

                    getCaseLocalFile(caseList.get(position));

                    CaseDealFragment dealFragment = new CaseDealFragment();
                    dealFragment.setDealFragment(listItemClickOpenFragment);
                    dealFragment.setMycase(caseList.get(position));
                    dealFragment.setBtn_position(btn_position);


                    FragmentManager fm = CaseManagerFragment.this.getParentFragment().getChildFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_root, dealFragment).commit();
                }
            }
        });


        //已选择案件则跳转到相应案件处理Fragment
        if (!"".equals(MainActivity.selectCaseId)) {

            Case selectCase = null;

            //找出对应案件
            for (int i = 0; i < caseList.size(); i++) {
                if (caseList.get(i).getAJID().equals(MainActivity.selectCaseId)) {
                    selectCase = caseList.get(i);
                    break;
                }
            }

            if (selectCase != null && listItemClickOpenFragment != null) {
                if (listItemClickOpenFragment instanceof BaseCaseFragment) {
                    ((BaseCaseFragment) listItemClickOpenFragment).setMyCase(selectCase);


                } else if (listItemClickOpenFragment instanceof WebViewFragment) {
                    String url = ((WebViewFragment) listItemClickOpenFragment).getUrlStr();
                    url += "ajid=" + selectCase.getAJID();
                    url += "&deptid=" + DefaultContants.CURRENTUSER.getDeptId();

                    ((WebViewFragment) listItemClickOpenFragment).setUrlStr(url);

                }

                getCaseLocalFile(selectCase);

                CaseDealFragment dealFragment = new CaseDealFragment();
                dealFragment.setDealFragment(listItemClickOpenFragment);
                dealFragment.setMycase(selectCase);
                dealFragment.setBtn_position(btn_position);


                FragmentManager fm = CaseManagerFragment.this.getParentFragment().getChildFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_root, dealFragment).commit();
            }
        }
    }

    public void setListItemClickOpenFragment(BaseFragment listItemClickOpenFragment) {
        this.listItemClickOpenFragment = listItemClickOpenFragment;
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
            mMyCallBack = (MyCallBack) context;
        }
    }
    public void setBtn_position(int btn_position) {
        this.btn_position = btn_position;
    }
}
