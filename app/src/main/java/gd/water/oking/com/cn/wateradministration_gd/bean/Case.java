package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016/9/12.
 */
public class Case implements Parcelable{

    private String AJID;
    private long SLRQ;
    private String ZFBM;
    private String SLR;
    private String AJLX;
    private String AJMC;
    private String AY;
    private String AFDD;
    private long AFSJ;
    private String AQJY;
    private String XWZSD;
    private String JGD;
    private String SSD;
    private String WHJGFSD;
    private String DSRQK;
    private String SQWTR;
    private String FLYJ;
    private String CFYJ;
    private String CFNR;
    private String CBR1;
    private String CBRDW1;
    private String ZFZH1;
    private String CBR2;
    private String CBRDW2;
    private String ZFZH2;
    private String ZT;
    private String AJLY;
    private String AJLXID;
    private String CBRID1;
    private String CBRID2;
    private String SLXX_ZT;

//    private ArrayList<Uri> soundList = new ArrayList<>();
//    private ArrayList<Uri> picList = new ArrayList<>();
//    private ArrayList<Uri> videoList = new ArrayList<>();
//    private ArrayList<Uri> certificateList = new ArrayList<>();
//    private ArrayList<Uri> otherList = new ArrayList<>();

    private ArrayList<Evidence> evidenceList = new ArrayList<>();
    private ArrayList<SurveyRecord> surveyRecordList = new ArrayList<>();

    public Case() {

    }

    protected Case(Parcel in) {
        AJID = in.readString();
        SLRQ = in.readLong();
        ZFBM = in.readString();
        SLR = in.readString();
        AJLX = in.readString();
        AJMC = in.readString();
        AY = in.readString();
        AFDD = in.readString();
        AFSJ = in.readLong();
        AQJY = in.readString();
        XWZSD = in.readString();
        JGD = in.readString();
        SSD = in.readString();
        WHJGFSD = in.readString();
        DSRQK = in.readString();
        SQWTR = in.readString();
        FLYJ = in.readString();
        CFYJ = in.readString();
        CFNR = in.readString();
        CBR1 = in.readString();
        CBRDW1 = in.readString();
        ZFZH1 = in.readString();
        CBR2 = in.readString();
        CBRDW2 = in.readString();
        ZFZH2 = in.readString();
        ZT = in.readString();
        AJLY = in.readString();
        AJLXID = in.readString();
        CBRID1 = in.readString();
        CBRID2 = in.readString();
        SLXX_ZT = in.readString();
        evidenceList = in.createTypedArrayList(Evidence.CREATOR);
    }

    public static final Creator<Case> CREATOR = new Creator<Case>() {
        @Override
        public Case createFromParcel(Parcel in) {
            return new Case(in);
        }

        @Override
        public Case[] newArray(int size) {
            return new Case[size];
        }
    };

    public String getAJID() {
        return AJID;
    }

    public void setAJID(String AJID) {
        this.AJID = AJID;
    }

    public long getSLRQ() {
        return SLRQ;
    }

    public void setSLRQ(long SLRQ) {
        this.SLRQ = SLRQ;
    }

    public String getZFBM() {
        return ZFBM;
    }

    public void setZFBM(String ZFBM) {
        this.ZFBM = ZFBM;
    }

    public String getSLR() {
        return SLR;
    }

    public void setSLR(String SLR) {
        this.SLR = SLR;
    }

    public String getAJLX() {
        return AJLX;
    }

    public void setAJLX(String AJLX) {
        this.AJLX = AJLX;
    }

    public String getAJMC() {
        return AJMC;
    }

    public void setAJMC(String AJMC) {
        this.AJMC = AJMC;
    }

    public String getAY() {
        return AY;
    }

    public void setAY(String AY) {
        this.AY = AY;
    }

    public String getAFDD() {
        return AFDD;
    }

    public void setAFDD(String AFDD) {
        this.AFDD = AFDD;
    }

    public long getAFSJ() {
        return AFSJ;
    }

    public void setAFSJ(long AFSJ) {
        this.AFSJ = AFSJ;
    }

    public String getAQJY() {
        return AQJY;
    }

    public void setAQJY(String AQJY) {
        this.AQJY = AQJY;
    }

