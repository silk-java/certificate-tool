package cn.juhe.zjsb.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DragAndDropImageExample extends Application {

    private HBox hbox;
    private final double boxWidth = 200;
    private final double boxHeight = 200;

    @Override
    public void start(Stage primaryStage) {
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane, 400, 300);

        hbox = new HBox();
        hbox.setPrefSize(boxWidth, boxHeight);
        hbox.setStyle("-fx-border-width: 2px; -fx-border-color: transparent;");
        hbox.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles() || event.getDragboard().hasUrl()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
                hbox.setStyle("-fx-border-width: 2px; -fx-border-color: red;");
            }
            event.consume();
        });

        hbox.setOnDragDropped(event -> {
            if (event.getDragboard().hasFiles()) {
                Image image = new Image("file:" + event.getDragboard().getFiles().get(0).getAbsolutePath());
                updateImageView(image);
            } else if (event.getDragboard().hasUrl()) {
                Image image = new Image(event.getDragboard().getUrl());
                updateImageView(image);
            }
            hbox.setStyle("-fx-border-width: 2px; -fx-border-color: transparent;");
            event.setDropCompleted(true);
            event.consume();
        });

        anchorPane.getChildren().add(hbox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Drag and Drop Image Example");
        primaryStage.show();
    }

    private void updateImageView(Image image) {
        ImageView imageView = new ImageView(image);
        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();
        double scaleFactor = Math.min(boxWidth / imgWidth, boxHeight / imgHeight);
        imageView.setFitWidth(imgWidth * scaleFactor);
        imageView.setFitHeight(imgHeight * scaleFactor);
        hbox.getChildren().clear();
        hbox.getChildren().add(imageView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
