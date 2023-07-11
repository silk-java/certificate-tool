module cn.juhe.zjsb {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires httpmime;
    requires httpclient;
    requires httpcore;
    requires quartz;
    requires slf4j.api;
    requires sqlite.jdbc;
    requires org.mybatis;
    requires java.sql;
    requires fastjson;
    requires com.google.common;
    requires poi.ooxml;
    requires lombok;
    requires easyexcel.core;
    requires org.codehaus.groovy;
    requires poi;
    requires java.desktop;
    requires javafx.swing;
    requires commons.codec;
    opens cn.juhe.zjsb;
    opens cn.juhe.zjsb.util;
    opens cn.juhe.zjsb.entity;
    opens cn.juhe.zjsb.event;
    opens cn.juhe.zjsb.task;
}