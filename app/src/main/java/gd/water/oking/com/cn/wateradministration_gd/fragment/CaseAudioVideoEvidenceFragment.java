package gd.water.oking.com.cn.wateradministration_gd.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.easeui.model.EaseVoiceRecorder;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.PathUtil;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.SoundSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.SpinnerArrayAdapter;
import gd.water.oking.com.cn.wateradministration_gd.Adapter.VideoSimpleAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Case;
import gd.water.oking.com.cn.wateradministration_gd.bean.Evidence;
import gd.water.oking.com.cn.wateradministration_gd.http.DefaultContants;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_TYPE = "type";
    private static final String ARG_EVIDENCE = "evidence";
    //    private static final int PHOTO_FROM_CAMERA = 100;
//    private static final int PHOTO_FROM_GALLERY = 101;
    private static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;

    private Evidence myEvidence;
    private Case mycase;
    private TextView evidence_name_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private Spinner type_spinner, evidence_source_spinner;
    private Button save_button, close_button;
    private GridView video_gridView, sound_gridView;

    private SoundSimpleAdapter soundAdapter;
    private VideoSimpleAdapter videoAdapter;

    private ArrayList<Uri> soundList = new ArrayList<>();
    private ArrayList<Uri> videoList = new ArrayList<>();
    private int mType;
    //    private Uri photouri, videouri;
    private File videoStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_video");


