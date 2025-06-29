package com.example.demo.model.enums;

public enum NotificationMessageType {

    REPORT_APPROVED("你的檢舉已通過，該作品已刪除"),
    REPORT_REJECTED("你的檢舉未通過，未發現問題"),
    ARTWORK_REMOVED("你的作品因違規已被刪除"),
    ROLE_UPGRADED("你已被提升為管理者權限,期待您的協助"),
    ROLE_DOWNGRADED("經審查決定取消您的管理者權限，感謝您過去的協助");

    private final String message;

    NotificationMessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}