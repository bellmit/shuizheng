package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;
import android.net.Uri;

import java.io.Serializable;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016/11/16.
 */

public class Member implements Serializable {

    private String id;
    private String taskid;
    private String userid;
    private String username;
    private String post;

    private Uri signPic;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Uri getSignPic() {
        return signPic;
    }

    public void setSignPic(Uri signPic) {
        this.signPic = signPic;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("meid", Member.this.getId());
        values.put("task_id", Member.this.getTaskid());
        values.put("userid", Member.this.getUserid());
        values.put("uName", Member.this.getUsername());
        values.put("jsonStr", DataUtil.toJson(Member.this));

        db.insert(LocalSqlite.MEMBER_TABLE, values);

    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.MEMBER_TABLE, "meid = ?", new String[]{Member.this.getId()});


    }

    public void updateDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("task_id", Member.this.getTaskid());
        values.put("userid", Member.this.getUserid());
        values.put("uName", Member.this.getUsername());
        values.put("jsonStr", DataUtil.toJson(Member.this));

        db.update(LocalSqlite.MEMBER_TABLE, values, "meid = ?", new String[]{Member.this.getId()});
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", taskid='" + taskid + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", post='" + post + '\'' +
                ", signPic=" + signPic +
                '}';
    }
}
