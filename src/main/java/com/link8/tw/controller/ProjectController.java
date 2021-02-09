package com.link8.tw.controller;

import com.link8.tw.controller.advice.ErrorCodeException;
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
import com.link8.tw.service.ProjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ProjectController {
    private final static Log LOG = LogFactory.getLog(PointController.class);

    @Autowired
    ProjectService projectService;

    @PostMapping("/project/add")
    public boolean addProject(@Valid @RequestBody ProjectAddRequest request) throws ErrorCodeException {
        try {
            return projectService.addProject(request);
        } catch (ProjectNotFoundException | GroupsExistException | UserNotFoundException | ProjectExistException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/project/edit")
    public boolean editProject(@Valid @RequestBody ProjectEditRequest request) throws ErrorCodeException {
        try {
            return projectService.editProject(request);
        } catch (ProjectNotFoundException | UserNotFoundException | GroupsExistException | GroupsNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @DeleteMapping("/project/remove/{id}")
    public boolean removeProject(@PathVariable int id) throws ErrorCodeException {
        try {
            return projectService.removeProject(id);
        } catch (ProjectNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/project/get/{id}")
    public ProjectGetResponse getProject(@PathVariable int id) throws ErrorCodeException {
        try {
            return projectService.getProject(id);
        } catch (ProjectNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/project/list")
    public Page<ProjectListResponse> projectList(@Valid ProjectPageRequest request) {
        return projectService.projectList(request);
    }

    @GetMapping("/project/dropDownList")
    public List<BaseResponse> dropDownList() {
        return projectService.getProjectDropDown();
    }

    @GetMapping("/project/mine")
    public List<BaseResponse> myProject() throws ErrorCodeException {
        try {
            return projectService.myProject();
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/project/subscribe")
    public List<BaseResponse> subscribeProjectList() throws ErrorCodeException {
        try {
            return projectService.subscribeProjectList();
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/project/change/list")
    public List<ProjectLastResponse> changeTaskProjectList() {
        return projectService.changeTaskProjectList();
    }

    @PostMapping("/groups/add")
    public boolean addGroups(@Valid @RequestBody GroupsAddRequest request) throws ErrorCodeException {
        try {
            return projectService.addGroups(request);
        } catch (ProjectNotFoundException | GroupsExistException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/groups/edit")
    public boolean editGroups(@Valid @RequestBody GroupsEditRequest request) throws ErrorCodeException {
        try {
            return projectService.editGroups(request);
        } catch (GroupsNotFoundException | GroupsExistException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @DeleteMapping("/groups/remove/{id}")
    public boolean removeGroups(@PathVariable int id) throws ErrorCodeException {
        try {
            return projectService.removeGroups(id);
        } catch (GroupsNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/groups/get/{id}")
    public GroupsGetResponse getGroups(@PathVariable int id) throws ErrorCodeException {
        try {
            return projectService.getGroups(id);
        } catch (GroupsNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/groups/list/{id}")
    public List<GroupsGetResponse> groupsList(@PathVariable int id) throws ErrorCodeException {
        try {
            return projectService.groupsList(id);
        } catch (ProjectNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }


}
