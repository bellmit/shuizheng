package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.SurveyRecord;

/**
 * Created by zhao on 2017-4-3.
 */

public class SurveyRecordAdapter extends BaseAdapter {

    private ArrayList<SurveyRecord> surveyRecords;
    private Context context;
    private OnButtonClick onButtonClick;


    public SurveyRecordAdapter(Context context, ArrayList<SurveyRecord> surveyRecords) {
        this.context = context;
        this.surveyRecords = surveyRecords;
    }

    @Override
    public int getCount() {
        return surveyRecords.size();
    }

    @Override
    public Object getItem(int position) {
        return surveyRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_surveyrecord, null);

            holder.bdcr_tv = (TextView) convertView.findViewById(R.id.bdcr_tv);
            holder.dcry_tv = (TextView) convertView.findViewById(R.id.dcry_tv);
            holder.dcsj_tv = (TextView) convertView.findViewById(R.id.dcsj_tv);
            holder.edit_button = (Button) convertView.findViewById(R.id.edit_button);
            holder.del_button = (Button) convertView.findViewById(R.id.del_button);
            holder.upload_button = (Button) convertView.findViewById(R.id.upload_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SurveyRecord surveyRecord = surveyRecords.get(position);
        if (surveyRecord != null) {
            holder.bdcr_tv.setText(surveyRecord.getBDCR());
            holder.dcry_tv.setText(surveyRecord.getDCRY1());
            holder.dcsj_tv.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(surveyRecord.getDCSJ_KS())));
            holder.del_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClick != null) {
                        onButtonClick.onDelClick(position, v);
                    }

                    notifyDataSetChanged();
                }
            });

            holder.edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClick != null) {
                        onButtonClick.onEditClick(position, v);
                    }

                    notifyDataSetChanged();
                }
            });

            holder.upload_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onButtonClick != null) {
                        onButtonClick.onUploadClick(position, v);
                    }

                    notifyDataSetChanged();
                }
            });

            if (surveyRecord.isUpload()) {
                holder.upload_button.setVisibility(View.GONE);
                holder.del_button.setVisibility(View.GONE);
                holder.edit_button.setText("查看");
            }
        }

        return convertView;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public interface OnButtonClick {
        void onEditClick(int position, View view);
        void onDelClick(int position, View view);
        void onUploadClick(int position, View view);
    }

    private static class ViewHolder {
        TextView bdcr_tv;
        TextView dcry_tv;
        TextView dcsj_tv;
        Button del_button;
        Button edit_button;
        Button upload_button;
    }
}
