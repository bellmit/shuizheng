package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.RegulationsExListViewAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawsRegulation;
import gd.water.oking.com.cn.wateradministration_gd.db.LawDao;

/**
 * 法规详情
 */
public class RegulationsDetailsFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "mmid";
    private static final String ARG_PARAM2 = "rulesContent";

    private String mmid;
    private String rulesContent;
    private ImageButton mBack_button;
    private ExpandableListView mEl;
    private TextView mTv_detail;
    private ArrayList<LawsRegulation> mLawChapter;
    private TextView mTv_itemdetail;
    private EditText mSearch_editText;
    private ImageView mIv_serch;


    public RegulationsDetailsFragment() {
        // Required empty public constructor
    }

    public static RegulationsDetailsFragment newInstance(String mmid, String rulesContent) {
        RegulationsDetailsFragment fragment = new RegulationsDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mmid);
        args.putString(ARG_PARAM2, rulesContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mmid = getArguments().getString(ARG_PARAM1);
            rulesContent = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regulations_details, container, false);
    }

    @Override
    public void initView(View rootView) {
        mEl = rootView.findViewById(R.id.el);
        mSearch_editText = rootView.findViewById(R.id.search_editText);
        mTv_detail = rootView.findViewById(R.id.tv_detail);
        mTv_itemdetail = rootView.findViewById(R.id.tv_itemdetail);
        mBack_button = rootView.findViewById(R.id.back_button);
        mIv_serch = rootView.findViewById(R.id.iv_serch);
        initData();
        setListener();
    }

    private void setListener() {
        mBack_button.setOnClickListener(this);

        mIv_serch.setOnClickListener(this);

        mEl.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                String articlesContent = mLawChapter.get(groupPosition).getArticlesContent();
                for (int i = 0; i < mLawChapter.size(); i++) {
                    if (i != groupPosition) {
                        mEl.collapseGroup(i); //收起某个指定的组
                    }
                }
                mTv_itemdetail.setText(articlesContent);

            }
        });

        mEl.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                String chapterItem = mLawChapter.get(groupPosition).getChapterDirectory().get(groupPosition).getSection().get(childPosition).getChapterItem();
                String articlesContent = LawDao.getArticlesContent(chapterItem);
                mTv_itemdetail.setText(articlesContent);
                return false;
            }
        });
    }

    private void initData() {
        mLawChapter = LawDao.getLawChapter(mmid);
        RegulationsExListViewAdapter regulationsExListViewAdapter = new RegulationsExListViewAdapter(getActivity(),mLawChapter);
        mEl.setAdapter(regulationsExListViewAdapter);
        mTv_detail.setText(rulesContent);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                FragmentManager fm = getFragmentManager();
                LawsAndRegulationsFragment lawFragment = LawsAndRegulationsFragment.newInstance(null, null);
                fm.beginTransaction().replace(R.id.fragment_root, lawFragment).commit();
                break;
            case R.id.iv_serch:
                String serch = mSearch_editText.getText().toString().trim();
                if (TextUtils.isEmpty(serch)){
                    return;
                }
                for (LawsRegulation regulation:mLawChapter){
                    for (int i=0;i<regulation.getChapterDirectory().size();i++){
                        String chapterDirectory = regulation.getChapterDirectory().get(i).getChapterDirectory();
                        if (chapterDirectory.contains(serch)){
                            mEl.expandGroup(i);
                            continue;
                        }
                    }
                }
                break;
            default:
        }
    }
}