    public String getXWZSD() {
        return XWZSD;
    }

    public void setXWZSD(String XWZSD) {
        this.XWZSD = XWZSD;
    }

    public String getJGD() {
        return JGD;
    }

    public void setJGD(String JGD) {
        this.JGD = JGD;
    }

    public String getSSD() {
        return SSD;
    }

    public void setSSD(String SSD) {
        this.SSD = SSD;
    }

    public String getWHJGFSD() {
        return WHJGFSD;
    }

    public void setWHJGFSD(String WHJGFSD) {
        this.WHJGFSD = WHJGFSD;
    }

    public String getDSRQK() {
        return DSRQK;
    }

    public void setDSRQK(String DSRQK) {
        this.DSRQK = DSRQK;
    }

    public String getSQWTR() {
        return SQWTR;
    }

    public void setSQWTR(String SQWTR) {
        this.SQWTR = SQWTR;
    }

    public String getFLYJ() {
        return FLYJ;
    }

    public void setFLYJ(String FLYJ) {
        this.FLYJ = FLYJ;
    }

    public String getCFYJ() {
        return CFYJ;
    }

    public void setCFYJ(String CFYJ) {
        this.CFYJ = CFYJ;
    }

    public String getCFNR() {
        return CFNR;
    }

    public void setCFNR(String CFNR) {
        this.CFNR = CFNR;
    }

    public String getCBR1() {
        return CBR1;
    }

    public void setCBR1(String CBR1) {
        this.CBR1 = CBR1;
    }

    public String getCBRDW1() {
        return CBRDW1;
    }

