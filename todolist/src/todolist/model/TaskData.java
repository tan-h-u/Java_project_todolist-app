package todolist.model;

import todolist.model.TaskDAO;

import java.util.ArrayList;
import java.util.List;

public class TaskData {
    private static List<Taskmodel> tasks = new ArrayList<>();

    public static void addTask(Taskmodel task) {
        tasks.add(task);
        TaskDAO.addTask(task); 
    }

 // TaskData.java
    public static List<Taskmodel> getAllTasks() {
        if (tasks.isEmpty()) {
        	tasks.addAll(TaskDAO.getAllTasks());  // 從資料庫撈
        }
        return tasks;
    }

    	
    
    
}
