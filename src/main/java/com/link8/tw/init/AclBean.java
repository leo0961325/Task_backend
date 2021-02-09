package com.link8.tw.init;

import com.link8.tw.model.User;
import com.link8.tw.repository.TaskRepository;
import com.link8.tw.repository.UserRepository;
import com.tgfc.acl.response.UserResponse;
import com.tgfc.acl.service.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AclBean {

    @Autowired
    AclService aclService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    public void getAclUser() {
        List<User> all = userRepository.findAll();
        List<UserResponse> userResponses = aclService.userListByGroupAndSubGroup(1);
        List<User> users = userResponses.stream().map(User::new).collect(Collectors.toList());
        List<User> result = users.stream().filter(e -> all.stream().noneMatch(a -> a.getAccount().equals(e.getAccount()))).collect(Collectors.toList());
        userRepository.saveAll(result);
    }
}
