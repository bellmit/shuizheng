package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016-12-6.
 */

public class Contacts {

    private String userid;
    private String username;
    private String deptid;
    private String deptname;
    private String phone;
    private String account;
    private String zfzh;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getZfzh() {
        return zfzh;
    }

    public void setZfzh(String zfzh) {
        this.zfzh = zfzh;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("ctid", Contacts.this.getUserid());
        values.put("name", Contacts.this.getUsername());
        values.put("deptid", Contacts.this.getDeptid());
        values.put("deptname", Contacts.this.getDeptname());
        values.put("phone", Contacts.this.getPhone());
        values.put("account", Contacts.this.getAccount());
        values.put("zfzh", Contacts.this.getZfzh());
        values.put("jsonStr", DataUtil.toJson(Contacts.this));

        db.insert(LocalSqlite.CONTACTS_TABLE, values);

    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.CONTACTS_TABLE, "ctid = ?", new String[]{Contacts.this.getUserid()});

    }

    public void updateDB(final LocalSqlite db) {
        ContentValues values = new ContentValues();
        values.put("name", Contacts.this.getUsername());
        values.put("deptid", Contacts.this.getDeptid());
        values.put("deptname", Contacts.this.getDeptname());
        values.put("phone", Contacts.this.getPhone());
        values.put("account", Contacts.this.getAccount());
        values.put("zfzh", Contacts.this.getZfzh());
        values.put("jsonStr", DataUtil.toJson(Contacts.this));

        db.update(LocalSqlite.CONTACTS_TABLE, values, "ctid = ?", new String[]{Contacts.this.getUserid()});
    }
}
