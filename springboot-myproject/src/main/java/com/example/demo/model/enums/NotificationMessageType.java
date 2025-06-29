package com.example.demo.model.enums;

public enum NotificationMessageType {

    REPORT_APPROVED("你的檢舉已通過，該作品已刪除"),
    REPORT_REJECTED("你的檢舉未通過，未發現問題"),
    ARTWORK_REMOVED("你的作品因違規已被刪除");

    private final String message;

    NotificationMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}