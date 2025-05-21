package todolist.model;

/** 任務優先序（LOW=0, MEDIUM=1, HIGH=2） */
public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int code;

    /** 建構子（列舉型別的建構子必為 private） */
    Priority(int code) {
        this.code = code;
    }

    
    public int getCode() {
        return code;
    }

    
    public static Priority fromCode(int code) {
        if (code == 0) {
            return LOW;
        } else if (code == 1) {
            return MEDIUM;
        } else if (code == 2) {
            return HIGH;
        } else {
            throw new IllegalArgumentException("無效 Priority: " + code);
        }
    }
}
