package com.link8.tw.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.link8.tw.controller.errorCode.UserErrorCode;
import com.link8.tw.controller.request.PageListRequest;
import com.link8.tw.controller.request.user.ProjectSubscribeRequest;
import com.link8.tw.controller.request.user.UserSubscribeRequest;
import com.link8.tw.controller.response.point.TaskStatisticResponse;
import com.link8.tw.controller.response.project.ProjectSubscribeResponse;
import com.link8.tw.controller.response.task.SocketTaskResponse;
import com.link8.tw.controller.response.task.TaskEditResponse;
import com.link8.tw.controller.response.task.TaskListResponse;
import com.link8.tw.controller.response.task.TaskSocketResponse;
import com.link8.tw.controller.response.user.UserAssignResponse;
import com.link8.tw.controller.response.user.UserListResponse;
import com.link8.tw.controller.response.user.UserResponse;
import com.link8.tw.controller.response.user.UserSubscribeResponse;
import com.link8.tw.enums.Action;
import com.link8.tw.enums.SocketType;
import com.link8.tw.enums.TaskColumn;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.file.FileNotFoundException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.task.TaskNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.*;
import com.link8.tw.repository.*;
import com.link8.tw.security.UserLoginResponse;
import com.link8.tw.service.FileService;
import com.link8.tw.service.ProjectService;
import com.link8.tw.service.TaskService;
import com.link8.tw.service.UserService;
import com.link8.tw.socket.MessageEventHandler;
import com.tgfc.acl.service.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AclService aclService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    GroupsRepository groupsRepository;

    @Autowired
    MessageEventHandler messageEventHandler;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    FileService fileService;

    @Autowired
    HistoryRepository historyRepository;

    @Override
    public User getLoginUser() {
        UserLoginResponse login = (UserLoginResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByAccount(login.getAccount());
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @Override
    public List<UserListResponse> getList() {
        User login = getLoginUser();
        List<User> all = userRepository.findAll();
        List<UserListResponse> result = new ArrayList<>();
        Set<String> dept = all.stream().sorted(Comparator.comparing(User::getDeptId)).map(User::getDeptName).collect(Collectors.toSet());
        result.add(new UserListResponse(login.getDeptName(), all));
        dept.removeIf(e -> e.equals(login.getDeptName()));
        result.addAll(dept.stream().map(e -> new UserListResponse(e, all)).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<UserResponse> getRecentList() {
        User login = getLoginUser();
        List<History> history = historyRepository.findAllByActionAndExecutor(Action.ASSIGN, login);
        List<String> assign = new ArrayList<>();
        for (History dr : history) {
            List<TaskEditResponse> list = new Gson().fromJson(dr.getChangeItem(), new TypeToken<List<TaskEditResponse>>() {
            }.getType());
            if (list.get(0) != null) {
                assign.addAll(list.stream().filter(e -> e.getColumn().equals("assign") && e.getAfter() != null).map(TaskEditResponse::getAfter).collect(Collectors.toList()));
            }
        }
        List<User> userList = getUserList(assign).stream().sorted(Comparator.comparing(u -> assign.stream().filter(e -> e.equals(u.getAccount())).count())).collect(Collectors.toList());
        Collections.reverse(userList);
        return userList.stream().map(UserResponse::new).collect(Collectors.toList());
    }

    @Override
    public Page<UserSubscribeResponse> getSubscribeUserList(PageListRequest request) {
        User login = getLoginUser();
        List<User> all = userRepository.findAll();
        List<UserSubscribeResponse> result = all.stream().filter(e -> e.getDeptId() == login.getDeptId()).sorted(Comparator.comparing(User::getAccount)).map(e -> new UserSubscribeResponse(e, login)).collect(Collectors.toList());
        all.removeIf(e -> e.getDeptId() == login.getDeptId());
        all.sort(Comparator.comparing(User::getDeptId).thenComparing(User::getAccount));
        result.addAll(all.stream().map(e -> new UserSubscribeResponse(e, login)).collect(Collectors.toList()));
        if (request.getKeyword() != null) {
            result = result.stream().filter(e -> e.getName() != null && e.getName().toUpperCase().contains(request.getKeyword().toUpperCase())).collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(request.getNumber(), request.getSize());
        int start = (int) pageable.getOffset();
        int end = (Math.min(start + pageable.getPageSize(), result.size()));
        List<UserSubscribeResponse> sub = result.subList(start, end);
        return new PageImpl<>(sub, pageable, result.size());
    }

    @Override
    public List<ProjectSubscribeResponse> getSubscribeProjectList(PageListRequest request) {
        User login = getLoginUser();
        List<Project> all = projectRepository.findAll();
        Collections.sort(all,Comparator.comparing(Project::getName));
        List<ProjectSubscribeResponse> result = all.stream().map(ProjectSubscribeResponse::new).collect(Collectors.toList());
        result.stream().filter(e -> login.getSubscribeProject().stream().anyMatch(p -> p.getId() == e.getId())).forEach(e -> e.setSubscribe(true));
        if (request.getKeyword() != null) {
            return result.stream().filter(e -> e.getName() != null && e.getName().toUpperCase().contains(request.getKeyword().toUpperCase())).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean subscribeUser(UserSubscribeRequest request) throws UserNotFoundException {
        User user = getUser(request.getAccount());
        User login = getLoginUser();
        if (request.isSubscribe()) {
            login.getSubscribe().add(user);
        } else {
            login.getSubscribe().removeIf(e -> e.getAccount().equals(request.getAccount()));
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean subscribeProject(ProjectSubscribeRequest request) throws ProjectNotFoundException {
        Project project = projectService.checkProject(request.getProjectId());
        User login = getLoginUser();
        if(request.isSubscribe()) {
            login.getSubscribeProject().add(project);
        } else {
            login.getSubscribeProject().removeIf(e -> e.getId() == project.getId());
        }
        return true;
    }

    @Override
    public User getUser(String account) throws UserNotFoundException {
        Optional<User> user = userRepository.findByAccount(account);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFollow(Integer id) throws TaskNotFoundException {
        Task task = taskService.checkTaskExist(id);
        List<User> users = userRepository.findAllByFollowContains(task);
        users.stream().forEach(e -> e.getFollow().removeIf(t -> t.getId() == id));
        for (User dr : users) {
            TaskSocketResponse taskResponse = new TaskSocketResponse();
            taskResponse.setId(task.getId());
            SocketTaskResponse result = new SocketTaskResponse(SocketType.REMOVE, taskResponse);
            String message = new Gson().toJson(result);
            messageEventHandler.sendTaskToUser(dr.getAccount(), TaskColumn.TRACE, message);
        }
    }

    @Override
    public List<User> getUserList(List<String> accounts) {
        List<User> userList = userRepository.findAllByAccountIn(accounts);
        return userList;
    }

    @Override
    public List<UserListResponse> getProjectUsers(Integer[] projectId) throws UserNotFoundException {
        User login = getLoginUser();
        List<UserListResponse> result = new ArrayList<>();
        List<Project> projects = projectId == null ? projectService.myProjectModel(login.getAccount()) : projectRepository.findAllByIdIn(Arrays.asList(projectId));
        List<Groups> groups = groupsRepository.findAllByProjectIn(projects);
        Set<User> users = projects.stream().map(Project::getManager).collect(Collectors.toSet());
        groups.stream().forEach(e -> users.addAll(e.getUsers()));

        Set<String> dept = users.stream().sorted(Comparator.comparing(User::getDeptId)).map(User::getDeptName).collect(Collectors.toSet());
        result.add(new UserListResponse(login.getDeptName(), new ArrayList<>(users)));
        dept.removeIf(e -> e.equals(login.getDeptName()));
        result.addAll(dept.stream().map(e -> new UserListResponse(e, new ArrayList<>(users))).collect(Collectors.toList()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userImage(int imageId) throws FileNotFoundException {
        FileInfo image = fileService.getFileInfo(imageId);
        User login = getLoginUser();
        login.setImg(image);
        return true;
    }
}