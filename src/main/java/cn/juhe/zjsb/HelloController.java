package cn.juhe.zjsb;

import cn.juhe.zjsb.entity.APIResult;
import cn.juhe.zjsb.event.DownLoadEvent;
import cn.juhe.zjsb.task.MyJob;
import cn.juhe.zjsb.task.TaskManger;
import cn.juhe.zjsb.util.EventBusUtil;
import cn.juhe.zjsb.util.MybatisUtil;
import com.google.common.eventbus.Subscribe;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.ibatis.session.SqlSession;
import org.quartz.SchedulerException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HelloController {
    public static final String RUN_MODEL_REAL = "真实模式";
    public static final String RUN_MODEL_TEST = "测试模式";
    public static int ROWS_PER_PAGE = 15; // 每页显示的行数
    public static String RUN_MODEL = "真实模式"; // 每页显示的行数
    public static Integer RUN_SPEED = 1; // 运行速度

    @FXML
    public ScrollPane sc;
    @FXML
    public TableColumn<APIResult, String> filename;
    @FXML
    public Label finishAndTotal;

    private final Queue<String> logQueue = new LinkedList<>();
    ;
    private static final int MAX_LOGS = 10;
    @FXML
    public TableView<APIResult> tableview;

    @FXML
    public TableColumn<APIResult, String> name;
    @FXML
    public Pagination pageBar;

    public ImageView newImg;
    @FXML
    public Rectangle rightr;
    Timeline timeline = new Timeline();

    @FXML
    private TableColumn<APIResult, String> address;

    @FXML
    private TableColumn<APIResult, String> birthday;
    public int picCount;
    public static int finished;
    FillTransition ft2 = new FillTransition();

    @FXML
    private TextField chooseDir;
    public static ObservableList<APIResult> list = FXCollections.observableArrayList();


    @FXML
    private TableColumn<APIResult, Image> headImg;

    @FXML
    private TableColumn<APIResult, String> idno;

    @FXML
    private TableColumn<APIResult, String> minzu;
    public static int offset;

    FadeTransition ft = new FadeTransition();
    FadeTransition ft1 = new FadeTransition();

    @FXML
    private ProgressBar progress;

    @FXML
    private TableColumn<APIResult, String> sex;

    @FXML
    private TextArea textareaLog;

    @FXML
    void onBeginAction(ActionEvent ignoredEvent) throws SchedulerException {
        String msg = null;
        if (chooseDir.getText() == null || chooseDir.getText().isEmpty()) {
            msg = "请选择要识别的文件夹";
        }
        if (MyJob.APPKEY == null || MyJob.APPKEY.isEmpty() || !MyJob.APPKEY.matches("^[0-9a-zA-Z]{32}$")) {
            msg = "请填写正确的appkey";
        }
        if (msg != null) {
            alertAppKey(msg);
            //无论用户点击了什么，都不往下执行，只有填写了appkey才能开始操作
            return;
        }
        TaskManger.startJobs();
        System.out.println("任务启动完成");
        playTimeline();


    }

    /**
     * 启动动画
     */
    private void playTimeline() {
        rightr.setVisible(true);

        timeline.play();

        ft.play();

        ft1.play();
        ft2.play();
    }

    /**
     * alert提醒key未配置
     */
    private static void alertAppKey(String msg) {
        //请确认是否配置了正确的appkey
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText("警告信息");
        alert.setContentText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK button clicked");
        } else {
            System.out.println("Dialog closed without clicking OK");
        }
    }

    @FXML
    void initialize() {
        initAnimation();
        loadDBConfig();
        initTableView();
        timer.start();
        EventBusUtil.getDefault().register(this);
        configOtherControl();
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //x轴初始位置
        KeyValue kv1 = new KeyValue(rightr.translateXProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.seconds(0), "kf1", kv1);
        //2秒钟之后，移动结束位置
        KeyValue kv2 = new KeyValue(rightr.translateXProperty(), -352);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(2), "kf2", kv2);
        //初始角度
        KeyValue kv3 = new KeyValue(rightr.rotateProperty(), 0);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0), "kf3", kv3);
        //2秒钟之后旋转结束角度
        KeyValue kv4 = new KeyValue(rightr.rotateProperty(), 360 * 3);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(2), "kf4", kv4);
        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        //设置进度条闪烁
        ft.setNode(progress);
        ft.setFromValue(1);
        ft.setToValue(0.2);
        ft.setDuration(Duration.seconds(1));
        ft.setAutoReverse(true);
        ft.setCycleCount(Animation.INDEFINITE);
        //设置矩形闪烁
        ft1.setNode(rightr);
        ft1.setFromValue(1);
        ft1.setToValue(0.5);
        ft1.setDuration(Duration.seconds(0.5));
        ft1.setAutoReverse(true);
        ft1.setCycleCount(Animation.INDEFINITE);
        //设置颜色动画
        ft2.setDuration(Duration.seconds(1));
        ft2.setShape(rightr);
        ft2.setFromValue(Color.valueOf("#0000CD"));
        ft2.setToValue(Color.valueOf("#EE1289"));
        ft2.setAutoReverse(true);
        ft2.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * 其他控件初始化
     */
    private void configOtherControl() {
        // 绑定TextArea的宽度和高度到ScrollPane
        textareaLog.prefWidthProperty().bind(sc.widthProperty());
        textareaLog.prefHeightProperty().bind(sc.heightProperty());
        pageBar.setCurrentPageIndex(0);
        pageBar.setPageFactory(this::createPage);
    }

    private static final int ITEMS_PER_PAGE = 10;

    private TableView<APIResult> createPage(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int TOTAL_ITEMS = list.size();
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, TOTAL_ITEMS);
        List<APIResult> subList = list.subList(fromIndex, toIndex);
        tableview.setItems(FXCollections.observableArrayList(subList));
        return tableview;
    }

    /**
     * 初始化tableview各个列属性
     */
    private void initTableView() {
        // 自动调整列宽度
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        address.setCellValueFactory(param -> param.getValue().addressProperty());
        sex.setCellValueFactory(param -> param.getValue().sexProperty());
        minzu.setCellValueFactory(param -> param.getValue().minzuProperty());
        idno.setCellValueFactory(param -> param.getValue().idnoProperty());
        filename.setCellValueFactory(param -> param.getValue().filenameProperty());
        name.setCellValueFactory(param -> param.getValue().idnoProperty());
        birthday.setCellValueFactory(param -> param.getValue().birthdayProperty());
        headImg.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHeadImg()));

        // 设置图片列的CellFactory，自定义显示图片的TableCell
        headImg.setCellFactory(new Callback<>() {
            @Override
            public TableCell<APIResult, Image> call(TableColumn<APIResult, Image> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Image image, boolean empty) {
                        super.updateItem(image, empty);

                        if (empty || image == null) {
                            setGraphic(null);
                        } else {
                            // 设置 ImageView 的大小
                            double desiredWidth = 20; // 自定义宽度
                            double desiredHeight = 20; // 自定义高度
                            imageView.setFitWidth(desiredWidth);
                            imageView.setFitHeight(desiredHeight);
                            imageView.setImage(image);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        tableview.setItems(list);

    }

    /**
     * 从数据库加载key和运行模式
     */
    private static void loadDBConfig() {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        ApikeyMapper mapper = sqlSession.getMapper(ApikeyMapper.class);
        String select = mapper.select();
        if (select != null && select.length() == 32) {
            MyJob.APPKEY = select;
        }
        HelloController.RUN_MODEL = mapper.selectRunmodel();
        sqlSession.close();
    }

    /**
     * 监听日志
     */
    @Subscribe
    void subscribeLog(String log) {
        simulateLogOutput(log);
    }

    /**
     * 监听识别结果
     */
    @Subscribe
    void subscribeData(APIResult data) throws InterruptedException {
        newImg.setFitWidth(100);
        newImg.setFitHeight(100);
        Image tmo = data.getHeadImg();
        newImg.setPreserveRatio(true); // 保持宽高比
        newImg.setImage(tmo);
        System.out.println("收到消息：" + data);
        simulateLogOutput(data.toString());
        list.add(0, data);
        finished++;
        //此处更新进度条
        Platform.runLater(() -> {
            finishAndTotal.setText(finished + "/" + picCount);
            progress.setProgress((finished / (double) picCount));
            //设置总页数
            int totalPage;
            if (finished % ITEMS_PER_PAGE == 0) {
                totalPage = finished / ITEMS_PER_PAGE;
            } else {
                totalPage = finished / ITEMS_PER_PAGE + 1;
            }
            pageBar.setPageCount(totalPage);
            pageBar.setMaxPageIndicatorCount(totalPage);
            pageBar.setPageFactory(this::createPage);

        });

    }

    /**
     * 遍历文件夹里面的文件，没有采用递归
     */
    public static Queue<String> listFiles(File folder) {
        Queue<String> list = new LinkedList<>();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory()) {
                        System.out.println(file.getAbsolutePath());
                        list.offer(file.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println(folder.getAbsolutePath());
        }
        return list;
    }

    @FXML
    public void onChooseAction(ActionEvent ignoredEvent) {
        System.out.println("onChooseFileAction");
        //判断是否本次任务已经完成，如果没有完成则加入警告提示
        if (finished != picCount && picCount != 0) {
            // 显示确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认选择");
            alert.setHeaderText("当前任务尚未完成，确认要重新选择文件吗？");
            alert.setContentText("点击确定重新选择。");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(sc.getScene().getWindow());
            // 处理对话框按钮点击事件
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                //如果用户点击OK，需要关闭这个窗口，并且取消当前的任务
                try {
                    TaskManger.shutdown();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
                alert.close();
            } else {
                return;
            }
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择文件夹");
        // 打开文件夹选择对话框
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            // 在此处处理选择的文件夹
            System.out.println("选择的文件夹路径: " + selectedDirectory.getAbsolutePath());
            chooseDir.setText(selectedDirectory.getAbsolutePath());
            String path = chooseDir.getText();
            //此时要判断，fileQueue有没有被初始化过，如果为空，则加载，否则，不用再次加载
            File folder = new File(path); // 替换为你的文件夹路径
            MyJob.fileQueue.clear();//清空之前的
            MyJob.fileQueue = listFiles(folder);
            this.picCount = MyJob.fileQueue.size();
            HelloController.finished = 0;
            HelloController.list.clear();
        }
    }

    @FXML
    void onExportResultAction(ActionEvent ignoredEvent) throws IOException, SchedulerException {
        //判断是否本次任务已经完成，如果没有完成则加入警告提示
        if (finished != picCount && picCount != 0) {
            // 显示确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认选择");
            alert.setHeaderText("当前任务尚未完成，确认要导出结果吗？");
            alert.setContentText("点击确定导出");
            Stage stage = (Stage) pageBar.getScene().getWindow();
            alert.initOwner(stage);
            // 处理对话框按钮点击事件
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                alert.close();
                //先把任务关闭
                TaskManger.shutdown();
            } else {

                return;
            }
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.setInitialFileName(chooseDir.getText());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("excel", "*.xlsx", "xls"),
                new FileChooser.ExtensionFilter("所有类型", "*.*")
        );
        //保存
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            TaskManger.reStart();
            //此时用户点击了取消
            return;
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 在当前控制器中加载新的场景
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cn/juhe/zjsb/downprogress.fxml"));
        Parent root = loader.load();
        //此处发布下载文件事件
        EventBusUtil.getDefault().post(new DownLoadEvent(finished, chooseDir.getText(), file.getCanonicalPath()));
        Scene newScene = new Scene(root);

        Stage currentStage = new Stage();
        currentStage.initModality(Modality.APPLICATION_MODAL);
        currentStage.initOwner(pageBar.getScene().getWindow());

        currentStage.setScene(newScene);
        currentStage.show();
    }

    @FXML
    void onPauseAction(ActionEvent ignoredEvent) throws SchedulerException {
        TaskManger.shutdown();
        System.out.println("任务关闭");
        timeline.pause();
//        ft.pause();
//        rightr.setVisible(false);
    }

    AnimationTimer timer = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            //一秒钟
//            if (now - lastUpdate >= 1_000_000_000) {
            //100毫秒
            if (now - lastUpdate >= 100_000_000) {
                // 将日志添加到队列
                // 如果日志数量超过最大值，删除最早的日志
                if (logQueue.size() > MAX_LOGS) {
                    logQueue.poll();
                }
                // 更新TextArea的内容
                updateTextArea();
                lastUpdate = now;
            }
        }
    };

    private void simulateLogOutput(String log) {
        boolean offer = logQueue.offer(log);
    }

    private void updateTextArea() {
        if (!logQueue.isEmpty()) {

            StringBuilder sb = new StringBuilder();
            for (String log : logQueue) {
                sb.append(log);
            }
            textareaLog.setText(sb.toString());

            // 自动滚动到底部
            textareaLog.selectPositionCaret(textareaLog.getLength());
            textareaLog.deselect();

            // 设置滚动条位置为最底部
//            textareaLog.setScrollTop(Double.MAX_VALUE);
        }
    }

    @FXML
    public void configAppkey(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("输入APPKEY");
        dialog.setHeaderText("请输入您在聚合数据申请的证件识别接口的appkey:");
        dialog.setContentText("key:");
        Stage stage = (Stage) pageBar.getScene().getWindow();
        dialog.initOwner(stage);
        dialog.showAndWait().ifPresent(name -> {
            // 处理用户输入的结果
            System.out.println("key: " + name);
            MyJob.APPKEY = name;
            SqlSession sqlSession = MybatisUtil.getSqlSession();
            ApikeyMapper mapper = sqlSession.getMapper(ApikeyMapper.class);
            mapper.updateApikey(name);
            sqlSession.close();
        });
    }

    public void configPagesize(ActionEvent actionEvent) {
        List<Integer> choices = new ArrayList<>();
        choices.add(5);
        choices.add(10);
        choices.add(15);
        choices.add(20);
        choices.add(30);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(15, choices);
        dialog.setTitle("每页数量");
        dialog.setHeaderText("请选择:");
        dialog.setContentText("数量:");
        Stage stage = (Stage) pageBar.getScene().getWindow();
        dialog.initOwner(stage);
        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(selectedOption -> {
            HelloController.ROWS_PER_PAGE = selectedOption;
        });
    }

    public void configRunModel(ActionEvent actionEvent) {
        List<String> choices = new ArrayList<>();
        choices.add(HelloController.RUN_MODEL_REAL);
        choices.add(HelloController.RUN_MODEL_TEST);
        ChoiceDialog<String> dialog = new ChoiceDialog<>("测试模式", choices);
        dialog.setTitle("运行模式");
        Stage stage = (Stage) pageBar.getScene().getWindow();
        dialog.initOwner(stage);
        dialog.setHeaderText("选择运行模式");
        dialog.setContentText("测试模式不会真正请求接口，识别数据为随机模拟，真实模式反之");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedOption -> {
            System.out.println("Selected option: " + selectedOption);
            HelloController.RUN_MODEL = selectedOption;
            SqlSession sqlSession = MybatisUtil.getSqlSession();
            ApikeyMapper mapper = sqlSession.getMapper(ApikeyMapper.class);
            mapper.updateRunModel(selectedOption);
            sqlSession.close();
        });
    }

    public void configRunSpeed(ActionEvent actionEvent) {
        List<Integer> choices = new ArrayList<>();
        choices.add(1);
        choices.add(2);
        choices.add(3);
        choices.add(4);
        choices.add(5);
        choices.add(6);
        choices.add(7);
        choices.add(8);
        choices.add(9);
        choices.add(10);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
        dialog.setTitle("运行速度");
        dialog.setHeaderText("选择运行速度");
        dialog.setContentText("每秒钟识别的数量");
        Stage stage = (Stage) pageBar.getScene().getWindow();
        dialog.initOwner(stage);
        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(selectedOption -> {
            System.out.println("Selected option: " + selectedOption);
            HelloController.RUN_SPEED = selectedOption;
            //此时需要重新启动任务
            if (TaskManger.IS_RUNNING) {
                try {
                    TaskManger.reStart();
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
