package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2017-6-9.
 */

public class Area {

    private String id;
    private String taskid;
    private String area_type;
    private String coordinate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getArea_type() {
        return area_type;
    }

    public void setArea_type(String area_type) {
        this.area_type = area_type;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("aid", Area.this.getId());
        values.put("task_id", Area.this.getTaskid());
        values.put("area_type", Area.this.getArea_type());
        values.put("coordinate", Area.this.getCoordinate());
        values.put("jsonStr", DataUtil.toJson(Area.this));

        db.insert(LocalSqlite.AREA_TABLE, values);

    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.MEMBER_TABLE, "aid = ?", new String[]{Area.this.getId()});

    }

    public void updateDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("task_id", Area.this.getTaskid());
        values.put("area_type", Area.this.getArea_type());
        values.put("coordinate", Area.this.getCoordinate());
        values.put("jsonStr", DataUtil.toJson(Area.this));

        db.update(LocalSqlite.AREA_TABLE, values, "aid = ?", new String[]{Area.this.getId()});
    }

}
