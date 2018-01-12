package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.MediaManager;

/**
 * Created by zhao on 2016/10/16.
 */

public class SoundSimpleAdapter extends BaseAdapter {

    private ArrayList<Uri> uriArrayList;
    private OnClickListener onClickListener;
    private BaseFragment f;
    private boolean canAdd;

    private View viewanim;

    public SoundSimpleAdapter(ArrayList<Uri> uriArrayList, BaseFragment f, boolean canAdd) {
        this.uriArrayList = uriArrayList;
        this.f = f;
        this.canAdd = canAdd;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return uriArrayList.size();
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

        int width = (parent.getWidth() - ((GridView) parent).getHorizontalSpacing() * (((GridView) parent).getNumColumns() - 1)) / ((GridView) parent).getNumColumns();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item_layout, null);
        view.setLayoutParams(new AbsListView.LayoutParams(width, width));
        try {
            MediaPlayer mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(FileUtil.PraseUritoPath(parent.getContext(), uriArrayList.get(position)));
            mPlayer.prepare();
            TextView time_tv = (TextView) view.findViewById(R.id.recorder_time);
            int time = Math.round(mPlayer.getDuration() / 1000);
            time_tv.setText(time + "\"");
            mPlayer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewanim = v.findViewById(R.id.id_recorder_anim);
                viewanim.setBackgroundResource(R.drawable.play);
                AnimationDrawable drawable = (AnimationDrawable) viewanim.getBackground();
                drawable.start();

                if (MediaManager.mPlayer == null) {
                    MediaManager.playSound(FileUtil.PraseUritoPath(parent.getContext(), uriArrayList.get(position)), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            viewanim.setBackgroundResource(R.drawable.adj);
                        }
                    });
                } else {
                    MediaManager.release();
                    drawable.stop();
                    viewanim.setBackgroundResource(R.drawable.adj);
                }

            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener != null && canAdd) {
                    onClickListener.onLongItemClick(SoundSimpleAdapter.this, uriArrayList, position);
                }
                return false;
            }
        });

        return view;
    }



    public interface OnClickListener {
        void onLongItemClick(SoundSimpleAdapter adapter, ArrayList<Uri> data, int position);
    }
}
