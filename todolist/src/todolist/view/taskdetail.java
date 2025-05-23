package todolist.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;


import todolist.model.*;
import todolist.controller.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * TaskDetail － 任務詳細視窗
 * -------------------------------------------------------------
 * ● 以 Modal 方式顯示，阻塞父視窗操作
 * ● 可檢視 / 編輯：標題、描述、起訖時間、優先級、標籤、完成狀態
 * ● [儲存] 後立即更新 TaskData 與資料庫
 */
public class taskdetail {

    private final Taskmodel task;           // 當前編輯的任務
    private final Stage parentStage;

    /* ===== UI 元件 ===== */
    private final TextField titleField       = new TextField();
    private final TextArea  descArea         = new TextArea();
    private final DatePicker startDatePicker = new DatePicker();
    private final Spinner<Integer> startHour = new Spinner<>(0,23,0);
    private final Spinner<Integer> startMin  = new Spinner<>(0,59,0);
    private final DatePicker endDatePicker   = new DatePicker();
    private final Spinner<Integer> endHour   = new Spinner<>(0,23,0);
    private final Spinner<Integer> endMin    = new Spinner<>(0,59,0);
    private final ComboBox<String> priorityCbx = new ComboBox<>();
    private final TextField tagField         = new TextField();
    private final CheckBox finishedChk       = new CheckBox("已完成");

    public taskdetail(Stage parentStage, Taskmodel task) {
        this.parentStage = parentStage;
        this.task        = task;
    }

    /** 顯示視窗 */
    public void show() {
        Stage dlg = new Stage();
        dlg.initModality(Modality.WINDOW_MODAL);
        dlg.initOwner(parentStage);
        dlg.setTitle("任務詳情 – " + task.getTitle());

        GridPane grid = buildForm();
        populateForm();

        // 儲存按鈕
        Button saveBtn = new Button("儲存");
        saveBtn.setOnAction(e -> {
            if (applyChanges()) {
                dlg.close();
            }
        });
        GridPane.setColumnSpan(saveBtn, 2);
        grid.add(saveBtn, 0, 9);

        dlg.setScene(new Scene(grid));
        dlg.showAndWait();
    }

    /*  建立表單  */
    private GridPane buildForm() {
        GridPane g = new GridPane();
        g.setPadding(new Insets(20));
        g.setVgap(10); g.setHgap(8);

        priorityCbx.getItems().addAll("高", "中", "低");

        int r = 0;
        g.addRow(r++, new Label("標題"), titleField);
        g.addRow(r++, new Label("描述"), descArea);
        g.addRow(r++, new Label("開始"), new HBox(5,startDatePicker,startHour,new Label(":"),startMin));
        g.addRow(r++, new Label("結束"), new HBox(5,endDatePicker,endHour,new Label(":"),endMin));
        g.addRow(r++, new Label("優先級"), priorityCbx);
        g.addRow(r++, new Label("標籤"), tagField);
        g.addRow(r++, new Label("狀態"), finishedChk);

        return g;
    }

    /*  將 task 資料填入表單  */
    private void populateForm() {
        titleField.setText(task.getTitle());
        descArea.setText(task.getDescription());

        LocalDateTime st = task.getStartTime();
        LocalDateTime ed = task.getEndTime();
        if (st!=null) {
            startDatePicker.setValue(st.toLocalDate());
            startHour.getValueFactory().setValue(st.getHour());
            startMin.getValueFactory().setValue(st.getMinute());
        }
        if (ed!=null) {
            endDatePicker.setValue(ed.toLocalDate());
            endHour.getValueFactory().setValue(ed.getHour());
            endMin.getValueFactory().setValue(ed.getMinute());
        }

        priorityCbx.setValue(switch(task.getPriority()) {
            case HIGH -> "高";
            case LOW  -> "低";
            default   -> "中";
        });
        tagField.setText(task.getTag());
        finishedChk.setSelected(task.isCompleted());
    }

    /*  儲存表單內容回 Taskmodel 並同步 DB  */
    private boolean applyChanges() {
        if (titleField.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "標題不可空白", ButtonType.OK).showAndWait();
            return false;
        }

        task.setTitle(titleField.getText().trim());
        task.setDescription(descArea.getText());

        LocalDateTime newStart = LocalDateTime.of(
                startDatePicker.getValue(),
                LocalTime.of(startHour.getValue(), startMin.getValue()));
        LocalDateTime newEnd = LocalDateTime.of(
                endDatePicker.getValue(),
                LocalTime.of(endHour.getValue(), endMin.getValue()));
        task.setStartTime(newStart);
        task.setEndTime(newEnd);
        task.setPriority(parsePriority(priorityCbx.getValue()));
        task.setTag(tagField.getText());
        task.setCompleted(finishedChk.isSelected());

        // ✅ 用實例呼叫非 static 方法
        TaskService service = new TaskService();
        service.updateTask(task);

        return true;
    }


    /*  字串轉優先級  */
    private Priority parsePriority(String s) {
        return switch (s) {
            case "高" -> Priority.HIGH;
            case "低" -> Priority.LOW;
            default -> Priority.MEDIUM;
        };
    }
}
