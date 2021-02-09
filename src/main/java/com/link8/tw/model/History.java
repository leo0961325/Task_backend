package com.link8.tw.model;

import com.link8.tw.enums.Action;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "executor")
    private User executor;

    @Column(name = "action")
    private Action action;

    @Column(name = "context")
    private String context;

    @Lob
    @Column(name = "change_item")
    private String changeItem;

    @Column(name = "time")
    private LocalDateTime time;

    public History() {
    }

    public History(Task task, User executor, Action action, String changeItem,String context) {
        this.task = task;
        this.executor = executor;
        this.action = action;
        this.changeItem = changeItem;
        this.time = LocalDateTime.now();
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getChangeItem() {
        return changeItem;
    }

    public void setChangeItem(String changeItem) {
        this.changeItem = changeItem;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
