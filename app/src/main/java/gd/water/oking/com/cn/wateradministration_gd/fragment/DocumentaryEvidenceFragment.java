package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PicSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentaryEvidenceFragment extends BaseSearchFragment {

    private static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;

    private Evidence myEvidence;
    private Case mycase;
    private boolean isAdd = false;
    private TextView evidence_name_tv, evidence_source_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private Spinner evidence_source_spinner;
    private Button save_button, close_button;

    private PicSimpleAdapter picAdapter;
    private ArrayList<Uri> picList;
//    private Uri picuri;

    private File documentaryStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_documentary");

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_CASEFILE_UI_LIST:
                    if (picAdapter != null) {
                        picAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    public DocumentaryEvidenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_CASEFILE_UI_LIST));
        return inflater.inflate(R.layout.fragment_documentary_evidence, container, false);
    }

    @Override
    public void onDestroyView() {
        MyApp.getApplictaion().unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {
        evidence_name_tv = (TextView) rootView.findViewById(R.id.evidence_name_tv);
        evidence_source_spinner = (Spinner) rootView.findViewById(R.id.evidence_source_spinner);
        String[] plandataset = getResources().getStringArray(R.array.spinner_evidence_source);
        SpinnerArrayAdapter plandataAdapter = new SpinnerArrayAdapter(plandataset);
        evidence_source_spinner.setAdapter(plandataAdapter);

        evidence_content_tv = (TextView) rootView.findViewById(R.id.evidence_content_tv);
        evidence_remark_tv = (TextView) rootView.findViewById(R.id.evidence_remark_tv);
        evidence_getLocation_tv = (TextView) rootView.findViewById(R.id.evidence_getLocation_tv);
        evidence_man_textView = (TextView) rootView.findViewById(R.id.evidence_man_textView);
        evidence_dept_tv = (TextView) rootView.findViewById(R.id.evidence_dept_tv);
        evidence_pagerCount_tv = (TextView) rootView.findViewById(R.id.evidence_pagerCount_tv);
        save_button = (Button) rootView.findViewById(R.id.save_button);
        if (!isAdd) {
            if (myEvidence != null) {
                save_button.setVisibility(myEvidence.isUpload() ? View.GONE : View.VISIBLE);
            }
        }
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (localSaveEvidence()) {
                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(getContext());
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("保存成功");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    normalDialog.show();

                    isAdd = false;

                    DocumentaryEvidenceFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
                }
            }
        });
        close_button = (Button) rootView.findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentaryEvidenceFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        if (isAdd && myEvidence == null) {
            myEvidence = new Evidence();
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());
            myEvidence.setZJLX("SZ");
        }

        if (myEvidence != null) {

            evidence_name_tv.setText(myEvidence.getZJMC());
//            evidence_source_spinner.setText(myEvidence.getZJLYMC());
            evidence_content_tv.setText(myEvidence.getZJNR());
            evidence_remark_tv.setText(myEvidence.getBZ());
            evidence_getLocation_tv.setText(myEvidence.getCJDD());
            evidence_man_textView.setText(myEvidence.getJZR());
            evidence_dept_tv.setText(myEvidence.getDW());
            evidence_pagerCount_tv.setText(myEvidence.getYS());

            picList = myEvidence.getPicList();

            setPicGridView(rootView);
        }
    }
    private File mDataPicFile;
    private void setPicGridView(View rootView) {
        GridView picGridView = (GridView) rootView.findViewById(R.id.pic_gridView);

        picAdapter = new PicSimpleAdapter(picList, this, !myEvidence.isUpload(),null);
        picAdapter.setOnClickListener(new PicSimpleAdapter.OnClickListener() {
            @Override
            public void onAddPic() {

                if (!documentaryStorageDir.exists()) {
                    documentaryStorageDir.mkdirs();
                }

                mDataPicFile = new File(documentaryStorageDir.getPath(), android.text.format.DateFormat
                        .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                        + ".jpg");
                mDataPicFile.getParentFile().mkdirs();
                startActivityForResult(
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mDataPicFile)),
                        PHOTO_FROM_CAMERA);

            }

            @Override
            public void onLongItemClick(final PicSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("是否删除原图片？").
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = data.get(position);
                                String path = null;

                                path = FileUtil.PraseUritoPath(getContext(), uri);

                                File file = new File(path);
                                if (file.exists()) {
                                    file.delete();

                                }
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
                dialog.show();
            }
        });

        picGridView.setAdapter(picAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri picUri = data.getData();
                    if (picUri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        picUri = Uri.parse(MediaStore.Images.Media.insertImage(DocumentaryEvidenceFragment.this.getActivity().getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }
                    myEvidence.getPicList().add(picUri);
                    picAdapter.notifyDataSetChanged();
                    break;
                case PHOTO_FROM_CAMERA:

                    if (mDataPicFile != null && mDataPicFile.exists()){

                        myEvidence.getPicList().add(Uri.fromFile(mDataPicFile));
                        picAdapter.notifyDataSetChanged();
                    }


                    //通知系统扫描文件
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(mDataPicFile));
                    DocumentaryEvidenceFragment.this.getContext().sendBroadcast(intent);
                    break;
            }

            //localSaveEvidence();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean localSaveEvidence() {

        if ("".equals(evidence_name_tv.getText().toString())) {
            Toast.makeText(getContext(), "证据名称不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (evidence_source_spinner.getText().toString().equals("")) {
//            Toast.makeText(getContext(), "证据来源不能为空！", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if ("".equals(evidence_content_tv.getText().toString())) {
            Toast.makeText(getContext(), "证据内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(evidence_getLocation_tv.getText().toString())) {
            Toast.makeText(getContext(), "采集地点不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        myEvidence.setPicList(picList);

        myEvidence.setZJMC(evidence_name_tv.getText().toString());
        switch (evidence_source_spinner.getSelectedItem().toString()) {
            case "当事人提供":
                myEvidence.setZJLY("DSRTG");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
            case "当事人口述":
                myEvidence.setZJLY("DSRKS");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
            case "调查搜集":
                myEvidence.setZJLY("DCSJ");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
            case "执法人员制作":
                myEvidence.setZJLY("ZFRYZZ");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
            case "执法人员拍摄":
                myEvidence.setZJLY("ZFRYPS");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
            case "局审批科室":
                myEvidence.setZJLY("JSPKS");
                myEvidence.setZJLYMC(evidence_source_spinner.getSelectedItem().toString());
                break;
        }
        myEvidence.setZJNR(evidence_content_tv.getText().toString());
        myEvidence.setBZ(evidence_remark_tv.getText().toString());
        myEvidence.setCJDD(evidence_getLocation_tv.getText().toString());
        myEvidence.setJZR(evidence_man_textView.getText().toString());
        myEvidence.setDW(evidence_dept_tv.getText().toString());
        myEvidence.setYS(evidence_pagerCount_tv.getText().toString());
        myEvidence.setCJSJ(System.currentTimeMillis());
        myEvidence.setCJR(DefaultContants.CURRENTUSER.getUserName());

//        //修改后可上传
//        myEvidence.setUpload(false);

        if (isAdd) {
            mycase.getEvidenceList().add(myEvidence);
        }

        localSaveCase();

        return true;
    }

    private void localSaveCase() {

        String jsonStr = DataUtil.toJson(mycase);
        SharedPreferences sharedPreferences = MyApp.getApplictaion().getSharedPreferences("case", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mycase.getAJID(), jsonStr);
        editor.commit();
    }

    public void setMyEvidence(Evidence myEvidence) {
        this.myEvidence = myEvidence;
    }

    public void setMycase(Case mycase) {
        this.mycase = mycase;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
