module d.base.final_dbase {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens d.base.final_dbase to javafx.fxml;
    exports d.base.final_dbase;
}