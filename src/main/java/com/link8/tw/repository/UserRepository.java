package com.link8.tw.repository;

import com.link8.tw.model.Task;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByFollowContains(Task task);

    Optional<User> findByAccount(String account);

    List<User> findAllByAccountIn(List<String> accounts);
}
