package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.model.EaseVoiceRecorder;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.vondear.rxtools.view.RxToast;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.ProblemBean;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AskAdapter extends BaseAdapter {
    private ArrayList<ProblemBean> problemContent;
    //    private PowerManager.WakeLock wakeLock;
    private EaseVoiceRecorder voiceRecorder;
    private Drawable[] micImages;
    private Activity mActivity;
    private ImageView mMic_image;
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            mMic_image.setImageDrawable(micImages[msg.what]);
        }
    };


    public AskAdapter(Activity activity, ArrayList<ProblemBean> problemContent) {
        this.mActivity = activity;
//        wakeLock = ((PowerManager) MyApp.getApplictaion().getSystemService(Context.POWER_SERVICE)).newWakeLock(
//                PowerManager.SCREEN_DIM_WAKE_LOCK, "oking");

//        micImages = new Drawable[] { MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_01),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_02),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_03),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_04),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_05),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_06),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_07),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_08),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_09),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_10),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_11),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_12),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_13),
//                MyApp.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_14), };
//
//        voiceRecorder = new EaseVoiceRecorder(micImageHandler);
        this.problemContent = problemContent;
    }


    @Override
    public int getCount() {
        return problemContent.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView( int position, View contentView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (contentView == null) {
            viewHolder = new ViewHolder();
            contentView = View.inflate(mActivity, R.layout.survey_ask_item, null);
            viewHolder.tv_ask_content = contentView.findViewById(R.id.tv_ask_content);
            viewHolder.et_answer_content = contentView.findViewById(R.id.et_answer_content);
            contentView.setTag(viewHolder);
            AutoUtils.auto(contentView);
        }

        viewHolder = (ViewHolder) contentView.getTag();
        viewHolder.et_answer_content.setText("");
//        ImageView iv_record = contentView.findViewById(R.id.iv_record);
        final ProblemBean problemBean = problemContent.get(position);
        viewHolder.tv_ask_content.setText("问：" + problemBean.getAsk());
//        iv_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//                View inflate = View.inflate(mActivity, R.layout.voice_recorder_dialog, null);
//                mMic_image = inflate.findViewById(R.id.mic_image);
//                builder.setView(inflate);
//                builder.setCancelable(false);
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        stopRecoding();
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.setPositiveButton("停止", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        try {
//                            int length = stopRecoding();
//                            if (length > 0) {
//                                System.out.println(getVoiceFilePath() + "<<<<<<<<<<<<<<<");
//
//                            } else if (length == EMError.FILE_INVALID) {
//                                RxToast.error(MyApp.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
//                            } else {
//                                RxToast.warning(MyApp.getApplictaion(), "录音时间太短", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            RxToast.error(MyApp.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                WindowManager.LayoutParams params =
//                        alertDialog.getWindow().getAttributes();
//                params.width = 450;
//                params.height = 400;
//                alertDialog.getWindow().setAttributes(params);
//                startRecording();
//            }
//        });

        return contentView;
    }

    class ViewHolder{
        TextView tv_ask_content;
        EditText et_answer_content;
    }

    private int stopRecoding() {
//        if (wakeLock.isHeld()){
//
//            wakeLock.release();
//        }
        return voiceRecorder.stopRecoding();
    }


    private void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            RxToast.error(MyApp.getApplictaion(), "请插上sd卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
//            wakeLock.acquire();
            voiceRecorder.startRecording(MyApp.getApplictaion());
        } catch (Exception e) {
            e.printStackTrace();
//            if (wakeLock.isHeld()){
//
//                wakeLock.release();
//            }
            if (voiceRecorder != null) {

                voiceRecorder.discardRecording();
            }
            RxToast.error(MyApp.getApplictaion(), "录音失败,请重试!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public void setDatas(ArrayList<ProblemBean> problemContent) {
        this.problemContent = problemContent;
        notifyDataSetChanged();
    }
}
