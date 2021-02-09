package com.link8.tw.model;

import com.link8.tw.enums.TaskColumn;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "task_sort")
public class TaskSort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "sort")
    private int sort;

    @Column(name = "task_column")
    private TaskColumn column;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task")
    private Task task;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user")
    private User user;

    public TaskSort() {
    }

    public TaskSort(Task task, TaskColumn column ,User user) {
        this.task = task;
        this.column = column;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public TaskColumn getColumn() {
        return column;
    }

    public void setColumn(TaskColumn column) {
        this.column = column;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
