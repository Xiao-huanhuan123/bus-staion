package com.basic.bustation.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.sql.Timestamp;

public class Word implements java.io.Serializable{

    private Long wordId;

    private String title;

    private String content;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Timestamp leaveTime;

    public Word() {
    }

    public Word(String title, String content, Timestamp leaveTime) {
        this.title = title;
        this.content = content;
        this.leaveTime = leaveTime;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Timestamp leaveTime) {
        this.leaveTime = leaveTime;
    }
}
