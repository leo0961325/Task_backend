package com.link8.tw.repository;

import com.link8.tw.model.Message;
import com.link8.tw.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {

    List<Message> findAllByTask(Task task);

    List<Message> findAllByTaskIn(List<Task> list);

    void deleteAllByTask(Task task);
}
