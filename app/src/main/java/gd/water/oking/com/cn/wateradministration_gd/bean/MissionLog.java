package gd.water.oking.com.cn.wateradministration_gd.bean;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhao on 2016/11/9.
 */

public class MissionLog implements Serializable {

    private String id;
    private String task_id;
    private String name;
    private String post;
    private String id_card;
    private String other_part;
    private int other_person;
    private String weather;
    private String equipment;
    private String addr;
    private String time;
    private int plan;
    private int item;
    private int type;
    private String area;
    private String route;
    private String patrol;
    private int status;
    private String deal;
    private String result;
    private String dzyj;

    private ArrayList<Uri> photoUriList = new ArrayList<>();
    private ArrayList<Uri> videoUriList = new ArrayList<>();
    private ArrayList<Member> members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getOther_part() {
        return other_part;
    }

    public void setOther_part(String other_part) {
        this.other_part = other_part;
    }

    public int getOther_person() {
        return other_person;
    }

    public void setOther_person(int other_person) {
        this.other_person = other_person;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPatrol() {
        return patrol;
    }

    public void setPatrol(String patrol) {
        this.patrol = patrol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDzyj() {
        return dzyj;
    }

    public void setDzyj(String dzyj) {
        this.dzyj = dzyj;
    }

    public ArrayList<Uri> getPhotoUriList() {
        return photoUriList;
    }

    public void setPhotoUriList(ArrayList<Uri> photoUriList) {
        this.photoUriList = photoUriList;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public ArrayList<Uri> getVideoUriList() {
        return videoUriList;
    }

    public void setVideoUriList(ArrayList<Uri> videoUriList) {
        this.videoUriList = videoUriList;
    }
}
