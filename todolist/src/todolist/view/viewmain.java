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

import todolist.view.task;
import todolist.view.week;

public class viewmain {

    public void start(Stage primaryStage) {
        // 任務容器：顯示所有任務的格子
        FlowPane taskPane = new FlowPane();
        taskPane.setHgap(10);
        taskPane.setVgap(10);
        taskPane.setPadding(new Insets(10));
        taskPane.setPrefWrapLength(600); // 每行最大寬度

        // 模擬一些任務格子
        for (int i = 1; i <= 6; i++) {
            VBox taskCard = createTaskCard("任務 " + i);
            taskPane.getChildren().add(taskCard);
        }

        // 右下角的新增任務按鈕
        Button addButton = new Button("+");
        addButton.setStyle("-fx-font-size: 24px; -fx-background-radius: 50%; -fx-min-width: 50px; -fx-min-height: 50px;");
        addButton.setOnAction(e -> {
            task addTaskWindow = new task();
            addTaskWindow.start(primaryStage);
        });
        StackPane.setAlignment(addButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addButton, new Insets(20));
        
        // 顯示本週按鈕
        Button weekButton = new Button("week");
        weekButton.setStyle("-fx-font-size: 14px;");
        StackPane.setAlignment(weekButton, Pos.TOP_RIGHT);
        StackPane.setMargin(weekButton, new Insets(20));

        // 跳轉事件
        weekButton.setOnAction(e -> {
            week weekView = new week();
            Parent newRoot = weekView.getView(); // 正確方法名稱是 getView()
            Scene scene = new Scene(newRoot, 640, 480); // 建立新 Scene
            primaryStage.setScene(scene); // 切換畫面
        });


        
        // 最外層 StackPane：讓任務清單和按鈕重疊顯示
        StackPane root = new StackPane();
        root.getChildren().addAll(taskPane, addButton, weekButton);

        Scene scene = new Scene(root, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("待辦清單");
        primaryStage.show();
    }

    private VBox createTaskCard(String title) {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setSpacing(5);
        box.setPrefSize(120, 100);
        box.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(10), Insets.EMPTY)));
        box.getChildren().add(new Text(title));
        return box;
    }
}
