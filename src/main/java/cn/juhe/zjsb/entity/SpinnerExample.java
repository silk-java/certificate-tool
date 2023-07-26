package cn.juhe.zjsb.entity;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SpinnerExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个Spinner控件
        Spinner<Integer> spinner = new Spinner<>();

        // 设置Spinner的值工厂，这里我们使用整数值工厂
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinner.setValueFactory(valueFactory);

        // 可以设置其他样式，例如每次增加或减少的步长
        // valueFactory.setAmountToStepBy(5);

        // 创建一个布局并将Spinner添加进去
        StackPane root = new StackPane();
        root.getChildren().add(spinner);

        // 创建场景并显示舞台
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("JavaFX Spinner Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
