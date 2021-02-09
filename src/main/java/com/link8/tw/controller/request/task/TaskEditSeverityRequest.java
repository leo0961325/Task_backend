package com.link8.tw.controller.request.task;

import com.link8.tw.enums.Severity;

import javax.validation.constraints.Min;

public class TaskEditSeverityRequest {

  @Min(value = 1)
  private int id;

  private Severity severity;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(Severity severity) {
    this.severity = severity;
  }
}
