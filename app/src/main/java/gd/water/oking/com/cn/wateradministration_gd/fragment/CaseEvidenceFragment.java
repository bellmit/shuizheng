package gd.water.oking.com.cn.wateradministration_gd.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseCaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.EvidenceSaveParams;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zhao on 2017-3-30.
 */

public class CaseEvidenceFragment extends BaseCaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String DELETE_EVIDENCE_ACTION = "com.gdWater.deleteevidence";
    private String mParam2;
    private Case mycase;

    private ViewPager mViewPager;
    //private TabLayout mTabLayout;

    private Button add_evidence_button, upload_evidence_button, document_tabBtn, audioVideo_tabBtn;
    private int selectIndex, selectFragmentIndex = 0;
    private FragmentStatePagerAdapter fragmentPagerAdapter;

    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private SimpleDateFormat mSimpleDateFormat;

    public static CaseEvidenceFragment newInstance(Case aCase, String param2) {
        CaseEvidenceFragment fragment = new CaseEvidenceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, aCase);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mycase = (Case) getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inflater.inflate(R.layout.fragment_case_evidence, container, false);
    }

    @Override
    public void initView(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_view);
        //mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        fragmentPagerAdapter = new FragmentStatePagerAdapter(getParentFragment().getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        titleList.clear();
        titleList.add("书证");
        titleList.add("视听资料");

        fragments.clear();
        DocumentaryEvidenceListFragment documentaryListFragment = DocumentaryEvidenceListFragment.newInstance(mycase,null);
        fragments.add(documentaryListFragment);
        CaseAudioVideoEvidenceListFragment caseAudioVideoListFragment = CaseAudioVideoEvidenceListFragment.newInstance(mycase,null);
        fragments.add(caseAudioVideoListFragment);

        //mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectFragmentIndex = position;
                if (position == 0) {
                    document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                    document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                    audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                    audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                } else {
                    audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                    audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                    document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                    document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mViewPager.setCurrentItem(selectFragmentIndex);

        document_tabBtn = (Button) rootView.findViewById(R.id.document_tabBtn);
        audioVideo_tabBtn = (Button) rootView.findViewById(R.id.audioVideo_tabBtn);
        document_tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                mViewPager.setCurrentItem(0);
            }
        });
        audioVideo_tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                mViewPager.setCurrentItem(1);
            }
        });
        if (selectFragmentIndex == 0) {
            document_tabBtn.callOnClick();
        } else if (selectFragmentIndex == 1) {
            audioVideo_tabBtn.callOnClick();
        }
