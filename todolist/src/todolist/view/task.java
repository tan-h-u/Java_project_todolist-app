package todolist.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import todolist.model.Taskmodel;
import todolist.model.Priority;
import todolist.model.RepeatType;
import todolist.model.Status;
import todolist.model.TaskData;

public class task {
	private Runnable onFinishCallback;

	public task(Runnable onFinishCallback) {
	    this.onFinishCallback = onFinishCallback;
	}

	public task() {
	    this(null); // 保留原本無參版本
	}


    public void start(Stage parentStage) {
        Stage taskStage = new Stage();
        taskStage.setTitle("新增任務");
        taskStage.initModality(Modality.WINDOW_MODAL);
        taskStage.initOwner(parentStage);

        /* ---------- 表單欄位 ---------- */
        TextField nameField = new TextField();
        nameField.setPromptText("請輸入任務名稱");

        DatePicker dueDatePicker = new DatePicker();
        Spinner<Integer> dueHourSpinner   = new Spinner<>(7, 23, 0);
        Spinner<Integer> dueMinuteSpinner = new Spinner<>(0, 59, 0);
        
        DatePicker startDatePicker = new DatePicker();
        Spinner<Integer> startHourSpinner   = new Spinner<>(7, 23, 0);
        Spinner<Integer> startMinuteSpinner = new Spinner<>(0, 59, 0);

        TextField locationField = new TextField();
        locationField.setPromptText("請輸入地點");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("請輸入詳細內容");
        descriptionArea.setPrefRowCount(3);

        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("高", "中", "低");
        priorityCombo.setPromptText("選擇優先級");

        TextField tagField = new TextField();
        tagField.setPromptText("輸入標籤（例如：工作、學習）");

        /* ---------- 新增按鈕 ---------- */
        Button confirmButton = new Button("新增");
        confirmButton.setOnAction(e -> {
            String title = nameField.getText().trim();
            if (title.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "請輸入任務名稱", ButtonType.OK).showAndWait();
                return;
            }

            /* 若日後要恢復 startTime，直接取消上方註解並使用即可*/
            LocalDateTime startDateTime = LocalDateTime.of(
                startDatePicker.getValue(),
                LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue())
            );
            

            // 截止日＋時間
            LocalDateTime dueDateTime = LocalDateTime.of(
                dueDatePicker.getValue(),
                LocalTime.of(dueHourSpinner.getValue(), dueMinuteSpinner.getValue())
            );
            

            // 建立 Taskmodel（Taskmodel 目前只收 LocalDate，因此傳 date 部分即可）
            Taskmodel task = new Taskmodel();
            task.setTitle(title);
            task.setDescription(descriptionArea.getText());
            task.setStartTime(startDateTime);   // ✅ 含時間
            task.setEndTime(dueDateTime);       // ✅ 含時間
            task.setPriority(parsePriority(priorityCombo.getValue()));
            task.setCompleted(false);
            task.setTag(tagField.getText());
            task.setRepeatType(RepeatType.NONE);
            task.setStatus(Status.TO_DO);

            TaskData.addTask(task);
            System.out.println("✅ 已儲存任務：" + task);
            // TODO: TaskData.addTask(task); 或 DAO.insert(task);
            System.out.println("✅ 新增任務：" + task);

            taskStage.close();
        });

        /* ---------- 排版 ---------- */
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);

        layout.getChildren().addAll(
            new Label("任務名稱"), nameField,
            new Label("開始日期與時間"),
            new HBox(10, startDatePicker, startHourSpinner, new Label(":"), startMinuteSpinner),
            new Label("截止日期與時間"),
            new HBox(10, dueDatePicker, dueHourSpinner, new Label(":"), dueMinuteSpinner),
            new Label("地點"), locationField,
            new Label("詳細內容"), descriptionArea,
            new Label("優先級"), priorityCombo,
            new Label("標籤"), tagField,
            confirmButton
        );

        taskStage.setScene(new Scene(layout, 400, 620));
        taskStage.show();
    }

    /* 將「高 / 中 / 低」字串轉 Enum；預設 MEDIUM */
    private Priority parsePriority(String val) {
        if (val == null) return Priority.MEDIUM;
        return switch (val) {
            case "高" -> Priority.HIGH;
            case "低" -> Priority.LOW;
            default   -> Priority.MEDIUM;
        };
    }
    
}
