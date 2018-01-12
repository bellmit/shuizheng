package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import java.util.Arrays;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016-12-12.
 */

public class User {

    private String userId;
    private String deptId;
    private String userName;
    private String deptName;
    private String account;
    private String password;
    private String phone;
    private String ZFZH;
    private byte[] profile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZFZH() {
        return ZFZH;
    }

    public void setZFZH(String ZFZH) {
        this.ZFZH = ZFZH;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("uid", User.this.getUserId());
        values.put("deptId", User.this.getDeptId());
        values.put("userName", User.this.getUserName());
        values.put("deptName", User.this.getDeptName());
        values.put("account", User.this.getAccount());
        values.put("password", User.this.getPassword());
        values.put("phone", User.this.getPhone());
        values.put("profile", User.this.getProfile());
        values.put("jsonStr", DataUtil.toJson(User.this));

        db.insert(LocalSqlite.USER_TABLE, values);
        db.close();

    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.USER_TABLE, "uid = ?", new String[]{User.this.getUserId()});
        db.close();
    }

    public void updateDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("deptId", User.this.getDeptId());
        values.put("userName", User.this.getUserName());
        values.put("deptName", User.this.getDeptName());
        values.put("account", User.this.getAccount());
        values.put("password", User.this.getPassword());
        values.put("phone", User.this.getPhone());
        values.put("profile", User.this.getProfile());
        values.put("jsonStr", DataUtil.toJson(User.this));

        db.update(LocalSqlite.USER_TABLE, values, "uid = ?", new String[]{User.this.getUserId()});

    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", deptId='" + deptId + '\'' +
                ", userName='" + userName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", ZFZH='" + ZFZH + '\'' +
                ", profile=" + Arrays.toString(profile) +
                '}';
    }
}
