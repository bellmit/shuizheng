package gd.water.oking.com.cn.wateradministration_gd.View;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import gd.water.oking.com.cn.wateradministration_gd.R;

/**
 * Created by zhao on 2016/10/16.
 */

public class AudioDialog {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    private Context mContext;

    public AudioDialog(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void showRecordingDialog() {
        // TODO Auto-generated method stub

        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_manager, null);
        mDialog.setContentView(view);

        //不获取焦点
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        mIcon = (ImageView) mDialog.findViewById(R.id.dialog_icon);
        mVoice = (ImageView) mDialog.findViewById(R.id.dialog_voice);
        mLable = (TextView) mDialog.findViewById(R.id.recorder_dialogtext);
        mDialog.show();

    }

    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("上滑，取消发送");
        }
    }

    public void wantToCancel() {
        // TODO Auto-generated method stub
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指，取消发送");
        }

    }

    public void tooShort() {
        // TODO Auto-generated method stub
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("录音时间太短");
        }

    }

    public void dimissDialog() {
        // TODO Auto-generated method stub

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }

    public void updateVoiceLevel(int level) {
        // TODO Auto-generated method stub

        if (mDialog != null && mDialog.isShowing()) {

            int resId = mContext.getResources().getIdentifier("v" + level,
                    "drawable", mContext.getPackageName());

            mVoice.setImageResource(resId);
        }

    }

}
