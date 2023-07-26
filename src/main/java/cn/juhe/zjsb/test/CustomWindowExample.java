package cn.juhe.zjsb.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomWindowExample extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        root.setPrefWidth(400);
        root.setPrefHeight(300);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        // 实现拖拽功能
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // 水平排列的按钮
        HBox buttonBox = new HBox(5);
        buttonBox.setLayoutX(5);
        buttonBox.setLayoutY(5);
        buttonBox.setPadding(new Insets(5));

        // 最小化按钮
        Button minimizeButton = new Button("-");
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        // 关闭按钮
        Button closeButton = new Button("x");
        closeButton.setOnAction(event -> primaryStage.close());

        // 最大化按钮
        Button maximizeButton = new Button("□");
        maximizeButton.setOnAction(event -> {
            if (primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(false);
                // 重置窗口大小和位置为之前的设置
                primaryStage.setWidth(400);
                primaryStage.setHeight(300);
            } else {
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());
                primaryStage.setFullScreen(true);
            }
        });

        buttonBox.getChildren().addAll(closeButton, minimizeButton, maximizeButton);
        root.getChildren().add(buttonBox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
