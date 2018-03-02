package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PunishmentProceduresResouceAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.PunishmentRecyAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.DividerItemDecoration;
import gd.water.oking.com.cn.wateradministration_gd.bean.MultipleItem;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;


public class TestFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PunishmentProceduresResouceAdapter mPunishmentProceduresResouceAdapter;
    private ArrayList<MultipleItem.Item> mResouceItems;
    private View mInflate;
    private ArrayList<ArrayList<MultipleItem.Item>> mItems;
    private ArrayList<MultipleItem> mMultipleItems;


    public TestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_test, container, false);
        }
        return mInflate;
    }

    @Override
    public void initView(View rootView) {
        final RecyclerView ry_detail = rootView.findViewById(R.id.ry_detail);
        ry_detail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ry_detail.setItemAnimator(new DefaultItemAnimator());
        ry_detail.addItemDecoration(new DividerItemDecoration(MyApp.getApplictaion(), 1));
//=================================================================================
        RecyclerView ry = rootView.findViewById(R.id.ry);

        ry.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ry.setItemAnimator(new DefaultItemAnimator());
        initData();

        final PunishmentRecyAdapter punishmentRecyAdapter = new PunishmentRecyAdapter(mMultipleItems);
        ry.setAdapter(punishmentRecyAdapter);
        punishmentRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);

                switch (itemViewType) {
                    case 1:
                        if (view.getId() == R.id.iv_accept) {          //受理
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(0);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }


                        } else if (view.getId() == R.id.iv_complaint) {         //转办
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(1);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else {
                            ry_detail.setVisibility(View.GONE);
                        }

                        break;
                    case 2:
                        if (view.getId() == R.id.iv_investigation) {     ///承办并调查取证
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(0);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else {
                            ry_detail.setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        if (view.getId() == R.id.iv_record) {           //立案
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(0);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }

                        } else if (view.getId() == R.id.iv_supplementary_evidence) {        //补充证据
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(1);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else {
                            ry_detail.setVisibility(View.GONE);
                        }
                        break;
                    case 4:
                        if (view.getId() == R.id.iv_case_review) {     //案件审查
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(0);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        } else if (view.getId() == R.id.iv_transfer) {          //移送
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(1);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else {
                            ry_detail.setVisibility(View.GONE);
                        }
                        break;
                    case 5:
                        if (view.getId() == R.id.iv_group_discussion) {         //集体讨论
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(0);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if(view.getId() == R.id.iv_punishment_inform){    //处罚告知或听证
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(1);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if (view.getId() == R.id.iv_hearing){     //听证
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(2);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if(view.getId()==R.id.iv_review){         //复核
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(3);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if (view.getId()==R.id.iv_penalty_decision){      //处罚决定
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(4);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if (view.getId()==R.id.iv_case_and_file){     //结案并归档
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(5);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else if (view.getId()==R.id.iv_transfer2){
                            ry_detail.setVisibility(View.VISIBLE);
                            MultipleItem multipleItem = mMultipleItems.get(position);
                            ArrayList<MultipleItem.Item> items = multipleItem.getArrayLists().get(6);
                            if (mPunishmentProceduresResouceAdapter == null) {

                                mPunishmentProceduresResouceAdapter = new PunishmentProceduresResouceAdapter(R.layout.maptask_item, items);
                                ry_detail.setAdapter(mPunishmentProceduresResouceAdapter);
                            } else {
                                mPunishmentProceduresResouceAdapter.setNewData(items);
                            }
                        }else {
                            ry_detail.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initData() {
        mMultipleItems = new ArrayList<>();
        final MultipleItem multipleItem1 = new MultipleItem(1);
        mItems = new ArrayList<>();
        mResouceItems = new ArrayList<>();
        MultipleItem.Item acceptItem = new MultipleItem.Item();
        acceptItem.setIsok(false);
        acceptItem.setTitle("1、受理资料");
        mResouceItems.add(acceptItem);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<>();
        MultipleItem.Item complaintItme1 = new MultipleItem.Item();
        complaintItme1.setIsok(true);
        complaintItme1.setTitle("1、案件移送函");
        MultipleItem.Item complaintItme2 = new MultipleItem.Item();
        complaintItme2.setIsok(false);
        complaintItme2.setTitle("2、案件移送函(回执)");
        MultipleItem.Item complaintItme3 = new MultipleItem.Item();
        complaintItme3.setIsok(true);
        complaintItme3.setTitle("3、案件移送告知书");
        mResouceItems.add(complaintItme1);
        mResouceItems.add(complaintItme2);
        mResouceItems.add(complaintItme3);
        mItems.add(mResouceItems);
        multipleItem1.setArrayLists(mItems);
        mMultipleItems.add(multipleItem1);


        //==========================================================================================
        MultipleItem multipleItem2 = new MultipleItem(2);
        mItems = new ArrayList<>();
        mResouceItems = new ArrayList<>();
        MultipleItem.Item investigationItem1 = new MultipleItem.Item();
        investigationItem1.setTitle("1、责令停止违法行为通知书");
        investigationItem1.setIsok(true);
        mResouceItems.add(investigationItem1);
        MultipleItem.Item investigationItem2 = new MultipleItem.Item();
        investigationItem2.setTitle("2、协助调查通知书");
        investigationItem2.setIsok(false);
        mResouceItems.add(investigationItem2);
        MultipleItem.Item investigationItem3 = new MultipleItem.Item();
        investigationItem3.setTitle("3、调查笔录");
        investigationItem3.setIsok(false);
        mResouceItems.add(investigationItem3);
        MultipleItem.Item investigationItem4 = new MultipleItem.Item();
        investigationItem4.setTitle("4、抽样取证清单");
        investigationItem4.setIsok(false);
        mResouceItems.add(investigationItem4);
        MultipleItem.Item investigationItem5 = new MultipleItem.Item();
        investigationItem5.setTitle("5、先行登记保存呈批表");
        investigationItem5.setIsok(false);
        mResouceItems.add(investigationItem5);
        MultipleItem.Item investigationItem6 = new MultipleItem.Item();
        investigationItem6.setTitle("6、先行登记保存清单");
        investigationItem6.setIsok(false);
        mResouceItems.add(investigationItem6);
        MultipleItem.Item investigationItem7 = new MultipleItem.Item();
        investigationItem7.setTitle("7、先行登记保存物品处理告知书");
        investigationItem7.setIsok(true);
        mResouceItems.add(investigationItem7);
        MultipleItem.Item investigationItem8 = new MultipleItem.Item();
        investigationItem8.setTitle("8、水行政现场勘验笔录");
        investigationItem8.setIsok(true);
        mResouceItems.add(investigationItem8);
        MultipleItem.Item investigationItem9 = new MultipleItem.Item();
        investigationItem9.setTitle("9、案件照片(摄像)记录");
        investigationItem9.setIsok(true);
        mResouceItems.add(investigationItem9);
        MultipleItem.Item investigationItem10 = new MultipleItem.Item();
        investigationItem10.setTitle("10、送达回证");
        investigationItem10.setIsok(true);
        mResouceItems.add(investigationItem10);
        mItems.add(mResouceItems);
        multipleItem2.setArrayLists(mItems);
        mMultipleItems.add(multipleItem2);

//============================================================================
        MultipleItem multipleItem3 = new MultipleItem(3);
        mItems = new ArrayList<>();
        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item srecordItem1 = new MultipleItem.Item();
        srecordItem1.setTitle("1、水行政案件立案呈批表");
        srecordItem1.setIsok(false);
        mResouceItems.add(srecordItem1);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item supplementaryEvidenceItem1 = new MultipleItem.Item();
        supplementaryEvidenceItem1.setIsok(true);
        supplementaryEvidenceItem1.setTitle("1、书证");
        mResouceItems.add(supplementaryEvidenceItem1);
        MultipleItem.Item supplementaryEvidenceItem2 = new MultipleItem.Item();
        supplementaryEvidenceItem2.setIsok(true);
        supplementaryEvidenceItem2.setTitle("2、现场笔录");
        mResouceItems.add(supplementaryEvidenceItem2);
        MultipleItem.Item supplementaryEvidenceItem3 = new MultipleItem.Item();
        supplementaryEvidenceItem3.setIsok(true);
        supplementaryEvidenceItem3.setTitle("3、视听资料");
        mResouceItems.add(supplementaryEvidenceItem3);
        MultipleItem.Item supplementaryEvidenceItem4 = new MultipleItem.Item();
        supplementaryEvidenceItem4.setIsok(true);
        supplementaryEvidenceItem4.setTitle("4、勘验笔录");
        mResouceItems.add(supplementaryEvidenceItem4);
        mItems.add(mResouceItems);
        multipleItem3.setArrayLists(mItems);
        mMultipleItems.add(multipleItem3);

//=================================================================================
        MultipleItem multipleItem4 = new MultipleItem(4);
        mItems = new ArrayList<>();
        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item caseReviewItem1 = new MultipleItem.Item();
        caseReviewItem1.setIsok(true);
        caseReviewItem1.setTitle("1、水行政案件调查报告");
        mResouceItems.add(caseReviewItem1);
        MultipleItem.Item caseReviewItem2 = new MultipleItem.Item();
        caseReviewItem2.setIsok(false);
        caseReviewItem2.setTitle("2、水行政案件合议笔录");
        mResouceItems.add(caseReviewItem2);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item transferItem1 = new MultipleItem.Item();
        transferItem1.setTitle("1、案件移送函");
        transferItem1.setIsok(false);
        mResouceItems.add(transferItem1);
        MultipleItem.Item transferItem2 = new MultipleItem.Item();
        transferItem2.setTitle("2、案件移送函(回执)");
        transferItem2.setIsok(false);
        mResouceItems.add(transferItem2);
        MultipleItem.Item transferItem3 = new MultipleItem.Item();
        transferItem3.setTitle("3、案件移送告知书");
        transferItem3.setIsok(false);
        mResouceItems.add(transferItem3);
        mItems.add(mResouceItems);
        multipleItem4.setArrayLists(mItems);
        mMultipleItems.add(multipleItem4);

//===========================================================================
        MultipleItem multipleItem5 = new MultipleItem(5);
        mItems = new ArrayList<ArrayList<MultipleItem.Item>>();
        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item grouDiscussionItem1 = new MultipleItem.Item();
        grouDiscussionItem1.setIsok(false);
        grouDiscussionItem1.setTitle("1、水行政案件集体讨论笔录");
        mResouceItems.add(grouDiscussionItem1);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item punishmentInform1 = new MultipleItem.Item();
        punishmentInform1.setIsok(false);
        punishmentInform1.setTitle("1、水行政处罚告知书(符合听证)");
        mResouceItems.add(punishmentInform1);
        MultipleItem.Item punishmentInform2 = new MultipleItem.Item();
        punishmentInform2.setIsok(false);
        punishmentInform2.setTitle("2、水行政处罚告知书(不符合听证)");
        mResouceItems.add(punishmentInform2);
        MultipleItem.Item punishmentInform3 = new MultipleItem.Item();
        punishmentInform3.setIsok(false);
        punishmentInform3.setTitle("3、送达回证");
        mResouceItems.add(punishmentInform2);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item hearing1 = new MultipleItem.Item();
        hearing1.setIsok(false);
        hearing1.setTitle("1、水行政处罚听证通知书");
        mResouceItems.add(hearing1);
        MultipleItem.Item hearing2 = new MultipleItem.Item();
        hearing2.setIsok(false);
        hearing2.setTitle("2、权利义务告知书");
        mResouceItems.add(hearing2);
        MultipleItem.Item hearing3 = new MultipleItem.Item();
        hearing3.setIsok(false);
        hearing3.setTitle("3、授权委托书");
        mResouceItems.add(hearing3);
        MultipleItem.Item hearing4 = new MultipleItem.Item();
        hearing4.setIsok(false);
        hearing4.setTitle("4、水行政处罚听证公告");
        mResouceItems.add(hearing4);
        MultipleItem.Item hearing5 = new MultipleItem.Item();
        hearing5.setIsok(false);
        hearing5.setTitle("5、听证笔录");
        mResouceItems.add(hearing5);
        MultipleItem.Item hearing6 = new MultipleItem.Item();
        hearing6.setIsok(false);
        hearing6.setTitle("6、听证报告书");
        mResouceItems.add(hearing6);
        MultipleItem.Item hearing7 = new MultipleItem.Item();
        hearing7.setIsok(false);
        hearing7.setTitle("7、送达回证");
        mResouceItems.add(hearing7);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item reviewItem1 = new MultipleItem.Item();
        reviewItem1.setIsok(false);
        reviewItem1.setTitle("1、当事人陈述、申辩笔录");
        mResouceItems.add(reviewItem1);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item penaltyDecisionItem1 = new MultipleItem.Item();
        penaltyDecisionItem1.setIsok(false);
        penaltyDecisionItem1.setTitle("1、责令限期改正违法行为决定书");
        mResouceItems.add(penaltyDecisionItem1);
        MultipleItem.Item penaltyDecisionItem2 = new MultipleItem.Item();
        penaltyDecisionItem2.setIsok(false);
        penaltyDecisionItem2.setTitle("2、处罚(处理)决定呈批表");
        mResouceItems.add(penaltyDecisionItem2);
        MultipleItem.Item penaltyDecisionItem3 = new MultipleItem.Item();
        penaltyDecisionItem3.setIsok(false);
        penaltyDecisionItem3.setTitle("3、水行政处罚决定书");
        mResouceItems.add(penaltyDecisionItem3);
        MultipleItem.Item penaltyDecisionItem4 = new MultipleItem.Item();
        penaltyDecisionItem4.setIsok(false);
        penaltyDecisionItem4.setTitle("4、水行政处理决定书");
        mResouceItems.add(penaltyDecisionItem4);
        MultipleItem.Item penaltyDecisionItem5 = new MultipleItem.Item();
        penaltyDecisionItem5.setIsok(false);
        penaltyDecisionItem5.setTitle("5、送达回证");
        mResouceItems.add(penaltyDecisionItem5);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item caseAndFileItem1 = new MultipleItem.Item();
        caseAndFileItem1.setIsok(false);
        caseAndFileItem1.setTitle("1、水行政案件结案报告");
        mResouceItems.add(caseAndFileItem1);
        MultipleItem.Item caseAndFileItem2 = new MultipleItem.Item();
        caseAndFileItem2.setIsok(false);
        caseAndFileItem2.setTitle("2、证据清单");
        mResouceItems.add(caseAndFileItem2);
        MultipleItem.Item caseAndFileItem3 = new MultipleItem.Item();
        caseAndFileItem3.setIsok(false);
        caseAndFileItem3.setTitle("3、结案归档打印");
        mResouceItems.add(caseAndFileItem3);
        mItems.add(mResouceItems);

        mResouceItems = new ArrayList<MultipleItem.Item>();
        MultipleItem.Item transfer2Item1 = new MultipleItem.Item();
        transfer2Item1.setTitle("1、案件移送函");
        transfer2Item1.setIsok(false);
        mResouceItems.add(transfer2Item1);
        MultipleItem.Item transfer2Item2 = new MultipleItem.Item();
        transfer2Item2.setTitle("2、案件移送函(回执)");
        transfer2Item2.setIsok(false);
        mResouceItems.add(transfer2Item2);
        MultipleItem.Item transfer2Item3 = new MultipleItem.Item();
        transfer2Item3.setTitle("3、案件移送告知书");
        transfer2Item3.setIsok(false);
        mResouceItems.add(transfer2Item3);
        mItems.add(mResouceItems);
        multipleItem5.setArrayLists(mItems);
        mMultipleItems.add(multipleItem5);
    }


}
