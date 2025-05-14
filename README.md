# Java_project_todolist-app
基本功能
• 任務新增/刪除：可輸入標題、描述、截止日、優先順序、標籤
• 任務完成切換：任務可點選切換完成與否狀態
• 任務排序：根據優先順序、截止日排序
• 彈出提醒視窗：當任務接近截止日（例如剩 1 天）自動提醒
• 一週日曆檢視：顯示最近七天任務清單（含已完成與未完成）
• 統計分析：計算總任務數、完成率、未完成任務
進階個人化功能建議
• 任務分類 Tag：使用者可自定分類（如學業、工作、生活）並標記任務
• 重複任務設定：設定每日、每週、每月重複（適合例行公事）
• 主題切換：支援深色模式 / 淺色模式（改善視覺疲勞）
• 音效提醒：自選提示音效果（例如：系統鈴聲、簡單提示音）
• 匯出行事曆：可匯出為 CSV / PDF 檔案儲存備份
• 個人資料設定：可自訂使用者名稱、頭像、每日提醒時間
• 自動備份：每次開啟程式時自動備份上次資料到指定路徑
• 到期日視覺提示：任務距離截止日越近，背景或文字顏色越強烈，提醒使用者
四、系統架構（MVC + DAO 架構）
JavaFX 視覺介面（main.fxml） ←→ MainController.java
                                    ↓
                              TodoDAO.java ←→ MySQL 資料庫（calendar_db）
                                    ↑
                                 Todo.java
五、兩週開發時程規劃（兩人分工）
第 1 週：核心功能開發
第 2 週：功能擴充與測試
六、補充功能詳解：到期日視覺提示
當任務接近截止日（例如剩 3 天內），其背景顏色或文字顏色會逐漸變暗或變紅，提示使用者這個任務快到期了，可用 JavaFX TableView 的 cellFactory 實作條件變色。
七、參考資源
•	• JavaFX 官方網站：https://openjfx.io/
•	• W3Schools SQL 課程：https://www.w3schools.com/sql/
•	• Makery JavaFX 教學：https://code.makery.ch/library/javafx-tutorial/
•	• iText PDF 匯出工具：https://itextpdf.com/
