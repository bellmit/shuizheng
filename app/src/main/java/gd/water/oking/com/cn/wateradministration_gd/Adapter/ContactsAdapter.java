package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.Dept;

/**
 * Created by zhao on 2016-12-6.
 */

public class ContactsAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Dept.RowsBean> rows;

    public ContactsAdapter(Activity mActivity,List<Dept.RowsBean> rows) {
        this.mActivity = mActivity;
        this.rows = rows;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_contacts, null);

            holder.name_tv = (TextView) convertView.findViewById(R.id.name_textView);
            holder.dept_tv = (TextView) convertView.findViewById(R.id.dept_textView);
            holder.phone_tv = (TextView) convertView.findViewById(R.id.phone_textView);
//            holder.call_btn.setTypeface(MyApp.fontIcon);

            convertView.setTag(holder);
            AutoUtils.auto(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Dept.RowsBean rowsBean = rows.get(position);

        if (rowsBean != null) {
            holder.name_tv.setText(rowsBean.getUsername());
            holder.dept_tv.setText(rowsBean.getDeptname());
            if (rowsBean.getPhone() != null) {
                holder.phone_tv.setText(rowsBean.getPhone());
            } else {
                holder.phone_tv.setText("");
            }

//            holder.call_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (holder.phone_tv.getText().toString().equals("")) {
//                        return;
//                    }
//
//                    try {
////                        Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + holder.phone_tv.getText().toString()));
////                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.phone_tv.getText().toString()));
////                        context.startActivity(phoneIntent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView name_tv;
        TextView dept_tv;
        TextView phone_tv;
        Button call_btn;
    }
}