//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (intent.getAction()) {
//                case MainActivity.UPDATE_CASEFILE_UI_LIST:
//                    if (videoAdapter != null) {
//                        videoAdapter.notifyDataSetChanged();
//                    }
//
//                    if (soundAdapter != null) {
//                        soundAdapter.notifyDataSetChanged();
//                    }
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    private RxDialogSureCancel mRxDialogSureCancel;

    public CaseAudioVideoEvidenceFragment() {
        // Required empty public constructor
    }

    public static CaseAudioVideoEvidenceFragment newInstance(Case aCase, Evidence evidence, int type) {
        CaseAudioVideoEvidenceFragment fragment = new CaseAudioVideoEvidenceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, aCase);
        args.putParcelable(ARG_EVIDENCE, evidence);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mycase =  getArguments().getParcelable(ARG_PARAM1);
            myEvidence = getArguments().getParcelable(ARG_EVIDENCE);
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().registerReceiver(mReceiver, new IntentFilter(MainActivity.UPDATE_CASEFILE_UI_LIST));
        return inflater.inflate(R.layout.fragment_case_forensics, container, false);
    }

    @Override
    public void onDestroyView() {
//        getActivity().unregisterReceiver(mReceiver);
        if (MediaManager.mPlayer != null) {

            MediaManager.mPlayer.reset();
            MediaManager.mPlayer = null;
        }
        super.onDestroyView();
    }

    @Override
    public void initView(View rootView) {


        evidence_name_tv = (TextView) rootView.findViewById(R.id.evidence_name_tv);
        evidence_source_spinner = (Spinner) rootView.findViewById(R.id.evidence_source_spinner);
        video_gridView = (GridView) rootView.findViewById(R.id.video_gridView);
        sound_gridView = (GridView) rootView.findViewById(R.id.sound_gridView);

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
                        video_gridView.setVisibility(View.VISIBLE);
                        ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        video_gridView.setVisibility(View.INVISIBLE);
                        ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
                        break;
                    default:
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
        if (myEvidence!=null) {
            evidence_name_tv.setText(myEvidence.getZJMC());
//            evidence_source_spinner.setText(myEvidence.getZJLYMC());
            evidence_content_tv.setText(myEvidence.getZJNR());
            evidence_remark_tv.setText(myEvidence.getBZ());
            evidence_getLocation_tv.setText(myEvidence.getCJDD());
            evidence_man_textView.setText(myEvidence.getJZR());
            evidence_dept_tv.setText(myEvidence.getDW());
            evidence_pagerCount_tv.setText(myEvidence.getYS());

            if (videoList != null && videoList.size() > 0) {
                type_spinner.setSelection(1);
                video_gridView.setVisibility(View.VISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            } else if (soundList != null && soundList.size() > 0) {
                type_spinner.setSelection(2);
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
            } else {
                type_spinner.setSelection(0);
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            }


        } else {
            myEvidence = Evidence.CREATOR.createFromParcel(Parcel.obtain());
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());

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
                    rxDialogSure.setContent("保存成功！");
                    rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSure.cancel();
                        }
                    });
                    rxDialogSure.show();

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


        soundList = myEvidence.getSoundList();

        videoList = myEvidence.getVideoList();

        initVoiceRecorder();

        setSoundGridView();
        setVideoGridView();


    }

    private PowerManager.WakeLock wakeLock;
    private EaseVoiceRecorder voiceRecorder;
    private Drawable[] micImages;
    private ImageView mMic_image;
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            mMic_image.setImageDrawable(micImages[msg.what]);
        }
    };

    /**
     * 初始化录音
     */
    private void initVoiceRecorder() {
        wakeLock = ((PowerManager) MyApp.getApplictaion().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "oking");

        micImages = new Drawable[]{MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_01),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_02),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_03),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_04),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_05),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_06),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_07),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_08),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_09),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_10),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_11),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_12),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_13),
                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_14),};

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

    }

    private void setSoundGridView() {


        soundAdapter = new SoundSimpleAdapter(soundList, !myEvidence.isUpload());
        soundAdapter.setOnClickListener(new SoundSimpleAdapter.OnClickListener() {

            @Override
            public void onLongItemClick(final SoundSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {
                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());

                }
                mRxDialogSureCancel.setTitle("提示");
                mRxDialogSureCancel.setContent("是否删除声音文件？");
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = data.get(position);
                        String path  = PathUtil.getInstance().getVoicePath().getPath()+"/"+uri.getLastPathSegment();
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

            @Override
            public void onAddSoundClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View inflate = View.inflate(getActivity(), R.layout.voice_recorder_dialog, null);
                mMic_image = inflate.findViewById(R.id.mic_image);
                builder.setView(inflate);
                builder.setCancelable(false);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopRecoding();
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("停止", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            int length = stopRecoding();
                            if (length > 0) {

                                soundList.add(Uri.parse(getVoiceFileName()));
                                soundAdapter.notifyDataSetChanged();
                            } else if (length == EMError.FILE_INVALID) {
                                RxToast.error(MyApp.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
                            } else {
                                RxToast.warning(MyApp.getApplictaion(), "录音时间太短", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            RxToast.error(MyApp.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
                        }


                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                WindowManager.LayoutParams params =
                        alertDialog.getWindow().getAttributes();
                params.width = 450;
                params.height = 400;
                alertDialog.getWindow().setAttributes(params);
                startRecording();
            }
        });

        sound_gridView.setAdapter(soundAdapter);

//        AudioRecordButton button = (AudioRecordButton) rootView.findViewById(R.id.audio_record_button);
//        button.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
//            @Override
//            public void onFinished(float seconds, String filePath) {
//
//                if (picList.size() > 0 || videoList.size() > 0 || soundList.size() > 0) {
//                    Toast.makeText(getContext(), "已有证据附件，不能再添加附件", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                soundList.add(Uri.fromFile(new File(filePath)));
////                if(localSaveEvidence()) {
////                    soundAdapter.notifyDataSetChanged();
////                }
//
//                myEvidence.setPicList(picList);
//                myEvidence.setVideoList(videoList);
//                myEvidence.setSoundList(soundList);
//
//                soundAdapter.notifyDataSetChanged();
//            }
//        });
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    private int stopRecoding() {
        if (wakeLock.isHeld()) {

            wakeLock.release();
        }
        return voiceRecorder.stopRecoding();
    }


    private void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            RxToast.error(MyApp.getApplictaion(), "请插上sd卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            voiceRecorder.startRecording(MyApp.getApplictaion());
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld()) {

                wakeLock.release();
            }
            if (voiceRecorder != null) {

                voiceRecorder.discardRecording();
            }
            RxToast.error(MyApp.getApplictaion(), "录音失败,请重试!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void setVideoGridView() {

        videoAdapter = new VideoSimpleAdapter(videoList, this, mType != 0, "视听资料");
        videoAdapter.setOnClickListener(new VideoSimpleAdapter.OnClickListener() {
            @Override
            public void onAddVideo() {

//                if (picList.size() > 0 || videoList.size() > 0 || soundList.size() > 0) {
//                    Toast.makeText(getContext(), "已有证据附件，不能再添加附件", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                if (!videoStorageDir.exists()) {
                    videoStorageDir.mkdirs();
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoRecordActivity.class);
                CaseAudioVideoEvidenceFragment.this.startActivityForResult(intent, VIDEO_FROM_CAMERA);
            }

            @Override
            public void onLongItemClick(final VideoSimpleAdapter adapter, final ArrayList<Uri> data, final int position) {

                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());

                }
                mRxDialogSureCancel.setTitle("提示");
                mRxDialogSureCancel.setContent("是否删除原视频？");
                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mRxDialogSureCancel.cancel();
                    }
                });

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

                mRxDialogSureCancel.show();
            }
        });

        video_gridView.setAdapter(videoAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
                default:
                    break;
            }

//            localSaveEvidence();

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

        if(soundList.size()>0||videoList.size()>0){

        }else {
            Toast.makeText(getContext(), "请录入语音或视频！", Toast.LENGTH_SHORT).show();
            return false;
        }

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
        myEvidence.setZJLX("STZL");
        if (myEvidence.getVideoList().size()>0&&myEvidence.getSoundList().size()>0){
            myEvidence.setOtype("YYSP");            //语音、视频
        }else if (myEvidence.getVideoList().size()>0&&myEvidence.getSoundList().size()<1){
            myEvidence.setOtype("SP");          //视频
        }else if (myEvidence.getVideoList().size()<1&&myEvidence.getSoundList().size()>0){
            myEvidence.setOtype("YY");          //语音
        }

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
