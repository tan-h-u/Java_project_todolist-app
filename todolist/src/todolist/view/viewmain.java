package todolist.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;

import todolist.controller.TaskService;
import todolist.view.task;
import todolist.view.week;
import todolist.model.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 主畫面 (依任務狀態分組 + 新增按鈕 + Week 視圖切換)
 */
public class viewmain {
	

    public void start(Stage primaryStage) {
        /* ---------- 狀態區塊：尚未開始 / 執行中 / 已完成 ---------- */
        VBox todoBox = new VBox(10);
        todoBox.getChildren().add(new Text("尚未開始"));
        todoBox.setPadding(new Insets(10));
        todoBox.setPrefWidth(200);

        VBox doingBox = new VBox(10);
        doingBox.getChildren().add(new Text("執行中"));
        doingBox.setPadding(new Insets(10));
        doingBox.setPrefWidth(200);

        VBox doneBox = new VBox(10);
        doneBox.getChildren().add(new Text("已完成"));
        doneBox.setPadding(new Insets(10));
        doneBox.setPrefWidth(200);

        // 從資料庫載入所有任務
        for (Taskmodel task : TaskData.getAllTasks()) {  // ➜ 從 SQL 讀取
        	VBox card = createTaskCard(primaryStage,task); // ✅ 傳整個物件進去

            switch (task.getStatus()) {                  // Status enum
                case TO_DO       -> todoBox.getChildren().add(card);
                case IN_PROGRESS -> doingBox.getChildren().add(card);
                case DONE        -> doneBox.getChildren().add(card);
            }
        }

        HBox taskPane = new HBox(20, todoBox, doingBox, doneBox);
        taskPane.setPadding(new Insets(10));

        /* ---------- 右下「新增任務」 ---------- */
        Button addButton = new Button("+");
        addButton.setStyle("""
            -fx-font-size: 24px;
            -fx-background-radius: 50%;
            -fx-min-width: 50px;
            -fx-min-height: 50px;
        """);
        addButton.setOnAction(e -> {
            task addTaskWindow = new task(() -> refreshTaskView(primaryStage, todoBox, doingBox, doneBox));
            addTaskWindow.start(primaryStage);
        });

        StackPane.setAlignment(addButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addButton, new Insets(20));

        /* ---------- 右上「Week」切換按鈕 ---------- */
        Button weekButton = new Button("Week");
        weekButton.setStyle("-fx-font-size: 14px;");
        StackPane.setAlignment(weekButton, Pos.TOP_RIGHT);
        StackPane.setMargin(weekButton, new Insets(20));

        weekButton.setOnAction(e -> {
            week weekView = new week(primaryStage);
            Parent newRoot = weekView.getView();

            double width  = 60 + 7 * 100;
            double height = 85 + (23 - 7 + 1) * 40;

            Scene scene   = new Scene(newRoot, width, height);
            primaryStage.setY(0);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        });

        /* ---------- 最外層 StackPane ---------- */
        StackPane root = new StackPane(taskPane, addButton, weekButton);
        Scene scene   = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("待辦清單");
        primaryStage.show();
    }

    /** 建立單一任務卡片 */
    private VBox createTaskCard(Stage primaryStage,Taskmodel task) {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setPrefSize(180, Region.USE_COMPUTED_SIZE);
        box.setAlignment(Pos.CENTER);
        box.setBackground(new Background(
            new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10), Insets.EMPTY)
        ));   
        box.setOnMouseClicked(e -> {
            taskdetail dlg = new taskdetail(primaryStage, task); // task 是迴圈中的 Taskmodel
            dlg.show();
        });

        

        // 顯示標題
        Text titleText = new Text( task.getTitle());
        box.getChildren().addAll(titleText);
        return box;
        
    }
    private void refreshTaskView(Stage primaryStage, VBox todoBox, VBox doingBox, VBox doneBox) {
        // 1. 清除原有任務卡（除了最上方的標題文字）
    	todoBox.getChildren().remove(1, todoBox.getChildren().size());
    	doingBox.getChildren().remove(1, doingBox.getChildren().size());
    	doneBox.getChildren().remove(1, doneBox.getChildren().size());

        // 2. 重新從 DB 撈最新任務（跳過快取）
        List<Taskmodel> tasks = TaskDAO.getAllTasks();

        // 3. 依狀態分類新增卡片
        for (Taskmodel task : tasks) {
            VBox card = createTaskCard(primaryStage, task);
            switch (task.getStatus()) {
                case TO_DO -> todoBox.getChildren().add(card);
                case IN_PROGRESS -> doingBox.getChildren().add(card);
                case DONE  -> doneBox.getChildren().add(card);
            }
        }
    }



}