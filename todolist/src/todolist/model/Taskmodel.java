package todolist.model;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;


/** 任務實體 */
public class Taskmodel {

    private int id;                 // 任務 ID（PK）
    private String title;           // 標題
    private String description;     // 描述
    private LocalDate dueDate;      // 截止日
    private Priority priority;      // 優先序
    private boolean completed;      // 是否完成
    private String tag;             // 標籤
    private RepeatType repeatType;  // 重複類型
    private Status status = Status.TO_DO; // 任務狀態（預設 TO_DO）
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /** 無參建構子：給 DAO 用 */
    public Taskmodel() {}

    /** 複製建構子 */
    public Taskmodel(Taskmodel other) {
        this.id          = other.id;
        this.title       = other.title;
        this.description = other.description;
        this.dueDate     = other.dueDate;
        this.priority    = other.priority;
        this.completed   = other.completed;
        this.tag         = other.tag;
        this.repeatType  = other.repeatType;
        this.status      = other.status;
    }

    public Taskmodel(String title, String description, LocalDate dueDate,
                Priority priority, boolean completed,
                String tag, RepeatType repeatType, Status status) {
        this.title       = title;
        this.description = description;
        this.dueDate     = dueDate;
        this.priority    = priority;
        this.completed   = completed;
        this.tag         = tag;
        this.repeatType  = repeatType;
        this.status      = status;
    }

    /* ===== Getter / Setter ===== */
    public int getId() {
    	return id; 
    }
    public void setId(int id){
    	this.id = id; 
    }

    public String getTitle(){
    	return title; 
    }
    public void setTitle(String title){
    	this.title = title; 
    }

    public String getDescription(){
    	return description;
    }
    public void setDescription(String d){
    	this.description = d; 
    }

    public LocalDate getDueDate(){
    	return dueDate;
    }
    public void setDueDate(LocalDate due){
    	this.dueDate = due; 
    }

    public Priority getPriority(){
    	return priority; 
    }
    public void setPriority(Priority p){
    	this.priority = p; 
    }

    public boolean isCompleted(){
    	return completed; 
    }
    public void setCompleted(boolean c){
    	this.completed = c; 
    }

    public String getTag(){
    	return tag; 
    }
    public void setTag(String tag){
    	this.tag = tag; 
    }

    public RepeatType getRepeatType(){
    	return repeatType; 
    }
    public void setRepeatType(RepeatType r){
    	this.repeatType = r; 
    }
    
    public Status getStatus(){
    	return status; 
    }
    public void setStatus(Status status){
    	this.status = status; 
    }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    @Override
    public String toString() {
        return "Task{" +"id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", dueDate=" + dueDate +", priority=" + priority +", completed=" + completed +", tag='" + tag + '\'' + ", repeatType=" + repeatType +", status=" + status + '}';
    }
}
