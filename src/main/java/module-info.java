module sample  {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.bouncycastle.provider;
    requires org.apache.commons.lang3;
    requires java.desktop;
    opens sample to javafx.fxml;
    exports sample;
}