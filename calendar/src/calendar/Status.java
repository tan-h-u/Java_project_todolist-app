package calendar;

/** 任務狀態 */
public enum Status {
    TO_DO,        // 尚未開始
    IN_PROGRESS,  // 執行中
    DONE;         // 已完成

    /** 將資料庫字串轉回 enum（大小寫不敏感） */
    public static Status fromString(String s) {
        if (s == null) {
        	return TO_DO;
        }
        return valueOf(s.toUpperCase());
    }
}
