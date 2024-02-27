module org.universityofsouthampton.runwayredeclarationtool {
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

    opens org.universityofsouthampton.runwayredeclarationtool to javafx.fxml;
    exports org.universityofsouthampton.runwayredeclarationtool;
    exports org.universityofsouthampton.runwayredeclarationtool.airport;
    opens org.universityofsouthampton.runwayredeclarationtool.airport to javafx.fxml;
    exports org.universityofsouthampton.runwayredeclarationtool.utility;
    opens org.universityofsouthampton.runwayredeclarationtool.utility to javafx.fxml;
}