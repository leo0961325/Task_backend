package com.link8.tw.repository;

import com.link8.tw.enums.Action;
import com.link8.tw.model.History;
import com.link8.tw.model.Task;
import com.link8.tw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    void deleteAllByTask(Task task);

    List<History> findAllByTask(Task task);

    List<History> findAllByActionAndExecutor(Action action, User executor);
}
