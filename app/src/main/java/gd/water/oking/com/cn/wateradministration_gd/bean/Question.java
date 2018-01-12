package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2017-4-13.
 */

public class Question {

    private String XH;
    private String WTLX;
    private String AJLX;
    private String WT;
    private String JQXX;
    private String ZT;
    private String PX;

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public String getWTLX() {
        return WTLX;
    }

    public void setWTLX(String WTLX) {
        this.WTLX = WTLX;
    }

    public String getAJLX() {
        return AJLX;
    }

    public void setAJLX(String AJLX) {
        this.AJLX = AJLX;
    }

    public String getWT() {
        return WT;
    }

    public void setWT(String WT) {
        this.WT = WT;
    }

    public String getJQXX() {
        return JQXX;
    }

    public void setJQXX(String JQXX) {
        this.JQXX = JQXX;
    }

    public String getZT() {
        return ZT;
    }

    public void setZT(String ZT) {
        this.ZT = ZT;
    }

    public String getPX() {
        return PX;
    }

    public void setPX(String PX) {
        this.PX = PX;
    }

    public void insertDB(final LocalSqlite db) {

                ContentValues values = new ContentValues();
                values.put("XH", Question.this.getXH());
                values.put("WTLX", Question.this.getWTLX());
                values.put("AJLX", Question.this.getAJLX());
                values.put("WT", Question.this.getWT());
                values.put("JQXX", Question.this.getJQXX());
                values.put("ZT", Question.this.getZT());
                values.put("PX", Question.this.getPX());
                values.put("jsonStr", DataUtil.toJson(Question.this));

                db.insert(LocalSqlite.QUESTION_TABLE, values);

    }

    public void deleteDB(final LocalSqlite db) {

                db.delete(LocalSqlite.QUESTION_TABLE, "XH = ?", new String[]{Question.this.getXH()});

    }

    public void updateDB(final LocalSqlite db) {

                ContentValues values = new ContentValues();
                values.put("WTLX", Question.this.getWTLX());
                values.put("AJLX", Question.this.getAJLX());
                values.put("WT", Question.this.getWT());
                values.put("JQXX", Question.this.getJQXX());
                values.put("ZT", Question.this.getZT());
                values.put("PX", Question.this.getPX());
                values.put("jsonStr", DataUtil.toJson(Question.this));

                db.update(LocalSqlite.QUESTION_TABLE, values, "XH = ?", new String[]{Question.this.getXH()});

    }
}
