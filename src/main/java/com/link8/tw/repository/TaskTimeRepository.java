package com.link8.tw.repository;

import com.link8.tw.model.Task;
import com.link8.tw.model.TaskTime;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskTimeRepository extends JpaRepository<TaskTime, Integer> {

    Optional<TaskTime> findByTaskAndAssignAndEndTime(Task task, User user, LocalDateTime time);

    List<TaskTime> findByTaskAndAssign(Task task, User loginUser);

    void deleteAllByTask(Task task);
}
