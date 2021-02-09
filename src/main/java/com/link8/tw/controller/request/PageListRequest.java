package com.link8.tw.controller.request;

import javax.validation.constraints.Min;

public class PageListRequest {

    @Min(value = 10)
    private int size = 10;

    @Min(value = 1)
    private int number = 1;

    private String keyword;

    private String sortKey = "id";

    private String order = "ascend";

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number - 1;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}


