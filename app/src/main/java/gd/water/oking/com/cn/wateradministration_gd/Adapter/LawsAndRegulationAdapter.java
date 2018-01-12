package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawBean;
import gd.water.oking.com.cn.wateradministration_gd.fragment.LawsAndRegulationsFragment;
import gd.water.oking.com.cn.wateradministration_gd.fragment.RegulationsDetailsFragment;

/**
 * Created by Administrator on 2017/10/27.
 */

public class LawsAndRegulationAdapter extends BaseAdapter{
    private List<LawBean> mLawBeans;
    private Activity mActivity;
    private LawsAndRegulationsFragment mLawsAndRegulationsFragment;


    public LawsAndRegulationAdapter(Activity mActivity,LawsAndRegulationsFragment lawsAndRegulationsFragment, List<LawBean> lawBeans) {
        this.mActivity = mActivity;
        this.mLawBeans = lawBeans;
        this.mLawsAndRegulationsFragment = lawsAndRegulationsFragment;
    }

    @Override
    public int getCount() {
        return mLawBeans.size();
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
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if (contentView==null){
            mViewHolder = new ViewHolder();
            contentView = View.inflate(mActivity, R.layout.lawandregulation_item,null);
            mViewHolder.tv_title = contentView.findViewById(R.id.tv_title);
            mViewHolder.tv_level_effectiveness = contentView.findViewById(R.id.tv_level_effectiveness);
            mViewHolder.tv_publishing_department = contentView.findViewById(R.id.tv_publishing_department);
            mViewHolder.tv_release_time = contentView.findViewById(R.id.tv_release_time);
            mViewHolder.tv_implementation_time = contentView.findViewById(R.id.tv_implementation_time);
            mViewHolder.bt_toview = contentView.findViewById(R.id.bt_toview);
            contentView.setTag(mViewHolder);
            AutoUtils.auto(contentView);
        }
        mViewHolder = (ViewHolder) contentView.getTag();
        final LawBean bean = mLawBeans.get(position);
        mViewHolder.tv_title.setText(bean.getTitle());
        mViewHolder.tv_implementation_time.setText(bean.getImplementationTime());
        mViewHolder.tv_level_effectiveness.setText(bean.getLevelEffectiveness());
        mViewHolder.tv_publishing_department.setText(bean.getPublishingDepartment());
        mViewHolder.tv_release_time.setText(bean.getReleaseTime());

        mViewHolder.bt_toview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = mLawsAndRegulationsFragment.getFragmentManager();
                RegulationsDetailsFragment regulationsDetailsFragment  = RegulationsDetailsFragment.newInstance(bean.getMmid(),bean.getRulesContent());
                fm.beginTransaction().replace(R.id.fragment_root, regulationsDetailsFragment).commit();
            }
        });
        return contentView;
    }

    class ViewHolder{
        TextView tv_title;
        TextView tv_level_effectiveness;
        TextView tv_publishing_department;
        TextView tv_release_time;
        TextView tv_implementation_time;
        Button bt_toview;
    }
}
