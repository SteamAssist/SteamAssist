module eus.ehu.template {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.transaction;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires com.h2database;
    requires org.jsoup;
    requires okhttp3;
    requires java.desktop;
    requires java.scripting;
    requires unirest.java;

    opens eus.ehu.template.domain to org.hibernate.orm.core, com.google.gson;
    opens eus.ehu.template.ui to javafx.fxml;
    exports eus.ehu.template.ui;
}
