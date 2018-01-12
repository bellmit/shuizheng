package gd.water.oking.com.cn.wateradministration_gd.bean;

import java.util.ArrayList;

/**
 * Created by zhao on 2017-4-3.
 */

public class SurveyRecord {

    private String XH;
    private String AJID;
    private String WSID;
    private String BDCR;
    private int XB = 0;
    private String NL;
    private String LXDH;
    private String SFZHM;
    private String ZW;
    private String BDCDWMC;
    private String FRMC;
    private String FRZW;
    private String DZ;
    private String XDZ;
    private Long DCSJ_KS;
    private Long DCSJ_ZZ;
    private String DCDD;
    private String DCRY1;
    private String DW1;
    private String ZFZH1;
    private String DCRY2;
    private String DW2;
    private String ZFZH2;
    private String JLR;
    private String JLRDW;
    private String DCRY1USERID;
    private String DCRY2USERID;
    private String JLRUSERID;
    private String AY;

    private boolean isUpload = false;

    private ArrayList<QuestionAnswer> questionAnswers = new ArrayList<>();

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getAJID() {
        return AJID;
    }

    public void setAJID(String AJID) {
        this.AJID = AJID;
    }

    public String getWSID() {
        return WSID;
    }

    public void setWSID(String WSID) {
        this.WSID = WSID;
    }

    public String getBDCR() {
        return BDCR;
    }

    public void setBDCR(String BDCR) {
        this.BDCR = BDCR;
    }

    public int getXB() {
        return XB;
    }

    public void setXB(int XB) {
        this.XB = XB;
    }

    public String getNL() {
        return NL;
    }

    public void setNL(String NL) {
        this.NL = NL;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getSFZHM() {
        return SFZHM;
    }

    public void setSFZHM(String SFZHM) {
        this.SFZHM = SFZHM;
    }

    public String getZW() {
        return ZW;
    }

    public void setZW(String ZW) {
        this.ZW = ZW;
    }

    public String getBDCDWMC() {
        return BDCDWMC;
    }

    public void setBDCDWMC(String BDCDWMC) {
        this.BDCDWMC = BDCDWMC;
    }

    public String getFRMC() {
        return FRMC;
    }

    public void setFRMC(String FRMC) {
        this.FRMC = FRMC;
    }

    public String getFRZW() {
        return FRZW;
    }

    public void setFRZW(String FRZW) {
        this.FRZW = FRZW;
    }

    public String getDZ() {
        return DZ;
    }

    public void setDZ(String DZ) {
        this.DZ = DZ;
    }

    public long getDCSJ_KS() {
        return DCSJ_KS;
    }

    public void setDCSJ_KS(long DCSJ_KS) {
        this.DCSJ_KS = DCSJ_KS;
    }

    public long getDCSJ_ZZ() {
        return DCSJ_ZZ;
    }

    public void setDCSJ_ZZ(long DCSJ_ZZ) {
        this.DCSJ_ZZ = DCSJ_ZZ;
    }

    public String getDCDD() {
        return DCDD;
    }

    public void setDCDD(String DCDD) {
        this.DCDD = DCDD;
    }

    public String getDCRY1() {
        return DCRY1;
    }

    public void setDCRY1(String DCRY1) {
        this.DCRY1 = DCRY1;
    }

    public String getDW1() {
        return DW1;
    }

    public void setDW1(String DW1) {
        this.DW1 = DW1;
    }

    public String getZFZH1() {
        return ZFZH1;
    }

    public void setZFZH1(String ZFZH1) {
        this.ZFZH1 = ZFZH1;
    }

    public String getDCRY2() {
        return DCRY2;
    }

    public void setDCRY2(String DCRY2) {
        this.DCRY2 = DCRY2;
    }

    public String getDW2() {
        return DW2;
    }

    public void setDW2(String DW2) {
        this.DW2 = DW2;
    }

    public String getZFZH2() {
        return ZFZH2;
    }

    public void setZFZH2(String ZFZH2) {
        this.ZFZH2 = ZFZH2;
    }

    public String getJLR() {
        return JLR;
    }

    public void setJLR(String JLR) {
        this.JLR = JLR;
    }

    public String getJLRDW() {
        return JLRDW;
    }

    public void setJLRDW(String JLRDW) {
        this.JLRDW = JLRDW;
    }

    public String getDCRY1USERID() {
        return DCRY1USERID;
    }

    public void setDCRY1USERID(String DCRY1USERID) {
        this.DCRY1USERID = DCRY1USERID;
    }

    public String getDCRY2USERID() {
        return DCRY2USERID;
    }

    public void setDCRY2USERID(String DCRY2USERID) {
        this.DCRY2USERID = DCRY2USERID;
    }

    public String getJLRUSERID() {
        return JLRUSERID;
    }

    public void setJLRUSERID(String JLRUSERID) {
        this.JLRUSERID = JLRUSERID;
    }

    public String getAY() {
        return AY;
    }

    public void setAY(String AY) {
        this.AY = AY;
    }

    public String getXDZ() {
        return XDZ;
    }

    public void setXDZ(String XDZ) {
        this.XDZ = XDZ;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public ArrayList<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(ArrayList<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }
}
