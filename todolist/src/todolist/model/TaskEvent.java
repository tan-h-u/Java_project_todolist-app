package todolist.model;

import java.time.LocalDateTime;


public class TaskEvent {
    private String title;
    private int status; // 1: 尚未開始, 2: 執行中, 3: 已完成

    public TaskEvent(String title, int status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }
}

