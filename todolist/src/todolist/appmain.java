package todolist;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import todolist.view.viewmain;

public class appmain extends Application {
    @Override
    public void start(Stage primaryStage) {
        viewmain view = new viewmain();
        view.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}