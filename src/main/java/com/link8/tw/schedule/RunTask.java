package com.link8.tw.schedule;

import com.link8.tw.exception.project.ProjectNotFoundException;
import com.link8.tw.exception.user.UserNotFoundException;
import com.link8.tw.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RunTask {

    @Autowired
    TaskService taskService;

    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0 8 11 * * ?")
    public void addScheduleTask() throws IOException, UserNotFoundException, ProjectNotFoundException {
        taskService.addScheduleTask();
    }
}
