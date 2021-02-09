package com.link8.tw.controller;

import com.link8.tw.controller.advice.ErrorCodeException;
import com.link8.tw.controller.request.PageListRequest;
import com.link8.tw.controller.request.user.ProjectSubscribeRequest;
import com.link8.tw.controller.request.user.UserSubscribeRequest;
import com.link8.tw.controller.response.project.ProjectSubscribeResponse;
import com.link8.tw.controller.response.user.UserListResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.controller.response.user.UserSubscribeResponse;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.FileNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/list")
    public List<UserListResponse> getList() {
        return userService.getList();
    }

    @GetMapping("/list/recent")
    public List<UserResponse> getRecentList() {
        return userService.getRecentList();
    }

    @GetMapping("/subscribe/list/user")
    public Page<UserSubscribeResponse> getSubscribeUserList(PageListRequest request) {
        return userService.getSubscribeUserList(request);
    }

    @GetMapping("/subscribe/list/project")
    public List<ProjectSubscribeResponse> getSubscribeProjectList(PageListRequest request) {
        return userService.getSubscribeProjectList(request);
    }

    @PostMapping("/subscribe/user")
    public boolean subscribeUser(@RequestBody UserSubscribeRequest request) throws ErrorCodeException {
        try {
            return userService.subscribeUser(request);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PostMapping("/subscribe/project")
    public boolean subscribeUser(@RequestBody ProjectSubscribeRequest request) throws ErrorCodeException {
        try {
            return userService.subscribeProject(request);
        } catch (ProjectNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/getProjectUsers")
    public List<UserListResponse> getProjectUsers(Integer[] projectId) throws ErrorCodeException {
        try {
            return userService.getProjectUsers(projectId);
        } catch (ProjectNotFoundException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/image/{imageId}")
    public boolean userImage(@PathVariable int imageId) throws ErrorCodeException {
        try {
            return userService.userImage(imageId);
        } catch (FileNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }


}
