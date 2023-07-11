/**
 * Sample Skeleton for 'downprogress.fxml' Controller Class
 */

package cn.juhe.zjsb;


import cn.juhe.zjsb.event.DownLoadEvent;
import cn.juhe.zjsb.event.DownProgressEvent;
import cn.juhe.zjsb.util.EventBusUtil;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DownProgressController {
    private static final Logger log = LoggerFactory.getLogger(DownProgressController.class);


    @FXML
    public ProgressBar downProgressBar;
    public DownLoadTask myTask;
    @FXML
    private Button finishButton;

    @FXML
    private ImageView finishImg;

    @FXML
    void close(MouseEvent ignoredEvent) {
        Stage stage = (Stage) this.finishButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    void initialize() {
        EventBusUtil.getDefault().register(this);
        downProgressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("当前下载进度：" + newValue);
            if (newValue.doubleValue() >= 1) {
                finishButton.setVisible(true);
                finishImg.setVisible(true);
            }
        });
    }

    @Subscribe
    private void updateProgress(DownProgressEvent downProgressEvent) {
        System.out.println("更新下载进度" + downProgressEvent);
        downProgressBar.setProgress(downProgressEvent.value());
    }

    @Subscribe
    private void subscribe(DownLoadEvent downLoadEvent) {
        System.out.println("接收到下载事件：{}" + downLoadEvent);
        myTask = new DownLoadTask(downLoadEvent.totalpage(), downLoadEvent.dirname(), downLoadEvent.filename());
        //设置延迟启动的时间
        myTask.setDelay(Duration.millis(100));
        //设置运行时间间隔
        myTask.setPeriod(Duration.millis(Integer.MAX_VALUE));
        myTask.start();
    }
}
