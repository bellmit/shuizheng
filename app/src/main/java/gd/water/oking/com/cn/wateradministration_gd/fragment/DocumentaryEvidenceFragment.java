package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PicSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * 书证录入
 */
public class DocumentaryEvidenceFragment extends BaseSearchFragment {

    private static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;

    private Evidence myEvidence;
    private Case mycase;
    private TextView evidence_name_tv, evidence_source_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private Spinner evidence_source_spinner;
    private Button save_button, close_button;
    private static final String ARG_CASE = "aCase";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_EVIDENCE = "evidence";
    private PicSimpleAdapter picAdapter;
    private ArrayList<Uri> picList = new ArrayList<>();
//    private Uri picuri;

    private File documentaryStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_documentary");
    private RxDialogSureCancel mRxDialogSureCancel;
    private int mType;

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (intent.getAction()) {
//                case MainActivity.UPDATE_CASEFILE_UI_LIST:
//                    if (picAdapter != null) {
//                        picAdapter.notifyDataSetChanged();
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    public DocumentaryEvidenceFragment() {
        // Required empty public constructor
    }

    public static DocumentaryEvidenceFragment newInstance(Case aCase, Evidence evidence, int type) {
        DocumentaryEvidenceFragment fragment = new DocumentaryEvidenceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CASE, aCase);
        args.putParcelable(ARG_EVIDENCE, evidence);
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mycase = (Case) getArguments().getParcelable(ARG_CASE);
            mType = getArguments().getInt(ARG_PARAM3);
            myEvidence = getArguments().getParcelable(ARG_EVIDENCE);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        MyApp.getApplictaion().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_CASEFILE_UI_LIST));
        return inflater.inflate(R.layout.fragment_documentary_evidence, container, false);
    }

    @Override
    public void onDestroyView() {
//        MyApp.getApplictaion().unregisterReceiver(mReceiver);
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

        if (myEvidence != null) {
            evidence_name_tv.setText(myEvidence.getZJMC());
//            evidence_source_spinner.setText(myEvidence.getZJLYMC());
            evidence_content_tv.setText(myEvidence.getZJNR());
            evidence_remark_tv.setText(myEvidence.getBZ());
            evidence_getLocation_tv.setText(myEvidence.getCJDD());
            evidence_man_textView.setText(myEvidence.getJZR());
            evidence_dept_tv.setText(myEvidence.getDW());
            evidence_pagerCount_tv.setText(myEvidence.getYS());

        } else {
            myEvidence = Evidence.CREATOR.createFromParcel(Parcel.obtain());
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());
            myEvidence.setZJLX("SZ");

        }

        if (mType == 0) {
            save_button.setVisibility(View.GONE);

        } else {
            save_button.setVisibility(View.VISIBLE);
        }
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (localSaveEvidence()) {
                    final RxDialogSure rxDialogSure = new RxDialogSure(getActivity());
                    rxDialogSure.setTitle("提示");
                    rxDialogSure.setContent("保存成功");
                    rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSure.cancel();
                        }
                    });
                    rxDialogSure.show();

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


        picList = myEvidence.getPicList();

        setPicGridView(rootView);
    }

    private File mDataPicFile;

    private void setPicGridView(View rootView) {
        GridView picGridView = (GridView) rootView.findViewById(R.id.pic_gridView);

        picAdapter = new PicSimpleAdapter(picList, this, mType != 0, "书证");
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
                if (mRxDialogSureCancel == null) {
                    mRxDialogSureCancel = new RxDialogSureCancel(getContext());
                    mRxDialogSureCancel.setTitle("提示");
                    mRxDialogSureCancel.setContent("是否删除原图片？");
                }

                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = data.get(position);
                        String path = null;

                        path = FileUtil.PraseUritoPath(getContext(), uri);

                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();

                        }
                        data.remove(position);
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
                    picList.add(picUri);
                    myEvidence.setPicList(picList);
                    picAdapter.notifyDataSetChanged();
                    break;
                case PHOTO_FROM_CAMERA:

                    if (mDataPicFile != null && mDataPicFile.exists()) {
                        picList.add(Uri.fromFile(mDataPicFile));
                        myEvidence.setPicList(picList);
                        picAdapter.notifyDataSetChanged();
                    }


                    //通知系统扫描文件
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(mDataPicFile));
                    DocumentaryEvidenceFragment.this.getContext().sendBroadcast(intent);
                    break;
                default:
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
            default:
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

        if (mType == 0 ||mType==2&& myEvidence != null) {
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


}
