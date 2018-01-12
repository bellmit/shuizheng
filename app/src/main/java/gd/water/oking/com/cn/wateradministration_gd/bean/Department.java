package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016-12-13.
 */

public class Department {

    private int sortno;
    private String deptid;
    private String remark;
    private String leaf;
    private String customid;
    private String parentid;
    private String enabled;
    private String deptname;

    public int getSortno() {
        return sortno;
    }

    public void setSortno(int sortno) {
        this.sortno = sortno;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public void insertDB(final LocalSqlite db) {
        ContentValues values = new ContentValues();
        values.put("deptId", Department.this.getDeptid());
        values.put("sortno", Department.this.getSortno());
        values.put("parentid", Department.this.getParentid());
        values.put("customid", Department.this.getCustomid());
        values.put("leaf", Department.this.getLeaf());
        values.put("enabled", Department.this.getEnabled());
        values.put("deptname", Department.this.getDeptname());
        values.put("jsonStr", DataUtil.toJson(Department.this));

        db.insert(LocalSqlite.DEPARTMENT_TABLE, values);
    }

    public void deleteDB(final LocalSqlite db) {
        db.delete(LocalSqlite.DEPARTMENT_TABLE, "deptId = ?", new String[]{Department.this.getDeptid()});

    }

    public void updateDB(final LocalSqlite db) {
        ContentValues values = new ContentValues();
        values.put("sortno", Department.this.getSortno());
        values.put("parentid", Department.this.getParentid());
        values.put("customid", Department.this.getCustomid());
        values.put("leaf", Department.this.getLeaf());
        values.put("enabled", Department.this.getEnabled());
        values.put("deptname", Department.this.getDeptname());
        values.put("jsonStr", DataUtil.toJson(Department.this));

        db.update(LocalSqlite.DEPARTMENT_TABLE, values, "deptId = ?", new String[]{Department.this.getDeptid()});
    }
}