//        add_evidence_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                final String[] items = new String[]{"书证", "视听资料", "调查笔录", "勘验笔录"};
//                final String[] items = new String[]{"书证", "视听资料"};
//                selectIndex = 0;
//                new AlertDialog.Builder(getContext())
//                        .setTitle("请选择")
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                selectIndex = which;
//                            }
//                        })
//                        .setPositiveButton("确定",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        String eType = "";
//                                        switch (selectIndex) {
//                                            case 0:
//                                                eType = "SZ";
//                                                break;
//                                            case 1:
//                                                eType = "STZL";
//                                                break;
//                                            case 2:
//                                                eType = "DCBL";
//                                                break;
//                                            case 3:
//                                                eType = "KYBL";
//                                                break;
//                                            default:
//                                                break;
//                                        }
//
//                                        mViewPager.setCurrentItem(selectIndex);
//                                    }
//                                })
//                        .setNegativeButton("取消", null)
//                        .show();
//            }
//        });

        upload_evidence_button = (Button) rootView.findViewById(R.id.upload_evidence_button);
        upload_evidence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mycase.getEvidenceList().size(); i++) {
                    saveEvidence(mycase.getEvidenceList().get(i));
                }
            }
        });
    }

    private Evidence addEvidence(Case mycase, String evidenceType) {
        Evidence evidence = Evidence.CREATOR.createFromParcel(Parcel.obtain());
        evidence.setZJID(UUID.randomUUID().toString());
        evidence.setAJID(mycase.getAJID());
        evidence.setZJLX(evidenceType);

        mycase.getEvidenceList().add(evidence);

        localSaveCase();

        return evidence;
    }

    private void localSaveCase() {

        String jsonStr = DataUtil.toJson(mycase);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("case", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mycase.getAJID(), jsonStr);
        editor.commit();
    }

    private void saveEvidence(final Evidence evidence) {

        EvidenceSaveParams params = new EvidenceSaveParams();
        try {
            if (evidence.getZJID() != null) {
                params.zjid = URLEncoder.encode(evidence.getZJID(), "utf-8");
            }
            if (evidence.getAJID() != null) {
                params.ajid = URLEncoder.encode(evidence.getAJID(), "utf-8");
            }
            if (evidence.getZJLX() != null) {
                params.zjlx = URLEncoder.encode(evidence.getZJLX(), "utf-8");
            }
            if (evidence.getZJMC() != null) {
                params.zjmc = URLEncoder.encode(evidence.getZJMC(), "utf-8");
            }
            if (evidence.getZJLY() != null) {
                params.zjly = URLEncoder.encode(evidence.getZJLY(), "utf-8");
            }
            if (evidence.getZJNR() != null) {
                params.zjnr = URLEncoder.encode(evidence.getZJNR(), "utf-8");
            }
            if (evidence.getSL() != null) {
                params.sl = URLEncoder.encode(evidence.getSL(), "utf-8");
            }
            if (((Long) evidence.getCJSJ()) != null) {

                params.cjsj = URLEncoder.encode(mSimpleDateFormat.format(new Date(evidence.getCJSJ())), "utf-8");
            }
            if (evidence.getCJR() != null) {
                params.cjr = URLEncoder.encode(evidence.getCJR(), "utf-8");
            }
            if (evidence.getCJDD() != null) {
                params.cjdd = URLEncoder.encode(evidence.getCJDD(), "utf-8");
            }
            if (evidence.getJZR() != null) {
                params.jzr = URLEncoder.encode(evidence.getJZR(), "utf-8");
            }
            if (evidence.getDW() != null) {
                params.dw = URLEncoder.encode(evidence.getDW(), "utf-8");
            }
            if (evidence.getBZ() != null) {
                params.bz = URLEncoder.encode(evidence.getBZ(), "utf-8");
            }
            if (DefaultContants.CURRENTUSER != null) {
                params.scr = URLEncoder.encode(DefaultContants.CURRENTUSER.getUserName(), "utf-8");
            }

            params.scsj = URLEncoder.encode(mSimpleDateFormat.format(new Date()), "utf-8");

            if (evidence.getZT() != null) {
                params.zt = URLEncoder.encode(evidence.getZT(), "utf-8");
            }
            if (evidence.getWSID() != null) {
                params.wsid = URLEncoder.encode(evidence.getWSID(), "utf-8");
            }
            if (evidence.getLXMC() != null) {
                params.lxmc = URLEncoder.encode(evidence.getLXMC(), "utf-8");
            }
            if (evidence.getZJLYMC() != null) {
                params.zjlymc = URLEncoder.encode(evidence.getZJLYMC(), "utf-8");
            }
            if (evidence.getYS() != null) {
                params.ys = URLEncoder.encode(evidence.getYS(), "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("EvidenceSave", "onSuccess>>>>>>" + result);

                if ("success".equals(result)) {

                }
                uploadEvidenceFile(evidence);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("EvidenceSave", "onError>>>>>>" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void uploadEvidenceFile(Evidence evidence) {

        for (int i = 0; i < evidence.getPicList().size(); i++) {
            Uri uri = evidence.getPicList().get(i);
            uploadFile(uri, evidence.getZJID(), "jpg");
        }

        for (int i = 0; i < evidence.getVideoList().size(); i++) {
            Uri uri = evidence.getVideoList().get(i);
            uploadFile(uri, evidence.getZJID(), "mp4");
        }

        for (int i = 0; i < evidence.getSoundList().size(); i++) {
            Uri uri = evidence.getSoundList().get(i);
            uploadFile(uri, evidence.getZJID(), "YY");
        }
    }

    private void uploadFile(Uri uri, String zjid, final String type) {

        //上传图片
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/app/uploadfile");
        // 加到url里的参数, http://xxxx/s?wd=xUtils
        params.addQueryStringParameter("zjid", zjid);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("userid", DefaultContants.CURRENTUSER.getUserId());
        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.

        // 使用multipart表单上传文件
        params.setMultipart(true);
        params.addBodyParameter(
                "files",
                new File(FileUtil.PraseUritoPath(getContext(), uri)),
                null); // 如果文件没有扩展名, 最好设置contentType参数.

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("UploadEvidenceFile", "onSuccess>>>>>>" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("UploadEvidenceFile", "onError>>>>>>" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
