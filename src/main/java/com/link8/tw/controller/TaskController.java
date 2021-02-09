package com.link8.tw.controller;

import com.link8.tw.controller.advice.ErrorCodeException;
import com.link8.tw.controller.request.TaskPageRequest;
import com.link8.tw.controller.request.message.MessageAddRequest;
import com.link8.tw.controller.request.task.*;
import com.link8.tw.controller.response.message.MessageResponse;
import com.link8.tw.controller.response.task.*;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.PermissionDeniedException;
import com.link8.tw.exception.groups.GroupsNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.task.TaskNotFoundException;
import com.link8.tw.exception.trans.TransDateException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public TaskSimpleResponse addTask(@Valid @RequestBody TaskAddRequest request) throws ErrorCodeException {
        try {
            return new TaskSimpleResponse(taskService.addTask(request));
        } catch (TaskNotFoundException | UserNotFoundException | PermissionDeniedException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/edit")
    public boolean ediTask(@Valid @RequestBody TaskEditRequest request) throws ErrorCodeException {
        try {
            return taskService.ediTask(request);
        } catch (TaskNotFoundException | UserNotFoundException | ProjectNotFoundException | PermissionDeniedException | ActionFailException | TransDateException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/editName")
    public boolean editName(@Valid @RequestBody TaskEditNameRequest request) throws ErrorCodeException {
        try {
            return taskService.editName(request);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/edit/checkMan")
    public boolean editCheckMan(@Valid @RequestBody TaskCheckRequest request) throws ErrorCodeException {
        try {
            return taskService.editCheckMan(request);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }


    @PutMapping("/editPoint")
    public boolean editPoint(@Valid @RequestBody TaskEditPointRequest request) throws ErrorCodeException {
        try {
            return taskService.editPoint(request);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/assign")
    public boolean assignTask(@RequestBody TaskAssignRequest request) throws ErrorCodeException {
        try {
            return taskService.assignTask(request, false);
        } catch (TaskNotFoundException | UserNotFoundException | PermissionDeniedException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/changeProject")
    public boolean changeTaskProject(@RequestBody TaskChangeProjectRequest request) throws ErrorCodeException {
        try {
            return taskService.changeTaskProject(request, true);
        } catch (TaskNotFoundException | ProjectNotFoundException | PermissionDeniedException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/sort")
    public boolean sortTask(@Valid @RequestBody TaskSortRequest request) throws ErrorCodeException {
        try {
            return taskService.sortTask(request);
        } catch (TaskNotFoundException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @DeleteMapping("/remove")
    public boolean removeTask(@RequestParam Integer id) throws ErrorCodeException {
        try {
            return taskService.removeTask(id);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @DeleteMapping("/close")
    public boolean closeTask(@RequestParam Integer id) throws ErrorCodeException {
        try {
            return taskService.closeTask(id);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/closeList")
    public Page<TaskCloseResponse> closeList(@Valid TaskPageRequest request) {
        return taskService.closeList(request);
    }

    @PutMapping("/open")
    public boolean openTask(@RequestParam Integer id) throws ErrorCodeException {
        try {
            return taskService.openTask(id);
        } catch (TaskNotFoundException | UserNotFoundException | PermissionDeniedException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/getTask")
    public TaskGetResponse getTask(@RequestParam Integer id) throws ErrorCodeException {
        try {
            return taskService.getTask(id);
        } catch (TaskNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/list")
    public List<TaskListResponse> getTaskList(String keyword) {
        return taskService.getTaskList(keyword);
    }

    @PostMapping("/message/add")
    public boolean messageAdd(@RequestBody MessageAddRequest request) throws ErrorCodeException {
        try {
            return taskService.messageAdd(request);
        } catch (TaskNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/message/get/{id}")
    public List<MessageResponse> messageGet(@PathVariable int id) throws ErrorCodeException {
        try {
            return taskService.messageGet(id);
        } catch (TaskNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @DeleteMapping("/message/remove/{id}")
    public boolean messageRemove(@PathVariable int id) throws ErrorCodeException {
        try {
            return taskService.messageRemove(id);
        } catch (ActionFailException | PermissionDeniedException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/get/process")
    public TaskProcessResponse getProcess(@RequestParam String account) throws ErrorCodeException {
        try {
            return taskService.getProcess(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/get/followList")
    public List<TaskSocketResponse> getFollowList(String account) throws ErrorCodeException {
        try {
            return taskService.getFollowList(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/get/nobodyList")
    public List<TaskSocketResponse> getNobodyList(String account) throws ErrorCodeException {
        try {
            return taskService.taskBox(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/get/myList")
    public List<TaskSocketResponse> getMyList(String account) throws ErrorCodeException {
        try {
            return taskService.getMyList(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/get/finishList")
    public List<TaskSocketResponse> getFinishList(String account) throws ErrorCodeException {
        try {
            return taskService.getFinishList(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/start/{id}")
    public boolean startTask(@PathVariable Integer id) throws ErrorCodeException {
        try {
            return taskService.startTask(id);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/pause/{id}")
    public boolean pauseTask(@PathVariable Integer id) throws ErrorCodeException {
        try {
            return taskService.pauseTask(id, true);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/finish/{id}")
    public boolean finishTask(@PathVariable Integer id) throws ErrorCodeException {
        try {
            return taskService.finishTask(id);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/finishSub/{id}/{finish}")
    public boolean finishSubTask(@PathVariable Integer id ,@PathVariable boolean finish) throws ErrorCodeException {
        try {
            return taskService.finishSubTask(id,finish);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/cancelExecute/{id}")
    public boolean cancelExecute(@PathVariable Integer id) throws ErrorCodeException {
        try {
            return taskService.cancelExecute(id);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/follow")
    public boolean followTask(@RequestBody TaskTraceRequest request) throws ErrorCodeException {
        try {
            return taskService.followTask(request);
        } catch (TaskNotFoundException | UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @PutMapping("/read")
    public boolean readTask(@RequestParam Integer[] id) throws ErrorCodeException {
        try {
            return taskService.readTask(id);
        } catch (ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/follow/list/user")
    public List<TaskFollowResponse> getUserFollow(TaskUserFollowRequest request) throws ErrorCodeException {
        try {
            return taskService.getUserFollow(request);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/follow/list/project")
    public List<TaskProjectFollowResponse> getProjectFollow(TaskProjectFollowRequest request) throws ErrorCodeException {
        try {
            return taskService.getProjectFollow(request);
        } catch (UserNotFoundException | GroupsNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

    @GetMapping("/follow/detail/user/{account}")
    public TaskFollowResponse getUserFollowDetail(@PathVariable String account) throws ErrorCodeException {
        try {
            return taskService.getUserFollowDetail(account);
        } catch (UserNotFoundException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }

//    @GetMapping("/follow/detail/project/{account}")
//    public TaskFollowResponse getProjectFollowDetail(@PathVariable String account) throws ErrorCodeException {
//        try {
//            return taskService.getProjectFollowDetail(account);
//        } catch (UserNotFoundException e) {
//            throw new ErrorCodeException(e.getErrorCode());
//        }
//    }

    @GetMapping("/fix/dispatcher")
    public List<Integer> fixDispatcher() {
        return taskService.fixDispatcher();
    }

    @GetMapping("/fix/history")
    public void fixHistory() {
        taskService.fixHistory();
    }


    @PutMapping("/editSeverity")
    public boolean editSeverity(@Valid @RequestBody TaskEditSeverityRequest request) throws ErrorCodeException {
        try {
            return taskService.editSeverity(request);
        } catch (TaskNotFoundException | PermissionDeniedException | UserNotFoundException | ActionFailException e) {
            throw new ErrorCodeException(e.getErrorCode());
        }
    }
}
