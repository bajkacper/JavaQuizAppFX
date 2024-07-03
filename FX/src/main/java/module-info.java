module com.example.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires spring.context;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.desktop;

    opens com.example.fx to javafx.fxml;
    opens com.example.fx.entity to com.fasterxml.jackson.databind,javafx.base;
    exports com.example.fx;
}