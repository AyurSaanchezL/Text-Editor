package org.group.practica_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReemplazarTexto implements Initializable {
    private String textoCompleto;
    private String textoAReemplazar;
    public static String textoResultado;

    @FXML
    private Button btnReemplazarDef;

    @FXML
    private Label lblTextoAReemplazar;

    @FXML
    private Label lblTitulo;

    @FXML
    private TextField txtReemplazado;

    // Este es como el constructor
    public void setInitialData(String completo, String aReemplazar) {
        textoCompleto = completo;
        textoAReemplazar = aReemplazar;
        textoResultado = completo; // Inicialmente, el resultado es el texto completo

        lblTextoAReemplazar.setText("Reemplazar '" + textoAReemplazar + "' por:");
    }

    // La acción del botón
    @FXML
    void doReemplazarDef(ActionEvent event) {
        textoResultado = textoCompleto.replaceAll(textoAReemplazar, txtReemplazado.getText());

        ((Stage) btnReemplazarDef.getScene().getWindow()).close();
    }

    // Métod-o que devuelve el resultado
    public String getResultado() {
        return textoResultado;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTextoAReemplazar.setText("Reemplazar '"+textoAReemplazar+"' por:");

        txtReemplazado.textProperty().addListener((observable, oldValue, newValue) -> {
            btnReemplazarDef.setDisable(newValue.isEmpty());
        });
    }
}
