package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2017/11/13.
 */
public class ProblemBean {
    private int rowid;
    private String ask;
    private String content;
    private String type;
    private String typename;

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProblemBean{" +
                "rowid=" + rowid +
                ", ask='" + ask + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                '}';
    }
}
