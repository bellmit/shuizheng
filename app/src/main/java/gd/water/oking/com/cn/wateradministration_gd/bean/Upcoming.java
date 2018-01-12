package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by zhao on 2017-5-5.
 */

public class Upcoming {
    private String taskId;
    private String jjcd;
    private String rwlx;
    private String rwnr;
    private String fqr;
    private String fqsj;

    public Upcoming(String taskId, String jjcd, String rwlx, String rwnr, String fqr, String fqsj) {
        this.taskId = taskId;
        this.jjcd = jjcd;
        this.rwlx = rwlx;
        this.rwnr = rwnr;
        this.fqr = fqr;
        this.fqsj = fqsj;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJjcd() {
        return jjcd;
    }

    public void setJjcd(String jjcd) {
        this.jjcd = jjcd;
    }

    public String getRwlx() {
        return rwlx;
    }

    public void setRwlx(String rwlx) {
        this.rwlx = rwlx;
    }

    public String getRwnr() {
        return rwnr;
    }

    public void setRwnr(String rwnr) {
        this.rwnr = rwnr;
    }

    public String getFqr() {
        return fqr;
    }

    public void setFqr(String fqr) {
        this.fqr = fqr;
    }

    public String getFqsj() {
        return fqsj;
    }

    public void setFqsj(String fqsj) {
        this.fqsj = fqsj;
    }
}
