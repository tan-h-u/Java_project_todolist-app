package todolist;

import javafx.application.Application;
import javafx.stage.Stage;
import todolist.view.viewmain;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 把主畫面交給 viewmain 處理（包含 Scene 設定）
        new viewmain().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
