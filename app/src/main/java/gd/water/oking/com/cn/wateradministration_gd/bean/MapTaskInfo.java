package gd.water.oking.com.cn.wateradministration_gd.bean;

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
                '}';
    }
}
