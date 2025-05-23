package todolist.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /* 新增任務 */
    public static void addTask(Taskmodel task) {
        String sql = """
            INSERT INTO tasks (title, description, start_date, due_date, priority, is_completed, tag, repeat_type, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
        	//設定SQL佔位符
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setTimestamp  (3, Timestamp.valueOf(task.getStartTime()));
            stmt.setTimestamp  (4, Timestamp.valueOf(task.getDueTime()));
            stmt.setInt   (5, task.getPriority().getCode());
            stmt.setBoolean(6, task.isCompleted());
            stmt.setString(7, task.getTag());
            stmt.setString(8, task.getRepeatType().name());
            stmt.setString(9, task.getStatus().name()); // 假設 status 是第 8 個參數


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
    public static List<Taskmodel> getAllTasks() {
        List<Taskmodel> tasks = new ArrayList<>();
        try (Connection c = DBConnector.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tasks")) {

            while (rs.next()) {
                Taskmodel t = new Taskmodel();
                t.setId(rs.getInt("id"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                //t.setDueTime(rs.getDate("due_date").toLocalDate());
                t.setPriority(Priority.fromCode(rs.getInt("priority")));
                t.setCompleted(rs.getBoolean("is_completed"));
                t.setTag(rs.getString("tag"));
                t.setRepeatType(RepeatType.fromString(rs.getString("repeat_type")));
                t.setStartTime(rs.getTimestamp("start_date").toLocalDateTime());
                t.setEndTime(rs.getTimestamp("due_date").toLocalDateTime());
                t.setStatus(Status.fromString(rs.getString("status")));

                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /* 更新完成狀態 */
    public void updateCompletion(int taskId) {
        String sql = "UPDATE tasks SET WHERE id = ?";
        try (Connection c = DBConnector.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(2, taskId);
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 更新整筆任務 */
    public void updateTask(Taskmodel t) {
        String sql = """
            UPDATE tasks
            SET title = ?, description = ?, start_date = ?, due_date = ?, priority = ?, is_completed = ?, tag = ?, repeat_type = ?, status = ?
            WHERE id = ?
        """;
        try (Connection c = DBConnector.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            
            s.setString(1, t.getTitle());
            s.setString(2, t.getDescription());
            s.setTimestamp  (3, Timestamp.valueOf(t.getStartTime()));
            s.setTimestamp  (4, Timestamp.valueOf(t.getDueTime()));
            s.setInt   (5, t.getPriority().getCode());
            s.setBoolean(6, t.isCompleted());
            s.setString(7, t.getTag());
            s.setString(8, t.getRepeatType().name());
            s.setString(9, t.getStatus().name());
            s.setInt   (10, t.getId());
            s.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateAllTaskStatuses() {			//更改status欄位值
        String sql = """
            UPDATE tasks
			SET status = 
			    CASE
			        WHEN start_date > NOW() AND due_date > NOW() THEN 'TO_DO'
			        WHEN start_date < NOW() AND due_date < NOW() THEN 'DONE'
			        ELSE 'IN_PROGRESS'
			    END
        """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateCompletion(int id, boolean completed) {
        String sql = "UPDATE tasks SET is_completed = ? WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, completed);  // ✅ 傳入 true/false
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
