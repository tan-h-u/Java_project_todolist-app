package todolist.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import todolist.model.RepeatType;
import todolist.model.Taskmodel;
import todolist.model.TaskDAO;

public class TaskService {

    private final TaskDAO taskDAO = new TaskDAO();

    /** 取得所有任務，並依「截止日 → 優先序」排序 */
    public List<Taskmodel> getSortedTasks() {
        List<Taskmodel> tasks = taskDAO.getAllTasks();

        // 自訂 Comparator：先比較截止日，若相同再比優先級（高在前）
        Comparator<Taskmodel> cmp = new Comparator<Taskmodel>() {
            @Override
            public int compare(Taskmodel a, Taskmodel b) {
                int dateCompare = a.getDueDate().compareTo(b.getDueDate());
                if (dateCompare != 0) {
                	return dateCompare;
                }

                // Priority.code 數字越大優先級越高 → 要放前面 → 取相反序
                return Integer.compare(b.getPriority().getCode(), a.getPriority().getCode());
            }
        };

        Collections.sort(tasks, cmp);
        return tasks;
    }

    /** 取得「今天起算 7 天內」的任務，並同樣排好順序 */
    public List<Taskmodel> getTasksForNext7Days() {
        LocalDate today     = LocalDate.now();
        LocalDate sevenDays = today.plusDays(7);

        List<Taskmodel> result = new ArrayList<>();

        // 1. 先用傳統 for 迭代過濾
        for (Taskmodel t : taskDAO.getAllTasks()) {
            LocalDate d = t.getDueDate();
            
            if (d == null) {
            	continue;                   // 避免 null
            }
            
            if (!d.isBefore(today) && !d.isAfter(sevenDays)) {
                result.add(t);
            }
        }

        // 2. 再用前面寫好的排序邏輯排一次
        result.sort(new Comparator<Taskmodel>() {
            @Override
            public int compare(Taskmodel a, Taskmodel b) {
                int dateCompare = a.getDueDate().compareTo(b.getDueDate());
                if (dateCompare != 0) {
                	return dateCompare;
                }
                return Integer.compare(b.getPriority().getCode(),
                                       a.getPriority().getCode());
            }
        });

        return result;
    }
    
    /** 判斷是否「一天內到期」 */
    public boolean isDueSoon(Taskmodel t) {
        if (t.getDueDate() == null) {
        	return false;
        }
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return !t.getDueDate().isAfter(tomorrow);
    }

    /* === 將 DAO 方法直接包一層，方便 UI 呼叫 === */
    public void addTask(Taskmodel t){ 
    	taskDAO.addTask(t); 
    }
    public void deleteTask(int id){ 
    	taskDAO.deleteTask(id);
    }
    public void updateCompletion(int id, boolean c){ 
    	taskDAO.updateCompletion(id, c); 
    }
    public void updateTask(Taskmodel t){ 
    	taskDAO.updateTask(t);
   	}

    /** 依標籤尋找 */
    public List<Taskmodel> getTasksByTag(String tag) {
        List<Taskmodel> matches = new ArrayList<>();
        for (Taskmodel t : taskDAO.getAllTasks()) {
            if (tag != null && tag.equalsIgnoreCase(t.getTag())) {
                matches.add(t);
            }
        }
        return matches;
    }

    /** 產生重複任務（以 DAILY / WEEKLY / MONTHLY 設定為例） */
    public List<Taskmodel> generateRepeatedTasks(Taskmodel origin,
                                            LocalDate from,
                                            LocalDate to) {
        List<Taskmodel> list = new ArrayList<>();
        if (origin.getRepeatType() == RepeatType.NONE) {
        	return list;
        }

        LocalDate cur = origin.getDueDate();
        while (!cur.isAfter(to)) {
            if (!cur.isBefore(from)) {
                Taskmodel copy = new Taskmodel(origin);  // 使用複製建構子
                copy.setDueDate(cur);
                list.add(copy);
            }

            // 依 repeat type 前進
            switch (origin.getRepeatType()) {
                case DAILY: 
                	cur = cur.plusDays(1);  
                	break;
                
                case WEEKLY: 
                	cur = cur.plusWeeks(1); 
                	break;
                	
                case MONTHLY:
                	cur = cur.plusMonths(1);
                	break;
                	
                default:      
                	cur = to.plusDays(1);    // 安全跳出
            }
        }
        return list;
    }
}
