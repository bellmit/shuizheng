package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.util.PathUtil;
import com.vondear.rxtools.RxNetUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.CaseAudioVideoEvidenceListRecyAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.http.EvidenceSaveParams;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseAudioVideoEvidenceListFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam2;
    private Case mycase;

    private RecyclerView ryMain;
    private ArrayList<Evidence> evidences = new ArrayList<>();
    private Button add_evidence_button;

    private boolean uploadPic, uploadSound, uploadVideo;
    private int uploadPicCount, uploadSoundCount, uploadVideoCount;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CaseEvidenceFragment.DELETE_EVIDENCE_ACTION:

                    for (int i = 0; i < mycase.getEvidenceList().size(); i++) {
                        if (mycase.getEvidenceList().get(i).getZJID().equals(intent.getStringExtra("ZJID"))) {
                            mycase.getEvidenceList().remove(i);
                            break;
                        }
                    }

                    localSaveCase();

                    loadEvidence();
                    if (mCaseAudioVideoEvidenceListRecyAdapter != null) {
                        mCaseAudioVideoEvidenceListRecyAdapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    break;
            }

        }
    };
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ;
    private CaseAudioVideoEvidenceListRecyAdapter mCaseAudioVideoEvidenceListRecyAdapter;
    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogLoading mRxDialogLoading;

    public CaseAudioVideoEvidenceListFragment() {
        // Required empty public constructor
    }

    public static CaseAudioVideoEvidenceListFragment newInstance(Case aCase, String param2) {
        CaseAudioVideoEvidenceListFragment fragment = new CaseAudioVideoEvidenceListFragment();
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
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(CaseEvidenceFragment.DELETE_EVIDENCE_ACTION));
        return inflater.inflate(R.layout.fragment_case_audio_video_evidence_list, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        ryMain = rootView.findViewById(R.id.ry_main);
        ryMain.setLayoutManager(new LinearLayoutManager(MyApp.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        ryMain.setItemAnimator(new DefaultItemAnimator());
        loadEvidence();
        mCaseAudioVideoEvidenceListRecyAdapter = new CaseAudioVideoEvidenceListRecyAdapter(R.layout.list_item_audiovideoevidence, evidences);
        ryMain.setAdapter(mCaseAudioVideoEvidenceListRecyAdapter);
        mCaseAudioVideoEvidenceListRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.upload_button:            //上传
                        if (RxNetUtils.isConnected(MyApp.getApplictaion())) {

                            saveEvidence(evidences.get(position));
                        } else {
                            RxToast.warning(MyApp.getApplictaion(), "网络无连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.delete_button:            //删除
                        if (mRxDialogSureCancel == null) {

                            mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                            mRxDialogSureCancel.setTitle("提示");
                            mRxDialogSureCancel.setContent("是否删除证据");
                        }
                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteEvidence(evidences.get(position));
                                mycase.getEvidenceList().remove(evidences.get(position));
                                localSaveCase();

                                loadEvidence();
                                adapter.notifyDataSetChanged();
                                mRxDialogSureCancel.cancel();
                            }
                        });
                        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSureCancel.cancel();
                            }
                        });
                        mRxDialogSureCancel.show();

                        break;
                    case R.id.edit_button:            //查看编辑
                        if (((Button) view).getText().equals("查看")) {
                            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
                            ft.addToBackStack(null);

                            CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(mycase, evidences.get(position), 0);
                            ft.replace(R.id.sub_fragment_root, caseAudioVideoEvidenceFragment).commit();
                        } else {
                            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
                            ft.addToBackStack(null);

                            CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(mycase, evidences.get(position), 1);
                            ft.replace(R.id.sub_fragment_root, caseAudioVideoEvidenceFragment).commit();
                        }

                        break;
                    default:
                        break;
                }
            }
        });

        add_evidence_button = (Button) rootView.findViewById(R.id.add_evidence_button);
        add_evidence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(mycase, null, 2);

                ft.replace(R.id.sub_fragment_root, caseAudioVideoEvidenceFragment).commit();

            }
        });
    }


    private void deleteEvidence(Evidence evidence) {
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("evidence", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(evidence.getZJID());
        editor.commit();
    }

    private void localSaveCase() {

        String jsonStr = DataUtil.toJson(mycase);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("case", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mycase.getAJID(), jsonStr);
        editor.commit();
    }

    private void saveEvidence(final Evidence evidence) {

        if (mRxDialogLoading == null) {

            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRxDialogLoading.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("上传数据中...");
        mRxDialogLoading.show();
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
            mRxDialogLoading.cancel();
        }

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                if ("success".equals(result)) {
                    uploadEvidenceFile(evidence);
                } else {
                    RxToast.error(MyApp.getApplictaion(), "上传证据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                RxToast.error(MyApp.getApplictaion(), "网络连接有误！1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void uploadEvidenceFile(final Evidence evidence) {

        uploadPic = true;
        uploadSound = true;
        uploadVideo = true;
        uploadPicCount = 0;
        uploadVideoCount = 0;
        uploadSoundCount = 0;

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < evidence.getPicList().size(); i++) {

                    uploadPic = false;
                    Uri uri = evidence.getPicList().get(i);
                    uploadFile(uri, "jpg", evidence);
                }

                for (int i = 0; i < evidence.getVideoList().size(); i++) {

                    uploadVideo = false;
                    Uri uri = evidence.getVideoList().get(i);
                    uploadFile(uri, "mp4", evidence);
                }

                for (int i = 0; i < evidence.getSoundList().size(); i++) {

                    uploadSound = false;
                    Uri uri = evidence.getSoundList().get(i);
                    uploadFile(uri, "YY", evidence);
                }
                e.onNext(1);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                if (value == 1) {
                    checkChangeState(evidence);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    private void uploadFile(Uri uri, final String type, final Evidence evidence) {
        //上传图片
        RequestParams params = new RequestParams(DefaultContants.SERVER_HOST + "/app/uploadfile");
        // 加到url里的参数, http://xxxx/s?wd=xUtils
        params.addQueryStringParameter("zjid", evidence.getZJID());
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("ajid", evidence.getAJID());
        params.addQueryStringParameter("userid", DefaultContants.CURRENTUSER.getUserId());
        // 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.

        // 使用multipart表单上传文件
        params.setMultipart(true);
        if (type.equals("YY")){
            params.addBodyParameter(
                    "files",
                    new File(PathUtil.getInstance().getVoicePath().getPath() + "/" + uri.getLastPathSegment()),
                    null); // 如果文件没有扩展名, 最好设置contentType参数.
        }else {
            params.addBodyParameter(
                    "files",
                    new File(uri.getPath()),
                    null); // 如果文件没有扩展名, 最好设置contentType参数.
        }


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if ("success".equals(result)) {
                    switch (type) {
                        case "jpg":
                            uploadPicCount++;
                            if (uploadPicCount == evidence.getPicList().size()) {
                                uploadPic = true;
                                checkChangeState(evidence);
                            }
                            break;
                        case "mp4":
                            uploadVideoCount++;
                            if (uploadVideoCount == evidence.getVideoList().size()) {
                                uploadVideo = true;
                                checkChangeState(evidence);
                            }
                            break;
                        case "YY":
                            uploadSoundCount++;
                            if (uploadSoundCount == evidence.getSoundList().size()) {
                                uploadSound = true;
                                checkChangeState(evidence);
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    mRxDialogLoading.cancel();
                    RxToast.error(MyApp.getApplictaion(), "上传证据附件失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mRxDialogLoading.cancel();
                RxToast.error(MyApp.getApplictaion(), "网络连接有误！2", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void notifyDataSetChanged() {
        if (mCaseAudioVideoEvidenceListRecyAdapter != null) {
            mCaseAudioVideoEvidenceListRecyAdapter.notifyDataSetChanged();
        }
    }

    private void loadEvidence() {
        evidences.clear();
        if (mycase != null) {
            for (int i = 0; i < mycase.getEvidenceList().size(); i++) {
                if ("YYSP".equals(mycase.getEvidenceList().get(i).getOtype()) || "SP".equals(mycase.getEvidenceList().get(i).getOtype())
                        || "YY".equals(mycase.getEvidenceList().get(i).getOtype())) {
                    evidences.add(mycase.getEvidenceList().get(i));
                }
            }
        }
    }

    private void checkChangeState(Evidence evidence) {
        if (uploadSound && uploadVideo && uploadPic) {
            mRxDialogLoading.cancel();
            evidence.setUpload(true);
            localSaveEvidence(evidence);
            mCaseAudioVideoEvidenceListRecyAdapter.notifyDataSetChanged();
            localSaveCase();

            final RxDialogSure rxDialogSure = new RxDialogSure(getActivity());
            rxDialogSure.setTitle("提示");
            rxDialogSure.setContent("上传成功！");
            rxDialogSure.show();
            rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rxDialogSure.cancel();
                }
            });

//            CaseAudioVideoEvidenceListFragment.this.getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void localSaveEvidence(Evidence myEvidence) {
        String jsonStr = DataUtil.toJson(myEvidence);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("evidence", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(myEvidence.getZJID(), jsonStr);
        editor.commit();
    }
}
