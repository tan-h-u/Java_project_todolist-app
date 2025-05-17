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

public class task {

    public void start(Stage parentStage) {
        Stage taskStage = new Stage();
        taskStage.setTitle("新增任務");
        taskStage.initModality(Modality.WINDOW_MODAL);
        taskStage.initOwner(parentStage);

        // 表單欄位
        TextField nameField = new TextField();
        nameField.setPromptText("請輸入任務名稱");

     // 日期選擇
        DatePicker startDatePicker = new DatePicker();
        Spinner<Integer> startHourSpinner = new Spinner<>(0, 23, 9);
        Spinner<Integer> startMinuteSpinner = new Spinner<>(0, 59, 0);

        DatePicker endDatePicker = new DatePicker();
        Spinner<Integer> endHourSpinner = new Spinner<>(0, 23, 10);
        Spinner<Integer> endMinuteSpinner = new Spinner<>(0, 59, 0);

        TextField locationField = new TextField();
        locationField.setPromptText("請輸入地點");

        TextArea detailArea = new TextArea();
        detailArea.setPromptText("請輸入詳細內容");
        detailArea.setPrefRowCount(3);

        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("高", "中", "低");
        priorityCombo.setPromptText("選擇優先級");

        TextField tagField = new TextField();
        tagField.setPromptText("輸入標籤（例如：工作、學習）");

        CheckBox completedCheckBox = new CheckBox("已完成");

        // 新增按鈕
        Button confirmButton = new Button("新增");
        confirmButton.setOnAction(e -> {
            String taskName = nameField.getText();
            if (taskName.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "請輸入任務名稱", ButtonType.OK);
                alert.showAndWait();
                return;
            }
         // 組合 LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.of(
                startDatePicker.getValue(),
                LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue())
            );

            LocalDateTime endDateTime = LocalDateTime.of(
                endDatePicker.getValue(),
                LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue())
            );

            // 顯示輸入資料（你可以改為儲存或回傳資料）
            System.out.println("名稱：" + taskName);
            System.out.println("開始時間：" + startDateTime);
            System.out.println("結束時間：" + endDateTime);
            System.out.println("地點：" + locationField.getText());
            System.out.println("內容：" + detailArea.getText());
            System.out.println("優先級：" + priorityCombo.getValue());
            System.out.println("標籤：" + tagField.getText());
            System.out.println("是否完成：" + completedCheckBox.isSelected());

            taskStage.close();
        });

        // 排版
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        layout.getChildren().addAll(
            new Label("任務名稱"), nameField,
            new Label("開始日期與時間"),
            new HBox(10, startDatePicker, startHourSpinner, new Label(":"), startMinuteSpinner),
            new Label("結束日期與時間"),
            new HBox(10, endDatePicker, endHourSpinner, new Label(":"), endMinuteSpinner),
            new Label("地點"), locationField,
            new Label("詳細內容"), detailArea,
            new Label("優先級"), priorityCombo,
            new Label("標籤"), tagField,
            completedCheckBox,
            confirmButton
        );

        Scene scene = new Scene(layout, 400, 600);
        taskStage.setScene(scene);
        taskStage.show();
    }
}
