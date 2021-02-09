package com.link8.tw.jet;

import java.util.List;

public class SendTextMessageRequest {
    private int chatType;
    private long chatId;
    private String text;
    private int atPosition;
    private List<String> atAccounts;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAtPosition() {
        return atPosition;
    }

    public void setAtPosition(int atPosition) {
        this.atPosition = atPosition;
    }

    public List<String> getAtAccounts() {
        return atAccounts;
    }

    public void setAtAccounts(List<String> atAccounts) {
        this.atAccounts = atAccounts;
    }
}
