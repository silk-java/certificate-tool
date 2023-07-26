package cn.juhe.zjsb.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DragAndDropExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane, 400, 300);

        Label label = new Label("Hello, World!");
        label.setLayoutX(50);
        label.setLayoutY(50);
        label.setOnDragDetected(event -> {
            Dragboard dragboard = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            dragboard.setContent(content);
            //增加拖拽时候图片的功能
            Text text = new Text(label.getText());
            WritableImage writableImage = new WritableImage((int) label.getWidth(), (int) label.getHeight());
            text.snapshot(new SnapshotParameters(),writableImage);
            dragboard.setDragView(writableImage);
        });

        TextField textField = new TextField();
        textField.setLayoutX(200);
        textField.setLayoutY(150);
          textField.setOnDragOver(event -> {
            if (event.getGestureSource() != textField && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        textField.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                textField.setText(dragboard.getString());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        //拖拽完，如果想要原来的label消失，则调用这个方法
        label.setOnDragDone(dragEvent -> {
            if (dragEvent.getTransferMode()==TransferMode.MOVE){
                label.setText("");
            }
        });
        anchorPane.getChildren().addAll(label, textField);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Drag and Drop Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
