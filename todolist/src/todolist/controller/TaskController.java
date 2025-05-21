package todolist.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import todolist.model.*;

import java.time.*;

/** 任務對話框控制器 */
public class TaskController {

    /* ==== 對應 FXML 元件 ==== */
    @FXML private TextField nameField;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Spinner<Integer> startHourSpinner, startMinuteSpinner;
    @FXML private Spinner<Integer> endHourSpinner, endMinuteSpinner;
    @FXML private TextField locationField, tagField;
    @FXML private TextArea detailArea;
    @FXML private ComboBox<String> priorityCombo;
    @FXML private ComboBox<String> repeatCombo;
    @FXML private CheckBox completedCheck;
    @FXML private Button confirmBtn;

    /* === 回傳結果用 === */
    private Taskmodel result;
    public Taskmodel getResult() {
        return result;
    }

    /** 初始化 UI 控制元件 */
    @FXML private void initialize() {
        initSpinner(startHourSpinner, 0, 23, 9);
        initSpinner(startMinuteSpinner, 0, 59, 0);
        initSpinner(endHourSpinner,   0, 23, 10);
        initSpinner(endMinuteSpinner, 0, 59, 0);

        priorityCombo.getItems().addAll("高", "中", "低");
        repeatCombo.getItems().addAll("不重複", "每日", "每週", "每月");

        priorityCombo.setValue("中");
        repeatCombo.setValue("不重複");
        completedCheck.setSelected(false);

        confirmBtn.setOnAction(e -> onConfirm());
    }

    private void initSpinner(Spinner<Integer> spinner, int min, int max, int def) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, def));
        spinner.setEditable(true);
    }

    /** 處理確認按鈕邏輯 */
    private void onConfirm() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("請輸入任務名稱");
            return;
        }
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showAlert("請選擇開始與結束日期");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(
                startDatePicker.getValue(),
                LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()));

        LocalDateTime endDateTime = LocalDateTime.of(
                endDatePicker.getValue(),
                LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue()));

        // 使用結束日作為 dueDate（符合你的 model.Task）
        LocalDate dueDate = endDatePicker.getValue();

        // 建立 Task 實體
        result = new Taskmodel(
                nameField.getText(),
                detailArea.getText(),
                dueDate,
                parsePriority(priorityCombo.getValue()),
                completedCheck.isSelected(),
                tagField.getText(),
                parseRepeatType(repeatCombo.getValue()),
                Status.TO_DO  // 預設狀態為尚未開始
        );

        ((Stage) confirmBtn.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }

    private Priority parsePriority(String value) {
        return switch (value) {
            case "高" -> Priority.HIGH;
            case "中" -> Priority.MEDIUM;
            case "低" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }

    private RepeatType parseRepeatType(String value) {
        return switch (value) {
            case "每日" -> RepeatType.DAILY;
            case "每週" -> RepeatType.WEEKLY;
            case "每月" -> RepeatType.MONTHLY;
            default -> RepeatType.NONE;
        };
    }
}
