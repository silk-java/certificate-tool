package cn.juhe.zjsb.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerWindowImageExample extends Application {
    double xOffset, yOffset;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: lightblue; -fx-background-radius: 20;");
        anchorPane.setPrefHeight(300);
        anchorPane.setPrefWidth(400);
        Scene scene = new Scene(anchorPane);
        scene.setFill(Color.TRANSPARENT);
        // 实现拖拽功能
        anchorPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        anchorPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        // 水平排列的按钮
        HBox hBox = new HBox(5);
        hBox.setLayoutX(5);
        hBox.setLayoutY(5);
        hBox.setPadding(new Insets(5));

        //图片的目录为/src/main/resources/cn/juhe/zjsb/img/close.png
        // 最小化按钮
        ImageView minImageView = createImage("min");
        // 关闭按钮
        ImageView closeImageView = createImage("close");
        // 最大化按钮
        ImageView fullImageView = createImage("full");

        HBox closeImageViewHBox = createHbox(closeImageView);
        HBox fullImageViewHBox = createHbox(fullImageView);
        HBox minImageViewHBox = createHbox(minImageView);

        //设置点击事件
        minImageViewHBox.setOnMouseClicked(event -> primaryStage.setIconified(true));
        closeImageViewHBox.setOnMouseClicked(event -> primaryStage.close());
        fullImageViewHBox.setPickOnBounds(true); // Add this line to enable mouse events on transparent areas
        fullImageViewHBox.setOnMouseClicked(event -> {
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

        //设置图片进入为手掌,之所以设置图片放在hbox中
        //是因为有的图片小，鼠标进入时，不会变为手掌形状
        setHandCursorOnMouseOver(closeImageViewHBox, fullImageViewHBox, minImageViewHBox);
        hBox.getChildren().addAll(minImageViewHBox, fullImageViewHBox, closeImageViewHBox);
        anchorPane.getChildren().add(hBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    HBox createHbox(ImageView i) {
        return new HBox(i);
    }

    ImageView createImage(String p) {
        ImageView mi = new ImageView(new Image(getClass().getResourceAsStream("/cn/juhe/zjsb/img/" + p + ".png")));
        mi.setFitWidth(20);
        mi.setFitHeight(20);
        return mi;
    }

    void setHandCursorOnMouseOver(HBox... h) {
        for (HBox c : h) {
            c.setOnMouseEntered(event -> c.getScene().setCursor(Cursor.HAND));
            c.setOnMouseExited(event -> c.getScene().setCursor(Cursor.DEFAULT));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
