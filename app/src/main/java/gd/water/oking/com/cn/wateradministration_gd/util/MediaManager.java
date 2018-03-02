package gd.water.oking.com.cn.wateradministration_gd.util;

/**
 * Created by zhao on 2016/10/16.
 */


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import java.io.IOException;

public class MediaManager {


    public static MediaPlayer mPlayer ;

    private static boolean isPause;

    public static void init() {
        // TODO Auto-generated method stub
        if (mPlayer == null) {
            mPlayer= new MediaPlayer();

        }
    }

    public static void playSound(String filePathString,
                                 OnCompletionListener onCompletionListener,OnErrorListener onErrorListener) {


        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(onCompletionListener);
            mPlayer.setOnErrorListener(onErrorListener);
            mPlayer.setDataSource(filePathString);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //停止函数
    public static void pause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    //继续
    public static void resume() {
        if (mPlayer != null && isPause) {
            mPlayer.start();
            isPause = false;
        }
    }

}

