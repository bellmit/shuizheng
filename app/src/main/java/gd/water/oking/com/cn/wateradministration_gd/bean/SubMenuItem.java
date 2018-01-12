package gd.water.oking.com.cn.wateradministration_gd.bean;

import gd.water.oking.com.cn.wateradministration_gd.BaseView.BaseFragment;

/**
 * Created by zhao on 2017-4-27.
 */

public class SubMenuItem {

    private String resId;
    private String title;
    private int messageCount;
    private BaseFragment fragment;

    public SubMenuItem(String resId, String title, int messageCount) {
        this.resId = resId;
        this.title = title;
        this.messageCount = messageCount;
    }

    public SubMenuItem(String resId, String title, int messageCount, BaseFragment fragment) {
        this.resId = resId;
        this.title = title;
        this.messageCount = messageCount;
        this.fragment = fragment;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }
}
