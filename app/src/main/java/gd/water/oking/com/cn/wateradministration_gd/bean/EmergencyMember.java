package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2017/10/9.
 */

public class EmergencyMember {
    private String deptId;
    private String deptName;
    private String remark;
    private String username;
    private String userId;
    private String zfzh;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getZfzh() {
        return zfzh;
    }

    public void setZfzh(String zfzh) {
        this.zfzh = zfzh;
    }

    @Override
    public String toString() {
        return "EmergencyMember{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", remark='" + remark + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", zfzh='" + zfzh + '\'' +
                '}';
    }
}
