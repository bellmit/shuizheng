package gd.water.oking.com.cn.wateradministration_gd.BaseView;

import gd.water.oking.com.cn.wateradministration_gd.bean.Case;

/**
 * Created by zhao on 2017-3-29.
 */

public abstract class BaseCaseFragment extends BaseFragment {

    private String title;

    public abstract void setMyCase(Case mycase);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
