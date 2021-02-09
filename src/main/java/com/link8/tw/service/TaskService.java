package com.link8.tw.service;

import com.link8.tw.controller.request.TaskPageRequest;
import com.link8.tw.controller.request.message.MessageAddRequest;
import com.link8.tw.controller.request.point.PointDetailRequest;
import com.link8.tw.controller.request.point.PointListRequest;
import com.link8.tw.controller.request.task.*;
import com.link8.tw.controller.response.message.MessageResponse;
import com.link8.tw.controller.response.point.PersonalPointResponse;
import com.link8.tw.controller.response.point.PointDetailResponse;
import com.link8.tw.controller.response.point.TaskStatisticResponse;
import com.link8.tw.controller.response.task.*;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.PermissionDeniedException;
import com.link8.tw.exception.groups.GroupsNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.task.TaskNotFoundException;
import com.link8.tw.exception.trans.TransDateException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    Task addTask(TaskAddRequest request) throws TaskNotFoundException, UserNotFoundException, PermissionDeniedException;

    boolean ediTask(TaskEditRequest request) throws TaskNotFoundException, UserNotFoundException, ProjectNotFoundException, PermissionDeniedException, ActionFailException, TransDateException;

    boolean editName(TaskEditNameRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    boolean editPoint(TaskEditPointRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException;

    boolean editCheckMan(TaskCheckRequest request) throws TaskNotFoundException, UserNotFoundException, PermissionDeniedException;

    boolean removeTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    boolean closeTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    PersonalPointResponse getPersonalPoint();

    TaskGetResponse getTask(Integer id) throws TaskNotFoundException;

    boolean assignTask(TaskAssignRequest request, boolean add) throws UserNotFoundException, PermissionDeniedException, TaskNotFoundException;

    boolean changeTaskProject(TaskChangeProjectRequest request, boolean reload) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ProjectNotFoundException;

    List<TaskSocketResponse> getFollowList(String account) throws UserNotFoundException;

    List<TaskSocketResponse> taskBox(String account) throws UserNotFoundException;

    List<TaskSocketResponse> getMyList(String account, String from) throws UserNotFoundException;
    List<TaskSocketResponse> getMyList(String account) throws UserNotFoundException;

    List<TaskSocketResponse> getFinishList(String account) throws UserNotFoundException;

    Task checkTaskExist(Integer id) throws TaskNotFoundException;

    boolean startTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    boolean pauseTask(Integer id, boolean reload) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    boolean finishTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;

    TaskProcessResponse getProcess(String account) throws UserNotFoundException;

    Page<TaskStatisticResponse> getPointList(PointListRequest request) throws ProjectNotFoundException;

    boolean sortTask(TaskSortRequest request) throws TaskNotFoundException, UserNotFoundException;

    List<TaskFollowResponse> getUserFollow(TaskUserFollowRequest request) throws UserNotFoundException;

    boolean finishSubTask(int id,boolean finish) throws PermissionDeniedException, TaskNotFoundException, ActionFailException, UserNotFoundException;

    boolean cancelExecute(Integer id) throws TaskNotFoundException, PermissionDeniedException, ActionFailException, UserNotFoundException;

    boolean followTask(TaskTraceRequest request) throws TaskNotFoundException, UserNotFoundException;

    boolean openTask(Integer id) throws TaskNotFoundException, UserNotFoundException, PermissionDeniedException;

    Page<TaskCloseResponse> closeList(TaskPageRequest request);

    void addScheduleTask() throws UserNotFoundException;

    Page<PointDetailResponse> getPointDetail(PointDetailRequest request) throws UserNotFoundException, ProjectNotFoundException;

    List<Integer> fixDispatcher();

    boolean messageAdd(MessageAddRequest request) throws TaskNotFoundException;

    List<MessageResponse> messageGet(int id) throws TaskNotFoundException;

    boolean messageRemove(int id) throws ActionFailException, PermissionDeniedException;

    TaskFollowResponse getUserFollowDetail(String account) throws UserNotFoundException;

    void fixHistory();

    List<TaskListResponse> getTaskList(String keyword);

    boolean readTask(Integer[] id) throws ActionFailException;

    List<TaskProjectFollowResponse> getProjectFollow(TaskProjectFollowRequest request) throws UserNotFoundException, GroupsNotFoundException;

    boolean editSeverity(TaskEditSeverityRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException;
}
