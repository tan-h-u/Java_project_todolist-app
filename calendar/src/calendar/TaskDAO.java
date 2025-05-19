package calendar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /* 新增任務 */
    public void addTask(Task task) {
        String sql = """
            INSERT INTO tasks (title, description, due_date, priority, is_completed, tag, repeat_type, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
        	//設定SQL佔位符
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate  (3, Date.valueOf(task.getDueDate()));
            stmt.setInt   (4, task.getPriority().getCode());
            stmt.setBoolean(5, task.isCompleted());
            stmt.setString(6, task.getTag());
            stmt.setString(7, task.getRepeatType().name());
            stmt.setString(8, task.getStatus().name()); // 假設 status 是第 8 個參數


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 刪除任務 */
    public void deleteTask(int taskId) {
        try (Connection c = DBConnector.getConnection();
             PreparedStatement s = c.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            s.setInt(1, taskId);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 讀取全部任務 */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try (Connection c = DBConnector.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tasks")) {

            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setDueDate(rs.getDate("due_date").toLocalDate());
                t.setPriority(Priority.fromCode(rs.getInt("priority")));
                t.setCompleted(rs.getBoolean("is_completed"));
                t.setTag(rs.getString("tag"));
                t.setRepeatType(RepeatType.fromString(rs.getString("repeat_type")));
                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /* 更新完成狀態 */
    public void updateCompletion(int taskId, boolean completed) {
        String sql = "UPDATE tasks SET is_completed = ? WHERE id = ?";
        try (Connection c = DBConnector.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setBoolean(1, completed);
            s.setInt(2, taskId);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 更新整筆任務 */
    public void updateTask(Task t) {
        String sql = """
            UPDATE tasks
            SET title = ?, description = ?, due_date = ?, priority = ?, is_completed = ?, tag = ?, repeat_type = ?
            WHERE id = ?
        """;
        try (Connection c = DBConnector.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, t.getTitle());
            s.setString(2, t.getDescription());
            s.setDate  (3, Date.valueOf(t.getDueDate()));
            s.setInt   (4, t.getPriority().getCode());
            s.setBoolean(5, t.isCompleted());
            s.setString(6, t.getTag());
            s.setString(7, t.getRepeatType().name());
            s.setInt   (8, t.getId());

            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
