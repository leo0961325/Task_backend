package com.link8.tw.controller.request.task;

import com.link8.tw.enums.TaskColumn;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class TaskSortRequest {

    @Min(0)
    @Max(1)
    private Integer column;

    private int id;

    private int sort;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public TaskColumn getTaskColumn() {
        switch (this.column) {
            case 0:
                return TaskColumn.TRACE;
            case 1:
                return TaskColumn.WAIT_DEAL;
        }
        return null;
    }

    public TaskSortRequest() {
    }
}
