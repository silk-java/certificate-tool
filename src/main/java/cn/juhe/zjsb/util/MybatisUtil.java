package cn.juhe.zjsb.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

//每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。
// SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。
// 而 SqlSessionFactoryBuilder 则可以从 XML 配置文件或一个预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例。
public class MybatisUtil {
    private static Logger log = LoggerFactory.getLogger(MybatisUtil.class);

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config.xml";

        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);

        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSession getSqlSession() {
        //从sqlSessionFactory中获取到SqlSession
        log.info(Thread.currentThread().getName()+"获取数据库连接");

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        log.info(Thread.currentThread().getName()+"获取数据库连接成功");
        return sqlSession;
    }

}

