package com.link8.tw.controller.request.task;

import com.link8.tw.controller.errorCode.TransErrorCode;
import com.link8.tw.enums.ScheduleType;
import com.link8.tw.exception.trans.TransDateException;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

public class TaskScheduleRequest {

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    private List<Integer> dates;

    private String endDate;

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public List<Integer> getDates() throws TransDateException {
        if(this.scheduleType == ScheduleType.WEEK) {
            if(this.dates.stream().anyMatch(e -> e > 7 || e < 1)) {
                throw new TransDateException(TransErrorCode.TRANS_DATE_FAIL_WEEK);
            }
        }
        if(this.scheduleType == ScheduleType.MONTH) {
            if(this.dates.stream().anyMatch(e -> e > 31 || e < 1)) {
                throw new TransDateException(TransErrorCode.TRANS_DATE_FAIL_MONTH);
            }
        }
        return dates;
    }

    public void setDates(List<Integer> dates) {
        this.dates = dates;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
