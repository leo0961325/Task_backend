package com.link8.tw.controller.response.task;

import com.link8.tw.controller.response.message.MessageResponse;
import com.link8.tw.enums.Status;
import com.link8.tw.enums.TaskColumn;
import com.link8.tw.model.Message;
import com.link8.tw.model.Task;

public class TaskMessageResponse {

    private int id;

    private String column;

    private MessageResponse message;

    public TaskMessageResponse(int id, String column, MessageResponse message) {
        this.id = id;
        this.column = column;
        this.message = message;
    }

    public TaskMessageResponse(Task task,MessageResponse message) {
        this.id = task.getId();
        if(task.getAssign() == null) {
            this.column = TaskColumn.TASK_BOX.name();
        } else {
            if (task.getStatus() == Status.FINISH) {
                this.column = TaskColumn.FINISHED.name();
            } else if (task.getStatus() == Status.PROCESSING) {
                this.column = TaskColumn.DOING.name();
            } else {
                this.column = TaskColumn.WAIT_DEAL.name();
            }
        }
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public MessageResponse getMessage() {
        return message;
    }

    public void setMessage(MessageResponse message) {
        this.message = message;
    }
}
