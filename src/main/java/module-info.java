module com.example._207011_sulaiman_cgpa_builder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example._207011_sulaiman_cgpa_builder to javafx.fxml;
    exports com.example._207011_sulaiman_cgpa_builder;
}