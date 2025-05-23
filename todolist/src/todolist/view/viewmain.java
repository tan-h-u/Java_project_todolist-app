package todolist.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Parent;

import todolist.controller.TaskService;
import todolist.model.Taskmodel;

import java.util.List;

/**
 * 主畫面 (依任務狀態分組 + 新增按鈕 + Week 視圖切換)
 */
public class viewmain {
	

    private final TaskService service = new TaskService();

    public void start(Stage primaryStage) {
    	
    	service.getSortedTasks();
        /* ---------- 狀態區塊 ---------- */
        VBox todoBox  = buildColumn("尚未開始");
        VBox doingBox = buildColumn("執行中");
        VBox doneBox  = buildColumn("已完成");

        // 依排序結果載入任務
        populateColumns(primaryStage, todoBox, doingBox, doneBox);

        HBox taskPane = new HBox(20, todoBox, doingBox, doneBox);
        taskPane.setPadding(new Insets(10));

        /* ---------- + 按鈕 ---------- */
        Button addButton = new Button("+");
        addButton.setStyle("""
            -fx-font-size: 24px;
            -fx-background-radius: 50%;
            -fx-min-width: 50px;
            -fx-min-height: 50px;
        """);
        addButton.setOnAction(e -> {
            task addTaskWindow = new task(
                () -> refreshTaskView(primaryStage, todoBox, doingBox, doneBox)
            );
            addTaskWindow.start(primaryStage);
        });
        StackPane.setAlignment(addButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addButton, new Insets(20));

        /* ---------- Week 按鈕 ---------- */
        Button weekButton = new Button("📅");
        weekButton.setStyle("""      		
        					-fx-font-size: 14px;
        					-fx-background-radius: 50%;
        					-fx-min-width: 50px;
        					-fx-min-height: 50px;
        		          """);
        StackPane.setAlignment(weekButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(weekButton, new Insets(0, 20, 80, 0)); 

        weekButton.setOnAction(e -> {
            week w = new week(primaryStage);
            Parent newRoot = w.getView();
            Scene scene = new Scene(newRoot, 60 + 7 * 100, 85 + (23 - 7 + 1) * 40);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        });
        
        VBox buttonBox = new VBox(10, weekButton, addButton); // 10px 間距
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(buttonBox, new Insets(20));

        /* ---------- 根節點 ---------- */
        StackPane root = new StackPane(taskPane, addButton, weekButton);
        Scene scene = new Scene(root, 800, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("待辦清單");
        primaryStage.show();
    }

    /** 產生欄位框架 + 標題 */
    private VBox buildColumn(String title) {
        VBox box = new VBox(10);
        box.getChildren().add(new Text(title));
        box.setPadding(new Insets(10));
        box.setPrefWidth(200);
        return box;
    }

    /** 依任務狀態把卡片放到三欄裡 */
    private void populateColumns(Stage stage, VBox todo, VBox doing, VBox done) {
        List<Taskmodel> tasks = service.getSortedTasks();
        for (Taskmodel t : tasks) {
            VBox card = createTaskCard(stage, t);
            switch (t.getStatus()) {
                case TO_DO       -> todo.getChildren().add(card);
                case IN_PROGRESS -> doing.getChildren().add(card);
                case DONE        -> done.getChildren().add(card);
            }
        }
    }

    /** 建立任務卡片（點擊可開詳細視窗） */
    private VBox createTaskCard(Stage stage, Taskmodel task) {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setPrefSize(180, Region.USE_COMPUTED_SIZE);
        box.setAlignment(Pos.CENTER);
        Color backgroundColor = Color.LIGHTGRAY;

		 // ✅ 如果是 DONE 卻沒完成，就變紅色
		 if (task.getStatus() == task.getStatus().DONE && !task.isCompleted()) {
		     backgroundColor = Color.SALMON;  // 或 Color.RED 也可
		 }
		
		 box.setBackground(new Background(
		     new BackgroundFill(backgroundColor, new CornerRadii(10), Insets.EMPTY)
		 ));


        box.setOnMouseClicked(e -> {
            taskdetail dlg = new taskdetail(stage, task);
            dlg.show();
        });
        
        box.getChildren().add(new Text(task.getTitle()));
        return box;
    }

    /** 刷新三欄任務卡片 */
    private void refreshTaskView(Stage stage, VBox todo, VBox doing, VBox done) {
        todo.getChildren().remove(1, todo.getChildren().size());
        doing.getChildren().remove(1, doing.getChildren().size());
        done.getChildren().remove(1, done.getChildren().size());
        populateColumns(stage, todo, doing, done);
    }
}
