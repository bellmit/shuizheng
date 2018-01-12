package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016/11/24.
 */

public class Equipment {

    private String type2;
    private String mc1;      //装备类型
    private String type;      //装备类型code
    private String mc2;    //装备名称
    private String value;
    private String ly;
    private String remarks;
    private String deptId;

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getMc1() {
        return mc1;
    }

    public void setMc1(String mc1) {
        this.mc1 = mc1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMc2() {
        return mc2;
    }

    public void setMc2(String mc2) {
        this.mc2 = mc2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("deptid", Equipment.this.getDeptId());
        values.put("type", Equipment.this.getType());
        values.put("type2", Equipment.this.getType2());
        values.put("value", Equipment.this.getValue());
        values.put("mc1", Equipment.this.getMc1());
        values.put("mc2", Equipment.this.getMc2());
        values.put("remarks", Equipment.this.getRemarks());
        values.put("ly", Equipment.this.getLy());

        long insert = db.insert(LocalSqlite.EQUIPMENT_TABLE, values);
    }


    @Override
    public String toString() {
        return "Equipment{" +
                "type2='" + type2 + '\'' +
                ", mc1='" + mc1 + '\'' +
                ", type='" + type + '\'' +
                ", mc2='" + mc2 + '\'' +
                ", value='" + value + '\'' +
                ", ly='" + ly + '\'' +
                ", remarks='" + remarks + '\'' +
                ", deptId='" + deptId + '\'' +
                '}';
    }
}
