package gd.water.oking.com.cn.wateradministration_gd.bean;

import com.amap.api.maps.offlinemap.OfflineMapCity;

/**
 * Created by zhao on 2017-6-26.
 */

public class OfflineMapDownload {

    public static final String OnDownload = "正在下载";
    public static final String OnUnZIP = "正在解压";
    public static final String NewVersionOrUnDownload = "未下载或有新版本";
    public static final String Error = "错误";
    public static final String Waiting = "等待下载";
    public static final String Normal = "";

    private OfflineMapCity city;
    private int progress;
    private String state;

    public OfflineMapDownload() {
    }

    public OfflineMapDownload(OfflineMapCity city, int progress, String state) {
        this.city = city;
        this.progress = progress;
        this.state = state;
    }

    public OfflineMapCity getCity() {
        return city;
    }

    public void setCity(OfflineMapCity city) {
        this.city = city;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
