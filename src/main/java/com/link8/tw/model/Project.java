package com.link8.tw.model;

import com.link8.tw.controller.response.project.ProjectResponse;
import com.link8.tw.enums.ProjectStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "dept")
    private String dept;

    @ManyToOne
    @JoinColumn(name = "manager")
    private User manager;

    @Column(name = "status")
    private ProjectStatus status;

    public Project() {
    }

    public Project(ProjectResponse response) {
        this.id = response.getProjectId();
        this.name = response.getName();
        this.dept = response.getDept();
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}
