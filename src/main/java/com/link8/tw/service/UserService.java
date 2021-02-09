package com.link8.tw.service;

import com.link8.tw.controller.request.PageListRequest;
import com.link8.tw.controller.request.user.ProjectSubscribeRequest;
import com.link8.tw.controller.request.user.UserSubscribeRequest;
import com.link8.tw.controller.response.project.ProjectSubscribeResponse;
import com.link8.tw.controller.response.user.UserAssignResponse;
import com.link8.tw.controller.response.user.UserListResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.controller.response.user.UserSubscribeResponse;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.FileNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.task.TaskNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User getLoginUser();

    List<UserListResponse> getList();

    User getUser(String account) throws UserNotFoundException;

    void removeFollow(Integer id) throws TaskNotFoundException, UserNotFoundException;

    List<User> getUserList(List<String> accounts);

    List<UserListResponse> getProjectUsers(Integer[] projectId) throws ProjectNotFoundException, UserNotFoundException;

    boolean userImage(int imageId) throws ActionFailException, FileNotFoundException;

    Page<UserSubscribeResponse> getSubscribeUserList(PageListRequest request);

    List<ProjectSubscribeResponse> getSubscribeProjectList(PageListRequest request);

    boolean subscribeUser(UserSubscribeRequest request) throws UserNotFoundException;

    List<UserResponse> getRecentList();

    boolean subscribeProject(ProjectSubscribeRequest request) throws ProjectNotFoundException;
}
