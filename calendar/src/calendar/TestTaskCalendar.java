package calendar;

import java.time.LocalDate;
import java.util.List;

public class TestTaskCalendar {

    public static void main(String[] args) {
        TaskService service = new TaskService();

        /* === 0. 清空資料（開發測試用，正式環境請謹慎） === */
        for (Task t : service.getSortedTasks()) {
            service.deleteTask(t.getId());
        }
        System.out.println("🧹 已清空所有任務\n");

        /* === 1. 新增三筆任務 === */
        Task t1 = new Task(
                "期中報告",
                "準備資訊管理課程報告",
                LocalDate.parse("2025-05-25"),
                Priority.HIGH,
                false,
                "學業",
                RepeatType.NONE,
                Status.TO_DO);

        Task t2 = new Task(
                "打掃房間",
                "整理書桌和衣櫃",
                LocalDate.parse("2025-05-23"),
                Priority.LOW,
                false,
                "生活",
                RepeatType.NONE,
                Status.TO_DO);

        Task t3 = new Task(
                "考試複習",
                "複習資料庫考試",
                LocalDate.parse("2025-05-22"),
                Priority.MEDIUM,
                false,
                "學業",
                RepeatType.NONE,
                Status.TO_DO);

        service.addTask(t1);
        service.addTask(t2);
        service.addTask(t3);
        System.out.println("✅ 已新增 3 筆任務\n");

        /* === 2. 取得排序後清單 === */
        List<Task> sorted = service.getSortedTasks();
        System.out.println("📋 依 截止日→優先序 排序後：");
        for (Task t : sorted) {
            System.out.printf("  %s | 截止: %s | 優先: %s%n",
                    t.getTitle(), t.getDueDate(), t.getPriority());
        }

        /* === 3. 取得近 7 天任務 === */
        List<Task> next7 = service.getTasksForNext7Days();
        System.out.println("\n📆 近 7 天內任務：");
        for (Task t : next7) {
            System.out.printf("  %s | 截止: %s | 優先: %s%n",
                    t.getTitle(), t.getDueDate(), t.getPriority());
        }

        /* === 4. 將第一筆標記為已完成，並刪除第二筆 === */
        if (!sorted.isEmpty()) {
            int firstId = sorted.get(0).getId();
            service.updateCompletion(firstId, true);
            System.out.printf("%n✔️ 任務 ID=%d 已標記為完成%n", firstId);
        }
        if (sorted.size() > 1) {
            int secondId = sorted.get(1).getId();
            service.deleteTask(secondId);
            System.out.printf("🗑️  任務 ID=%d 已刪除%n", secondId);
        }

        /* === 5. 最後再次列出全部任務確認結果 === */
        System.out.println("\n🔄 最終任務清單：");
        for (Task t : service.getSortedTasks()) {
            System.out.printf("  %s | 截止: %s | 優先: %s | 完成: %s%n",
                    t.getTitle(), t.getDueDate(), t.getPriority(), t.isCompleted());
        }
    }
}
