package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.SimpleGridViewAdapter;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;
import gd.water.oking.com.cn.wateradministration_gd.main.VideoActivity;
import gd.water.oking.com.cn.wateradministration_gd.util.FileUtil;

/**
 * Created by zhao on 2016/10/17.
 */

public class VideoSimpleAdapter extends SimpleGridViewAdapter {

    private ArrayList<Uri> uriArrayList;
    private OnClickListener onClickListener;
    private BaseFragment f;
    private boolean canAdd;
    private HashMap<Uri, Bitmap> bitmapCache;
    private String typeName;

    public VideoSimpleAdapter(ArrayList<Uri> uriArrayList, BaseFragment f, boolean canAdd,String typeName) {

        super(uriArrayList);
        this.uriArrayList = uriArrayList;
        this.f = f;
        this.canAdd = canAdd;
        bitmapCache = new HashMap<>();
        this.typeName = typeName;
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView =  View.inflate(MyApp.getApplictaion(),R.layout.pic_item,null);
            viewHolder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        if (position == getCount() - 1) {
        if (position == 0) {
            viewHolder.tv.setVisibility(View.GONE);
            viewHolder.iv_pic.setImageResource(R.drawable.video);
            if (canAdd) {
                viewHolder.iv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onAddVideo();
                        }
                    }
                });
            }

        } else {

            viewHolder.tv.setVisibility(View.VISIBLE);
            final Uri uri = uriArrayList.get(position - 1);
            Bitmap bitmap = bitmapCache.get(uri);

            if (bitmap == null) {
                bitmap = ThumbnailUtils.createVideoThumbnail(FileUtil.PraseUritoPath(parent.getContext(), uri),
                        MediaStore.Images.Thumbnails.MICRO_KIND);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, 120, 110, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                bitmapCache.put(uri, bitmap);
            }

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(f.getResources(), R.drawable.loadfail);
            }

            viewHolder.iv_pic.setImageBitmap(bitmap);

            viewHolder.iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(f.getActivity(), VideoActivity.class);
                    intent.setData(uri);
                    f.startActivity(intent);
                }
            });
            viewHolder.iv_pic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null && canAdd) {
                        onClickListener.onLongItemClick(VideoSimpleAdapter.this, uriArrayList, position - 1);
                    }

                    bitmapCache.remove(uri);


                    return false;
                }
            });

            String path = uri.getPath();

            String s = path.substring(path.lastIndexOf("/") + 1, path.length());
            viewHolder.tv.setText(s.split("_")[0]+typeName);

        }

        return convertView;

    }

    public interface OnClickListener {
        void onAddVideo();

        void onLongItemClick(VideoSimpleAdapter adapter, ArrayList<Uri> data, int position);
    }
    class ViewHolder{
        ImageView iv_pic;
        TextView tv;
    }
}
