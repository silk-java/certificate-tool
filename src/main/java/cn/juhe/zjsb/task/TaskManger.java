package cn.juhe.zjsb.task;

import cn.juhe.zjsb.HelloController;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TaskManger {
    private static final Logger log = LoggerFactory.getLogger(TaskManger.class);

    //任务是否在运行
    public  static boolean IS_RUNNING = false;
    public static String CURRENT_THREAD_NUM = "1";

    public static Scheduler scheduler;
    //线程总数量key
    private static final String COUNT_KEY = "org.quartz.threadPool.threadCount";
    //任务工程
    private static final StdSchedulerFactory FACTORY = new StdSchedulerFactory();
    //创建任务
    private static final JobDetail JOB = JobBuilder.newJob(MyJob.class)
            .withIdentity("myJob", "group1")
            .build();
    //创建触发器


    private TaskManger() {
    }


    public static synchronized void startJobs() throws SchedulerException {
        if (!IS_RUNNING) {
            reStart();
        }
    }

    /**
     * 关闭所有任务
     * 记住已经被shutdown的scheduler是不能在重新start的，需要重新初始化
     *
     * @throws SchedulerException 抛出异常
     */
    public static synchronized void shutdown() throws SchedulerException {
        if (scheduler != null && IS_RUNNING) {
            System.out.println("shutdown");
            scheduler.shutdown();
            IS_RUNNING = false;
        }
    }




    public static synchronized void reStart() throws SchedulerException {
        //更改线程数量的时候，需要先shutdown，再重新进行初始化操作
        if (scheduler != null) {
            scheduler.shutdown();
        }
        //重新加载默认配置
        Properties properties = loadDefaultProps();
        assert properties != null;
        properties.setProperty(COUNT_KEY, CURRENT_THREAD_NUM);
        //重新初始化工厂
        FACTORY.initialize(properties);
        //scheduler重新获取
        scheduler = FACTORY.getScheduler();
        log.info("线程数量：" + scheduler.getContext().get(COUNT_KEY)); // 初始线程数量为5
        //scheduler重启
        scheduler.start();
        IS_RUNNING = true;
        // 将任务和触发器关联到调度器
        Trigger TRIGGER = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(1000/ HelloController.RUN_SPEED) // 每10秒执行一次
                        .repeatForever())
                .build();
        scheduler.scheduleJob(JOB, TRIGGER);
    }

    public static synchronized Properties loadDefaultProps() {
        String filename = "quartz.properties";
        String propSrc;
        InputStream is;
        Properties props = new Properties();

        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

        try {
            if (is != null) {
                is = new BufferedInputStream(is);
                propSrc = "the specified file : '" + filename + "' from the class resource path.";
            } else {
                is = new BufferedInputStream(new FileInputStream(filename));
                propSrc = "the specified file : '" + filename + "'";
            }
            log.info(propSrc);
            props.load(is);
            return props;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignore) {
                }
        }
        return null;

    }

    public static void main(String[] args) {
        try {
            // 创建调度器
            // 创建调度器工厂
            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize();
            // 设置线程池属性
            Properties pro = loadDefaultProps();
            log.info("pro:{}", pro);
            assert pro != null;
            pro.setProperty(COUNT_KEY, "1");
            factory.initialize(pro);
            Scheduler scheduler = factory.getScheduler();


            log.info("线程数量：" + scheduler.getContext().get(COUNT_KEY)); // 初始线程数量为5
            log.info("-------------------");

            scheduler.start();

            // 创建任务
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity("myJob", "group1")
                    .build();

            // 创建触发器
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(1) // 每10秒执行一次
                            .repeatForever())
                    .build();

            // 将任务和触发器关联到调度器
            scheduler.scheduleJob(job, trigger);
            log.info("shutdown.....");
            scheduler.shutdown();
            log.info("start.....");
            scheduler = factory.getScheduler();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
