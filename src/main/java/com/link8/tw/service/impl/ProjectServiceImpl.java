package com.link8.tw.service.impl;

import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.link8.tw.controller.errorCode.ProjectErrorCode;
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
import com.link8.tw.enums.ProjectStatus;
import com.link8.tw.enums.Status;
import com.link8.tw.exception.ActionFailException;
import com.link8.tw.exception.groups.GroupsExistException;
import com.link8.tw.exception.groups.GroupsNotFoundException;
import com.link8.tw.exception.project.ProjectExistException;
import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.model.Groups;
import com.link8.tw.model.Project;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;
import com.link8.tw.repository.GroupsRepository;
import com.link8.tw.repository.ProjectRepository;
import com.link8.tw.repository.TaskRepository;
import com.link8.tw.service.ProjectService;
import com.link8.tw.service.TaskService;
import com.link8.tw.service.UserService;
import com.tgfc.acl.service.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserService userService;

    @Autowired
    AclService aclService;

    @Autowired
    TaskService taskService;

    @Autowired
    GroupsRepository groupsRepository;

    @Override
    public Project checkProject(int id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new ProjectNotFoundException(ProjectErrorCode.PROJECT_NOT_FOUND);
        }
    }

    @Override
    public List<BaseResponse> getProjectDropDown() {
        List<Project> projectList = projectRepository.findAll();
        return projectList.stream().sorted(Comparator.comparing(Project::getName)).map(BaseResponse::new).collect(Collectors.toList());
    }

    private void checkProjectName(String name, Integer id) throws ProjectExistException {
        Optional<Project> project = projectRepository.findByName(name);
        if (id == null) {
            if (project.isPresent()) {
                throw new ProjectExistException(ProjectErrorCode.PROJECT_EXIST);
            }
        } else {
            if (project.isPresent() && project.get().getId() != id) {
                throw new ProjectExistException(ProjectErrorCode.PROJECT_EXIST);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProject(ProjectAddRequest request) throws ProjectExistException, UserNotFoundException, ProjectNotFoundException, GroupsExistException, ActionFailException {
        checkProjectName(request.getName(), null);
        Project project = new Project();
        project.setName(request.getName());
        project.setManager(userService.getUser(request.getManager()));
        project.setStatus(ProjectStatus.START_NOT_YET);
        projectRepository.save(project);
        for (GroupsAddRequest dr : request.getGroups()) {
            dr.setProjectId(project.getId());
            addGroups(dr);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editProject(ProjectEditRequest request) throws ProjectNotFoundException, UserNotFoundException, GroupsExistException, GroupsNotFoundException, ActionFailException {
        Project project = checkProject(request.getId());
        project.setName(request.getName());
        project.setManager(userService.getUser(request.getManager()));
        project.setStatus(request.getStatus());
        for (GroupsAddRequest dr : request.getAddGroups()) {
            addGroups(dr);
        }
        for (GroupsEditRequest dr : request.getEditGroups()) {
            editGroups(dr);
        }
        for (Integer dr : request.getRemoveGroups()) {
            removeGroups(dr);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeProject(int id) throws ProjectNotFoundException, ActionFailException {
        Project project = checkProject(id);
        List<Task> all = taskRepository.findAllByProject(project);
        if (all.size() != 0) {
            throw new ActionFailException(ProjectErrorCode.PROJECT_HAS_TASK);
        }
        List<Groups> groups = groupsRepository.findAllByProject(project);
        groupsRepository.deleteAll(groups);
        projectRepository.delete(project);
        return true;
    }

    @Override
    public ProjectGetResponse getProject(int id) throws ProjectNotFoundException {
        Project project = checkProject(id);
        ProjectGetResponse result = new ProjectGetResponse(project);
        result.setGroups(groupsList(id));
        return result;
    }

    @Override
    public Page<ProjectListResponse> projectList(ProjectPageRequest request) {
        List<Project> all = projectRepository.findAllByIdIsNot(0);
        if (request.getManager() != null) {
            all = all.stream().filter(e -> e.getManager().getAccount().equals(request.getManager())).collect(Collectors.toList());
        }
        if (request.getStatus() != null) {
            all = all.stream().filter(e -> e.getStatus() == request.getStatus()).collect(Collectors.toList());
        }
        if (request.getKeyword() != null) {
            String simple = ZhConverterUtil.convertToSimple(request.getKeyword().toUpperCase());
            String traditional = ZhConverterUtil.convertToTraditional(request.getKeyword().toUpperCase());
            all = all.stream().filter(e -> e.getName().toUpperCase().contains(simple) ||
                    e.getName().toUpperCase().contains(traditional)).collect(Collectors.toList());
        }

        Pageable pageable = PageRequest.of(request.getNumber(), request.getSize());
        int start = (int) pageable.getOffset();
        int end = (Math.min(start + pageable.getPageSize(), all.size()));
        List<Project> sub = all.subList(start, end).stream().sorted(Comparator.comparing(Project::getName)).collect(Collectors.toList());

        List<ProjectListResponse> result = new ArrayList<>();
        for (Project project : sub) {
            ProjectListResponse temp = new ProjectListResponse(project);
            temp.setWaitClaimed(taskRepository.countByProjectAndAssignIsNullAndClose(project, false));
            temp.setWaitToDo(taskRepository.countByProjectAndAssignIsNotNullAndStatusNotAndClose(project, Status.FINISH, false));
            temp.setFinish(taskRepository.countByProjectAndStatusAndClose(project, Status.FINISH, false));
            result.add(temp);
        }
        return new PageImpl<>(result, pageable, all.size());
    }

    private void checkGroupsName(Project project, String name, Integer id) throws GroupsExistException {
        Optional<Groups> groups = groupsRepository.findByProjectAndName(project, name);
        if (id == null) {
            if (groups.isPresent()) {
                throw new GroupsExistException(ProjectErrorCode.GROUPS_EXIST);
            }
        } else {
            if (groups.isPresent() && groups.get().getId() != id) {
                throw new GroupsExistException(ProjectErrorCode.GROUPS_EXIST);
            }
        }
    }

    @Override
    public Groups checkGroups(int id) throws GroupsNotFoundException {
        Optional<Groups> groups = groupsRepository.findById(id);
        if (groups.isPresent()) {
            return groups.get();
        } else {
            throw new GroupsNotFoundException(ProjectErrorCode.GROUPS_NOT_FOUND);
        }
    }

    private void checkGroupLeader(Groups groups, List<User> userList, String account) throws ActionFailException {
        Optional<User> leader = userList.stream().filter(e -> e.getAccount().equals(account)).findFirst();
        if (leader.isPresent()) {
            groups.setLeader(leader.get());
        } else {
            throw new ActionFailException(ProjectErrorCode.GROUP_LEADER_HAS_IN);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGroups(GroupsAddRequest request) throws ProjectNotFoundException, GroupsExistException, ActionFailException {
        Project project = checkProject(request.getProjectId());
        checkGroupsName(project, request.getName(), null);
        List<User> userList = userService.getUserList(request.getAccounts());
        Groups groups = new Groups();
        checkGroupLeader(groups, userList, request.getLeader());
        groups.setName(request.getName());
        groups.setProject(project);
        groups.setUsers(userList);
        groupsRepository.save(groups);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editGroups(GroupsEditRequest request) throws GroupsNotFoundException, GroupsExistException, ActionFailException {
        Groups groups = checkGroups(request.getId());
        checkGroupsName(groups.getProject(), request.getName(), request.getId());
        List<User> userList = userService.getUserList(request.getAccounts());
        checkGroupLeader(groups, userList, request.getLeader());
        groups.setName(request.getName());
        groups.setUsers(userList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeGroups(int id) throws GroupsNotFoundException {
        Groups groups = checkGroups(id);
        groupsRepository.delete(groups);
        return true;
    }

    @Override
    public GroupsGetResponse getGroups(int id) throws GroupsNotFoundException {
        Groups groups = checkGroups(id);
        return new GroupsGetResponse(groups);
    }

    @Override
    public List<GroupsGetResponse> groupsList(int id) throws ProjectNotFoundException {
        Project project = checkProject(id);
        List<Groups> groupsList = groupsRepository.findAllByProject(project);
        return groupsList.stream().map(GroupsGetResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<BaseResponse> myProject() throws UserNotFoundException {
        User login = userService.getLoginUser();
        return myProjectModel(login.getAccount()).stream().sorted(Comparator.comparing(Project::getName)).map(BaseResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<ProjectLastResponse> changeTaskProjectList() {
        User login = userService.getLoginUser();
        List<Project> all = projectRepository.findAll().stream().sorted(Comparator.comparing(Project::getName)).collect(Collectors.toList());
        if (login.getLastProject() == null) {
            return all.stream().map(ProjectLastResponse::new).collect(Collectors.toList());
        }
        List<ProjectLastResponse> result = new ArrayList<>();
        result.add(new ProjectLastResponse(login.getLastProject(), true));
        all.removeIf(e -> e.getId() == login.getLastProject().getId());
        result.addAll(all.stream().map(ProjectLastResponse::new).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<BaseResponse> subscribeProjectList() throws UserNotFoundException {
        User login = userService.getLoginUser();
        List<Project> projects = myProjectModel(login.getAccount());
        projects.addAll(login.getSubscribeProject());
        List<Project> result = projects.stream().filter(distinctByKey(Project::getId)).collect(Collectors.toList());
        result.sort(Comparator.comparing(Project::getId));
        return result.stream().sorted(Comparator.comparing(Project::getName)).map(BaseResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<Project> myProjectModel(String account) throws UserNotFoundException {
        User user = userService.getUser(account);
        List<Groups> groups = groupsRepository.findAllByUsersContains(user);
        Set<Project> result = projectRepository.findAllByManager(user);
        result.addAll(groups.stream().map(Groups::getProject).collect(Collectors.toSet()));
        return new ArrayList<>(result);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
