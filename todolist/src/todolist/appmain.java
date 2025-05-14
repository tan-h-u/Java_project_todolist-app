 package todolist;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class appmain extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Hello, JavaFX!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200);

        primaryStage.setTitle("JavaFX 測試");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 🔧 這是最重要的一行！這是主方法，Java 執行程式從這裡開始
    public static void main(String[] args) {
        launch(args);
    }
}
