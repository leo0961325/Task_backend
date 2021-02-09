package com.link8.tw.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "context")
    private String context;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    @Column(name = "read_user")
    private String readUser;

    public Message() {
    }

    public Message(Task task, User user, String context) {
        this.context = context;
        this.time = LocalDateTime.now();
        this.user = user;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Set<String> getReadUser() {
        Set<String> result = new HashSet<>();
        if (this.readUser != null) {
            result.addAll(Arrays.asList(this.readUser.split(",")));
        }
        return result;
    }

    public void addReadUser(String account) {
        Set<String> temp = getReadUser();
        temp.add(account);
        setReadUser(temp);
    }

    public void setReadUser(Set<String> readUser) {
        this.readUser = readUser.stream().collect(Collectors.joining(","));
    }
}
