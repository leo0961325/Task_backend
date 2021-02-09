package com.link8.tw.model;


import com.tgfc.acl.response.UserResponse;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "account")
    private String account;

    @Column(name = "name")
    private String name;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "mail")
    private String mail;

    @ManyToOne
    @JoinColumn(name = "last_project")
    private Project lastProject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_follow",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id")})
    private Set<Task> follow;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_subscribe",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscribe_id")})
    private Set<User> subscribe;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_subscribe_project",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscribe_project_id")})
    private Set<Project> subscribeProject;

    @ManyToOne
    @JoinColumn(name = "img")
    private FileInfo img;

    public User() {
    }

    public User(UserResponse response) {
        this.englishName = response.getEnglishName();
        this.name = response.getName();
        this.account = response.getAccount();
        this.deptId = response.getGroup().getId();
        this.deptName = response.getGroup().getName();
        this.mail = response.getEmail();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Set<Task> getFollow() {
        if (this.follow == null) {
            this.follow = new HashSet<>();
        }
        return follow;
    }

    public void setFollow(Set<Task> follow) {
        this.follow = follow;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Project getLastProject() {
        return lastProject;
    }

    public void setLastProject(Project lastProject) {
        this.lastProject = lastProject;
    }

    public FileInfo getImg() {
        return img;
    }

    public void setImg(FileInfo img) {
        this.img = img;
    }

    public Set<User> getSubscribe() {
        if(this.subscribe == null) {
            this.subscribe = new HashSet<>();
        }
        return subscribe;
    }

    public void setSubscribe(Set<User> subscribe) {
        this.subscribe = subscribe;
    }

    public Set<Project> getSubscribeProject() {
        if(this.subscribeProject == null) {
            this.subscribeProject = new HashSet<>();
        }
        return subscribeProject;
    }

    public void setSubscribeProject(Set<Project> subscribeProject) {
        this.subscribeProject = subscribeProject;
    }
}
