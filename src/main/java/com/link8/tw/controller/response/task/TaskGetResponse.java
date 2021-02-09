package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.model.Task;
import com.link8.tw.tool.DateTool;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskGetResponse {

    private int id;

    private String name;

    private String status;

    private String type;

    private UserResponse assign;

    private UserResponse creator;

    private UserResponse dispatcher;

    private BaseResponse project;

    private String scheduleType;

    private List<Integer> scheduleDates;

    private String scheduleEndTime;

    private Integer estimateTime;

    private String estimateStartTime;

    private String estimateEndTime;

    private Integer time;

    private String startTime;

    private String endTime;

    private Integer point;

    private BaseUserResponse checkMan;

    private String context;

    private List<TaskSimpleResponse> sub;

    private List<TaskHistoryResponse> history;

    private List<BaseUserResponse> follow;

    public TaskGetResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.status = task.getStatus().name();
        this.type = task.getType().name();
        if (task.getAssign() != null) {
            this.assign = new UserResponse(task.getAssign());
        }
        this.creator = new UserResponse(task.getCreator());
        if(task.getDispatcher() != null) {
            this.dispatcher = new UserResponse(task.getDispatcher());
        }
        if (task.getProject() != null) {
            this.project = new BaseResponse(task.getProject());
        }
        if (task.getScheduleType() != null) {
            this.scheduleType = task.getScheduleType().name();
        }
        this.scheduleDates = task.getScheduleDates();
        this.scheduleEndTime = DateTool.getString(task.getScheduleEndTime(), DateTool.yyyy_MM_dd);
        this.estimateTime = task.getEstimateTime();
        this.estimateStartTime = DateTool.getString(task.getEstimateStartTime(), DateTool.yyyy_MM_dd);
        this.estimateEndTime = DateTool.getString(task.getEstimateEndTime(), DateTool.yyyy_MM_dd);
        this.startTime = DateTool.getString(task.getStartTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        this.endTime = DateTool.getString(task.getFinishTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        this.point = task.getPoint();
        if (task.getCheckMan() != null) {
            this.checkMan = new BaseUserResponse(task.getCheckMan());
        }
        this.context = task.getContext();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponse getAssign() {
        return assign;
    }

    public void setAssign(UserResponse assign) {
        this.assign = assign;
    }

    public UserResponse getCreator() {
        return creator;
    }

    public void setCreator(UserResponse creator) {
        this.creator = creator;
    }

    public UserResponse getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(UserResponse dispatcher) {
        this.dispatcher = dispatcher;
    }

    public BaseResponse getProject() {
        return project;
    }

    public void setProject(BaseResponse project) {
        this.project = project;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public List<Integer> getScheduleDates() {
        return scheduleDates;
    }

    public void setScheduleDates(List<Integer> scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public String getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(String scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public String getEstimateStartTime() {
        return estimateStartTime;
    }

    public void setEstimateStartTime(String estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public String getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(String estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public BaseUserResponse getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(BaseUserResponse checkMan) {
        this.checkMan = checkMan;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<TaskSimpleResponse> getSub() {
        return sub;
    }

    public void setSub(List<TaskSimpleResponse> sub) {
        this.sub = sub;
    }

    public List<TaskHistoryResponse> getHistory() {
        return history;
    }

    public void setHistory(List<TaskHistoryResponse> history) {
        this.history = history;
    }

    public List<BaseUserResponse> getFollow() {
        return follow;
    }

    public void setFollow(List<BaseUserResponse> follow) {
        this.follow = follow;
    }
}
