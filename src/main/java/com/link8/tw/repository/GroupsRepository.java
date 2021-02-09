package com.link8.tw.repository;

import com.link8.tw.model.Groups;
import com.link8.tw.model.Project;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupsRepository extends JpaRepository<Groups, Integer> {

    Optional<Groups> findByProjectAndName(Project project, String name);

    List<Groups> findAllByProject(Project project);

    List<Groups> findAllByUsersContains(User user);

    List<Groups> findAllByProjectIn(List<Project> projects);

    List<Groups> findAllByProjectAndUsersContains(Project project,User user);
}
