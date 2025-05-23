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

    	
    public static void updateTask(Taskmodel task) {
    	TaskDAO dao = new TaskDAO();
        dao.updateTask(task);  // 呼叫 DAO 寫回資料庫

        // 同步快取中的任務資料（可選）
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                break;
            }
        }
    }

    
}
