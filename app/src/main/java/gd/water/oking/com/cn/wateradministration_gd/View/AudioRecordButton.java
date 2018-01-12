package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.File;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.util.AudioManager;

/**
 * Created by zhao on 2016/10/16.
 */

public class AudioRecordButton extends android.support.v7.widget.AppCompatButton implements AudioManager.AudioStageListener {

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private static final int DISTANCE_Y_CANCEL = 20;
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;
    private int mCurrentState = STATE_NORMAL;
    private boolean isRecording = false;
    private AudioDialog mDialogManager;
    private AudioManager mAudioManager;
    private float mTime = 0;
    private boolean mReady;
    private String saveDir;
    private AudioFinishRecorderListener mListener;
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mhandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    MyApp.getGlobalThreadPool().execute(mGetVoiceLevelRunnable);

                    break;
                case MSG_VOICE_CHANGE:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));

                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    reset();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public AudioRecordButton(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            saveDir = new File(Environment.getExternalStorageDirectory(), "oking/case_sound").getPath();

            mDialogManager = new AudioDialog(getContext());

            mAudioManager = AudioManager.getInstance(saveDir);
            mAudioManager.setOnAudioStageListener(this);

//            setOnLongClickListener(new OnLongClickListener() {
//
//                @Override
//                public boolean onLongClick(View v) {
//                    // TODO Auto-generated method
//                    mReady = true;
//                    mAudioManager.prepareAudio(android.text.format.DateFormat
//                            .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
//                            + ".amr");
//                    return false;
//                }
//            });
        }
    }

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mListener = listener;
    }

    @Override
    public void wellPrepared() {
        // TODO Auto-generated method stub
        mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.i("AudioRecordButton", "X>>>>>" + x + "   Y>>>>>" + y);

//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i("AudioRecordButton", "Down");
//                changeState(STATE_RECORDING);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i("AudioRecordButton", "Move");
//                if (isRecording) {
//
//                    if (wantToCancel(x, y)) {
//                        changeState(STATE_WANT_TO_CANCEL);
//                    } else {
//                        changeState(STATE_RECORDING);
//                    }
//
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i("AudioRecordButton", "Up");
//                if (!mReady) {
//                    reset();
//                    return super.onTouchEvent(event);
//                }
//
//                if (!isRecording || mTime < 0.5f) {
//                    mDialogManager.tooShort();
//                    mAudioManager.cancel();
//                    mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);
//                } else if (mCurrentState == STATE_RECORDING) {
//
//                    mDialogManager.dimissDialog();
//
//                    mAudioManager.release();
//
//                    if (mListener!=null) {
//                        mListener.onFinished(mTime, mAudioManager.getCurrentFilePath());
//                    }
//                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
//                    // cancel
//                    mAudioManager.cancel();
//                    mDialogManager.dimissDialog();
//                }
//                reset();
//
//                break;
//        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (mCurrentState == STATE_NORMAL) {
                    mReady = true;
                    mAudioManager.prepareAudio(android.text.format.DateFormat
                            .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                            + ".amr");
                    changeState(STATE_RECORDING);
                } else if (mCurrentState == STATE_RECORDING) {
                    if (!mReady) {
                        reset();
                        return super.onTouchEvent(event);
                    }

                    if (!isRecording || mTime < 0.5f) {
                        mDialogManager.tooShort();
                        mAudioManager.cancel();
                        mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);
                    } else if (mCurrentState == STATE_RECORDING) {

                        mDialogManager.dimissDialog();

                        mAudioManager.release();

                        if (mListener != null) {
                            mListener.onFinished(mTime, mAudioManager.getCurrentFilePath());
                        }
                    } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                        // cancel
                        mAudioManager.cancel();
                        mDialogManager.dimissDialog();
                    }
                    reset();
                }

                break;
        }

        return super.onTouchEvent(event);
    }

    private void reset() {
        // TODO Auto-generated method stub
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        // TODO Auto-generated method stub

        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < 0 || y > getHeight()) {
            return true;
        }

        return false;
    }

    private void changeState(int state) {
        // TODO Auto-generated method stub
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    //setBackgroundResource(R.drawable.button_recordnormal);
                    setBackgroundResource(R.drawable.datetimeview_bg);
                    setTextColor(getResources().getColor(R.color.colorMain4));
//                    setText("按住  说话");
                    setText("点击开始录音");

                    break;
                case STATE_RECORDING:
                    //setBackgroundResource(R.drawable.button_recording);
                    setBackgroundResource(R.drawable.red_btn_bg);
                    setTextColor(getResources().getColor(R.color.colorMain6));
//                    setText("松开  结束");
                    setText("再次点击完成录音");
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;

                case STATE_WANT_TO_CANCEL:
                    //setBackgroundResource(R.drawable.button_recording);
                    setBackgroundResource(R.drawable.datetimeview_bg);
                    setTextColor(getResources().getColor(R.color.colorMain4));
                    setText("取消 发送");
                    mDialogManager.wantToCancel();
                    break;

            }
        }

    }

    @Override
    public boolean onPreDraw() {
        // TODO Auto-generated method stub
        return false;
    }

    public interface AudioFinishRecorderListener {
        void onFinished(float seconds, String filePath);
    }
}
