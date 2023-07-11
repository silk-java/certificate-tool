package cn.juhe.zjsb;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 900);
        stage.setTitle("证件识别!");
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();

        // 注册关闭事件处理器
        stage.setOnCloseRequest(event -> {
            event.consume(); // 阻止默认的关闭操作
            // 显示确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认退出");
            alert.setHeaderText("确定要退出应用吗？");
            alert.setContentText("点击确定退出应用。");
            alert.initOwner(stage);
            // 处理对话框按钮点击事件
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                // 执行退出操作
                stage.close();
                System.exit(0);
                Platform.exit();
            } else {
                System.out.println("cancel");
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}