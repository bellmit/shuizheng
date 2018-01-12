package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MenuBund implements Serializable{
    private List<MenuGsonBean> menuGsonBeen;

    public List<MenuGsonBean> getMenuGsonBeen() {
        return menuGsonBeen;
    }

    public void setMenuGsonBeen(List<MenuGsonBean> menuGsonBeen) {
        this.menuGsonBeen = menuGsonBeen;
    }

    @Override
    public String toString() {
        return "MenuBund{" +
                "menuGsonBeen=" + menuGsonBeen +
                '}';
    }
}
