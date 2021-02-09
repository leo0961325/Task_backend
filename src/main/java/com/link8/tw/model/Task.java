package com.link8.tw.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.link8.tw.enums.ScheduleType;
import com.link8.tw.enums.Severity;
import com.link8.tw.enums.Status;
import com.link8.tw.enums.Type;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "context")
    private String context;

    @Column(name = "type")
    private Type type;

    @Column(name = "estimateTime")
    private Integer estimateTime;

    @Column(name = "estimateStartTime")
    private LocalDate estimateStartTime;

    @Column(name = "estimateEndTime")
    private LocalDate estimateEndTime;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column(name = "startTime")
    private LocalDateTime startTime;

    @Column(name = "finishTime")
    private LocalDateTime finishTime;

    @Column(name = "point")
    private Integer point;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project")
    private Project project;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "assign")
    private User assign;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "dispatcher")
    private User dispatcher;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "creator")
    private User creator;

    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent")
    private Task parent;

//    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "parent", fetch = FetchType.LAZY)
//    @JsonBackReference
//    private Set<Task> sub;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", fetch = FetchType.LAZY)
    private Set<TaskSort> sortList;

    @Column(name = "schedule_dates")
    private String scheduleDates;

    @Column(name = "schedule_type")
    private ScheduleType scheduleType;

    @Column(name = "schedule_end_time")
    private LocalDate scheduleEndTime;

    @ManyToOne
    @JoinColumn(name = "check_man")
    private User checkMan;

    @NotNull
    @Column(name = "top")
    private boolean top;

    @NotNull
    @Column(name = "close")
    private boolean close;

    @NotNull
    @Column(name = "read")
    private boolean read;

    @NotNull
    @Column(name = "severity")
    private Severity severity;

    public Task() {
    }

    public Task(String name) {
        this.name = name;
        this.status = Status.START_NOT_YET;
        this.type = Type.TASK;
        this.createTime = LocalDateTime.now();
        this.severity = Severity.GENERAL;
    }

    public Task(Task task) {
        this.name = "例行任務 : " + task.getName();
        this.context = task.getContext();
        this.type = Type.SCHEDULE;
        this.createTime = LocalDateTime.now();
        this.estimateEndTime = LocalDate.now();
        this.estimateStartTime = LocalDate.now();
        this.point = task.getPoint();
        this.project = task.getProject();
        this.assign = task.getAssign();
        this.creator = task.getCreator();
        this.dispatcher = task.getDispatcher();
        this.status = Status.START_NOT_YET;
        this.read = false;
        this.severity = Severity.GENERAL;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocalDate getEstimateStartTime() {
        return estimateStartTime;
    }

    public void setEstimateStartTime(LocalDate estimateStartTime) {
        this.estimateStartTime = estimateStartTime;
    }

    public LocalDate getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(LocalDate estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssign() {
        return assign;
    }

    public void setAssign(User assign) {
        this.assign = assign;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

//    public Set<Task> getSub() {
//        if (this.sub == null) {
//            this.sub = new HashSet<>();
//        }
//        return sub;
//    }
//
//    public void setSub(Set<Task> sub) {
//        this.sub = sub;
//    }

    public Set<TaskSort> getSortList() {
        if (this.sortList == null) {
            this.sortList = new HashSet<>();
        }
        return sortList;
    }

    public void setSortList(Set<TaskSort> sortList) {
        this.sortList = sortList;
    }

    public List<Integer> getScheduleDates() {
        List<Integer> result = new ArrayList<>();
        int val = 0;
        if (this.scheduleDates != null) {
            for (String field : this.scheduleDates.split(",")) {
                try {
                    val = Integer.parseInt(field);
                }
                // If the String contains other thing that digits and commas
                catch (NumberFormatException e) {
                }
                result.add(val);
            }
        }
        return result;
    }

    public void setScheduleDates(List<Integer> scheduleDates) {
        String dateString = scheduleDates.stream().map(String::valueOf).collect(joining(","));
        this.scheduleDates = dateString;
    }


    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public LocalDate getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(LocalDate scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public User getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(User checkMan) {
        this.checkMan = checkMan;
    }

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public User getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(User dispatcher) {
        this.dispatcher = dispatcher;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
