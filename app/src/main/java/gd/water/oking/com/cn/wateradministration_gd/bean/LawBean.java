package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by Administrator on 2017/10/27.
 */

public class LawBean {
    private long rowId;
    private String levelEffectiveness;
    private String publishingDepartment;
    private String releaseTime;
    private String implementationTime;
    private String mmid;
    private String title;
    private String rulesContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevelEffectiveness() {
        return levelEffectiveness;
    }

    public void setLevelEffectiveness(String levelEffectiveness) {
        this.levelEffectiveness = levelEffectiveness;
    }

    public String getPublishingDepartment() {
        return publishingDepartment;
    }

    public void setPublishingDepartment(String publishingDepartment) {
        this.publishingDepartment = publishingDepartment;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getImplementationTime() {
        return implementationTime;
    }

    public void setImplementationTime(String implementationTime) {
        this.implementationTime = implementationTime;
    }

    public String getMmid() {
        return mmid;
    }

    public void setMmid(String mmid) {
        this.mmid = mmid;
    }

    public String getRulesContent() {
        return rulesContent;
    }

    public void setRulesContent(String rulesContent) {
        this.rulesContent = rulesContent;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    @Override
    public String toString() {
        return "LawBean{" +
                "rowId=" + rowId +
                ", levelEffectiveness='" + levelEffectiveness + '\'' +
                ", publishingDepartment='" + publishingDepartment + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", implementationTime='" + implementationTime + '\'' +
                ", mmid='" + mmid + '\'' +
                ", title='" + title + '\'' +
                ", rulesContent='" + rulesContent + '\'' +
                '}';
    }
}
