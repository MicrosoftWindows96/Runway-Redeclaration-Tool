module org.universityofsouthampton.runwayredeclarationtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires javafx.base;
    requires javafx.graphics;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.desktop;
    requires org.kordamp.ikonli.materialdesign;
    requires java.xml.bind;
    requires annotations;
    requires javafx.media;
    requires io;
    requires layout;
    requires kernel;
    requires javafx.swing;
    requires pdfbox;

    opens org.universityofsouthampton.runwayredeclarationtool to javafx.fxml;
    exports org.universityofsouthampton.runwayredeclarationtool;
    exports org.universityofsouthampton.runwayredeclarationtool.airport;
    opens org.universityofsouthampton.runwayredeclarationtool.airport to javafx.fxml;
    exports org.universityofsouthampton.runwayredeclarationtool.utility;
    opens org.universityofsouthampton.runwayredeclarationtool.utility to javafx.fxml;
    exports org.universityofsouthampton.runwayredeclarationtool.UI;
    opens org.universityofsouthampton.runwayredeclarationtool.UI to javafx.fxml;
    opens org.universityofsouthampton.runwayredeclarationtool.users;
    exports org.universityofsouthampton.runwayredeclarationtool.users to javafx.fxml;
}