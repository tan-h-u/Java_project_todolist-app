package todolist.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import todolist.model.Taskmodel;
import todolist.model.TaskDAO;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();

    /* ---------- 讀取 ---------- */

    /** 依「截止日 → 優先序」排序後回傳全部任務 */
    public List<Taskmodel> getSortedTasks() {
        // 先確保資料庫裡的 status 最新
        taskDAO.updateAllTaskStatuses();

        List<Taskmodel> tasks = taskDAO.getAllTasks();
        tasks.sort(Comparator
            .comparing((Taskmodel t) -> t.getEndTime().toLocalDate())
            .thenComparing((Taskmodel t) -> -t.getPriority().getCode()));
        return tasks;
    }

    /** 取得今天起算 7 天內到期的任務 */
    public List<Taskmodel> getTasksForNext7Days() {
        LocalDate today     = LocalDate.now();
        LocalDate sevenDays = today.plusDays(7);

        List<Taskmodel> result = new ArrayList<>();
        for (Taskmodel t : taskDAO.getAllTasks()) {
            LocalDate due = t.getEndTime().toLocalDate();
            if (!due.isBefore(today) && !due.isAfter(sevenDays)) {
                result.add(t);
            }
        }
        result.sort(Comparator
            .comparing((Taskmodel t) -> t.getEndTime().toLocalDate())
            .thenComparing((Taskmodel t) -> -t.getPriority().getCode()));
        return result;
    }

    /* ---------- 寫入 ---------- */

    public void addTask(Taskmodel t)             { taskDAO.addTask(t); }
    public void updateTask(Taskmodel t)          { taskDAO.updateTask(t); }
    public void deleteTask(int id)               { taskDAO.deleteTask(id); }
    public void updateCompletion(int id, boolean c){
        taskDAO.updateCompletion(id, c);         // ← 把布林值往下傳
    }

    /* ---------- 其他查詢 ---------- */

    public List<Taskmodel> getTasksByTag(String tag) {
        List<Taskmodel> matches = new ArrayList<>();
        for (Taskmodel t : taskDAO.getAllTasks()) {
            if (tag != null && tag.equalsIgnoreCase(t.getTag())) {
                matches.add(t);
            }
        }
        return matches;
    }
}
