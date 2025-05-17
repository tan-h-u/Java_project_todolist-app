package todolist.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class week {

    private static final int START_HOUR = 7;
    private static final int END_HOUR = 23;
    private static final double HOUR_HEIGHT = 40; // 每小時 40 像素

    public Parent getView() {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();
        LocalDate monday = today.minusDays(todayDayOfWeek.getValue() - 1);

        HBox weekBox = new HBox(10);
        weekBox.setAlignment(Pos.TOP_CENTER);
        weekBox.setPadding(new Insets(20));

        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);

            // 上方日期標籤
            String dayName = day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.TAIWAN);
            Label dayLabel = new Label(dayName + "\n" + day.getMonthValue() + "/" + day.getDayOfMonth());
            dayLabel.setFont(Font.font("Arial", 14));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefSize(60, 60);
            dayLabel.setStyle("-fx-border-color: gray;");

            if (day.equals(today)) {
                dayLabel.setTextFill(Color.WHITE);
                dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                dayLabel.setStyle("-fx-background-color: dodgerblue; -fx-border-color: gray;");
            }

            // 時間欄區域
            Pane timeGrid = new Pane();
            timeGrid.setPrefSize(60, (END_HOUR - START_HOUR + 1) * HOUR_HEIGHT);

            for (int h = START_HOUR; h <= END_HOUR; h++) {
                Label hourLabel = new Label(h + ":00");
                hourLabel.setFont(Font.font(12));
                hourLabel.setPrefSize(60, HOUR_HEIGHT);
                hourLabel.setLayoutY((h - START_HOUR) * HOUR_HEIGHT);
                hourLabel.setStyle("-fx-border-color: lightgray; -fx-alignment: center;");
                timeGrid.getChildren().add(hourLabel);
            }

            // 紅線：顯示目前時間（只畫在今天）
            if (day.equals(today)) {
                Line redLine = new Line(0, 0, 60, 0);
                redLine.setStroke(Color.RED);
                redLine.setStrokeWidth(2);
                redLine.setLayoutY(calculateNowY());

                timeGrid.getChildren().add(redLine);

                // 每分鐘更新紅線位置
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), e -> {
                    redLine.setLayoutY(calculateNowY());
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            // 將日期 + 時間欄包成垂直 VBox
            VBox dayColumn = new VBox(5);
            dayColumn.setAlignment(Pos.TOP_CENTER);
            dayColumn.getChildren().addAll(dayLabel, timeGrid);

            weekBox.getChildren().add(dayColumn);
        }

        return weekBox;
    }

    private double calculateNowY() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        if (hour < START_HOUR || hour > END_HOUR) {
            return -100; // 不顯示紅線
        }

        double y = (hour - START_HOUR + minute / 60.0) * HOUR_HEIGHT;
        return y;
    }
}
