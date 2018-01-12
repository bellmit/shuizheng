package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * A simple {@link Fragment} subclass.
 */
public class DocumentaryEvidenceListFragment extends BaseFragment {

    private Case mycase;

    private ListView main_listView;
    private ArrayList<Evidence> evidences = new ArrayList<>();
    private BaseAdapter adapter;
    private Button add_evidence_button;

    private boolean uploadPic;
    private int uploadPicCount;
    private ProgressDialog progressDialog;

    private Runnable DialogDismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

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

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };
    private SimpleDateFormat mSimpleDateFormat;

    public DocumentaryEvidenceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(CaseEvidenceFragment.DELETE_EVIDENCE_ACTION));
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inflater.inflate(R.layout.fragment_documentary_evidence_list, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        main_listView = (ListView) rootView.findViewById(R.id.main_listView);

        loadEvidence();

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return evidences.size();
            }

            @Override
            public Object getItem(int position) {
                return evidences.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getContext(), R.layout.list_item_documentaryevidence, null);

                TextView zjmc_tv = (TextView) view.findViewById(R.id.zjmc_tv);
                TextView cjdd_tv = (TextView) view.findViewById(R.id.cjdd_tv);
                TextView zjnr_tv = (TextView) view.findViewById(R.id.zjnr_tv);

                Button edit_button = (Button) view.findViewById(R.id.edit_button);
                Button delete_button = (Button) view.findViewById(R.id.delete_button);
                Button upload_button = (Button) view.findViewById(R.id.upload_button);

                if (evidences.get(position).isUpload()) {
                    upload_button.setVisibility(View.GONE);
                    delete_button.setVisibility(View.GONE);
                    edit_button.setText("查看");
                }

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = DocumentaryEvidenceListFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                        ft.addToBackStack(null);

                        DocumentaryEvidenceFragment f = new DocumentaryEvidenceFragment();
                        f.setMycase(mycase);
                        f.setAdd(true);
                        ft.replace(R.id.sub_fragment_root, f).commit();
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("是否删除证据？").
                                setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        deleteEvidence(evidences.get(position));
                                        mycase.getEvidenceList().remove(evidences.get(position));
                                        localSaveCase();

                                        loadEvidence();
                                        adapter.notifyDataSetChanged();
                                    }
                                }).setNegativeButton("否", null).create();
                        dialog.show();
                    }

                });

                upload_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(getContext());
                        }
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("上传数据中...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        SaveEvidence(evidences.get(position));
                    }
                });

                zjmc_tv.setText(evidences.get(position).getZJMC());
                cjdd_tv.setText(evidences.get(position).getCJDD());
                zjnr_tv.setText(evidences.get(position).getZJNR());

                return view;
            }
        };

        main_listView.setAdapter(adapter);

        add_evidence_button = (Button) rootView.findViewById(R.id.add_evidence_button);
        add_evidence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = DocumentaryEvidenceListFragment.this.getParentFragment().getChildFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                DocumentaryEvidenceFragment f = new DocumentaryEvidenceFragment();
                f.setMycase(mycase);
                f.setAdd(true);
                ft.replace(R.id.sub_fragment_root, f).commit();
            }
        });
    }

    public void setMycase(Case mycase) {
        this.mycase = mycase;
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

    private void SaveEvidence(final Evidence evidence) {

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
            getActivity().runOnUiThread(DialogDismissRunnable);
        }

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("EvidenceSave", "onSuccess>>>>>>" + result);

                if ("success".equals(result)) {
                    uploadEvidenceFile(evidence);
                } else {
                    getActivity().runOnUiThread(DialogDismissRunnable);
                    Toast.makeText(getContext(), "上传证据失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("EvidenceSave", "onError>>>>>>" + ex.toString());

                getActivity().runOnUiThread(DialogDismissRunnable);
                Toast.makeText(getContext(), "网络连接有误！", Toast.LENGTH_SHORT).show();
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

        uploadPic = true;
        uploadPicCount = 0;

        for (int i = 0; i < evidence.getPicList().size(); i++) {
            uploadPic = false;
            Uri uri = evidence.getPicList().get(i);
            uploadFile(uri, "jpg", evidence);
        }

        checkChangeState(evidence);
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
        params.addBodyParameter(
                "files",
                new File(FileUtil.PraseUritoPath(getContext(), uri)),
                null); // 如果文件没有扩展名, 最好设置contentType参数.

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("UploadEvidenceFile", "onSuccess>>>>>>" + result);

                if ("success".equals(result)) {
                    switch (type) {
                        case "jpg":
                            uploadPicCount++;
                            if (uploadPicCount == evidence.getPicList().size()) {
                                uploadPic = true;
                                checkChangeState(evidence);
                            }
                            break;
                    }
                } else {
                    getActivity().runOnUiThread(DialogDismissRunnable);
                    Toast.makeText(getContext(), "上传证据附件失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("UploadEvidenceFile", "onError>>>>>>" + ex.toString());
                getActivity().runOnUiThread(DialogDismissRunnable);
                Toast.makeText(getContext(), "网络连接有误！", Toast.LENGTH_SHORT).show();
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
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadEvidence() {
        evidences.clear();
        if (mycase != null) {
            for (int i = 0; i < mycase.getEvidenceList().size(); i++) {
                if ("SZ".equals(mycase.getEvidenceList().get(i).getZJLX())) {
                    evidences.add(mycase.getEvidenceList().get(i));
                }
            }
        }
    }

    private void localSaveEvidence(Evidence myEvidence) {

        String jsonStr = DataUtil.toJson(myEvidence);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("evidence", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(myEvidence.getZJID(), jsonStr);
        editor.commit();
    }

    private void checkChangeState(Evidence evidence) {
        if (uploadPic) {

            evidence.setUpload(true);
            localSaveEvidence(evidence);

            adapter.notifyDataSetChanged();
            localSaveCase();

            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(getContext());
            normalDialog.setTitle("提示");
            normalDialog.setMessage("上传成功");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            normalDialog.show();

            getActivity().runOnUiThread(DialogDismissRunnable);
//            CaseAudioVideoEvidenceListFragment.this.getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
