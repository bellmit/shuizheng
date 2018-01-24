package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2018/1/18.
 */

public class RecipientBean {
    private String DEPTID;
    private String DEPTNAME;
    private String REMARK;
    private String USERID;
    private String USERNAME;
    private String ZFZH;

    public String getDEPTID() {
        return DEPTID;
    }

    public void setDEPTID(String DEPTID) {
        this.DEPTID = DEPTID;
    }

    public String getDEPTNAME() {
        return DEPTNAME;
    }

    public void setDEPTNAME(String DEPTNAME) {
        this.DEPTNAME = DEPTNAME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getZFZH() {
        return ZFZH;
    }

    public void setZFZH(String ZFZH) {
        this.ZFZH = ZFZH;
    }

    @Override
    public String toString() {
        return "RecipientBean{" +
                "DEPTID='" + DEPTID + '\'' +
                ", DEPTNAME='" + DEPTNAME + '\'' +
                ", REMARK='" + REMARK + '\'' +
                ", USERID='" + USERID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", ZFZH='" + ZFZH + '\'' +
                '}';
    }
}
