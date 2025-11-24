package org.drk.di_ejercicioev_t3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.drk.di_ejercicioev_t3.utils.JavaFXUtil;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        JavaFXUtil.initStage(stage);
        JavaFXUtil.setScene("/org/drk/di_ejercicioev_t3/main-view.fxml");
    }
}
