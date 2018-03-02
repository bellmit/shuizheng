package gd.water.oking.com.cn.wateradministration_gd.bean;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 */

public class MapTaskInfo {
    private String taskName;
    private String taskState;
    private String taskTime;
    private String taskPeleasePeople;
    private String taskApprover;
    private String taskDescription;
    private String taskAre;
    private LatLng taskLatLng;
    private LatLng moveCenterLatLng;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private ArrayList<LatLng>mPicLatLngs;
    private ArrayList<LatLng>mRecordScreenLatLngs;

    public LatLng getTaskLatLng() {
        return taskLatLng;
    }

    public void setTaskLatLng(LatLng taskLatLng) {
        this.taskLatLng = taskLatLng;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskPeleasePeople() {
        return taskPeleasePeople;
    }

    public void setTaskPeleasePeople(String taskPeleasePeople) {
        this.taskPeleasePeople = taskPeleasePeople;
    }

    public String getTaskApprover() {
        return taskApprover;
    }

    public void setTaskApprover(String taskApprover) {
        this.taskApprover = taskApprover;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskAre() {
        return taskAre;
    }

    public void setTaskAre(String taskAre) {
        this.taskAre = taskAre;
    }

    public LatLng getMoveCenterLatLng() {
        return moveCenterLatLng;
    }

    public void setMoveCenterLatLng(LatLng moveCenterLatLng) {
        this.moveCenterLatLng = moveCenterLatLng;
    }

    public LatLng getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(LatLng startLatLng) {
        this.startLatLng = startLatLng;
    }

    public LatLng getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(LatLng endLatLng) {
        this.endLatLng = endLatLng;
    }

    public ArrayList<LatLng> getPicLatLngs() {
        return mPicLatLngs;
    }

    public void setPicLatLngs(ArrayList<LatLng> picLatLngs) {
        mPicLatLngs = picLatLngs;
    }

    public ArrayList<LatLng> getRecordScreenLatLngs() {
        return mRecordScreenLatLngs;
    }

    public void setRecordScreenLatLngs(ArrayList<LatLng> recordScreenLatLngs) {
        mRecordScreenLatLngs = recordScreenLatLngs;
    }

    @Override
    public String toString() {
        return "MapTaskInfo{" +
                "taskName='" + taskName + '\'' +
                ", taskState='" + taskState + '\'' +
                ", taskTime='" + taskTime + '\'' +
                ", taskPeleasePeople='" + taskPeleasePeople + '\'' +
                ", taskApprover='" + taskApprover + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskAre='" + taskAre + '\'' +
                ", taskLatLng=" + taskLatLng +
                ", moveCenterLatLng=" + moveCenterLatLng +
                ", startLatLng=" + startLatLng +
                ", endLatLng=" + endLatLng +
                ", mPicLatLngs=" + mPicLatLngs +
                ", mRecordScreenLatLngs=" + mRecordScreenLatLngs +
                '}';
    }
}
