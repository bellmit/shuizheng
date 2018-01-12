package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.SimpleGridViewAdapter;
import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.main.ImageViewActivity;
import gd.water.oking.com.cn.wateradministration_gd.main.MyApp;

/**
 * Created by zhao on 2016/10/9.
 */

public class PicSimpleAdapter extends SimpleGridViewAdapter {

    private ArrayList<Uri> uriArrayList;
    private OnClickListener onClickListener;
    private BaseFragment f;
    private boolean canAdd;
    private HashMap<Uri, Bitmap> bitmapCache;
    private String typeName;

    public PicSimpleAdapter(ArrayList<Uri> uriArrayList, BaseFragment f, boolean canAdd,String typeName) {
        super(uriArrayList);
        this.uriArrayList = uriArrayList;
        this.f = f;
        this.canAdd = canAdd;
        bitmapCache = new HashMap<>();
        this.typeName = typeName;
    }

    @Override
    public View getItemView(final int position, View convertView, final ViewGroup parent) {
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

        if (position == 0) {
            viewHolder.tv.setVisibility(View.GONE);
            viewHolder.iv_pic .setImageResource(R.drawable.photo);
            if (canAdd) {
                viewHolder.iv_pic .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onAddPic();
                        }
                    }
                });
            }


        } else {
            viewHolder.tv.setVisibility(View.VISIBLE);
            final Uri uri = uriArrayList.get(position - 1);
            Bitmap bitmap = bitmapCache.get(uri);

            if (bitmap == null) {
                try {
                    ContentResolver cr = f.getActivity().getContentResolver();
                    bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 120, 110, false);
                    bitmapCache.put(uri, bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(f.getResources(), R.drawable.loadfail);
            }

            viewHolder.iv_pic .setImageBitmap(bitmap);
            viewHolder.iv_pic .setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null && canAdd) {
                        onClickListener.onLongItemClick(PicSimpleAdapter.this, uriArrayList, position - 1);
                    }
                    bitmapCache.remove(uri);
                    return false;
                }
            });
            viewHolder.iv_pic .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(f.getActivity(), ImageViewActivity.class);
                    intent.setData(uri);
                    f.startActivity(intent);
                }
            });
            String path = uri.getPath();

            String s = path.substring(path.lastIndexOf("/") + 1, path.length());
            viewHolder.tv.setText(s.split("_")[0]+typeName);
        }

        return convertView;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onAddPic();

        void onLongItemClick(PicSimpleAdapter adapter, ArrayList<Uri> data, int position);
    }

    class ViewHolder{
        ImageView iv_pic;
        TextView tv;
    }
}
