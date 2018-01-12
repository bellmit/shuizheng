package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 * 菜单
 */

public class MenuBean {
    private int icon;
    private String title;
    private List<String>items;
    private ArrayList<Fragment> mFragments;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ArrayList<Fragment> getFragments() {
        return mFragments;
    }


    public void setFragments(ArrayList<Fragment> fragments) {
        mFragments = fragments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
