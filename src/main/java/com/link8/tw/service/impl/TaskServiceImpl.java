package com.link8.tw.service.impl;

import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.link8.tw.controller.errorCode.ActionErrorCode;
import com.link8.tw.controller.errorCode.MessageErrorCode;
import com.link8.tw.controller.errorCode.PermissionErrorCode;
import com.link8.tw.controller.errorCode.TaskErrorCode;
import com.link8.tw.controller.request.TaskPageRequest;
import com.link8.tw.controller.request.message.MessageAddRequest;
import com.link8.tw.controller.request.point.PointDetailRequest;
import com.link8.tw.controller.request.point.PointListRequest;
import com.link8.tw.controller.request.task.*;
import com.link8.tw.controller.response.BaseResponse;
import com.link8.tw.controller.response.groups.GroupsGetResponse;
import com.link8.tw.controller.response.message.MessageResponse;
import com.link8.tw.controller.response.message.NotifyResponse;
import com.link8.tw.controller.response.point.PersonalPointResponse;
import com.link8.tw.controller.response.point.PointDetailResponse;
import com.link8.tw.controller.response.point.TaskStatisticResponse;
import com.link8.tw.controller.response.project.ProjectFollowResponse;
import com.link8.tw.controller.response.task.*;
import com.link8.tw.controller.response.user.BaseUserResponse;
import com.link8.tw.enums.*;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.PermissionDeniedException;
import com.link8.tw.exception.groups.GroupsNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.task.TaskNotFoundException;
import com.link8.tw.exception.trans.TransDateException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.*;
import com.link8.tw.repository.*;
import com.link8.tw.service.JetService;
import com.link8.tw.service.ProjectService;
import com.link8.tw.service.TaskService;
import com.link8.tw.service.UserService;
import com.link8.tw.socket.MessageEventHandler;
import com.link8.tw.tool.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TaskTimeRepository taskTimeRepository;

    @Autowired
    TaskSortRepository taskSortRepository;

    @Autowired
    MessageEventHandler messageEventHandler;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    GroupsRepository groupsRepository;

    @Autowired
    MessageRepository messageRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Task addTask(TaskAddRequest request) throws TaskNotFoundException, UserNotFoundException, PermissionDeniedException {

        Task task = new Task(request.getName());
        User login = userService.getLoginUser();
        task.setCreator(login);
        if (request.getParent() != null) {
            Task parent = checkTaskExist(request.getParent());
            task.setParent(parent);
            if (request.isCheck()) {
                task.setType(Type.CHECK);
            } else {
                task.setType(Type.SUB_TASK);
            }
        }
        taskRepository.save(task);
        history(task, login, Action.ADD, null, null);
        assignTask(new TaskAssignRequest(Arrays.asList(task.getId()), request.getAssign(), false), true);
        if (task.getParent() != null) {
            checkFollow(task.getParent(), SocketType.REPLACE);
        }
        return task;
    }

    @Override
    public Task checkTaskExist(Integer id) throws TaskNotFoundException {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new TaskNotFoundException(TaskErrorCode.TASK_NOT_FOUND);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ediTask(TaskEditRequest request) throws TaskNotFoundException, UserNotFoundException, ProjectNotFoundException, PermissionDeniedException, ActionFailException, TransDateException {
        Task task = checkTaskExist(request.getId());
        if (task.getStatus() == Status.FINISH) {
            throw new ActionFailException(ActionErrorCode.TASK_FINISH_CAN_NOT_EDIT);
        }
        Project project = request.getProjectId() == null ? null : projectService.checkProject(request.getProjectId());
        List<TaskEditResponse> changeItem = getChangeItem(task, request, project);
        User login = checkAssignOrCreator(task);
        task.setName(request.getName());
        task.setContext(request.getContext());
        task.setEstimateTime(request.getEstimateTime());
        task.setEstimateStartTime(request.getEstimateStartTime());
        task.setEstimateEndTime(request.getEstimateEndTime());
        task.setPoint(request.getPoint());
        if (request.getCheckMan() != null) {
            task.setCheckMan(userService.getUser(request.getCheckMan()));
        }
        if (request.getProjectId() != null) {
            changeTaskProject(new TaskChangeProjectRequest(Arrays.asList(task.getId()), request.getProjectId()), false);
        }
        if (request.getSchedule() != null && request.getSchedule().getScheduleType() != null) {
            task.setScheduleType(request.getSchedule().getScheduleType());
            task.setScheduleDates(request.getSchedule().getDates());
            LocalDate endTime = request.getSchedule().getEndDate() == null ? null : DateTool.getTime(request.getSchedule().getEndDate(), DateTool.yyyy_MM_dd);
            task.setScheduleEndTime(endTime);
            task.setType(Type.SCHEDULE);
        }
        if (task.getAssign() != null) {
            if (request.getAssign() == null) {
                assignTask(new TaskAssignRequest(Arrays.asList(request.getId()), request.getAssign(), true), false);
            }
            if (!task.getAssign().getAccount().equals(request.getAssign())) {
                assignTask(new TaskAssignRequest(Arrays.asList(request.getId()), request.getAssign(), true), false);
            }
        } else {
            if (request.getAssign() != null) {
                assignTask(new TaskAssignRequest(Arrays.asList(request.getId()), request.getAssign(), true), false);
            }
        }
        checkFollow(task, SocketType.REPLACE);
        if (task.getParent() != null) socketTaskReplace(task.getParent());
        socketTaskReplace(task);
        history(task, login, Action.EDIT, new Gson().toJson(changeItem), null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editName(TaskEditNameRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        Task task = checkTaskExist(request.getId());
        if (task.getStatus() == Status.FINISH) {
            throw new ActionFailException(ActionErrorCode.TASK_FINISH_CAN_NOT_EDIT);
        }
        User login = checkAssignOrCreator(task);
        TaskEditResponse changeItem = new TaskEditResponse("name", task.getName(), request.getName());
        task.setName(request.getName());
        checkFollow(task, SocketType.REPLACE);
        if (task.getParent() != null && task.getParent().getId() > 0) {
            socketTaskReplace(task.getParent());
        }
        socketTaskReplace(task);
        history(task, login, Action.EDIT, new Gson().toJson(Arrays.asList(changeItem)), null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editPoint(TaskEditPointRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException {
        Task task = checkTaskExist(request.getId());
        User login = pointAuthority(task);
        TaskEditResponse changeItem = new TaskEditResponse("point", String.valueOf(task.getPoint()), String.valueOf(request.getPoint()));
        task.setPoint(request.getPoint());
        history(task, login, Action.EDIT, new Gson().toJson(Arrays.asList(changeItem)), null);
        socketTaskReplace(task);
        checkFollow(task, SocketType.REPLACE);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editCheckMan(TaskCheckRequest request) throws TaskNotFoundException, UserNotFoundException, PermissionDeniedException {
        Task task = checkTaskExist(request.getId());
        User login = pointAuthority(task);
        User user = userService.getUser(request.getAccount());
        TaskEditResponse changeItem = getChangeCheckManItem(task, request.getAccount());
        task.setCheckMan(user);
        history(task, login, Action.EDIT, new Gson().toJson(Arrays.asList(changeItem)), null);
        if (task.getStatus() == Status.FINISH) {
            addCheckTask(task);
        }
        return true;
    }

    private TaskEditResponse getChangeAssignItem(Task task, String assign) {
        if (task.getAssign() == null) {
            if (assign != null) {
                return new TaskEditResponse("assign", "", assign);
            }
        } else {
            if (assign == null) {
                return new TaskEditResponse("assign", task.getAssign().getAccount(), "");
            }
            if (!assign.equals(task.getAssign().getAccount())) {
                return new TaskEditResponse("assign", task.getAssign().getAccount(), assign);
            }
        }
        return null;
    }

    private TaskEditResponse getChangeCheckManItem(Task task, String checkMan) {
        if (task.getCheckMan() == null) {
            if (checkMan != null) {
                return new TaskEditResponse("checkMan", "", checkMan);
            }
        } else {
            if (checkMan == null) {
                return new TaskEditResponse("checkMan", task.getAssign().getAccount(), "");
            }
            if (!checkMan.equals(task.getAssign().getAccount())) {
                return new TaskEditResponse("checkMan", task.getAssign().getAccount(), checkMan);
            }
        }
        return null;
    }

    private TaskEditResponse getChangeProjectItem(Task task, Project project) {
        if (task.getProject() == null) {
            if (project != null) {
                return new TaskEditResponse("project", "", project.getName());
            }
        } else {
            if (project == null) {
                return new TaskEditResponse("project", task.getProject().getName(), "");
            }
            if (project.getId() != task.getProject().getId()) {
                return new TaskEditResponse("project", task.getProject().getName(), project.getName());
            }
        }
        return null;
    }

    private List<TaskEditResponse> getChangeItem(Task task, TaskEditRequest request, Project project) {
        List<TaskEditResponse> result = new ArrayList<>();
        if (!request.getName().equals(task.getName())) {
            result.add(new TaskEditResponse("name", task.getName(), request.getName()));
        }
        if (request.getContext() == null) {
            if (task.getContext() != null) {
                result.add(new TaskEditResponse("context", task.getContext(), ""));
            }
        } else {
            if (!request.getContext().equals(task.getContext())) {
                result.add(new TaskEditResponse("context", task.getContext(), request.getContext()));
            }
        }
        TaskEditResponse changeAssignItem = getChangeAssignItem(task, request.getAssign());
        if (changeAssignItem != null) {
            result.add(changeAssignItem);
        }
        TaskEditResponse changeProjectItem = getChangeProjectItem(task, project);
        if (changeProjectItem != null) {
            result.add(changeProjectItem);
        }

        if (request.getEstimateTime() == null) {
            if (task.getEstimateTime() != null) {
                result.add(new TaskEditResponse("e_time", String.valueOf(task.getEstimateTime()), ""));
            }
        } else {
            if (!request.getEstimateTime().equals(task.getEstimateTime())) {
                result.add(new TaskEditResponse("e_time", String.valueOf(task.getEstimateTime()), String.valueOf(request.getEstimateTime())));
            }
        }

        if (request.getEstimateStartTime() == null) {
            if (task.getEstimateStartTime() != null) {
                result.add(new TaskEditResponse("e_startTime", DateTool.getString(task.getEstimateStartTime(), DateTool.yyyy_MM_dd), ""));
            }
        } else {
            if (!request.getEstimateStartTime().equals(task.getEstimateStartTime())) {
                result.add(new TaskEditResponse("e_startTime", DateTool.getString(task.getEstimateStartTime(), DateTool.yyyy_MM_dd), DateTool.getString(request.getEstimateStartTime(), DateTool.yyyy_MM_dd)));
            }
        }

        if (request.getEstimateEndTime() == null) {
            if (task.getEstimateEndTime() != null) {
                result.add(new TaskEditResponse("e_finishTime", DateTool.getString(task.getEstimateEndTime(), DateTool.yyyy_MM_dd), ""));
            }
        } else {
            if (!request.getEstimateEndTime().equals(task.getEstimateEndTime())) {
                result.add(new TaskEditResponse("e_finishTime", DateTool.getString(task.getEstimateEndTime(), DateTool.yyyy_MM_dd), DateTool.getString(request.getEstimateEndTime(), DateTool.yyyy_MM_dd)));
            }
        }

        if (request.getPoint() == null) {
            if (task.getPoint() != null) {
                result.add(new TaskEditResponse("point", String.valueOf(task.getPoint()), ""));
            }
        } else {
            if (!request.getPoint().equals(task.getPoint())) {
                result.add(new TaskEditResponse("point", String.valueOf(task.getPoint()), String.valueOf(request.getPoint())));
            }
        }
        TaskEditResponse changeCheckManItem = getChangeCheckManItem(task, request.getCheckMan());
        if (changeCheckManItem != null) {
            result.add(changeAssignItem);
        }
        return result;
    }

    private SocketUserColumn getUserAndColumn(Task task) {

        String user;
        TaskColumn column;
        if (task.getAssign() == null) {
            user = task.getCreator().getAccount();
            column = TaskColumn.WAIT_DEAL;
        } else {
            user = task.getAssign().getAccount();

            if (task.getStatus() == Status.PROCESSING || task.isTop()) {
                column = TaskColumn.DOING;
            } else if (task.getStatus() == Status.FINISH) {
                column = TaskColumn.FINISHED;
            } else {
                if (task.isRead() || task.getCreator().getAccount().equals(user)) {
                    column = TaskColumn.WAIT_DEAL;
                } else {
                    column = TaskColumn.TASK_BOX;
                }
            }
        }
        return new SocketUserColumn(user, column);
    }

    private void reloadDoing(String account) throws UserNotFoundException {
        messageEventHandler.sendTaskToUser(account, TaskColumn.DOING, new Gson().toJson(getProcess(account)));
    }

    private void socketTaskAdd(Task task,boolean add) {
        User login = userService.getLoginUser();
        SocketUserColumn temp = getUserAndColumn(task);
        if (temp.getColumn() == TaskColumn.DOING) {
            return;
        }
        if (temp.getColumn() == TaskColumn.WAIT_DEAL || temp.getColumn() == TaskColumn.TRACE) {
            addSortTask(task, temp.getColumn(), 0, login);
        }
        TaskSocketResponse taskResponse = TaskToResponse(Arrays.asList(task), login).get(0);
        if(add && temp.getColumn() == TaskColumn.WAIT_DEAL && task.getCreator().getAccount().equals(temp.getUser())) {
            SocketTaskResponse result = new SocketTaskResponse(SocketType.NEW_ADD, taskResponse);
            String message = new Gson().toJson(result);
            messageEventHandler.sendTaskToUser(task.getCreator().getAccount(), temp.getColumn(), message);
        } else {
            SocketTaskResponse result = new SocketTaskResponse(SocketType.ADD, taskResponse);
            String message = new Gson().toJson(result);
            messageEventHandler.sendTaskToUser(temp.getUser(), temp.getColumn(), message);
        }
    }

    private void socketTaskListAdd(List<Task> tasks) {
        for(Task dr : tasks) {
            socketTaskAdd(dr,false);
        }
    }

    private void socketTaskReplace(Task task) throws UserNotFoundException {
        SocketUserColumn temp = getUserAndColumn(task);
        if(temp.getColumn() == TaskColumn.DOING) {
            reloadDoing(temp.getUser());
            return;
        }
        TaskSocketResponse taskResponse = TaskToResponse(Arrays.asList(task), userService.getLoginUser()).get(0);
        SocketTaskResponse result = new SocketTaskResponse(SocketType.REPLACE, taskResponse);
        String message = new Gson().toJson(result);
        messageEventHandler.sendTaskToUser(temp.getUser(), temp.getColumn(), message);
    }

    private void socketTaskListReplace(List<Task> tasks) throws UserNotFoundException {
        for (Task task : tasks) {
            socketTaskReplace(task);
        }
    }

    private void socketTaskRemove(Task task) {
        SocketUserColumn temp = getUserAndColumn(task);
        if (temp.getColumn() == TaskColumn.WAIT_DEAL || temp.getColumn() == TaskColumn.TRACE) {
            removeSortTask(task, temp.getColumn(), userService.getLoginUser());
        }
        TaskSocketResponse taskResponse = new TaskSocketResponse();
        taskResponse.setId(task.getId());
        SocketTaskResponse result = new SocketTaskResponse(SocketType.REMOVE, taskResponse);
        String message = new Gson().toJson(result);
        messageEventHandler.sendTaskToUser(temp.getUser(), temp.getColumn(), message);
    }

    private void socketTaskListRemove(List<Task> tasks) {
        tasks.forEach(this::socketTaskRemove);
    }


    private void checkFollowTasks(List<Task> tasks, SocketType type) {
        tasks.forEach(e -> checkFollow(e, type));
    }

    private void sendTraceSocketMessage(User user) {
        List<TaskSocketResponse> taskResponse = TaskToResponse(putTaskSort(new ArrayList<>(user.getFollow()).stream().filter(e -> !e.isClose()).collect(Collectors.toList()), user, TaskColumn.TRACE), user);
        SocketTaskResponse result = new SocketTaskResponse(SocketType.FOLLOW_LIST, taskResponse);
        String message = new Gson().toJson(result);
            messageEventHandler.sendTaskToUser(user.getAccount(), TaskColumn.TRACE, message);
    }

    private void checkFollow(Task task, SocketType type) {
        List<User> follower = userRepository.findAllByFollowContains(task);
        List<User> parentFollower = new ArrayList<>();
        if(task.getParent() != null)
            parentFollower = userRepository.findAllByFollowContains(task.getParent());
        List<User> notRepeatFollowers = new ArrayList<>();
        for (int i = 0; i < follower.size() + parentFollower.size(); i++) {
            final User f;
            if (i < follower.size()) {
                f = follower.get(i);
            } else {
                f = parentFollower.get(i - follower.size());
            }

            if (notRepeatFollowers.stream().noneMatch(u -> u.getAccount().equals(f.getAccount()))) {
                notRepeatFollowers.add(f);
            }
        }
        //上面取出父子任務的人，並去重刷新，防止一次刷兩次
        for (User dr : notRepeatFollowers) {
            User self = userService.getLoginUser();
            if (type.equals(SocketType.REMOVE) && follower.stream().noneMatch(f -> f.getAccount().equals(self.getAccount()))) {
                sendTraceSocketMessage(self);
            }
            sendTraceSocketMessage(dr);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        Task task = checkTaskExist(id);
        if (task.getStatus() == Status.FINISH) {
            throw new ActionFailException(ActionErrorCode.TASK_FINISH_CAN_NOT_REMOVE);
        }
        userService.removeFollow(id);
        checkAssignOrCreator(task);
        task.getSortList().clear();
        List<Task> sub = taskRepository.findAllByParent(task);
        for (Task dr : sub) {
            removeTask(dr.getId());
        }
        historyRepository.deleteAllByTask(task);
        taskSortRepository.deleteAllByTask(task);
        taskTimeRepository.deleteAllByTask(task);
        messageRepository.deleteAllByTask(task);
        taskRepository.deleteById(task.getId());
        checkFollow(task, SocketType.REMOVE);
        socketTaskRemove(task);
        if (task.getParent() != null) {
            socketTaskReplace(task.getParent());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        Task task = checkTaskExist(id);
        checkUser(task);
        if (task.getStatus() == Status.PROCESSING) {
            pauseTask(id, false);
        }
        task.setClose(true);
        socketTaskRemove(task);
        if (task.getParent() != null) {
            socketTaskReplace(task);
        }
        checkFollow(task, SocketType.REMOVE);
        history(task, userService.getLoginUser(), Action.CLOSE, null, null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean openTask(Integer id) throws TaskNotFoundException, PermissionDeniedException {
        Task task = checkTaskExist(id);
        checkCreator(task);
        task.setClose(false);
        socketTaskAdd(task,false);
        checkFollow(task, SocketType.ADD);
        history(task, userService.getLoginUser(), Action.OPEN, null, null);
        return true;
    }

    @Override
    public Page<TaskCloseResponse> closeList(TaskPageRequest request) {
        Pageable pageable = PageRequest.of(request.getNumber(), request.getSize());
        List<Task> tasks = selectTask(request, taskRepository.findAllByClose(true));
        int start = (int) pageable.getOffset();
        int end = (Math.min(start + pageable.getPageSize(), tasks.size()));
        List<TaskCloseResponse> result = tasks.subList(start, end).stream().map(TaskCloseResponse::new).collect(Collectors.toList());
        return new PageImpl<>(result, pageable, tasks.size());
    }

    private List<Task> selectTask(TaskPageRequest request, List<Task> tasks) {
        if (request.getProjectId().size() != 0) {
            tasks = tasks.stream().filter(e -> e.getProject() != null && request.getProjectId().contains(e.getProject().getId())).collect(Collectors.toList());
        }
        if (request.getAccount().size() != 0) {
            tasks = tasks.stream().filter(e -> e.getAssign() != null && request.getAccount().contains(e.getAssign().getAccount())).collect(Collectors.toList());
        }
        if (request.getKeyword() != null) {
            String traditional = ZhConverterUtil.convertToTraditional(request.getKeyword()).toUpperCase();
            String simple = ZhConverterUtil.convertToSimple(request.getKeyword()).toUpperCase();
            tasks = tasks.stream().filter(e -> e.getName().toUpperCase().contains(traditional) ||
                    e.getName().toUpperCase().contains(simple)).collect(Collectors.toList());
        }
        if (request.getSortKey().equals("id")) {
            tasks = tasks.stream().sorted(Comparator.comparing(Task::getId)).collect(Collectors.toList());
        }
        if (request.getSortKey().equals("assign")) {
            List<Task> temp = tasks.stream().filter(e -> e.getAssign() == null).sorted(Comparator.comparing(Task::getId)).collect(Collectors.toList());
            temp.addAll(tasks.stream().filter(e -> e.getAssign() != null).sorted(Comparator.comparing(e -> e.getAssign().getAccount())).collect(Collectors.toList()));
            tasks = temp;
        }
        if (request.getOrder().equals("descend")) {
            Collections.reverse(tasks);
        }
        return tasks;
    }

    @Override
    public PersonalPointResponse getPersonalPoint() {
        User user = userService.getLoginUser();
        LocalDateTime lastMonthFirstDay = LocalDateTime.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime lastMonthLastDay = LocalDateTime.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime thisMonthFirstDay = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime today = LocalDateTime.now();

        List<Task> lastMonthTaskList = taskRepository.findAllByAssignAndFinishTimeIsBetween(user, lastMonthFirstDay, lastMonthLastDay);
        List<Task> thisMonthTaskList = taskRepository.findAllByAssignAndFinishTimeIsBetween(user, thisMonthFirstDay, today);
        int lastMonthScore = lastMonthTaskList.stream().filter(e -> e.getPoint() != null).mapToInt(Task::getPoint).sum();
        int thisMonthScore = thisMonthTaskList.stream().filter(e -> e.getPoint() != null).mapToInt(Task::getPoint).sum();
        PersonalPointResponse response = new PersonalPointResponse();
        response.setLastMonthScore(lastMonthScore);
        response.setThisMonthScore(thisMonthScore);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskGetResponse getTask(Integer id) throws TaskNotFoundException {
        Task task = checkTaskExist(id);
        TaskGetResponse result = new TaskGetResponse(task);
        result.setSub(taskRepository.findAllByParent(task).stream().sorted(Comparator.comparing(Task::getId)).map(TaskSimpleResponse::new).collect(Collectors.toList()));
        Integer time = (int) (getTime(task) / 60);
        result.setTime(time);
        result.setHistory(getHistoryList(task));
        List<User> follow = userRepository.findAllByFollowContains(task);
        result.setFollow(follow.stream().map(BaseUserResponse::new).collect(Collectors.toList()));
        return result;
    }

    private List<TaskHistoryResponse> getHistoryList(Task task) {
        List<History> histories = historyRepository.findAllByTask(task);
        return histories.stream().map(TaskHistoryResponse::new).collect(Collectors.toList());
    }

    private long getTime(Task task) {
        List<TaskTime> times = taskTimeRepository.findByTaskAndAssign(task, task.getAssign());
        long time = 0;
        for (TaskTime dr : times) {
            if (dr.getStartTime() != null && dr.getEndTime() != null) {
                time = time + ChronoUnit.SECONDS.between(dr.getStartTime(), dr.getEndTime());
            } else {
                time = time + ChronoUnit.SECONDS.between(dr.getStartTime(), LocalDateTime.now());
            }
        }
        return time;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskProcessResponse getProcess(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        Optional<Task> process = taskRepository.findByAssignAndTopAndClose(user, true, false);
        if (process.isPresent()) {
            return new TaskProcessResponse(process.get(), getTime(process.get()) * 1000);
        } else {
            return new TaskProcessResponse();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sortTask(TaskSortRequest request) throws TaskNotFoundException, UserNotFoundException {
        Task task = checkTaskExist(request.getId());
        User login = userService.getLoginUser();
        List<TaskSort> sortList = taskSortRepository.findAllByUserAndColumn(login, request.getTaskColumn()).stream().sorted(Comparator.comparing(TaskSort::getSort)).collect(Collectors.toList());
        Optional<TaskSort> sort = taskSortRepository.findByUserAndColumnAndTask(login, request.getTaskColumn(), task);
        sort.ifPresent(sortList::remove);
        sort.ifPresent(e -> sortList.add(request.getSort(), e));
        sortList.stream().forEach(e -> e.setSort(sortList.indexOf(e)));
        SocketTaskResponse response;
        if (request.getTaskColumn() == TaskColumn.TRACE) {
            response = new SocketTaskResponse(SocketType.SORT, getFollowList(login.getAccount()));

        } else {
            response = new SocketTaskResponse(SocketType.SORT, getMyList(login.getAccount()));
        }
        messageEventHandler.sendTaskToUser(login.getAccount(), request.getTaskColumn(), new Gson().toJson(response));

        return true;
    }

    public static final Integer NO_PROCESS = 1;
    public static final Integer NO_WAIT_DEAL = 2;

    @Override
    public List<TaskFollowResponse> getUserFollow(TaskUserFollowRequest request) throws UserNotFoundException {

        List<TaskFollowResponse> result = new ArrayList<>();
        User login = userService.getLoginUser();
        List<Project> projects = request.getProjectId().size() == 0 ? projectService.myProjectModel(login.getAccount()) : projectRepository.findAllByIdIn(request.getProjectId());
        List<Groups> groups = groupsRepository.findAllByProjectIn(projects);
        Set<User> users = new HashSet<>();
        if (request.getAccounts().size() == 0) {
            users.addAll(login.getSubscribe());
            groups.forEach(e -> users.addAll(e.getUsers()));
        } else {
            users.addAll(userService.getUserList(request.getAccounts()));
        }
        List<Task> tasks = taskRepository.findAllByAssignInAndProjectInAndClose(users, projects, false);
        for (User user : users) {

            List<Groups> tempGroup;
            List<Task> tempTasks;
            if (login.getSubscribe().stream().anyMatch(e -> e.getAccount().equals(user.getAccount()))) {
                List<Project> tempProject = projectService.myProjectModel(user.getAccount());
                tempGroup = groupsRepository.findAllByProjectIn(tempProject);
                tempTasks = taskRepository.findAllByAssignInAndProjectInAndClose(users, tempProject, false);
            } else {
                tempGroup = groups;
                tempTasks = tasks;
            }
            TaskFollowResponse temp = new TaskFollowResponse(user, tempTasks.stream().filter(e -> e.getAssign().getAccount().equals(user.getAccount()) && e.getStatus() == Status.PROCESSING).findFirst());
            temp.setLogin(messageEventHandler.checkConnect(user.getAccount()));
            List<Groups> group = tempGroup.stream().filter(e -> e.getUsers().contains(user)).collect(Collectors.toList());
            List<ProjectFollowResponse> project = group.stream().map(e -> new ProjectFollowResponse(e, user, tempTasks)).collect(Collectors.toList());
            temp.setProjects(project.stream().sorted(Comparator.comparing(e -> e.getProject().getName())).collect(Collectors.toList()));
            result.add(temp);

        }

        Collections.sort(result, Comparator.comparing(e -> e.getUser().getName()));

        if (request.getStatus() != null) {
            if (request.getStatus().equals(NO_PROCESS)) {
                return result.stream().filter(e -> e.getProcess() == null).collect(Collectors.toList());
            }
            if (request.getStatus().equals(NO_WAIT_DEAL)) {
                return result.stream().filter(e -> e.getProjects().stream().anyMatch(p -> p.getTaskNnm() == 0)).collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    public List<TaskProjectFollowResponse> getProjectFollow(TaskProjectFollowRequest request) throws UserNotFoundException, GroupsNotFoundException {
        User login = userService.getLoginUser();
        List<Project> temp = projectService.myProjectModel(login.getAccount());
        temp.addAll(login.getSubscribeProject());
        List<Project> projects = temp.stream().filter(distinctByKey(Project::getId)).collect(Collectors.toList());
        if(request.getProjectId() != null) {
            projects = projects.stream().filter(e -> e.getId() == request.getProjectId()).collect(Collectors.toList());
        }
        List<Task> tasks = taskRepository.findAllByProjectIn(projects).stream().filter(e -> !e.isClose()).collect(Collectors.toList());
        if(request.getGroupId() != null) {
            Groups groups = projectService.checkGroups(request.getGroupId());
            tasks = tasks.stream().filter(e -> e.getAssign() != null && groups.getUsers().stream().anyMatch(u -> u.getAccount().equals(e.getAssign().getAccount()))).collect(Collectors.toList());
        }
        if(request.getKeyword() != null) {
            String traditional = ZhConverterUtil.convertToTraditional(request.getKeyword()).toUpperCase();
            String simple = ZhConverterUtil.convertToSimple(request.getKeyword()).toUpperCase();

            tasks = tasks.stream().filter(e -> e.getName().toUpperCase().contains(traditional) ||
                    e.getName().toUpperCase().contains(simple) ||
                    (e.getAssign() != null && e.getAssign().getAccount().toUpperCase().contains(request.getKeyword().toUpperCase())) ||
                    (e.getDispatcher() != null && e.getDispatcher().getAccount().toUpperCase().contains(request.getKeyword().toUpperCase()))
            ).collect(Collectors.toList());
        }
        if(request.getStatus() != null) {
            tasks = tasks.stream().filter(e -> e.getStatus().equals(request.getStatus())).collect(Collectors.toList());
        }
        switch (request.getSortKey()) {
            case "startTime":
                tasks.sort(Comparator.comparing(Task::getStartTime,Comparator.nullsFirst(Comparator.naturalOrder())));
                if(request.getOrder().equals("descend")) {
                    Collections.reverse(tasks);
                }
                break;
            case "endTime":
                tasks.sort(Comparator.comparing(Task::getFinishTime,Comparator.nullsFirst(Comparator.naturalOrder())));
                if(request.getOrder().equals("descend")) {
                    Collections.reverse(tasks);
                }
                break;
            case "point":
                tasks.sort(Comparator.comparing(Task::getPoint,Comparator.nullsFirst(Comparator.naturalOrder())));
                if(request.getOrder().equals("descend")) {
                    Collections.reverse(tasks);
                }
                break;
            default:
                tasks.sort(Comparator.comparing(Task::getId));
                if(request.getOrder().equals("descend")) {
                    Collections.reverse(tasks);
                }
                break;
        }

        return tasks.stream().map(TaskProjectFollowResponse::new).collect(Collectors.toList());
    }

    @Override
    public TaskFollowResponse getUserFollowDetail(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Groups> groups = groupsRepository.findAllByUsersContains(user);
        Optional<Task> process = taskRepository.findByAssignAndStatusAndClose(user, Status.PROCESSING, false);
        List<Task> tasks = taskRepository.findAllByAssignInAndProjectInAndClose(new HashSet<>(Arrays.asList(user)), groups.stream().map(Groups::getProject).collect(Collectors.toList()), false);
        TaskFollowResponse result = new TaskFollowResponse(user, process);
        List<ProjectFollowResponse> project = groups.stream().map(e -> new ProjectFollowResponse(e, user, tasks)).collect(Collectors.toList());
        result.setProjects(project.stream().sorted(Comparator.comparing(e -> e.getProject().getName())).collect(Collectors.toList()));
        return result;
    }

    @Autowired
    private JetService jetService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignTask(TaskAssignRequest request, boolean add) throws UserNotFoundException, PermissionDeniedException, TaskNotFoundException {
        List<Task> tasks = taskRepository.findAllByIdIn(request.getId());
        if (tasks.size() != request.getId().size()) {
            throw new TaskNotFoundException(TaskErrorCode.TASK_NOT_FOUND);
        }

        User login = userService.getLoginUser();

        if (tasks.stream().filter(e -> e.getAssign() != null).anyMatch(e -> !e.getAssign().getAccount().equals(login.getAccount()) && !e.getCreator().getAccount().equals(login.getAccount()))) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN);
        }

        User assign = request.getAssign() == null ? null : userService.getUser(request.getAssign());

        for (Task task : tasks) {
            if (assign != null && task.getAssign() != null && task.getAssign().getAccount().equals(assign.getAccount())) {
                continue;
            }
            if(task.getAssign() != null) {
                if (!add && (task.getType() != Type.SUB_TASK && task.isRead()) || (task.getType() != Type.SUB_TASK && task.getAssign().getAccount().equals(task.getCreator().getAccount())) ||
                  (task.getType() == Type.SUB_TASK && task.getParent().getAssign() != null && !login.getAccount().equals(assign.getAccount())) && task.isRead()) {
                    socketTaskRemove(task);
                }
            }
            TaskEditResponse changeItem = getChangeAssignItem(task, request.getAssign());
            if (assign != null && assign.getAccount().equals(login.getAccount())) {
                login.getFollow().removeIf(e -> e.getId() == task.getId());
                checkFollow(task, SocketType.REMOVE);
                request.setFollow(false);
            }
            task.setStatus(Status.START_NOT_YET);
            task.setRead(false);
            task.setAssign(assign);
            task.setDispatcher(login);
            if (assign != null && assign.getFollow().contains(task)) {
                assign.getFollow().removeIf(e -> e.getId() == task.getId());
                sendTraceSocketMessage(assign);
                checkFollow(task, SocketType.REMOVE);
            }
            if (request.isFollow()) {
                login.getFollow().add(task);
                checkFollow(task, SocketType.ADD);
            }

            if (task.getParent() != null) {
                socketTaskReplace(task.getParent());
            }

            if (assign != null && !login.getAccount().equals(assign.getAccount())) {

                String finishTime = task.getEstimateEndTime() == null ? "未指定" : DateTool.getString(task.getEstimateEndTime(), DateTool.yyyy_MM_dd);
                String point = task.getPoint() == null ? "未設定" : String.valueOf(task.getPoint());
                String title = task.getDispatcher().getEnglishName() + "(" + task.getDispatcher().getAccount() + ") 指派了 " + task.getName() + " 給您";
                String body = "預計完成時間 : " + finishTime + "\n" +
                  "積分 : " + point;
                String link = "https://footprint2.link8tech.tw/task-card-detail?id=" + task.getId();
                NotifyResponse notify = new NotifyResponse(title, body, link);

                jetService.sendMessage(task.getAssign().getAccount(), notify.toString());
                messageEventHandler.sendNotifyToUser(task.getAssign().getAccount(), new Gson().toJson(notify));
            }
            history(task, login, Action.ASSIGN, new Gson().toJson(Arrays.asList(changeItem)), null);
            if (task.getType() != Type.SUB_TASK || (assign != null &&  task.getType() == Type.SUB_TASK && task.getParent().getAssign() != null && !task.getParent().getAssign().getAccount().equals(assign.getAccount()))) {
                socketTaskAdd(task,add);
            }
            List<Task> sub = taskRepository.findAllByParent(task);
            socketTaskListRemove(sub.stream().filter(e -> e.getAssign() != null && e.getAssign().getAccount().equals(assign.getAccount())).collect(Collectors.toList()));
        }
        return true;
    }

    private User checkUser(Task task) throws PermissionDeniedException {
        try {
            return checkAssign(task);
        } catch (PermissionDeniedException e) {
            try {
                return checkCreator(task);
            } catch (PermissionDeniedException e1) {
                throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN);
            }
        }
    }

    private void addSortTask(Task task, TaskColumn column, int num, User user) {
        List<TaskSort> sortList = taskSortRepository.findAllByUserAndColumn(user, column).stream().sorted(Comparator.comparing(TaskSort::getSort)).collect(Collectors.toList());
        Optional<TaskSort> sort = taskSortRepository.findByUserAndColumnAndTask(user, column, task);
        TaskSort taskSort;
        if (sort.isPresent()) {
            taskSort = sort.get();
        } else {
            taskSort = new TaskSort(task, column, user);
            taskSortRepository.save(taskSort);
        }
        if (sortList.size() == 0) {
            sortList.add(taskSort);
        } else {
            sortList.add(num, taskSort);
        }
        sortList.forEach(e -> e.setSort(sortList.indexOf(e)));
    }

    private void removeSortTask(Task task, TaskColumn column, User user) {
        Optional<TaskSort> sort = taskSortRepository.findByUserAndColumnAndTask(user, column, task);
        sort.ifPresent(e -> taskSortRepository.deleteById(e.getId()));

        List<TaskSort> sortList = taskSortRepository.findAllByUserAndColumn(user, column).stream().sorted(Comparator.comparing(TaskSort::getSort)).collect(Collectors.toList());
        sortList.forEach(e -> e.setSort(sortList.indexOf(e)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeTaskProject(TaskChangeProjectRequest request, boolean reload) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ProjectNotFoundException {
        List<Task> tasks = new ArrayList<>();
        User login = userService.getLoginUser();
        for (Integer id : request.getId()) {
            tasks.add(checkTaskExist(id));
        }
        Project project = request.getProjectId() == null ? null : projectService.checkProject(request.getProjectId());
        for (Task dr : tasks) {
            TaskEditResponse changeItem = getChangeProjectItem(dr, project);
            User user = checkCreator(dr);
            dr.setProject(project);
            history(dr, user, Action.PROJECT, new Gson().toJson(Arrays.asList(changeItem)), null);
        }
        if (reload) {
            socketTaskListReplace(tasks);
            checkFollowTasks(tasks, SocketType.REPLACE);
        }
        if (project != null) {
            login.setLastProject(project);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TaskSocketResponse> getFollowList(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Task> sort = new ArrayList<>(user.getFollow()).stream().filter(e -> !e.isClose()).collect(Collectors.toList());
        List<Task> result = putTaskSort(sort, user, TaskColumn.TRACE);
        return TaskToResponse(result, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TaskSocketResponse> taskBox(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Task> nobody = taskRepository.findAllByAssignAndCloseAndRead(user, false, false);
        List<Task> result = nobody.stream().filter(e -> e.getType() != Type.SUB_TASK && !e.getCreator().getAccount().equals(account) ||
                (e.getType() == Type.SUB_TASK && e.getParent().getAssign() != null && !e.getParent().getAssign().getAccount().equals(account)) && !e.isRead()).collect(Collectors.toList());
        return TaskToResponse(result, user);
    }

    private List<Task> getWaitDealList(User user) throws UserNotFoundException {
        String account = user.getAccount();
        List<Task> list = taskRepository.findAllByAssignAndStatusInAndTopAndClose(user, Arrays.asList(Status.START_NOT_YET, Status.PAUSE), false, false);
        List<Task> temp = list.stream().filter(e -> (e.getType() != Type.SUB_TASK && e.isRead()) || (e.getType() != Type.SUB_TASK && e.getAssign().getAccount().equals(e.getCreator().getAccount())) ||
          (e.getType() == Type.SUB_TASK && e.getParent().getAssign() != null && !e.getParent().getAssign().getAccount().equals(account)) && e.isRead()).collect(Collectors.toList());
        return putTaskSort(temp, user, TaskColumn.WAIT_DEAL);
    }

    @Override
    @Transactional
    public List<TaskSocketResponse> getMyList(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        return TaskToResponse(getWaitDealList(user), user);
    }

    @Override
    @Transactional
    public List<TaskSocketResponse> getMyList(String account, String from) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Task> list = getWaitDealList(userService.getUser(account));
        if (!account.equals(from)) {
            List<Task> processingList = taskRepository.findAllByAssignAndTop(user, true);
            List<Task> result = Stream.concat(processingList.stream(), list.stream()).collect(Collectors.toList());
            return TaskToResponse(result, user);
        } else {
            return TaskToResponse(list, user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TaskSocketResponse> getFinishList(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Task> finish = taskRepository.findAllByAssignAndStatusAndClose(user, Status.FINISH, false);
        List<Task> result = finish.stream().filter(e -> e.getType() == Type.CHECK || e.getType() == Type.SCHEDULE || e.getType() == Type.TASK ||
                (e.getType() == Type.SUB_TASK && e.getParent().getAssign() != null && !e.getParent().getAssign().getAccount().equals(account))).collect(Collectors.toList());
        return TaskToResponse(result, user);
    }

    @Transactional
    public List<Task> putTaskSort(List<Task> tasks, User user, TaskColumn column) {
        List<TaskSort> result = new ArrayList<>();
        List<TaskSort> all = taskSortRepository.findAllByUserAndColumn(user, column);
        List<TaskSort> hasSort = all.stream().filter(e -> tasks.stream().anyMatch(t -> t.getId() == e.getTask().getId())).collect(Collectors.toList());
        List<Task> notSort = tasks.stream().filter(e -> all.stream().noneMatch(t -> t.getTask().getId() == e.getId())).collect(Collectors.toList());
        for (Task dr : notSort) {
            TaskSort temp = new TaskSort(dr, column, user);
            taskSortRepository.save(temp);
            result.add(temp);
        }
        result.addAll(hasSort.stream().sorted(Comparator.comparing(TaskSort::getSort)).collect(Collectors.toList()));
        result.forEach(e -> e.setSort(result.indexOf(e)));

        List<TaskSort> remove = all.stream().filter(e -> result.stream().noneMatch(r -> r.getTask().getId() == e.getTask().getId())).collect(Collectors.toList());
        remove.forEach(e -> taskSortRepository.deleteById(e.getId()));
        return result.stream().map(TaskSort::getTask).collect(Collectors.toList());
    }

    private List<TaskSocketResponse> TaskToResponse(List<Task> list, User user) {
        List<TaskSocketResponse> result = new ArrayList<>();
        List<Task> sub = taskRepository.findAllByParentIn(list);
        List<Message> messages = messageRepository.findAllByTaskIn(list);
        for (Task dr : list) {
            List<User> follow = userRepository.findAllByFollowContains(dr);
            TaskSocketResponse temp = new TaskSocketResponse(dr);
            List<Groups> groups = groupsRepository.findAllByProjectAndUsersContains(dr.getProject(), dr.getAssign());
            temp.setGroups(groups.stream().map(GroupsGetResponse::new).collect(Collectors.toList()));
            temp.setSub(sub.stream().filter(e -> e.getParent().getId() == dr.getId()).sorted(Comparator.comparing(Task::getId)).map(TaskSimpleResponse::new).collect(Collectors.toList()));
            List<Message> mes = messages.stream().filter(e -> e.getTask().getId() == dr.getId()).collect(Collectors.toList());
            if (mes.size() != 0)
                temp.setHasMessage(true);
            long count = mes.stream().filter(e -> !e.getReadUser().contains(user.getAccount())).count();
            temp.setNotRead((int) count);
            temp.setFollow(follow.stream().map(BaseUserResponse::new).collect(Collectors.toList()));
            temp.setSeverity(dr.getSeverity());
            result.add(temp);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        User login = userService.getLoginUser();
        Task task = checkTaskExist(id);
        if (task.getAssign() == null) {
            throw new ActionFailException(ActionErrorCode.TASK_NO_ASSIGN_CAN_NOT_START);
        }
        if (task.getStatus() != Status.START_NOT_YET && task.getStatus() != Status.PAUSE) {
            throw new ActionFailException(ActionErrorCode.TASK_STATUS_CAN_NOT_START);
        }
        checkAssign(task);
        Optional<Task> process = taskRepository.findByAssignAndStatusAndClose(login, Status.PROCESSING, false);
        if (process.isPresent()) {
            process.get().setTop(false);
            pauseTask(process.get().getId(), false);
        }
        Optional<Task> top = taskRepository.findByAssignAndTopAndClose(login, true, false);
        if (top.isPresent()) {
            top.get().setTop(false);
            socketTaskAdd(top.get(),false);
        }
        socketTaskRemove(task);
        task.setStatus(Status.PROCESSING);
        task.setTop(true);
        task.setStartTime(LocalDateTime.now());
        TaskTime taskTime = new TaskTime(task);
        taskTime.setAssign(login);
        taskTimeRepository.save(taskTime);
        reloadDoing(login.getAccount());
        checkFollow(task, SocketType.REPLACE);
        history(task, login, Action.START, null, null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseTask(Integer id, boolean reload) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        Task task = checkTaskExist(id);
        if (task.getAssign() == null) {
            throw new ActionFailException(ActionErrorCode.TASK_NO_ASSIGN_CAN_NOT_PAUSE);
        }
        if (task.getStatus() != Status.PROCESSING) {
            throw new ActionFailException(ActionErrorCode.TASK_STATUS_CAN_NOT_PAUSE);
        }
        User login = checkAssign(task);
        Optional<TaskTime> process = taskTimeRepository.findByTaskAndAssignAndEndTime(task, login, null);
        process.ifPresent(taskTime -> taskTime.setEndTime(LocalDateTime.now()));
        task.setStatus(Status.PAUSE);
        socketTaskAdd(task,false);
        reloadDoing(login.getAccount());
        checkFollow(task, SocketType.REPLACE);
        history(task, login, Action.PAUSE, null, null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishTask(Integer id) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException, ActionFailException {
        Task task = checkTaskExist(id);
        if (task.getAssign() == null) {
            throw new ActionFailException(ActionErrorCode.TASK_NO_ASSIGN_CAN_NOT_FINISH);
        }
        User login = checkAssign(task);
        if (task.getStatus() == Status.PROCESSING) {
            Optional<TaskTime> process = taskTimeRepository.findByTaskAndAssignAndEndTime(task, login, null);
            process.get().setEndTime(LocalDateTime.now());
        }
        Status old = task.getStatus();
        if(old != Status.PROCESSING) {
            socketTaskRemove(task);
        }
        task.setStatus(Status.FINISH);
        task.setTop(false);
        task.setFinishTime(LocalDateTime.now());

        if (task.getCheckMan() != null) {
            addCheckTask(task);
        }
        if(!login.getAccount().equals(task.getAssign().getAccount())) {
            String finishTime = task.getEstimateEndTime() == null ? "未指定" : DateTool.getString(task.getEstimateEndTime(), DateTool.yyyy_MM_dd);
            String point = task.getPoint() == null ? "未設定" : String.valueOf(task.getPoint());
            String title = task.getAssign().getEnglishName() + "(" + task.getAssign().getAccount() + ") 完成 " + task.getName();
            String body = "預計完成時間 : " + finishTime + "\n" +
                    "實際完成時間 : " + DateTool.getString(task.getFinishTime(), DateTool.yyyy_MM_dd_HH_mm_ss) + "\n" +
                    "積分 : " + point;
            String link = "https://footprint2.link8tech.tw/task-card-detail?id=" + task.getId();
            NotifyResponse notify = new NotifyResponse(title, body, link);
            jetService.sendMessage(task.getDispatcher().getAccount(), notify.toString());
            messageEventHandler.sendNotifyToUser(task.getDispatcher().getAccount(), new Gson().toJson(notify));
        }
        socketTaskAdd(task,false);
        if(old == Status.PROCESSING) {
            reloadDoing(login.getAccount());
        }
        if (task.getParent() != null) {
            socketTaskReplace(task.getParent());
        }
        checkFollow(task, SocketType.REPLACE);
        history(task, login, Action.FINISH, null, null);
        return true;
    }

    private void addCheckTask(Task task) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException {
        Task check = addTask(new TaskAddRequest(task.getId(), task.getName() + " : 驗證任務", task.getCheckMan().getAccount(), true));
        check.setProject(task.getProject());

        String finishTime = check.getEstimateEndTime() == null ? "未指定" : DateTool.getString(check.getEstimateEndTime(), DateTool.yyyy_MM_dd);
        String point = task.getPoint() == null ? "未設定" : String.valueOf(task.getPoint());
        String title = check.getCreator().getEnglishName() + "(" + check.getCreator().getAccount() + ") 指派了 " + task.getName() + "給您";
        String body = "請進行任務驗證\n" +
                "預計完成時間 : " + finishTime + "\n" +
                "積分 : " + point;
        String link = "https://footprint2.link8tech.tw/task-card-detail?id=" + check.getId();
        NotifyResponse notify = new NotifyResponse(title, body, link);
        jetService.sendMessage(check.getAssign().getAccount(), notify.toString());
        messageEventHandler.sendNotifyToUser(check.getAssign().getAccount(), new Gson().toJson(notify));

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean finishSubTask(int id,boolean finish) throws TaskNotFoundException, ActionFailException, UserNotFoundException, PermissionDeniedException {
        Task task = checkTaskExist(id);
        if (task.getType() != Type.SUB_TASK) {
            throw new ActionFailException(ActionErrorCode.TASK_NOT_SUB_CAN_NOT_USE);
        }
        User login = userService.getLoginUser();
        if (task.getParent().getAssign() == null) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_ASSIGN);
        } else {
            if (!task.getParent().getAssign().getAccount().equals(login.getAccount())) {
                throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_ASSIGN);
            }
        }
        if (task.getAssign() != null && !task.getAssign().getAccount().equals(login.getAccount())) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_ASSIGN);
        }
        if(task.getAssign() == null) {
            task.setAssign(task.getParent().getAssign());
        }
        if(finish) {
            task.setStatus(Status.FINISH);
        } else {
            task.setStatus(Status.START_NOT_YET);
        }
        task.setFinishTime(LocalDateTime.now());
        task.setStartTime(LocalDateTime.now());
        socketTaskReplace(task.getParent());
        checkFollow(task.getParent(), SocketType.REPLACE);
        history(task, login, Action.FINISH, null, null);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelExecute(Integer id) throws TaskNotFoundException, PermissionDeniedException, ActionFailException, UserNotFoundException {
        Task task = checkTaskExist(id);
        if (task.getAssign() == null) {
            throw new ActionFailException(ActionErrorCode.TASK_NO_ASSIGN_CAN_NOT_CANCEL);
        }
        if (task.getStatus() != Status.PAUSE) {
            throw new ActionFailException(ActionErrorCode.TASK_STATUS_CAN_NOT_CANCEL);
        }
        User login = checkAssign(task);
        task.setTop(false);
        socketTaskAdd(task,false);
        reloadDoing(login.getAccount());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean followTask(TaskTraceRequest request) throws TaskNotFoundException, UserNotFoundException {
        Task task = checkTaskExist(request.getId());
        User login = userService.getUser(request.getAccount());

        if (request.isFollow()) {
            login.getFollow().add(task);
        } else {
            login.getFollow().removeIf(e -> e.getId() == request.getId());
        }

        checkFollow(task, SocketType.REMOVE);
        history(task, login, Action.FOLLOW, null, null);
        return true;
    }

    private void history(Task task, User executor, Action action, String changeItem, String context) {
        History history = new History(task, executor, action, changeItem, context);
        historyRepository.save(history);
    }

    private User checkAssign(Task task) throws PermissionDeniedException {
        User login = userService.getLoginUser();
        if (task.getAssign() == null) {
            return login;
        }
        if (!task.getAssign().getAccount().equals(login.getAccount())) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_ASSIGN);
        }
        return login;
    }

    private User checkCreator(Task task) throws PermissionDeniedException {
        User login = userService.getLoginUser();
        if (!task.getCreator().getAccount().equals(login.getAccount())) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR);
        }
        return login;
    }

    private User checkAssignOrCreator(Task task) throws PermissionDeniedException {
        User login = userService.getLoginUser();
        if (task.getCreator().getAccount().equals(login.getAccount()) ||
            task.getAssign().getAccount().equals(login.getAccount())) {
            return login;
        } else {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN);
        }
    }

    private User pointAuthority(Task task) throws PermissionDeniedException {
        User login = userService.getLoginUser();
        List<Groups> groups = groupsRepository.findAllByProjectAndUsersContains(task.getProject(), task.getAssign());
        if (task.getCreator().getAccount().equals(login.getAccount())) {
            return login;
        }
        if (task.getAssign() != null && task.getAssign().getAccount().equals(login.getAccount())) {
            return login;
        }
        if (task.getAssign() != null && task.getDispatcher().getAccount().equals(login.getAccount())) {
            return login;
        }
        if (task.getProject() != null && task.getProject().getManager().getAccount().equals(login.getAccount())) {
            return login;
        }
        if (groups.size() > 0 && groups.stream().anyMatch(g -> g.getUsers().stream().anyMatch(u -> u.getAccount().equals(login.getAccount())))) {
            return login;
        }
        throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR_AND_ASSIGN_AND_DISPATCHER);
    }

    @Override
    public Page<TaskStatisticResponse> getPointList(PointListRequest request) throws ProjectNotFoundException {
        List<TaskStatisticResponse> result = new ArrayList<>();
        List<Project> projects = request.getProjectId() == null ? projectRepository.findAll() : Arrays.asList(projectService.checkProject(request.getProjectId()));
        LocalDateTime startTime = DateTool.getDateTime(request.getStartTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        LocalDateTime endTime = DateTool.getDateTime(request.getEndTime(), DateTool.yyyy_MM_dd_HH_mm_ss);

        List<Groups> groups = groupsRepository.findAllByProjectIn(projects);
        Set<User> users;
        if (request.getAccountList().size() != 0) {
            users = new HashSet<>(userService.getUserList(request.getAccountList()));
        } else {
            users = new HashSet<>();
            groups.forEach(e -> users.addAll(e.getUsers()));
        }
        users.stream().filter(distinctByKey(User::getAccount)).collect(Collectors.toList());
        List<Task> all = taskRepository.findAllByProjectInAndAssignInAndFinishTimeIsBetween(projects, new ArrayList<>(users), startTime, endTime);

        for (Groups dr : groups) {
            for (User user : users.stream().filter(e -> dr.getUsers().stream().anyMatch(g -> g.getAccount().equals(e.getAccount()))).collect(Collectors.toList())) {
                List<Task> temp = all.stream().filter(e -> e.getAssign().getAccount().equals(user.getAccount()) && e.getProject().getId() == dr.getProject().getId()).collect(Collectors.toList());
                result.add(new TaskStatisticResponse(temp, user, dr.getProject()));
            }
        }

        if (request.getSort().equals("project")) {
            result = result.stream().sorted(Comparator.comparing(e -> e.getProject().getName())).collect(Collectors.toList());
        }
        if (request.getSort().equals("point")) {
            result = result.stream().sorted(Comparator.comparing(TaskStatisticResponse::getScore)).collect(Collectors.toList());
        }
        if (request.getOrder().equals("descend")) {
            Collections.reverse(result);
        }
        Pageable pageable = PageRequest.of(request.getNumber(), request.getSize());
        int start = (int) pageable.getOffset();
        int end = (Math.min(start + pageable.getPageSize(), result.size()));
        List<TaskStatisticResponse> sub = result.subList(start, end);
        return new PageImpl<>(sub, pageable, result.size());
    }

    @Override
    public Page<PointDetailResponse> getPointDetail(PointDetailRequest request) throws UserNotFoundException, ProjectNotFoundException {
        User user = userService.getUser(request.getAccount());
        Project project = projectService.checkProject(request.getProjectId());
        LocalDateTime startTime = DateTool.getDateTime(request.getStartTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        LocalDateTime endTime = DateTool.getDateTime(request.getEndTime(), DateTool.yyyy_MM_dd_HH_mm_ss);
        List<Task> list = taskRepository.findAllByProjectAndAssignAndFinishTimeIsBetween(project, user, startTime, endTime);

        Pageable pageable = PageRequest.of(request.getNumber(), request.getSize());
        int start = (int) pageable.getOffset();
        int end = (Math.min(start + pageable.getPageSize(), list.size()));
        List<PointDetailResponse> sub = list.stream().map(PointDetailResponse::new).collect(Collectors.toList()).subList(start, end);
        return new PageImpl<>(sub, pageable, list.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addScheduleTask() {
        LocalDate today = LocalDate.now();
        List<Task> tasks = taskRepository.findAllByScheduleTypeNotNullAndScheduleEndTimeAfter(today);
        List<Task> add = new ArrayList<>();
        add.addAll(tasks.stream().filter(e -> e.getScheduleType() == ScheduleType.DAY).collect(Collectors.toList()));
        add.addAll(tasks.stream().filter(e -> e.getScheduleType() == ScheduleType.WEEK && e.getScheduleDates().contains(today.getDayOfWeek().getValue())).collect(Collectors.toList()));
        add.addAll(tasks.stream().filter(e -> e.getScheduleType() == ScheduleType.MONTH && e.getScheduleDates().contains(today.getDayOfMonth())).collect(Collectors.toList()));
        for (Task dr : add) {
            Task task = new Task(dr);
            dr.getCreator().getFollow().add(task);
            taskRepository.save(task);
            checkFollow(task, SocketType.ADD);
            socketTaskAdd(task,false);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean messageAdd(MessageAddRequest request) throws TaskNotFoundException {
        Task task = checkTaskExist(request.getTaskId());
        User login = userService.getLoginUser();
        Message message = new Message(task, login, request.getContext());
        message.addReadUser(login.getAccount());
        messageRepository.save(message);
        MessageResponse response = new MessageResponse(message);
        String json = new Gson().toJson(new TaskMessageResponse(task, response));
        if (task.getAssign() == null) {
            messageEventHandler.sendMessageToAll(json, login);
        } else {
            messageEventHandler.sendMessageToUser(task.getAssign().getAccount(), json, login);
        }
        List<User> follower = userRepository.findAllByFollowContains(task);
        String follow = new Gson().toJson(new TaskMessageResponse(task.getId(), TaskColumn.TRACE.name(), response));
        for (User dr : follower) {
            messageEventHandler.sendMessageToUser(dr.getAccount(), follow, login);
        }
        checkFollow(task, SocketType.REPLACE);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MessageResponse> messageGet(int id) throws TaskNotFoundException {
        Task task = checkTaskExist(id);
        User login = userService.getLoginUser();
        List<Message> messages = messageRepository.findAllByTask(task);
        List<MessageResponse> result = messages.stream().sorted(Comparator.comparing(Message::getId)).map(e -> new MessageResponse(e, login)).collect(Collectors.toList());
        messages.stream().filter(e -> !e.getReadUser().contains(login.getAccount())).forEach(e -> e.addReadUser(login.getAccount()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean messageRemove(int id) throws ActionFailException, PermissionDeniedException {
        Optional<Message> message = messageRepository.findById(id);
        if (!message.isPresent()) {
            throw new ActionFailException(MessageErrorCode.MESSAGE_NOT_FOUND);
        }
        User login = userService.getLoginUser();
        if (!message.get().getUser().getAccount().equals(login.getAccount())) {
            throw new PermissionDeniedException(PermissionErrorCode.PERMISSION_DENIED_NOT_CREATOR);
        }
        messageRepository.delete(message.get());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Integer> fixDispatcher() {
        List<Task> task = taskRepository.findAll().stream().filter(e -> e.getDispatcher() == null).collect(Collectors.toList());
        task.stream().forEach(e -> e.setDispatcher(e.getCreator()));
        taskRepository.saveAll(task);
        return task.stream().map(Task::getId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fixHistory() {
        List<History> fix = historyRepository.findAll().stream().filter(e -> e.getChangeItem() != null).collect(Collectors.toList());
        for (History history : fix) {
            List<TaskEditResponse> list = new Gson().fromJson(history.getChangeItem(), new TypeToken<List<TaskEditResponse>>() {
            }.getType());
            for (TaskEditResponse dr : list) {
                switch (dr.getColumn()) {
                    case "指派人":
                        dr.setColumn("assign");
                        break;
                    case "驗證人":
                        dr.setColumn("checkMan");
                        break;
                    case "任務專案":
                        dr.setColumn("project");
                        break;
                    case "任務名稱":
                        dr.setColumn("name");
                        break;
                    case "任務內容":
                        dr.setColumn("context");
                        break;
                    case "預計執行時間":
                        dr.setColumn("e_time");
                        break;
                    case "預計開始時間":
                        dr.setColumn("e_startTime");
                        break;
                    case "預計結束時間":
                        dr.setColumn("e_finishTime");
                        break;
                    case "任務分數":
                        dr.setColumn("point");
                        break;
                }
            }
            list.stream().filter(e -> e.getAfter().equals("無")).forEach(e -> e.setAfter(""));
            list.stream().filter(e -> e.getBefore().equals("無")).forEach(e -> e.setBefore(""));
            String json = new Gson().toJson(list);
            history.setChangeItem(json);
        }
        historyRepository.saveAll(fix);
    }

    @Override
    public List<TaskListResponse> getTaskList(String keyword) {
        List<Task> all = taskRepository.findAll();
        if (keyword != null) {
            String traditional = ZhConverterUtil.convertToTraditional(keyword).toUpperCase();
            String simple = ZhConverterUtil.convertToSimple(keyword).toUpperCase();
            all = all.stream().filter(e -> e.getName().toUpperCase().contains(traditional) ||
                    e.getName().toUpperCase().contains(simple)).collect(Collectors.toList());
        }
        return all.stream().map(TaskListResponse::new).collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean readTask(Integer[] id) throws ActionFailException {
        User login = userService.getLoginUser();
        List<Task> tasks = taskRepository.findAllByIdIn(Arrays.asList(id));
        if (tasks.stream().anyMatch(e -> !e.getAssign().getAccount().equals(login.getAccount()))) {
            throw new ActionFailException(PermissionErrorCode.PERMISSION_DENIED_NOT_ASSIGN);
        }
        socketTaskListRemove(tasks);
        tasks.forEach(e -> e.setRead(true));
        List<TaskSocketResponse> taskResponse = TaskToResponse(tasks, userService.getLoginUser());
        SocketTaskResponse result = new SocketTaskResponse(SocketType.SURPRISE, taskResponse);
        String message = new Gson().toJson(result);
        messageEventHandler.sendTaskToUser(login.getAccount(), TaskColumn.WAIT_DEAL, message);
        checkFollowTasks(tasks, SocketType.REPLACE);
        return true;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editSeverity(TaskEditSeverityRequest request) throws TaskNotFoundException, PermissionDeniedException, UserNotFoundException {
        Task task = checkTaskExist(request.getId());
        User login = pointAuthority(task);
        TaskEditResponse changeItem = new TaskEditResponse("severity", String.valueOf(task.getSeverity()), String.valueOf(request.getSeverity()));
        task.setSeverity(request.getSeverity());
        history(task, login, Action.SEVERITY, new Gson().toJson(Arrays.asList(changeItem)), null);
        socketTaskReplace(task);
        checkFollow(task, SocketType.REPLACE);
        return true;
    }
}
