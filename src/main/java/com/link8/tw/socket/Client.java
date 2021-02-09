package com.link8.tw.socket;

import java.util.UUID;

public class Client {
  private UUID id;
  private String account;
  private String from;

  public Client(UUID id, String account, String from) {
    this.id = id;
    this.account = account;
    this.from = from;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }
}
