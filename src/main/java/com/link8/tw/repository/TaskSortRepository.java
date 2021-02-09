package com.link8.tw.repository;

import com.link8.tw.enums.TaskColumn;
import com.link8.tw.model.Task;
import com.link8.tw.model.TaskSort;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskSortRepository extends JpaRepository<TaskSort, Integer> {

    List<TaskSort> findAllByUserAndColumn(User user, TaskColumn column);

    Optional<TaskSort> findByUserAndColumnAndTask(User user, TaskColumn column, Task task);

    @Query(value = "DELETE FROM task_sort WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);

    void deleteAllByTask(Task task);
}
