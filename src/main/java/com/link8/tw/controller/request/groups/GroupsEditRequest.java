package com.link8.tw.controller.request.groups;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class GroupsEditRequest {

    @Min(value = 1)
    private int id;

    @NotEmpty
    private String name;

    private List<String> accounts;

    private String leader;

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

    public List<String> getAccounts() {
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
