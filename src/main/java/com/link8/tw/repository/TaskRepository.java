package com.link8.tw.repository;

import com.link8.tw.enums.ScheduleType;
import com.link8.tw.enums.Status;
import com.link8.tw.model.Project;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findByAssignAndTopAndClose(User user, boolean top, boolean close);

    Optional<Task> findByAssignAndStatusAndClose(User user, Status status, boolean close);

    List<Task> findAllByAssignAndFinishTimeIsBetween(User user, LocalDateTime startTime, LocalDateTime endTime);

    List<Task> findAllByAssignAndCloseAndRead(User user, boolean close,boolean read);

    List<Task> findAllByAssignAndStatusAndClose(User user, Status status, boolean close);

    List<Task> findAllByAssignAndStatusInAndTopAndClose(User user,List<Status> asList, boolean top, boolean close);

    List<Task> findAllByAssignAndTop(User user, boolean top);

    List<Task> findAllByProject(Project project);

    long countByProjectAndAssignIsNullAndClose(Project project, boolean close);

    long countByProjectAndAssignIsNotNullAndStatusNotAndClose(Project project, Status status, boolean close);

    long countByProjectAndStatusAndClose(Project project, Status status, boolean close);

    @Query(value = "DELETE FROM task WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);

    List<Task> findAllByClose(boolean b);

    List<Task> findAllByProjectInAndAssignInAndFinishTimeIsBetween(List<Project> projects, List<User> users, LocalDateTime startTime, LocalDateTime endTime);

    List<Task> findAllByAssignInAndProjectInAndClose(Set<User> users, List<Project> projects, boolean b);

    List<Task> findAllByScheduleTypeNotNullAndScheduleEndTimeAfter(LocalDate date);

    List<Task> findAllByProjectAndAssignAndFinishTimeIsBetween(Project project,User user,  LocalDateTime startTime, LocalDateTime endTime);

    List<Task> findAllByIdIn(List<Integer> id);

    List<Task> findAllByParent(Task task);

    List<Task> findAllByParentIn(List<Task> tasks);

    List<Task> findAllByParentIn(Set<Task> tasks);

    List<Task> findAllByProjectIn(List<Project> project);
}
