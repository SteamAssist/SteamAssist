module eus.ehu.template {
    requires javafx.controls;
    requires javafx.fxml;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;

    requires com.h2database;

    opens eus.ehu.template.domain to org.hibernate.orm.core;
    opens eus.ehu.template.ui to javafx.fxml;
    exports eus.ehu.template.ui;
}
