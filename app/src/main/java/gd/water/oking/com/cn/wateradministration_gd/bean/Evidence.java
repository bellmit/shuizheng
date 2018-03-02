package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zhao on 2017-4-1.
 */

public class Evidence implements Parcelable{

    private String ZJID;
    private String AJID;
    private String ZJLX;
    private String ZJMC;
    private String ZJLY;
    private String ZJNR;
    private String SL;
    private long CJSJ;
    private String CJR;
    private String CJDD;
    private String JZR;
    private String DW;
    private String BZ;
    private String SCR;
    private long SCSJ;
    private String ZT;
    private String WSID;
    private String LXMC;
    private String ZJLYMC;
    private String LRSJ;
    private String YS;

    private String Otype;
    private boolean isUpload = false;

    private ArrayList<Uri> soundList = new ArrayList<>();
    private ArrayList<Uri> picList = new ArrayList<>();
    private ArrayList<Uri> videoList = new ArrayList<>();


    protected Evidence(Parcel in) {
        ZJID = in.readString();
        AJID = in.readString();
        ZJLX = in.readString();
        ZJMC = in.readString();
        ZJLY = in.readString();
        ZJNR = in.readString();
        SL = in.readString();
        CJSJ = in.readLong();
        CJR = in.readString();
        CJDD = in.readString();
        JZR = in.readString();
        DW = in.readString();
        BZ = in.readString();
        SCR = in.readString();
        SCSJ = in.readLong();
        ZT = in.readString();
        WSID = in.readString();
        LXMC = in.readString();
        ZJLYMC = in.readString();
        LRSJ = in.readString();
        YS = in.readString();
        Otype = in.readString();
        isUpload = in.readByte() != 0;
        soundList = in.createTypedArrayList(Uri.CREATOR);
        picList = in.createTypedArrayList(Uri.CREATOR);
        videoList = in.createTypedArrayList(Uri.CREATOR);
    }

    public static final Creator<Evidence> CREATOR = new Creator<Evidence>() {
        @Override
        public Evidence createFromParcel(Parcel in) {
            return new Evidence(in);
        }

        @Override
        public Evidence[] newArray(int size) {
            return new Evidence[size];
        }
    };

    public String getOtype() {
        return Otype;
    }

    public void setOtype(String otype) {
        Otype = otype;
    }

    public String getZJID() {
        return ZJID;
    }

    public void setZJID(String ZJID) {
        this.ZJID = ZJID;
    }

    public String getAJID() {
        return AJID;
    }

    public void setAJID(String AJID) {
        this.AJID = AJID;
    }

    public String getZJLX() {
        return ZJLX;
    }

    public void setZJLX(String ZJLX) {
        this.ZJLX = ZJLX;
    }

    public String getZJMC() {
        return ZJMC;
    }

    public void setZJMC(String ZJMC) {
        this.ZJMC = ZJMC;
    }

    public String getZJLY() {
        return ZJLY;
    }

    public void setZJLY(String ZJLY) {
        this.ZJLY = ZJLY;
    }

    public String getZJNR() {
        return ZJNR;
    }

    public void setZJNR(String ZJNR) {
        this.ZJNR = ZJNR;
    }

    public String getSL() {
        return SL;
    }

    public void setSL(String SL) {
        this.SL = SL;
    }

    public long getCJSJ() {
        return CJSJ;
    }

    public void setCJSJ(long CJSJ) {
        this.CJSJ = CJSJ;
    }

    public String getCJR() {
        return CJR;
    }

    public void setCJR(String CJR) {
        this.CJR = CJR;
    }

    public String getCJDD() {
        return CJDD;
    }

    public void setCJDD(String CJDD) {
        this.CJDD = CJDD;
    }

    public String getJZR() {
        return JZR;
    }

    public void setJZR(String JZR) {
        this.JZR = JZR;
    }

    public String getDW() {
        return DW;
    }

    public void setDW(String DW) {
        this.DW = DW;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getSCR() {
        return SCR;
    }

    public void setSCR(String SCR) {
        this.SCR = SCR;
    }

    public long getSCSJ() {
        return SCSJ;
    }

    public void setSCSJ(long SCSJ) {
        this.SCSJ = SCSJ;
    }

    public String getZT() {
        return ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getWSID() {
        return WSID;
    }

    public void setWSID(String WSID) {
        this.WSID = WSID;
    }

    public String getLXMC() {
        return LXMC;
    }

    public void setLXMC(String LXMC) {
        this.LXMC = LXMC;
    }

    public String getZJLYMC() {
        return ZJLYMC;
    }

    public void setZJLYMC(String ZJLYMC) {
        this.ZJLYMC = ZJLYMC;
    }

    public String getLRSJ() {
        return LRSJ;
    }

    public void setLRSJ(String LRSJ) {
        this.LRSJ = LRSJ;
    }

    public String getYS() {
        return YS;
    }

    public void setYS(String YS) {
        this.YS = YS;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public ArrayList<Uri> getSoundList() {
        return soundList;
    }

    public void setSoundList(ArrayList<Uri> soundList) {
        this.soundList = soundList;
    }

    public ArrayList<Uri> getPicList() {
        return picList;
    }

    public void setPicList(ArrayList<Uri> picList) {
        this.picList = picList;
    }

    public ArrayList<Uri> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Uri> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ZJID);
        parcel.writeString(AJID);
        parcel.writeString(ZJLX);
        parcel.writeString(ZJMC);
        parcel.writeString(ZJLY);
        parcel.writeString(ZJNR);
        parcel.writeString(SL);
        parcel.writeLong(CJSJ);
        parcel.writeString(CJR);
        parcel.writeString(CJDD);
        parcel.writeString(JZR);
        parcel.writeString(DW);
        parcel.writeString(BZ);
        parcel.writeString(SCR);
        parcel.writeLong(SCSJ);
        parcel.writeString(ZT);
        parcel.writeString(WSID);
        parcel.writeString(LXMC);
        parcel.writeString(ZJLYMC);
        parcel.writeString(LRSJ);
        parcel.writeString(YS);
        parcel.writeByte((byte) (isUpload ? 1 : 0));
        parcel.writeTypedList(soundList);
        parcel.writeTypedList(picList);
        parcel.writeTypedList(videoList);
    }

    @Override
    public String toString() {
        return "Evidence{" +
                "ZJID='" + ZJID + '\'' +
                ", AJID='" + AJID + '\'' +
                ", ZJLX='" + ZJLX + '\'' +
                ", ZJMC='" + ZJMC + '\'' +
                ", ZJLY='" + ZJLY + '\'' +
                ", ZJNR='" + ZJNR + '\'' +
                ", SL='" + SL + '\'' +
                ", CJSJ=" + CJSJ +
                ", CJR='" + CJR + '\'' +
                ", CJDD='" + CJDD + '\'' +
                ", JZR='" + JZR + '\'' +
                ", DW='" + DW + '\'' +
                ", BZ='" + BZ + '\'' +
                ", SCR='" + SCR + '\'' +
                ", SCSJ=" + SCSJ +
                ", ZT='" + ZT + '\'' +
                ", WSID='" + WSID + '\'' +
                ", LXMC='" + LXMC + '\'' +
                ", ZJLYMC='" + ZJLYMC + '\'' +
                ", LRSJ='" + LRSJ + '\'' +
                ", YS='" + YS + '\'' +
                ", isUpload=" + isUpload +
                ", soundList=" + soundList +
                ", picList=" + picList +
                ", videoList=" + videoList +
                '}';
    }
}
