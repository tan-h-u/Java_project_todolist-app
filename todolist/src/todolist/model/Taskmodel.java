package todolist.model;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;


/** 任務實體 */
public class Taskmodel {

    private int id;                 // 任務 ID（PK）
    private String title;           // 標題
    private String description;     // 描述
    private Priority priority;      // 優先序
    private boolean completed;      // 是否完成
    private String tag;             // 標籤
    private RepeatType repeatType;  // 重複類型
    private Status status = Status.TO_DO; // 任務狀態（預設 TO_DO）
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    /** 無參建構子：給 DAO 用 */
    public Taskmodel() {}

    /** 複製建構子 */
    public Taskmodel(Taskmodel other) {
        this.id          = other.id;
        this.title       = other.title;
        this.description = other.description;
        this.startDate     = other.startDate;
        this.endDate     = other.endDate;
        this.priority    = other.priority;
        this.completed   = other.completed;
        this.tag         = other.tag;
        this.repeatType  = other.repeatType;
        this.status      = other.status;
    }

    public Taskmodel(String title, String description, LocalDateTime startDate, LocalDateTime endDate,
                Priority priority, boolean completed,
                String tag, RepeatType repeatType, Status status) {
        this.title       = title;
        this.description = description;
        this.startDate   = startDate;
        this.endDate     = endDate;
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

    /*
    public LocalDate getDueDate(){
    	return dueDate;
    }
    public void setDueDate(LocalDate due){
    	this.dueDate = due; 
    }
	*/
    public LocalDateTime getStartTime() {
    	return startDate; 
    	}
    
    public void setStartTime(LocalDateTime startDate) {
    	this.startDate = startDate;
    }
    
    public LocalDateTime getDueTime() {
    	return endDate; 
    	}
    public void setDueTime(LocalDateTime startDate) {
    	this.endDate=startDate;
    }

    public LocalDateTime getEndTime() {
    	return endDate; 
    	}
    
    public void setEndTime(LocalDateTime endDate) {
    	this.endDate = endDate; 
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
    
    @Override
    public String toString() {
        return "Task{" +"id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", startDate=" + startDate +", dueDate=" + endDate +", priority=" + priority +", completed=" + completed +", tag='" + tag + '\'' + ", repeatType=" + repeatType +", status=" + status + '}';
    }
}
