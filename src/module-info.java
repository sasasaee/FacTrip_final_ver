module factripfinal{
    requires javafx.controls;
    requires javafx.fxml;
	requires java.desktop;
    requires javafx.graphics;
    requires javafx.web;
    requires transitive java.sql;
	requires javafx.base;
    
    opens application to javafx.fxml;
    exports application;
}

