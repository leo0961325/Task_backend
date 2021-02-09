package com.link8.tw.controller.response.message;

import com.link8.tw.model.Task;
import com.link8.tw.tool.DateTool;
import javassist.compiler.ast.StringL;

public class NotifyResponse {

    private String title;

    private String body;

    private String link;

    public NotifyResponse(String title, String body, String link) {
        this.title = title;
        this.body = body;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return title + "\n" +
                body + "\n" +
                "連結 : " + link + "\n";
    }
}
