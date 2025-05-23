package todolist.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javafx.stage.Stage;

import todolist.model.Taskmodel;
import todolist.controller.TaskService;

import java.time.*;
import java.time.Duration;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class week extends Pane {

    private static final int START_HOUR  = 7;
    private static final int END_HOUR    = 24;
    private static final double head_add = 4;
    private static final double ROW_H    = 40;
    private static final double TIME_COL_W = 60;
    private static final double DAY_COL_W  = 100;
    private static final double HEADER_H   = 35;
    private static final int CELL_HEIGHT = 40;
    private static final double HGAP = 1;
    private static final Font HEADER_FONT = Font.font("Roboto", FontWeight.BOLD, 15);

    private final double viewWidth  = TIME_COL_W + 7 * DAY_COL_W;
    private final double viewHeight = HEADER_H + (END_HOUR - START_HOUR + 1) * ROW_H;

    private final Line nowLine = new Line();
    private final Stage primaryStage;
    private final LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
    private final Pane taskLayer = new Pane();

    public week(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setPrefSize(viewWidth, viewHeight);
        setMinSize(viewWidth, viewHeight);
        setMaxSize(viewWidth, viewHeight);
        drawHeaders();
        drawHourRows();
        startNowLineTicker();
        getChildren().add(taskLayer);
        drawVerticalLines();   
        setupNowLine();
        placeTasks();
    }

    public Parent getView() {
        Button backButton = new Button("返回主頁");
        backButton.setOnAction(e -> {
            viewmain mainView = new viewmain();
            mainView.start(primaryStage);
        });
        backButton.setFont(Font.font(12));
        backButton.setStyle("-fx-background-color: #dddddd;");
        backButton.layoutXProperty().bind(widthProperty().subtract(100));
        backButton.layoutYProperty().bind(heightProperty().subtract(40));

        getChildren().add(backButton);
        return this;
    }

    private void drawHeaders() {
        LocalDate today = LocalDate.now();
        Label tzLabel = new Label();
        tzLabel.setPrefSize(TIME_COL_W, HEADER_H);
        tzLabel.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(tzLabel);

        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            DayOfWeek dow = date.getDayOfWeek();
            double x = TIME_COL_W + i * DAY_COL_W;

            VBox headerBox = new VBox();
            headerBox.setLayoutX(x);
            headerBox.setLayoutY(0);
            headerBox.setPrefSize(DAY_COL_W, HEADER_H);
            headerBox.setAlignment(Pos.CENTER);
            headerBox.setStyle("-fx-border-color:#e0e0e0; -fx-border-width:0 0 1 1;" +
                    (date.equals(today) ? "-fx-background-color:#1160ff;" : ""));

            Label dayName = new Label(dow.getDisplayName(TextStyle.SHORT, Locale.TAIWAN));
            dayName.setFont(HEADER_FONT);
            dayName.setTextFill(date.equals(today) ? Color.WHITE : Color.BLACK);

            Label dayNum = new Label(String.valueOf(date.getDayOfMonth()));
            dayNum.setTextFill(date.equals(today) ? Color.WHITE : Color.BLACK);

            headerBox.getChildren().addAll(dayName, dayNum);
            getChildren().add(headerBox);
        }
    }

    private void drawHourRows() {
        for (int h = START_HOUR; h <= END_HOUR; h++) {
            double y = HEADER_H + (h - START_HOUR) * ROW_H + head_add;

            Label timeLbl = new Label(String.format("%02d:00", h));
            timeLbl.setLayoutX(4);
            timeLbl.setLayoutY(y - 8);
            timeLbl.setTextAlignment(TextAlignment.RIGHT);
            timeLbl.setFont(Font.font(12));
            getChildren().add(timeLbl);

            Line line = new Line(TIME_COL_W, y, viewWidth, y);
            line.setStroke(Color.web("#e0e0e0"));
            getChildren().add(line);
        }
    }

    private void drawVerticalLines() {
        for (int i = 0; i <= 7; i++) {
            double x = TIME_COL_W + i * DAY_COL_W;
            Line vLine = new Line(x, HEADER_H, x, viewHeight - 38);
            vLine.setStroke(Color.GRAY);
            getChildren().add(vLine);
        }
    }

    private void setupNowLine() {
        int todayIndex = LocalDate.now().getDayOfWeek().getValue() - 1;
        double startX = TIME_COL_W + todayIndex * DAY_COL_W;
        double endX = startX + DAY_COL_W;
        nowLine.setStroke(Color.RED);
        nowLine.setStrokeWidth(2);
        nowLine.setStartX(startX);
        nowLine.setEndX(endX);
        getChildren().add(nowLine);
    }

    private void startNowLineTicker() {
        Timeline ticker = new Timeline(new KeyFrame(javafx.util.Duration.seconds(30), e -> refreshNowLine()));
        ticker.setCycleCount(Timeline.INDEFINITE);
        ticker.play();
        refreshNowLine();
    }

    private void refreshNowLine() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour < START_HOUR || hour > END_HOUR) {
            nowLine.setVisible(false);
            return;
        }

        nowLine.setVisible(true);
        int minutes = now.getMinute();
        double y = HEADER_H + (hour - START_HOUR + minutes / 60.0) * ROW_H + head_add;
        nowLine.setStartY(y);
        nowLine.setEndY(y);
    }

    private void placeTasks() {
        List<Taskmodel> tasks = new TaskService().getSortedTasks();
        for (Taskmodel t : tasks) {
            if (t.getStartTime() == null || t.getEndTime() == null) continue;
            LocalDate startDate = t.getStartTime().toLocalDate();
            LocalDate endDate   = t.getEndTime().toLocalDate();
            if (!isDateInWeek(startDate) && !isDateInWeek(endDate)) continue;
            if (!startDate.equals(endDate)) continue;
            addTaskBlock(t);
        }
    }

    private boolean isDateInWeek(LocalDate date) {
        return !date.isBefore(monday) && !date.isAfter(monday.plusDays(6));
    }

    private void addTaskBlock(Taskmodel task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end   = task.getEndTime();
        int dayIdx = start.getDayOfWeek().getValue() - 1;

        double offsetHrs  = (start.getHour() + start.getMinute() / 60.0) - START_HOUR;
        double durationHrs = Duration.between(start, end).toMinutes() / 60.0;

        double layoutY = offsetHrs * CELL_HEIGHT;
        double height  = durationHrs * CELL_HEIGHT;

        Pane block = new Pane();
        block.setLayoutX(TIME_COL_W + dayIdx * (DAY_COL_W ));
        block.setLayoutY(layoutY + HEADER_H + HGAP + 3);
        block.setPrefWidth(DAY_COL_W);
        block.setPrefHeight(height);
        block.setStyle("-fx-background-color:#90caf9; -fx-border-radius:4; -fx-background-radius:4;");

        Text title = new Text(task.getTitle());
        title.setFill(Color.BLACK);
        title.setWrappingWidth(DAY_COL_W - 8);
        title.setLayoutX(4);
        title.setLayoutY(14);
        block.getChildren().add(title);

        taskLayer.getChildren().add(block);
    }
}