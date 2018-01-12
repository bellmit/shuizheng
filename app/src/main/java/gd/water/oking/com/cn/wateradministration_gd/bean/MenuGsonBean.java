package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MenuGsonBean implements Serializable{
    private String id;
    private String text;
    private List<TopMenu>mTopMenus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TopMenu> getTopMenus() {
        return mTopMenus;
    }

    public void setTopMenus(List<TopMenu> topMenus) {
        mTopMenus = topMenus;
    }

    @Override
    public String toString() {
        return "MenuGsonBean{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", mTopMenus=" + mTopMenus +
                '}';
    }

   public static class TopMenu implements Serializable{
        private String id;
        private String text;
        private String request;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        @Override
        public String toString() {
            return "TopMenu{" +
                    "id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", request='" + request + '\'' +
                    '}';
        }
    }
}


