package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.ArrayList;

import gd.water.oking.com.cn.wateradministration_gd.util.DataUtil;
import gd.water.oking.com.cn.wateradministration_gd.util.LocalSqlite;

/**
 * Created by zhao on 2016/9/7.
 */
public class Mission implements Serializable {

    private String id;
    private String task_name;
    private Long delivery_time;
    private Long begin_time;
    private Long end_time;
    private String publisher;
    private int task_type;
    private String AreaId;
    private String assignment;
    private int status;
    private String task_content;
    private String contact;
    private Long create_time;
    private String approved_person;
    private String approved_person_name;
    private String typename;
    private String receiver_name;
    private String publisher_name;
    private Long approved_time;
    private String receiver;
    private Long execute_start_time;
    private Long execute_end_time;
    private String spyj;
    private String rwqyms;
    private String jjcd;
    private String rwly;
    private String jsr;
    private String jsdw;
    private String fbr;
    private String fbdw;
    private String spr;
    private String typeoftask;

    private ArrayList<Member> members = new ArrayList<>();

    //private boolean isShowDetail;//用于判断任务列表中是否已展开


    public String getTypeoftask() {
        return typeoftask;
    }

    public void setTypeoftask(String typeoftask) {
        this.typeoftask = typeoftask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public Long getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(Long delivery_time) {
        this.delivery_time = delivery_time;
    }

    public Long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getApproved_person() {
        return approved_person;
    }

    public void setApproved_person(String approved_person) {
        this.approved_person = approved_person;
    }

    public String getApproved_person_name() {
        return approved_person_name;
    }

    public void setApproved_person_name(String approved_person_name) {
        this.approved_person_name = approved_person_name;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }

    public Long getApproved_time() {
        return approved_time;
    }

    public void setApproved_time(Long approved_time) {
        this.approved_time = approved_time;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

//    public boolean isShowDetail() {
//        return isShowDetail;
//    }
//
//    public void setShowDetail(boolean showDetail) {
//        isShowDetail = showDetail;
//    }

    public Long getExecute_start_time() {
        return execute_start_time;
    }

    public void setExecute_start_time(Long execute_start_time) {
        this.execute_start_time = execute_start_time;
    }

    public Long getExecute_end_time() {
        return execute_end_time;
    }

    public void setExecute_end_time(Long execute_end_time) {
        this.execute_end_time = execute_end_time;
    }

    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    public String getRwqyms() {
        return rwqyms;
    }

    public void setRwqyms(String rwqyms) {
        this.rwqyms = rwqyms;
    }

    public String getJjcd() {
        return jjcd;
    }

    public void setJjcd(String jjcd) {
        this.jjcd = jjcd;
    }

    public String getRwly() {
        return rwly;
    }

    public void setRwly(String rwly) {
        this.rwly = rwly;
    }

    public String getJsr() {
        return jsr;
    }

    public void setJsr(String jsr) {
        this.jsr = jsr;
    }

    public String getJsdw() {
        return jsdw;
    }

    public void setJsdw(String jsdw) {
        this.jsdw = jsdw;
    }

    public String getFbr() {
        return fbr;
    }

    public void setFbr(String fbr) {
        this.fbr = fbr;
    }

    public String getFbdw() {
        return fbdw;
    }

    public void setFbdw(String fbdw) {
        this.fbdw = fbdw;
    }

    public String getSpr() {
        return spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }

    public void insertDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("mid", Mission.this.getId());
        values.put("task_name", Mission.this.getTask_name());
        values.put("delivery_time", Mission.this.getDelivery_time());
        values.put("begin_time", Mission.this.getBegin_time());
        values.put("end_time", Mission.this.getEnd_time());
        values.put("publisher", Mission.this.getPublisher());
        values.put("task_type", Mission.this.getTask_type());
        values.put("AreaId", Mission.this.getAreaId());
        values.put("assignment", Mission.this.getAssignment());
        values.put("status", Mission.this.getStatus());
        values.put("task_content", Mission.this.getTask_content());
        values.put("contact", Mission.this.getContact());
        values.put("create_time", Mission.this.getCreate_time());
        values.put("approved_person", Mission.this.getApproved_person());
        values.put("approved_person_name", Mission.this.getApproved_person_name());
        values.put("typename", Mission.this.getTypename());
        values.put("receiver_name", Mission.this.getReceiver_name());
        values.put("publisher_name", Mission.this.getPublisher_name());
        values.put("approved_time", Mission.this.getApproved_time());
        values.put("receiver", Mission.this.getReceiver());
        values.put("execute_start_time", Mission.this.getExecute_start_time());
        values.put("execute_end_time", Mission.this.getExecute_end_time());
        values.put("spyj", Mission.this.getSpyj());
        values.put("rwqyms", Mission.this.getRwqyms());
        values.put("jjcd", Mission.this.getJjcd());
        values.put("rwly", Mission.this.getRwly());
        values.put("jsr", Mission.this.getJsr());
        values.put("jsdw", Mission.this.getJsdw());
        values.put("fbr", Mission.this.getFbr());
        values.put("fbdw", Mission.this.getFbdw());
        values.put("spr", Mission.this.getSpr());
        values.put("typeoftask", Mission.this.getTypeoftask());
        values.put("jsonStr", DataUtil.toJson(Mission.this));

        db.insert(LocalSqlite.MISSION_TABLE, values);


    }

    public void deleteDB(final LocalSqlite db) {

        db.delete(LocalSqlite.MISSION_TABLE, "mid = ?", new String[]{Mission.this.getId()});

    }

    public void updateDB(final LocalSqlite db) {

        ContentValues values = new ContentValues();
        values.put("task_name", Mission.this.getTask_name());
        values.put("delivery_time", Mission.this.getDelivery_time());
        values.put("begin_time", Mission.this.getBegin_time());
        values.put("end_time", Mission.this.getEnd_time());
        values.put("publisher", Mission.this.getPublisher());
        values.put("task_type", Mission.this.getTask_type());
        values.put("AreaId", Mission.this.getAreaId());
        values.put("assignment", Mission.this.getAssignment());
        values.put("status", Mission.this.getStatus());
        values.put("task_content", Mission.this.getTask_content());
        values.put("contact", Mission.this.getContact());
        values.put("create_time", Mission.this.getCreate_time());
        values.put("approved_person", Mission.this.getApproved_person());
        values.put("approved_person_name", Mission.this.getApproved_person_name());
        values.put("typename", Mission.this.getTypename());
        values.put("receiver_name", Mission.this.getReceiver_name());
        values.put("publisher_name", Mission.this.getPublisher_name());
        values.put("approved_time", Mission.this.getApproved_time());
        values.put("receiver", Mission.this.getReceiver());
        values.put("execute_start_time", Mission.this.getExecute_start_time());
        values.put("execute_end_time", Mission.this.getExecute_end_time());
        values.put("spyj", Mission.this.getSpyj());
        values.put("rwqyms", Mission.this.getRwqyms());
        values.put("jjcd", Mission.this.getJjcd());
        values.put("rwly", Mission.this.getRwly());
        values.put("jsr", Mission.this.getJsr());
        values.put("jsdw", Mission.this.getJsdw());
        values.put("fbr", Mission.this.getFbr());
        values.put("fbdw", Mission.this.getFbdw());
        values.put("spr", Mission.this.getSpr());
        values.put("typeoftask", Mission.this.getTypeoftask());
        values.put("jsonStr", DataUtil.toJson(Mission.this));

        db.update(LocalSqlite.MISSION_TABLE, values, "mid = ?", new String[]{Mission.this.getId()});

    }

    @Override
    public String toString() {
        return "Mission{" +
                "id='" + id + '\'' +
                ", task_name='" + task_name + '\'' +
                ", delivery_time=" + delivery_time +
                ", begin_time=" + begin_time +
                ", end_time=" + end_time +
                ", publisher='" + publisher + '\'' +
                ", task_type=" + task_type +
                ", AreaId='" + AreaId + '\'' +
                ", assignment='" + assignment + '\'' +
                ", status=" + status +
                ", task_content='" + task_content + '\'' +
                ", contact='" + contact + '\'' +
                ", create_time=" + create_time +
                ", approved_person='" + approved_person + '\'' +
                ", approved_person_name='" + approved_person_name + '\'' +
                ", typename='" + typename + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", publisher_name='" + publisher_name + '\'' +
                ", approved_time=" + approved_time +
                ", receiver='" + receiver + '\'' +
                ", execute_start_time=" + execute_start_time +
                ", execute_end_time=" + execute_end_time +
                ", spyj='" + spyj + '\'' +
                ", rwqyms='" + rwqyms + '\'' +
                ", jjcd='" + jjcd + '\'' +
                ", rwly='" + rwly + '\'' +
                ", jsr='" + jsr + '\'' +
                ", jsdw='" + jsdw + '\'' +
                ", fbr='" + fbr + '\'' +
                ", fbdw='" + fbdw + '\'' +
                ", spr='" + spr + '\'' +
                ", typeoftask='" + typeoftask + '\'' +
                ", members=" + members +
                '}';
    }
}

