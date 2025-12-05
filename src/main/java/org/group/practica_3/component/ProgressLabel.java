package org.group.practica_3.component;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ProgressLabel extends HBox {

    @FXML
    private Label lblProgressBarText;

    @FXML
    private ProgressBar progressBar;

    public ProgressLabel() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/group/practica_3/components/ProgressLabel.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Initialize() se ejecuta justo después de inicializar
    @FXML
    public void initialize() {
        progressBar.setProgress(0);

        // Cuando cambia el progreso, el texto tiene que cambiar
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {

            if ((Integer) newValue.intValue() != null) {
                double progress = newValue.doubleValue();
                if (progress >= 0 && progress <= 1) {    // 0 mínimo -> 1 máximo
                    lblProgressBarText.setText(Math.round(progress * 100) + "%"); // Lo muestro en porcentaje (progress = 0.4) -> *100 = 40%
                } else {
                    lblProgressBarText.setText("...");
                }
            }

        });
    }

    public DoubleProperty progressProperty() {
        return progressBar.progressProperty();
    }
}
