package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;

import java.io.IOException;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.util.MediaManager;

/**
 * Created by zhao on 2016/10/16.
 */

public class SoundSimpleAdapter extends BaseAdapter {

    private ArrayList<Uri> uriArrayList;
    private OnClickListener onClickListener;
    private boolean canAdd;

    private View viewanim;
    private AnimationDrawable mDrawable;

    public SoundSimpleAdapter(ArrayList<Uri> uriArrayList, boolean canAdd) {
        this.uriArrayList = uriArrayList;
        this.canAdd = canAdd;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return uriArrayList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return uriArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item_layout, null);
        LinearLayout recorder_length = convertView.findViewById(R.id.recorder_length);
        ImageView iv_record = convertView.findViewById(R.id.iv_record);

        if (position == 0) {
            iv_record.setVisibility(View.VISIBLE);
            recorder_length.setVisibility(View.GONE);
            iv_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onAddSoundClick();
                }
            });
        } else {
            iv_record.setVisibility(View.GONE);
            recorder_length.setVisibility(View.VISIBLE);

            try {
                MediaPlayer mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(PathUtil.getInstance().getVoicePath().getPath()+"/"+uriArrayList.get(position - 1).getLastPathSegment());
                mPlayer.prepare();
                TextView time_tv = (TextView) convertView.findViewById(R.id.recorder_time);
                int time = Math.round(mPlayer.getDuration() / 1000);
                time_tv.setText(time + "\"");
                mPlayer.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewanim != null) {

                        mDrawable.stop();
                        viewanim.setBackgroundResource(R.drawable.adj);
                    }
                    MediaManager.init();
                    if (!MediaManager.mPlayer.isPlaying()) {
                        viewanim = v.findViewById(R.id.id_recorder_anim);
                        viewanim.setBackgroundResource(R.drawable.play);
                        mDrawable = (AnimationDrawable) viewanim.getBackground();
                        mDrawable.start();

                        if (MediaManager.mPlayer != null) {
                            MediaManager.playSound(PathUtil.getInstance().getVoicePath().getPath()+"/"+uriArrayList.get(position - 1).getLastPathSegment(), new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    MediaManager.mPlayer.reset();
                                    viewanim.setBackgroundResource(R.drawable.adj);
                                }
                            }, new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                    MediaManager.mPlayer.reset();
                                    return false;
                                }
                            });
                        }
                    } else {
                        MediaManager.mPlayer.reset();

                    }


                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null && canAdd) {
                        onClickListener.onLongItemClick(SoundSimpleAdapter.this, uriArrayList, position-1);
                    }
                    return false;
                }
            });
        }


        return convertView;
    }


    public interface OnClickListener {
        void onLongItemClick(SoundSimpleAdapter adapter, ArrayList<Uri> data, int position);

        void onAddSoundClick();
    }


}
