package todolist;

import javafx.application.Application;
import javafx.stage.Stage;
import todolist.view.viewmain;

/**
 * JavaFX 進入點：一開啟就載入 main 畫面
 */
public class appmain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 直接把 primaryStage 交給 main
        new viewmain().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
