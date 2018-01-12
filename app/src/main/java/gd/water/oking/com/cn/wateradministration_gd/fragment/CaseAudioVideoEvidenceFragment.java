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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.PicSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SoundSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.VideoSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.AudioRecordButton;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
import gd.water.oking.com.cn.wateradministration_gd.main.MainActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.main.VideoRecordActivity;
import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.MediaManager;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseAudioVideoEvidenceFragment extends BaseFragment {

    private static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;
    private static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;

    private Evidence myEvidence;
    private Case mycase;
    private boolean isAdd = false;
    private TextView evidence_name_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private Spinner type_spinner, evidence_source_spinner;
    private Button save_button, close_button;
    private GridView video_gridView, pic_gridView, sound_gridView;

    private SoundSimpleAdapter soundAdapter;
    private PicSimpleAdapter picAdapter;
    private VideoSimpleAdapter videoAdapter;

    private ArrayList<Uri> soundList;
    private ArrayList<Uri> picList;
    private ArrayList<Uri> videoList;

//    private Uri photouri, videouri;
    private File picStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_pic");
    private File videoStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_video");
    private File soundStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_sound");

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MainActivity.UPDATE_CASEFILE_UI_LIST:
                    if (picAdapter != null) {
                        picAdapter.notifyDataSetChanged();
                    }

                    if (videoAdapter != null) {
                        videoAdapter.notifyDataSetChanged();
                    }

                    if (soundAdapter != null) {
                        soundAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };

    public CaseAudioVideoEvidenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_CASEFILE_UI_LIST));
        return inflater.inflate(R.layout.fragment_case_forensics, container, false);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(mReceiver);
        MediaManager.release();
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {


        evidence_name_tv = (TextView) rootView.findViewById(R.id.evidence_name_tv);
        evidence_source_spinner = (Spinner) rootView.findViewById(R.id.evidence_source_spinner);

        String[] plandataset = getResources().getStringArray(R.array.spinner_evidence_source);
        SpinnerArrayAdapter plandataAdapter = new SpinnerArrayAdapter(plandataset);
        evidence_source_spinner.setAdapter(plandataAdapter);

        type_spinner = (Spinner) rootView.findViewById(R.id.type_spinner);
        String[] typedataset = getResources().getStringArray(R.array.spinner_data_type);
        SpinnerArrayAdapter typedataAdapter = new SpinnerArrayAdapter(typedataset);
        type_spinner.setAdapter(typedataAdapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        pic_gridView.setVisibility(View.VISIBLE);
                        video_gridView.setVisibility(View.INVISIBLE);
                        ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        pic_gridView.setVisibility(View.INVISIBLE);
                        video_gridView.setVisibility(View.VISIBLE);
                        ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        pic_gridView.setVisibility(View.INVISIBLE);
                        video_gridView.setVisibility(View.INVISIBLE);
                        ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                    CaseAudioVideoEvidenceFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
                }
            }
        });
        close_button = (Button) rootView.findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseAudioVideoEvidenceFragment.this.getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        if (isAdd && myEvidence == null) {
            myEvidence = new Evidence();
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());
            myEvidence.setZJLX("STZL");
            myEvidence.setWSID("STZL" + myEvidence.getAJID());
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

            soundList = myEvidence.getSoundList();
            picList = myEvidence.getPicList();
            videoList = myEvidence.getVideoList();

            setPicGridView(rootView);
            setSoundGridView(rootView);
            setVideoGridView(rootView);

            if (picList.size() > 0) {
                type_spinner.setSelection(0);
                pic_gridView.setVisibility(View.VISIBLE);
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            } else if (videoList.size() > 0) {
                type_spinner.setSelection(1);
                pic_gridView.setVisibility(View.INVISIBLE);
                video_gridView.setVisibility(View.VISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            } else if (soundList.size() > 0) {
                type_spinner.setSelection(2);
                pic_gridView.setVisibility(View.INVISIBLE);
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
            } else {
                type_spinner.setSelection(0);
                pic_gridView.setVisibility(View.VISIBLE);
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setSoundGridView(View rootView) {

        sound_gridView = (GridView) rootView.findViewById(R.id.sound_gridView);
        soundAdapter = new SoundSimpleAdapter(soundList, this, !myEvidence.isUpload());
        soundAdapter.setOnClickListener(new SoundSimpleAdapter.OnClickListener() {

            @Override
            public void onLongItemClick(final SoundSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("是否删除声音文件？").
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = data.get(position);
                                String path = null;

                                path = FileUtil.PraseUritoPath(getContext(), uri);


                                File file = new File(path);
                                if (file.exists()) {
                                    file.delete();
                                    //ACTION_MEDIA_SCANNER_SCAN_FILE

                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
                                    intent.setData(data.get(position));
                                    CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent);
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

        sound_gridView.setAdapter(soundAdapter);

        AudioRecordButton button = (AudioRecordButton) rootView.findViewById(R.id.audio_record_button);
        button.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {

                if (picList.size() > 0 || videoList.size() > 0 || soundList.size() > 0) {
                    Toast.makeText(getContext(), "已有证据附件，不能再添加附件", Toast.LENGTH_SHORT).show();
                    return;
                }

                soundList.add(Uri.fromFile(new File(filePath)));
//                if(localSaveEvidence()) {
//                    soundAdapter.notifyDataSetChanged();
//                }

                myEvidence.setPicList(picList);
                myEvidence.setVideoList(videoList);
                myEvidence.setSoundList(soundList);

                soundAdapter.notifyDataSetChanged();
            }
        });
    }
    private File mDataPicFile;
    private void setPicGridView(View rootView) {
        pic_gridView = (GridView) rootView.findViewById(R.id.pic_gridView);
        picAdapter = new PicSimpleAdapter(picList, this, !myEvidence.isUpload(),null);
        picAdapter.setOnClickListener(new PicSimpleAdapter.OnClickListener() {
            @Override
            public void onAddPic() {

                if (picList.size() > 0 || videoList.size() > 0 || soundList.size() > 0) {
                    Toast.makeText(getContext(), "已有证据附件，不能再添加附件", Toast.LENGTH_SHORT).show();
                    return;
                }

//             AlertDialog dialog = new AlertDialog.Builder(CaseAudioVideoEvidenceFragment.this.getContext()).setTitle("选择获取图片的方式").
//                        setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                AlertDialog dialog = new AlertDialog.Builder(CaseAudioVideoEvidenceFragment.this.getContext()).setTitle("选择获取图片的方式").
//                        setItems(new String[]{"拍照"}, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent();
//                                switch (which) {
//                                    case 0:
//                                        if (!picStorageDir.exists()) {
//                                            picStorageDir.mkdirs();
//                                        }
//                                        File saveFile = new File(picStorageDir, android.text.format.DateFormat
//                                                .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
//                                                + ".jpg");
//                                        photouri = Uri.fromFile(saveFile);
//
//                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);
//                                        CaseAudioVideoEvidenceFragment.this.startActivityForResult(intent, PHOTO_FROM_CAMERA);
//
//                                        break;
//                                    case 1:
//                                        File f = new File(picStorageDir.getPath());
//                                        intent.setType("image/*");
//                                        intent.setAction(Intent.ACTION_GET_CONTENT);
////                                            intent.setDataAndType(Uri.fromFile(f), "image/*");
//                                        CaseAudioVideoEvidenceFragment.this.startActivityForResult(intent, PHOTO_FROM_GALLERY);
//                                        break;
//                                }
//                            }
//                        }).setNegativeButton("取消", null).create();
//                dialog.show();

                Intent intent = new Intent();
                if (!picStorageDir.exists()) {
                    picStorageDir.mkdirs();
                }
//                File saveFile = new File(picStorageDir, android.text.format.DateFormat
//                        .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
//                        + ".jpg");
//                photouri = Uri.fromFile(saveFile);

//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photouri);

                mDataPicFile = new File(picStorageDir.getPath(), android.text.format.DateFormat
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

//                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                    intent.setData(data.get(position));
//                                    CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent);
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

        pic_gridView.setAdapter(picAdapter);
    }

    private void setVideoGridView(View rootView) {
        video_gridView = (GridView) rootView.findViewById(R.id.video_gridView);
        videoAdapter = new VideoSimpleAdapter(videoList, this, !myEvidence.isUpload(),null);
        videoAdapter.setOnClickListener(new VideoSimpleAdapter.OnClickListener() {
            @Override
            public void onAddVideo() {

                if (picList.size() > 0 || videoList.size() > 0 || soundList.size() > 0) {
                    Toast.makeText(getContext(), "已有证据附件，不能再添加附件", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent();
                if (!videoStorageDir.exists()) {
                    videoStorageDir.mkdirs();
                }
//                File saveFile = new File(videoStorageDir, android.text.format.DateFormat
//                        .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
//                        + ".mp4");
//                videouri = Uri.fromFile(saveFile);

//                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, videouri);

                intent.setClass(getActivity(), VideoRecordActivity.class);
                CaseAudioVideoEvidenceFragment.this.startActivityForResult(intent, VIDEO_FROM_CAMERA);
            }

            @Override
            public void onLongItemClick(final VideoSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle("是否删除原视频？").
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
                }).setNeutralButton("取消", null).create();
                dialog.show();
            }
        });

        video_gridView.setAdapter(videoAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri picuri = data.getData();
                    if (picuri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        picuri = Uri.parse(MediaStore.Images.Media.insertImage(CaseAudioVideoEvidenceFragment.this.getActivity().getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }
                    myEvidence.getPicList().add(picuri);
                    picAdapter.notifyDataSetChanged();
                    break;
                case PHOTO_FROM_CAMERA:

                    if (mDataPicFile != null && mDataPicFile.exists()){

                        myEvidence.getPicList().add(Uri.fromFile(mDataPicFile));
                        picAdapter.notifyDataSetChanged();
                    }


                    //通知系统扫描文件
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(picStorageDir));
                    CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent);
                    break;
                case VIDEO_FROM_CAMERA:

                    Uri videouri = data.getData();

                    myEvidence.getVideoList().add(videouri);
                    videoAdapter.notifyDataSetChanged();

                    //通知系统扫描文件
                    Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent2.setData(videouri);
                    CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent2);
                    break;
                case VIDEO_FROM_GALLERY:
                    Uri videoUri = data.getData();
                    myEvidence.getVideoList().add(videoUri);
                    videoAdapter.notifyDataSetChanged();
                    break;
            }

//            localSaveEvidence();

            myEvidence.setPicList(picList);
            myEvidence.setVideoList(videoList);
            myEvidence.setSoundList(soundList);
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
        myEvidence.setSoundList(soundList);
        myEvidence.setVideoList(videoList);

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
