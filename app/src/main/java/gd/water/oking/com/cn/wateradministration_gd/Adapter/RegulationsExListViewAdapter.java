package gd.water.oking.com.cn.wateradministration_gd.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.bean.ChapterDomain;
import gd.water.oking.com.cn.wateradministration_gd.bean.LawsRegulation;

/**
 * Created by Administrator on 2017/10/30.
 */

public class RegulationsExListViewAdapter extends AutoCompleteAdapter{
    private ArrayList<LawsRegulation>  lawChapter;
    private Activity mActivity;

    public RegulationsExListViewAdapter(Activity mActivity,ArrayList<LawsRegulation>  lawChapter) {
        this.mActivity = mActivity;
        this.lawChapter = lawChapter;
    }

    @Override
    public void setDataList(ArrayList dataList) {

    }

    @Override
    public int getGroupCount() {
        return lawChapter.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return lawChapter.get(i).getChapterDirectory().get(i).getSection().size();
    }

    @Override
    public Object getGroup(int i) {
        return super.getGroup(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return super.getChild(i, i1);
    }

    @Override
    public long getGroupId(int i) {
        return super.getGroupId(i);
    }

    @Override
    public long getChildId(int i, int i1) {
        return super.getChildId(i, i1);
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView==null){
            groupHold = new GroupHold();
            convertView = View.inflate(mActivity, R.layout.gegulation_group_item,null);
            groupHold.title = convertView.findViewById(R.id.tv_title);
            groupHold.ivGoToChildLv = convertView.findViewById(R.id.iv_goToChildLV);
            convertView.setTag(groupHold);
            AutoUtils.auto(convertView);
        }

        groupHold = (GroupHold) convertView.getTag();
        ChapterDomain chapterDomain = lawChapter.get(groupPosition).getChapterDirectory().get(groupPosition);
        groupHold.title.setText(chapterDomain.getChapterDirectory());
        //取消默认的groupIndicator后根据方法中传入的isExpand判断组是否展开并动态自定义指示器
        if (isExpanded) {   //如果组展开
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_down);
        } else {
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_right);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildHold childHold;
        if (convertView==null){
            childHold = new ChildHold();
            convertView = View.inflate(mActivity,R.layout.gegylation_child_item,null);
            childHold.itemTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(childHold);
            AutoUtils.auto(convertView);
        }
        childHold = (ChildHold) convertView.getTag();
        String itemTitle = lawChapter.get(groupPosition).getChapterDirectory().get(groupPosition).getSection().get(childPosition).getItemTitle();
        childHold.itemTitle.setText(itemTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHold {
        TextView title;
        ImageView ivGoToChildLv;
    }

    class ChildHold {
        TextView itemTitle;
    }
}