    public void setCBRDW1(String CBRDW1) {
        this.CBRDW1 = CBRDW1;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getCBR2() {
        return CBR2;
    }

    public void setCBR2(String CBR2) {
        this.CBR2 = CBR2;
    }

    public String getCBRDW2() {
        return CBRDW2;
    }

    public void setCBRDW2(String CBRDW2) {
        this.CBRDW2 = CBRDW2;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getZT() {
        return ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getAJLY() {
        return AJLY;
    }

    public void setAJLY(String AJLY) {
        this.AJLY = AJLY;
    }

    public String getAJLXID() {
        return AJLXID;
    }

    public void setAJLXID(String AJLXID) {
        this.AJLXID = AJLXID;
    }

    public String getCBRID1() {
        return CBRID1;
    }

    public void setCBRID1(String CBRID1) {
        this.CBRID1 = CBRID1;
    }

    public String getCBRID2() {
        return CBRID2;
    }

    public void setCBRID2(String CBRID2) {
        this.CBRID2 = CBRID2;
    }

    public String getSLXX_ZT() {
        return SLXX_ZT;
    }

    public void setSLXX_ZT(String SLXX_ZT) {
        this.SLXX_ZT = SLXX_ZT;
    }

    public ArrayList<Evidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(ArrayList<Evidence> evidenceList) {
        this.evidenceList = evidenceList;
    }

    public ArrayList<SurveyRecord> getSurveyRecordList() {
        return surveyRecordList;
    }

    public void setSurveyRecordList(ArrayList<SurveyRecord> surveyRecordList) {
        this.surveyRecordList = surveyRecordList;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("AJID", Case.this.getAJID());
        values.put("SLRQ", Case.this.getSLRQ());
        values.put("ZFBM", Case.this.getZFBM());
        values.put("SLR", Case.this.getSLR());
        values.put("AJLX", Case.this.getAJLX());
        values.put("AJMC", Case.this.getAJMC());
        values.put("AY", Case.this.getAY());
        values.put("AFDD", Case.this.getAFDD());
        values.put("AFSJ", Case.this.getAFSJ());
        values.put("AQJY", Case.this.getAQJY());
        values.put("XWZSD", Case.this.getXWZSD());
        values.put("JGD", Case.this.getJGD());
        values.put("SSD", Case.this.getSSD());
        values.put("WHJGFSD", Case.this.getWHJGFSD());
        values.put("DSRQK", Case.this.getDSRQK());
        values.put("SQWTR", Case.this.getSQWTR());
        values.put("FLYJ", Case.this.getFLYJ());
        values.put("CFYJ", Case.this.getCFYJ());
        values.put("CFNR", Case.this.getCFNR());
        values.put("CBR1", Case.this.getCBR1());
        values.put("CBRDW1", Case.this.getCBRDW1());
        values.put("ZFZH1", Case.this.getZFZH1());
        values.put("CBR2", Case.this.getCBR2());
        values.put("CBRDW2", Case.this.getCBRDW2());
        values.put("ZFZH2", Case.this.getZFZH2());
        values.put("ZT", Case.this.getZT());
        values.put("AJLY", Case.this.getAJLY());
        values.put("AJLXID", Case.this.getAJLXID());
        values.put("CBRID1", Case.this.getCBRID1());
        values.put("CBRID2", Case.this.getCBRID2());
        values.put("SLXX_ZT", Case.this.getSLXX_ZT());
        values.put("jsonStr", DataUtil.toJson(Case.this));

        db.insert(LocalSqlite.CASE_TABLE, values);

    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.CASE_TABLE, "AJID = ?", new String[]{Case.this.getAJID()});

    }

    public void updateDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("SLRQ", Case.this.getSLRQ());
        values.put("ZFBM", Case.this.getZFBM());
        values.put("SLR", Case.this.getSLR());
        values.put("AJLX", Case.this.getAJLX());
        values.put("AJMC", Case.this.getAJMC());
        values.put("AY", Case.this.getAY());
        values.put("AFDD", Case.this.getAFDD());
        values.put("AFSJ", Case.this.getAFSJ());
        values.put("AQJY", Case.this.getAQJY());
        values.put("XWZSD", Case.this.getXWZSD());
        values.put("JGD", Case.this.getJGD());
        values.put("SSD", Case.this.getSSD());
        values.put("WHJGFSD", Case.this.getWHJGFSD());
        values.put("DSRQK", Case.this.getDSRQK());
        values.put("SQWTR", Case.this.getSQWTR());
        values.put("FLYJ", Case.this.getFLYJ());
        values.put("CFYJ", Case.this.getCFYJ());
        values.put("CFNR", Case.this.getCFNR());
        values.put("CBR1", Case.this.getCBR1());
        values.put("CBRDW1", Case.this.getCBRDW1());
        values.put("ZFZH1", Case.this.getZFZH1());
        values.put("CBR2", Case.this.getCBR2());
        values.put("CBRDW2", Case.this.getCBRDW2());
        values.put("ZFZH2", Case.this.getZFZH2());
        values.put("ZT", Case.this.getZT());
        values.put("AJLY", Case.this.getAJLY());
        values.put("AJLXID", Case.this.getAJLXID());
        values.put("CBRID1", Case.this.getCBRID1());
        values.put("CBRID2", Case.this.getCBRID2());
        values.put("SLXX_ZT", Case.this.getSLXX_ZT());
        values.put("jsonStr", DataUtil.toJson(Case.this));

        db.update(LocalSqlite.CASE_TABLE, values, "AJID = ?", new String[]{Case.this.getAJID()});
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AJID);
        parcel.writeLong(SLRQ);
        parcel.writeString(ZFBM);
        parcel.writeString(SLR);
        parcel.writeString(AJLX);
        parcel.writeString(AJMC);
        parcel.writeString(AY);
        parcel.writeString(AFDD);
        parcel.writeLong(AFSJ);
        parcel.writeString(AQJY);
        parcel.writeString(XWZSD);
        parcel.writeString(JGD);
        parcel.writeString(SSD);
        parcel.writeString(WHJGFSD);
        parcel.writeString(DSRQK);
        parcel.writeString(SQWTR);
        parcel.writeString(FLYJ);
        parcel.writeString(CFYJ);
        parcel.writeString(CFNR);
        parcel.writeString(CBR1);
        parcel.writeString(CBRDW1);
        parcel.writeString(ZFZH1);
        parcel.writeString(CBR2);
        parcel.writeString(CBRDW2);
        parcel.writeString(ZFZH2);
        parcel.writeString(ZT);
        parcel.writeString(AJLY);
        parcel.writeString(AJLXID);
        parcel.writeString(CBRID1);
        parcel.writeString(CBRID2);
        parcel.writeString(SLXX_ZT);
        parcel.writeTypedList(evidenceList);
    }
}
