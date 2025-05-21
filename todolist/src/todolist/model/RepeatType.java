package todolist.model;

/** 重複類型 */
public enum RepeatType {
    NONE, DAILY, WEEKLY, MONTHLY;

    public static RepeatType fromString(String s) {
        if (s == null) return NONE;
        return valueOf(s.toUpperCase());
    }
}
