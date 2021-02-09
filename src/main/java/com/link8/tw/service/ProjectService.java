package com.link8.tw.service;

import com.link8.tw.controller.request.groups.GroupsAddRequest;
import com.link8.tw.controller.request.groups.GroupsEditRequest;
import com.link8.tw.controller.request.project.ProjectAddRequest;
import com.link8.tw.controller.request.project.ProjectEditRequest;
import com.link8.tw.controller.request.project.ProjectPageRequest;
import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.groups.GroupsGetResponse;
import com.link8.tw.controller.response.project.ProjectGetResponse;
import com.link8.tw.controller.response.project.ProjectLastResponse;
import com.link8.tw.controller.response.project.ProjectListResponse;
import com.link8.tw.controller.response.project.ProjectResponse;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.groups.GroupsExistException;
import com.link8.tw.exception.groups.GroupsNotFoundException;
import com.link8.tw.exception.project.ProjectExistException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.Groups;
import com.link8.tw.model.Project;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface ProjectService {

    Project checkProject(int id) throws ProjectNotFoundException;

    Groups checkGroups(int id) throws GroupsNotFoundException;

    List<BaseResponse> getProjectDropDown();

    boolean addProject(ProjectAddRequest request) throws GroupsExistException, UserNotFoundException, ProjectNotFoundException, ProjectExistException, ActionFailException;

    boolean editProject(ProjectEditRequest request) throws ProjectNotFoundException, UserNotFoundException, GroupsExistException, GroupsNotFoundException, ActionFailException;

    boolean removeProject(int id) throws ProjectNotFoundException, ActionFailException;

    ProjectGetResponse getProject(int id) throws ProjectNotFoundException;

    Page<ProjectListResponse> projectList(ProjectPageRequest request);

    boolean addGroups(GroupsAddRequest request) throws ProjectNotFoundException, GroupsExistException, ActionFailException;

    boolean editGroups(GroupsEditRequest request) throws GroupsNotFoundException, GroupsExistException, ActionFailException;

    boolean removeGroups(int id) throws GroupsNotFoundException;

    GroupsGetResponse getGroups(int id) throws GroupsNotFoundException;

    List<GroupsGetResponse> groupsList(int id) throws ProjectNotFoundException;

    List<BaseResponse> myProject() throws UserNotFoundException;

    List<Project> myProjectModel(String account) throws UserNotFoundException;

    List<ProjectLastResponse> changeTaskProjectList();

    List<BaseResponse> subscribeProjectList() throws UserNotFoundException;
}
