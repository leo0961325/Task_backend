package com.link8.tw.repository;

import com.link8.tw.model.Project;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findAllByIdIsNot(Integer id);

    Optional<Project> findByName(String name);

    Set<Project> findAllByManager(User user);

    List<Project> findAllByIdIn(List<Integer> id);
}
