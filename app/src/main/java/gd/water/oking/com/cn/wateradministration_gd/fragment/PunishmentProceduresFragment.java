package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PupViewAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * 一般行政处罚程序
 */
public class PunishmentProceduresFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button mBt_accept;
    private PupViewAdapter mPupViewAdapter;
    private PopupWindow mPopupWindow;
    private ListView mLv;
    private Button mBt_complaint;
    private Button mBt_investigation;
    private Button mBt_record;
    private Button mBt_supplementary_evidence;
    private Button mBt_case_review;
    private Button mBt_transfer;
    private Button mBt_group_discussion;
    private Button mBt_punishment_inform;
    private Button mBt_review;
    private Button mBt_hearing;
    private Button mBt_penalty_decision;
    private Button mBt_case_and_file;


    public PunishmentProceduresFragment() {
        // Required empty public constructor
    }

    public static PunishmentProceduresFragment newInstance(String param1, String param2) {
        PunishmentProceduresFragment fragment = new PunishmentProceduresFragment();
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
        return inflater.inflate(R.layout.fragment_punishment_procedures, container, false);
    }

    @Override
    public void initView(View rootView) {
        mBt_accept = rootView.findViewById(R.id.bt_accept);
        mBt_complaint = rootView.findViewById(R.id.bt_complaint);
        mBt_investigation = rootView.findViewById(R.id.bt_investigation);
        mBt_record = rootView.findViewById(R.id.bt_record);
        mBt_supplementary_evidence = rootView.findViewById(R.id.bt_supplementary_evidence);
        mBt_case_review = rootView.findViewById(R.id.bt_case_review);
        mBt_transfer = rootView.findViewById(R.id.bt_transfer);
        mBt_group_discussion = rootView.findViewById(R.id.bt_group_discussion);
        mBt_punishment_inform = rootView.findViewById(R.id.bt_punishment_inform);
        mBt_review = rootView.findViewById(R.id.bt_review);
        mBt_hearing = rootView.findViewById(R.id.bt_hearing);
        mBt_penalty_decision = rootView.findViewById(R.id.bt_penalty_decision);
        mBt_case_and_file = rootView.findViewById(R.id.bt_case_and_file);
        setListener();

    }

    private void setListener() {
        mBt_accept.setOnClickListener(this);
        mBt_complaint.setOnClickListener(this);
        mBt_investigation.setOnClickListener(this);
        mBt_record.setOnClickListener(this);
        mBt_supplementary_evidence.setOnClickListener(this);
        mBt_case_review.setOnClickListener(this);
        mBt_transfer.setOnClickListener(this);
        mBt_group_discussion.setOnClickListener(this);
        mBt_punishment_inform.setOnClickListener(this);
        mBt_review.setOnClickListener(this);
        mBt_hearing.setOnClickListener(this);
        mBt_penalty_decision.setOnClickListener(this);
        mBt_case_and_file.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_accept:      //受理
                String[] acceptArray = getResources().getStringArray(R.array.pup_accept);
                showPupView(mBt_accept, acceptArray);
                break;
            case R.id.bt_complaint:      //转办
                String[] complaintArray = getResources().getStringArray(R.array.pup_complaint);
                showPupView(mBt_complaint, complaintArray);
                break;
            case R.id.bt_investigation:      //承办并调查取证
                String[] investigationArray = getResources().getStringArray(R.array.pup_investigation);
                showPupView(mBt_investigation, investigationArray);
                break;
            case R.id.bt_record:      //立案
                String[] recordArray = getResources().getStringArray(R.array.pup_record);
                showPupView(mBt_record, recordArray);
                break;
            case R.id.bt_supplementary_evidence:      //补充证据
                String[] supplementaryEvidenceArray = getResources().getStringArray(R.array.pup_supplementary_evidence);
                showPupView(mBt_supplementary_evidence, supplementaryEvidenceArray);
                break;
            case R.id.bt_case_review:      //案件审查
                String[] caseReviewEvidenceArray = getResources().getStringArray(R.array.pup_case_review);
                showPupView(mBt_case_review, caseReviewEvidenceArray);
                break;
            case R.id.bt_transfer:      //移送
                String[] transferArray = getResources().getStringArray(R.array.pup_transfer);
                showPupView(mBt_transfer, transferArray);
                break;
            case R.id.bt_group_discussion:      //集体讨论
                String[] groupDiscussionArray = getResources().getStringArray(R.array.pup_group_discussion);
                showPupView(mBt_group_discussion, groupDiscussionArray);
                break;
            case R.id.bt_punishment_inform:      //处罚告知或听证
                String[] punishmentInformArray = getResources().getStringArray(R.array.pup_punishment_inform);
                showPupView(mBt_punishment_inform, punishmentInformArray);
                break;
            case R.id.bt_review:      //复核
                String[] reviewArray = getResources().getStringArray(R.array.pup_review);
                showPupView(mBt_review, reviewArray);
                break;
            case R.id.bt_hearing:      //听证
                String[] hearingArray = getResources().getStringArray(R.array.pup_hearing);
                showPupView(mBt_hearing, hearingArray);
                break;
            case R.id.bt_penalty_decision:      //处罚决定
                String[] penaltyDecisionArray = getResources().getStringArray(R.array.pup_penalty_decision);
                showPupView(mBt_penalty_decision, penaltyDecisionArray);
                break;
            case R.id.bt_case_and_file:      //结案并归档
                String[] caseAndFileArray = getResources().getStringArray(R.array.pup_case_and_file);
                showPupView(mBt_case_and_file, caseAndFileArray);
                break;
            default:
                break;
        }
    }

    private void showPupView(View parent, String[] datas) {
        if (mPopupWindow == null) {

            mPopupWindow = new PopupWindow(MyApp.getApplictaion());
            mPopupWindow.setWidth(250);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            View inflate = LayoutInflater.from(MyApp.getApplictaion()).inflate(R.layout.pupview_item, null);
            mLv = inflate.findViewById(R.id.lv);
            mPopupWindow.setContentView(inflate);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(true);
        }


        if (mPupViewAdapter == null) {

            mPupViewAdapter = new PupViewAdapter();
        }
        mPupViewAdapter.setDatas(datas);
        mLv.setAdapter(mPupViewAdapter);
        mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight=mPopupWindow.getContentView().getMeasuredHeight();

        mPopupWindow.showAsDropDown(parent, 135, -popHeight, Gravity.CENTER);
    }

}
