package todolist.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import todolist.controller.TaskService;
import todolist.model.*;
import todolist.model.Priority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class task {

    /* ====== 建構子：可選 callback ===== */
    private final Runnable onFinishCallback;
    public task() { this(null); }
    public task(Runnable onFinishCallback) {
        this.onFinishCallback = onFinishCallback;
    }

    public void start(Stage parentStage) {
        Stage taskStage = new Stage();
        taskStage.setTitle("新增任務");
        taskStage.initModality(Modality.WINDOW_MODAL);
        taskStage.initOwner(parentStage);

        /* ---------- 表單欄位 ---------- */
        TextField nameField = new TextField();
        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        Spinner<Integer> startHour   = new Spinner<>(7, 23, 0);
        Spinner<Integer> startMinute = new Spinner<>(0, 59, 0);
        DatePicker endDatePicker   = new DatePicker(LocalDate.now());
        Spinner<Integer> endHour   = new Spinner<>(8, 23, 0);
        Spinner<Integer> endMinute = new Spinner<>(0, 59, 0);
        TextArea  descArea = new TextArea();
        ComboBox<String> priorityCbx = new ComboBox<>();
        priorityCbx.getItems().addAll("高","中","低");
        TextField tagField = new TextField();

        /* ---------- 新增按鈕 ---------- */
        Button confirmButton = new Button("新增");
        confirmButton.setOnAction(e -> {
            if (nameField.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "請輸入任務名稱").showAndWait();
                return;
            }
            if (priorityCbx.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "請選擇優先級（必填）").showAndWait();
                return;
            }
            // 組合時間
            LocalDateTime start = LocalDateTime.of(
                    startDatePicker.getValue(),
                    LocalTime.of(startHour.getValue(), startMinute.getValue()));
            LocalDateTime end = LocalDateTime.of(
                    endDatePicker.getValue(),
                    LocalTime.of(endHour.getValue(), endMinute.getValue()));

            // 建立 Taskmodel
            Taskmodel t = new Taskmodel();
            t.setTitle(nameField.getText().trim());
            t.setDescription(descArea.getText());
            t.setStartTime(start);
            t.setEndTime(end);
            t.setPriority(parsePriority(priorityCbx.getValue()));
            t.setTag(tagField.getText());
            t.setRepeatType(RepeatType.NONE);
            t.setStatus(Status.TO_DO);
            t.setCompleted(false);

            // 寫入資料庫
            new TaskService().addTask(t);

            // 通知主畫面刷新
            if (onFinishCallback != null) onFinishCallback.run();

            taskStage.close();
        });

        VBox layout = new VBox(10,
            new Label("任務名稱"), nameField,
            new Label("開始時間"),
            new HBox(8, startDatePicker, startHour, new Label(":"), startMinute),
            new Label("結束時間"),
            new HBox(8, endDatePicker, endHour, new Label(":"), endMinute),
            new Label("描述"), descArea,
            new Label("優先級"), priorityCbx,
            new Label("標籤"), tagField,
            confirmButton
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);

        taskStage.setScene(new Scene(layout, 350, 500));
        taskStage.show();
    }

    /* 文字 → Priority */
    private Priority parsePriority(String s) {
        return switch (s) {
            case "高" -> Priority.HIGH;
            case "低" -> Priority.LOW;
            default   -> Priority.MEDIUM;
        };
    }
}
