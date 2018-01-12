package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2018/1/11.
 */

public class AnswBean {
    private String wt;
    private String hd;
    private int sort;

    public AnswBean(String wt, String hd, int sort) {
        this.wt = wt;
        this.hd = hd;
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "AnswBean{" +
                "wt='" + wt + '\'' +
                ", hd='" + hd + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
