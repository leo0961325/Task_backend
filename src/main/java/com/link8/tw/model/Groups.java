package com.link8.tw.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;

    @ManyToMany
    @JoinTable(name = "groups_user", joinColumns = {@JoinColumn(name = "groups_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "leader")
    private User leader;

    public Groups() {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<User> getUsers() {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }
}
