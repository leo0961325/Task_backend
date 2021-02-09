package com.link8.tw.controller.request.groups;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class GroupsAddRequest {

    @Min(value = 1)
    private Integer projectId;

    @NotEmpty
    private String name;

    private List<String> accounts;

    private String leader;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAccounts() {
        if(this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
