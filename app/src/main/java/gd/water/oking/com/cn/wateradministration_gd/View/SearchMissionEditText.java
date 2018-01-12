package gd.water.oking.com.cn.wateradministration_gd.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.Adapter.ReceivivingExpandableListAdapter;
import gd.water.oking.com.cn.wateradministration_gd.BaseView.AutoCompleteEditText;
import gd.water.oking.com.cn.wateradministration_gd.bean.Mission;

/**
 * Created by zhao on 2016/9/23.
 */

public class SearchMissionEditText extends AutoCompleteEditText<Mission> {

    private ExpandableListView missionListView;

    public SearchMissionEditText(Context context, ExpandableListView missionListView, ArrayList<Mission> dataList) {
        super(context);
        this.missionListView = missionListView;
        setDataList(dataList);
    }

    public SearchMissionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ArrayList filterData(CharSequence constraint, ArrayList<Mission> dataList) {
        ArrayList<Mission> filterDataList = new ArrayList<>();

        for (Mission mission : dataList) {
            if (mission.getTask_name().contains(constraint)) {
                filterDataList.add(mission);
            }
        }

        return filterDataList;
    }

    @Override
    public void showFilterData(CharSequence constraint, ArrayList<Mission> filterDataList) {
        if (missionListView != null&&filterDataList.size()>0) {
            ((ReceivivingExpandableListAdapter)missionListView.getExpandableListAdapter()).setDataList(filterDataList);
//            if (filterDataList.size() > 0) {
//                ((ReceivivingExpandableListAdapter) missionListView.getAdapter()).refresh();
//            } else {
//                ((ReceivivingExpandableListAdapter) missionListView.getAdapter()).refresh();
//            }
        }
    }

    public void setMissionListView(ExpandableListView missionListView) {
        this.missionListView = missionListView;
    }
}
