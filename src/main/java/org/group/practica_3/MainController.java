package org.group.practica_3;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.group.practica_3.component.ProgressLabel;
import org.group.practica_3.component.Selection;
import org.group.practica_3.nui.NuiCommand;
import org.group.practica_3.nui.NuiController;
import org.group.practica_3.nui.NuiSpeechService;

import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private String textoInicial = "";
    private boolean estaAlReves = false;
    private boolean vieneDeTeclado = false;
    private ArrayList<Selection> listaSelecciones = new ArrayList<>();
    private ArrayList<Selection> seleccionesGuardadas = new ArrayList<>();
    public boolean estaDictando;

    // Para el selector de texto (sustituye el textArea)
    private StyleClassedTextArea textArea;

    @FXML
    private Button btnAbrir;

    @FXML
    private Button btnExportar;

    @FXML
    private Button btnMinus;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnCopiar;

    @FXML
    private Button btnCursiva;

    @FXML
    private Button btnEspacios;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnMayus;

    @FXML
    private Button btnNegrita;

    @FXML
    private Button btnReemplazar;

    @FXML
    private Button btnReiniciar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnReves;

    @FXML
    private ChoiceBox<String> cboxContadores;

    @FXML
    private Label lblComando;

    @FXML
    private Label lblImportarExportar;

    @FXML
    private Label lblContenidoCopiado;

    @FXML
    private Label lblAtajoTeclado;

    @FXML
    private Label lblPrompt;

    @FXML
    private Label lblContador;

    @FXML
    private Label lblCursiva;

    @FXML
    private Label lblGuardar;

    @FXML
    private Label lblEspacios;

    @FXML
    private Label lblMayus;

    @FXML
    private Label lblMinus;

    @FXML
    private Label lblNegrita;

    @FXML
    private Label lblReiniciar;

    @FXML
    private Label lblSubrayar;

    @FXML
    private Label lblVoltear;

    @FXML
    private ToggleButton tglBtnVoz;

    @FXML
    private ProgressLabel pgssLabel;

    @FXML
    public AnchorPane textEditorContainer;  // en vez de TextArea

    @FXML
    public TextField txtBuscado;

    @FXML
    public void doAbrir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        StringBuilder texto = new StringBuilder();
        fileChooser.setInitialDirectory(new File("C:/Users/ayurs/Desktop"));

        File file = fileChooser.showOpenDialog(App.primaryStage);

        // Si el usuario cierra la ventana de 'abrir' sin haber seleccionado ningún archivo, salta error. Así lo evito.
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String linea = br.readLine();

                while (linea != null) {
                    texto.append(linea).append("\n");
                    linea = br.readLine();
                }

            } catch (IOException e) {
                System.out.println("No se puede abrir el archivo");
            }
            cargarTexto(texto.toString());
        }

    }

    @FXML
    public void doExportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File("C:/Users/ayurs/Desktop"));
        fileChooser.setInitialFileName("TextoEmbellecido.txt");

        File targetFile = fileChooser.showSaveDialog(App.primaryStage);

        if (targetFile == null) return;

        pgssLabel.setVisible(true);

        // Voy a usar un Task para que no se congele el Thread principal
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String texto = transformarTexto();
                char[] chars = texto.toCharArray();
                int total = chars.length;
                int escritos = 0;

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {

                    for (char c : chars) {  // Escribe caracter a caracter
                        bw.write(c);
                        escritos++;

                        if (escritos % 5 == 0) { // actualizar cada 5 caracteres para no saturar
                            updateProgress(escritos, total);
                        }
                    }
                }
                updateProgress(total, total);
                return null;
            }
        };

        /*
         *  task.progressProperty() → propiedad observable que contiene el progreso (0.0 a 1.0)
         *  bind(...) → hace que la ProgressBar siga automáticamente el valor de task.progressProperty()
         */
        pgssLabel.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(e -> {
            pgssLabel.setVisible(false);
            lblImportarExportar.setVisible(true);
            lblImportarExportar.setText("Archivo exportado con éxito");
            // Crear y configurar la transición de pausa
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

            // Cuando la pausa acabe, se quita el mensaje
            pausa.setOnFinished(_ -> lblImportarExportar.setVisible(false));

            pausa.play();

        });
        task.setOnFailed(e -> {
            pgssLabel.setVisible(false);
            new Alert(Alert.AlertType.ERROR, "Error al guardar").show();
        });

        // Empieza la tarea
        new Thread(task).start();
    }

    @FXML
    public void doBuscar(ActionEvent event) {
        if (hayTexto()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Buscar");
            alerta.setHeaderText(null);
            if (textArea.getText().contains(txtBuscado.getText())) {

                StringBuilder resultado = new StringBuilder("Texto encontrado!\n");
                for (String s: buscarCoincidencias(txtBuscado.getText())){
                    resultado.append(s).append("\n");
                }

                alerta.setContentText(resultado.toString());
            } else {
                alerta.setContentText("Texto no encontrado");
            }
            alerta.show();
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Buscar");
            alerta.setHeaderText(null);
            alerta.setContentText("No se puede buscar donde no hay texto...");
            alerta.show();
        }
    }

    @FXML
    public void doReemplazar(ActionEvent event) throws IOException {
        String textoCompleto = textArea.getText();
        String textoBuscado = txtBuscado.getText();

        // Cargo el FXML de la ventana de reemplazar
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista-reemplazar.fxml"));
        VBox root = loader.load();
        Scene scene = new Scene(root);

        // Cojo su controller
        ReemplazarTexto controller = loader.getController();

        // Le paso los datos que necesito al controlador
        controller.setInitialData(textoCompleto, textoBuscado); // Este méto-do es como el constructor

        // Creo la nueva ventana
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Reemplazar");
        newStage.setResizable(false);

        // Cuando la nueva ventana se cierra (con el métod-o .close()), le paso el texto resultante al textArea
        newStage.setOnHiding(e -> {
            textArea.replaceText(controller.getResultado());
        });

        newStage.show();

    }

    @FXML
    public void doCopiar(ActionEvent event) {
        if (hayTexto()) {
            // Obtener el portapapeles del sistema
            Clipboard clipboard = Clipboard.getSystemClipboard();

            // Crear el contenido para el portapapeles
            ClipboardContent contenido = new ClipboardContent();

            // Colocar el texto del textArea
            contenido.putString(textArea.getText());

            // Guardar en el portapapeles
            clipboard.setContent(contenido);

            lblContenidoCopiado.setVisible(true);

            // Crear y configurar la transición de pausa
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

            // Cuando la pausa acabe, se quita el mensaje
            pausa.setOnFinished(event1 -> {
                lblContenidoCopiado.setVisible(false);
            });

            pausa.play();

        }
    }

    @FXML
    public void doAlReves(ActionEvent event) {
        if (hayTexto()) {
            if (estaAlReves) {
                StringBuilder textoAlReves = new StringBuilder();
                for (int i = (textArea.getText().length() - 1); i >= 0; i--) {
                    char[] arrayChar = textArea.getText().toCharArray();
                    textoAlReves.append(arrayChar[i]);
                }
                textArea.replaceText(textoAlReves.toString());
                estaAlReves = false;
            } else {
                StringBuilder textoAlReves = new StringBuilder();
                for (int i = (textArea.getText().length() - 1); i >= 0; i--) {
                    char[] arrayChar = textArea.getText().toCharArray();
                    textoAlReves.append(arrayChar[i]);
                }
                textArea.replaceText(textoAlReves.toString());
                estaAlReves = true;
            }
            listaSelecciones.clear();
        }
    }

    @FXML
    public void doLimpiar(ActionEvent event) {
        if (hayTexto()) {
            textArea.replaceText("");
            listaSelecciones.clear();
        }
    }

    @FXML
    public void doMayusculas(ActionEvent event) {
        if (hayTexto()) {
            IndexRange selection = textArea.getSelection();
            int start = selection.getStart();
            int end = selection.getEnd();

            if (start < end) {
                textArea.replaceText(start, end, textArea.getSelectedText().toUpperCase());
            }
        }
    }

    @FXML
    public void doMinusculas(ActionEvent event) {
        if (hayTexto()) {
            IndexRange selection = textArea.getSelection();
            int start = selection.getStart();
            int end = selection.getEnd();

            if (start < end) {
                textArea.replaceText(start, end, textArea.getSelectedText().toLowerCase());
            }
        }
        atajoTeclado("mayus");
    }

    @FXML
    public void doNegrita(ActionEvent event) {
        // 1. Obtener la selección
        IndexRange selection = textArea.getSelection();
        int start = selection.getStart();   // Devuelve el carácter inicial
        int end = selection.getEnd();   // Devuelve el carácter final

        // 2. Comprobar si hay texto seleccionado
        if (start < end) {
            String styleClass;
            Selection seleccion = comprobarExistencia(selection.toString());

            // Si ya existe
            if (seleccion != null) {
                if (seleccion.isEsNegrita()) {       // Si ya era cursiva, tengo que ver si la dejo o no en negrita
                    if (seleccion.isEsCursiva()) {
                        styleClass = "italic-text";
                    } else {
                        styleClass = "not-bold";
                    }
                    seleccion.setEsNegrita(false);  // Actualizo el estado
                } else {
                    if (seleccion.isEsCursiva()) {       // Si no era cursiva, tengo que ver si también es negrita
                        styleClass = "bold-and-italic";
                    } else {
                        styleClass = "bold-text";
                    }
                    seleccion.setEsNegrita(true);   // Actualizo el estado
                }
            } else {  // Si no había sido ya seleccionada, la guardo
                seleccion = new Selection(selection.toString(), true, false);
                styleClass = "bold-text";
                listaSelecciones.add(seleccion);
            }
            textArea.setStyleClass(start, end, styleClass);
        }
        if (!vieneDeTeclado) {
            atajoTeclado("negrita");
        }
        vieneDeTeclado = false;
    }

    @FXML
    public void doCursiva(ActionEvent event) {
        IndexRange selection = textArea.getSelection();
        int start = selection.getStart();
        int end = selection.getEnd();

        if (start < end) {
            String styleClass;
            Selection seleccion = comprobarExistencia(selection.toString());

            // Si ya existe
            if (seleccion != null) {
                if (seleccion.isEsCursiva()) {       // Si ya era cursiva, tengo que ver si la dejo o no en negrita
                    if (seleccion.isEsNegrita()) {
                        styleClass = "bold-text";
                    } else {
                        styleClass = "not-italic";
                    }
                    seleccion.setEsCursiva(false);  // Actualizo el estado
                } else {
                    if (seleccion.isEsNegrita()) {       // Si no era cursiva, tengo que ver si también es negrita
                        styleClass = "bold-and-italic";
                    } else {
                        styleClass = "italic-text";
                    }
                    seleccion.setEsCursiva(true);   // Actualizo el estado
                }
            } else {  // Si no había sido ya seleccionada, la guardo
                seleccion = new Selection(selection.toString(), false, true);
                styleClass = "italic-text";
                listaSelecciones.add(seleccion);
            }
            textArea.setStyleClass(start, end, styleClass);

        }
        if (!vieneDeTeclado) {
            atajoTeclado("cursiva");
        }
        vieneDeTeclado = false;
    }

    @FXML
    public void doQuitarEspacios(ActionEvent event) {
        if (hayTexto()) {
            String texto = textArea.getText();
            textArea.replaceText(texto.replaceAll(" {2}", " "));
        }
    }

    @FXML
    public void doGuardar(ActionEvent event) {
        textoInicial = textArea.getText();
        seleccionesGuardadas = listaSelecciones;

        lblGuardar.setVisible(true);

        // Crear y configurar la transición de pausa
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

        // Cuando la pausa acabe, se quita el mensaje
        pausa.setOnFinished(_ -> lblGuardar.setVisible(false));

        pausa.play();
    }

    // Interpreto reiniciar como querer volver al estado del texto donde el usuario ha pulsado el botón 'Guardar'
    @FXML
    public void doReiniciar(ActionEvent event) {
        // TODO guardar el texto antes de reemplazar
        String style;
        String[] posicion;

        textArea.replaceText(textoInicial);
        for (Selection selection : seleccionesGuardadas) {
            posicion = selection.getPosition().split(",");
            if (selection.isEsNegrita() && selection.isEsCursiva()) {
                style = "bold-and-italic";
            } else if (selection.isEsNegrita()) {
                style = "bold-text";
            } else if (selection.isEsCursiva()) {
                style = "italic-text";
            } else {
                style = "normal";
            }
            textArea.setStyleClass(Integer.parseInt(posicion[0]), Integer.parseInt(posicion[1].trim()), style);
        }
        listaSelecciones.clear();
        if (estaAlReves) {
            doAlReves(event);
            estaAlReves = false;
        }
    }

    @FXML
    void doToggleVoice(ActionEvent event) {
        estaDictando = !estaDictando;
        if (estaDictando){
            tglBtnVoz.setText("Modo: Dictar 💬");
        }else{
            tglBtnVoz.setText("Modo: Comando 🗣");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // VOZ Y COMANDOS
        NuiController controller = new NuiController(this);
        NuiSpeechService speechService = new NuiSpeechService(controller, this);
        speechService.start();
         // Empieza a escuchar
        resetCommand();
        estaDictando = false;
        tglBtnVoz.setText("Modo: Comando 🗣");

        // Lo que sería el TextArea:
        lblAtajoTeclado.setVisible(false);
        lblImportarExportar.setVisible(false);
        lblGuardar.setVisible(false);
        textArea = new StyleClassedTextArea();
        textArea.replaceText("");
        AnchorPane.setTopAnchor(textArea, 0.0);
        AnchorPane.setBottomAnchor(textArea, 0.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);
        textArea.setWrapText(true);
        textEditorContainer.getChildren().add(textArea);
        lblPrompt.toFront();

        // Desabilitar botones y configuraciones iniciales
        btnReemplazar.setDisable(true);
        btnReiniciar.setDisable(true);
        lblContenidoCopiado.setVisible(false);

        // ProgressBar
        pgssLabel.setVisible(false);

        // Para añadir un texto que diga qué hace cada botón al pasar el ratón
        Tooltip tooltipNegrita = new Tooltip("Poner texto en negrita [Ctrl + B]");
        btnNegrita.setTooltip(tooltipNegrita);

        Tooltip tooltipCursiva = new Tooltip("Poner texto en cursiva [Ctrl + I]");
        btnCursiva.setTooltip(tooltipCursiva);

        Tooltip tooltipMayus = new Tooltip("Poner el texto en mayúsculas");
        btnMayus.setTooltip(tooltipMayus);

        Tooltip tooltipMinus = new Tooltip("Poner el texto en minusculas");
        btnMinus.setTooltip(tooltipMinus);

        Tooltip tooltipVoltear = new Tooltip("Voltear todo el texto");
        btnReves.setTooltip(tooltipVoltear);

        Tooltip tooltipEspacios = new Tooltip("Elimina los espacios dobles");
        btnEspacios.setTooltip(tooltipEspacios);

        Tooltip tooltipBuscar = new Tooltip("Buscar texto (sensible a mayus, minus y tildes!!)");
        btnBuscar.setTooltip(tooltipBuscar);

        Tooltip tooltipReemplazar = new Tooltip("Reemplaza el texto buscado");
        btnReemplazar.setTooltip(tooltipReemplazar);

        Tooltip tooltipCopiar = new Tooltip("Copiar texto [Ctrl + C]");
        btnCopiar.setTooltip(tooltipCopiar);

        Tooltip tooltipLimpiar = new Tooltip("Limpia el cuadro de texto");
        btnLimpiar.setTooltip(tooltipLimpiar);

        Tooltip tooltipReiniciar = new Tooltip("Deshace los últimos cambios");
        btnReiniciar.setTooltip(tooltipReiniciar);

        Tooltip tooltipContador = new Tooltip("Filtra qué quieres contar");
        cboxContadores.setTooltip(tooltipContador);

        Tooltip tooltipGuardar = new Tooltip("Guardar el texto actual");
        btnGuardar.setTooltip(tooltipGuardar);

        Tooltip tooltipAbrir = new Tooltip("Abrir un archivo");
        btnAbrir.setTooltip(tooltipAbrir);

        Tooltip tooltipExportar = new Tooltip("Exportar texto");
        btnExportar.setTooltip(tooltipExportar);

        // Configurar el choiceBox de contadores
        ArrayList<String> contadores = new ArrayList<>();
        contadores.add("Palabras");
        contadores.add("Líneas");
        contadores.add("Caracteres");

        cboxContadores.setValue(contadores.get(2));
        cboxContadores.getItems().setAll(contadores);

        // Contador de palabras, líneas o caracteres
        cboxContadores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            actualizarChoiceBox();
        });

        // Buscar texto
        txtBuscado.textProperty().addListener((observable, oldValue, newValue) -> {

            // Condiciones para btnReemplazar: El texto de txtBuscar tiene que estar en el txtArea

            boolean tieneBuscado = !newValue.trim().isEmpty();

            boolean tieneTextoArea = !textArea.getText().trim().isEmpty();

            btnReemplazar.setDisable(!(tieneBuscado && tieneTextoArea));
        });

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {

            // Condiciones para btnReiniciar: Tienen que tener solo el txtArea
            boolean tieneTextoArea = !newValue.trim().isEmpty();

            // Revisa si hay texto en el campo de búsqueda. Lo leemos directamente.
            boolean tieneBuscado = !txtBuscado.getText().trim().isEmpty();

            // Lógica para btnReiniciar (solo necesita que el TextArea tenga texto)
            btnReiniciar.setDisable(!tieneTextoArea);

            // Lógica para btnReemplazar (necesita AMBOS)
            btnReemplazar.setDisable(!(tieneTextoArea && tieneBuscado));
            lblPrompt.setVisible(!hayTexto());

            if (!hayTexto()) {
                lblContador.setText("0");
            } else {
                actualizarChoiceBox();
            }
        });

        // Teclado
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            final KeyCombination atajoNegrita = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
            final KeyCombination atajoCursiva = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);

            if (atajoNegrita.match(event)) {
                vieneDeTeclado = true;
                doNegrita(null);    // Llama al mismo method que el botón btnNegrita
                event.consume();          // Termina el evento
            } else if (atajoCursiva.match(event)) {
                vieneDeTeclado = true;
                doCursiva(null);
                event.consume();
            }
        });

    }

    // Otros MÉTODOS

    public void dictar(String a){
        if (a == null || a.isEmpty()) return;

        if (textArea.getText().isEmpty()) {
            textArea.appendText(a);
        } else {
            textArea.appendText(" " + a);
        }
    }

    public void setCommand(NuiCommand c){
        lblComando.setText("Comando: "+c);
    }

    public void resetCommand(){
        lblComando.setText("Comando: ______");
    }

    private void actualizarChoiceBox() {
        if (!hayTexto()) {
            lblContador.setText("0");
        } else {
            switch (cboxContadores.getValue()) {
                case "Palabras" -> {
                    String[] palabras = textArea.getText().trim().split("\\s+");
                    lblContador.setText(Integer.toString(palabras.length));
                }

                case "Líneas" -> {
                    lblContador.setText(String.valueOf(textArea.getText().lines().count()));
                }

                case "Caracteres" -> {
                    int caracteres = 0;
                    String texto = textArea.getText().replaceAll("\\n", "");
                    for (int i = 0; i < texto.length(); i++) {
                        caracteres++;
                    }
                    lblContador.setText(Integer.toString(caracteres));
                }
            }
        }
    }

    private ArrayList<String> buscarCoincidencias(String textoABuscar) {
        String[] lineas = textArea.getText().split("\\n");  // Separo el texto por cada salto de línea
        ArrayList<String> resultado = new ArrayList<>();    // El ArrayList que voy a devolver
        StringBuilder string = new StringBuilder();
        int lastIndex;  // Este int va a ir marcando la posición inicial de todas las coincidencias

        for (int i = 0; i < lineas.length; i++) {
            if (lineas[i].contains(textoABuscar)) { // Si la línea contiene el texto a buscar
                lastIndex = lineas[i].indexOf(textoABuscar);    // La primera coincidencia
                string.append("Línea ").append(i).append(", carácter ").append(lastIndex);  // Genero el texto de 'Línea _, carácter _'
                String subString = lineas[i].substring(lastIndex+textoABuscar.length());    // Corto el texto de la línea desde el carácter final de la primera coincidencia

                while (subString.contains(textoABuscar)){   // Si este nuevo String contiene el texto buscado, significa que aún hay más coincidencias

                    if (string.indexOf("eres") == -1){  // Si hay más de un carácter, cambio la palabla 'carácter' por 'caracteres'
                        string.replace(string.indexOf("carácter"), string.indexOf("carácter")+8, "caracteres");
                    }

                    lastIndex += subString.indexOf(textoABuscar)+textoABuscar.length(); // La nueva última coincidencia
                    subString = lineas[i].substring(lastIndex+textoABuscar.length());   // Vuelvo a cortar el texto de la línea desde esta última nueva coincidencia
                    string.append(", ").append(lastIndex);  // Añado al texto final 'Línea...' esta nueva coincidencia
                }

                resultado.add(string.toString());   // Si ya no hay más coincidencias, añado el string al ArrayList
                string = new StringBuilder();   // Reinicio el StringBuilder que voy cortando para comprobar si hay más de una coincidencia
            }
        }
        return resultado;   // Devuelvo el ArrayList
    }

    public boolean hayTexto() {
        return !textArea.getText().trim().isEmpty();
    }

    public Selection comprobarExistencia(String texto) {
        for (Selection a : listaSelecciones) {
            if (a.getPosition().equals(texto)) {
                return a;
            }
        }
        return null;
    }

    private void atajoTeclado(String atajo) {
        switch (atajo) {
            case "negrita" -> lblAtajoTeclado.setText("Atajo de teclado perdido: Ctrl + B");
            case "cursiva" -> lblAtajoTeclado.setText("Atajo de teclado perdido: Ctrl + I");
        }

        lblAtajoTeclado.setVisible(true);

        // Crear y configurar la transición de pausa
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

        // Cuando la pausa acabe, se quita el mensaje
        pausa.setOnFinished(_ -> lblAtajoTeclado.setVisible(false));

        pausa.play();
    }

    private String transformarTexto() {
        Map<Integer, List<String>> tagMap = new TreeMap<>();

        for (Selection sel : listaSelecciones) {
            String[] partes = sel.getPosition().split(",");
            if (partes.length == 2) { // Me aseguro de que el split ha funcionado
                int start = Integer.parseInt(partes[0].trim());
                int end = Integer.parseInt(partes[1].trim());

                if (sel.isEsNegrita()) {
                    tagMap.computeIfAbsent(start, k -> new ArrayList<>()).add("<b>");
                    tagMap.computeIfAbsent(end, k -> new ArrayList<>()).add("</b>");
                }
                if (sel.isEsCursiva()) {
                    tagMap.computeIfAbsent(start, k -> new ArrayList<>()).add("<i>");
                    tagMap.computeIfAbsent(end, k -> new ArrayList<>()).add("</i>");
                }
            }
        }

        StringBuilder html = new StringBuilder();   // String que voy a devolver
        for (int i = 0; i <= textArea.getText().length(); i++) {    // Por cada carácter del textArea (que realmente es un StyleClassedTextArea)
            if (tagMap.containsKey(i)) {    // Si la posición de ese carácter coincide con alguna de las claves del tagMap (que guarda la posición de las etiquetas HTML)
                for (String tag : tagMap.get(i)) {
                    html.append(tag);   // Combina el texto plano con las etiquetas correspondientes
                }
            }
            if (i < textArea.getText().length()) {
                html.append(textArea.getText().charAt(i));
            }
        }
        return html.toString();
    }

    private void cargarTexto(String contenidoArchivo) {

        pgssLabel.setVisible(true);

        // Voy a usar un Task para que no se congele el Thread principal
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int total = contenidoArchivo.length();
                int cantidadAProcesar = 500;
                StringBuilder texto = new StringBuilder(total);

                for (int i = 0; i < total; i += cantidadAProcesar) {
                    int fin = Math.min(i + cantidadAProcesar, total); // Voy añadiendo el texto poco a poco (desde donde se quedó la vez anterior, hasta el siguiente bloque de texto)
                    texto.append(contenidoArchivo, i, fin);

                    // actualizar progreso
                    updateProgress(fin, total);
                }

                final String textoFinal = texto.toString();

                /*
                 *
                 *  Todc lo que modifique elementos gráficos de JavaFX debe hacerse en el hilo de la interfaz (JavaFX Application Thread).
                 *  Por eso, aunque estamos en un Task (hilo de fondo), usamos Platform.runLater para cambiar el contenido de textArea y aplicar estilos.
                 *
                 */
                Platform.runLater(() -> {
                    // Si el contenido parece ser en formato HTML, lo parseo
                    if (textoFinal.contains("<b>") || textoFinal.contains("<i>")) {
                        Map<String, Object> resultado = convertirHTMLATexto(textoFinal);
                        String textoPlano = (String) resultado.get("texto");    // Coge el texto parseado a String (de HTML)
                        ArrayList<Selection> selecciones = (ArrayList<Selection>) resultado.get("selecciones"); // Coge las selecciones

                        textArea.replaceText(textoPlano);
                        listaSelecciones = selecciones;

                        // Aplica los estilos al StyleClassedTextArea
                        for (Selection sel : listaSelecciones) {
                            String[] partes = sel.getPosition().split(",");
                            if (partes.length == 2) {
                                int start = Integer.parseInt(partes[0].trim());
                                int end = Integer.parseInt(partes[1].trim());

                                String styleClass = "";
                                if (sel.isEsNegrita() && sel.isEsCursiva()) {
                                    styleClass = "bold-and-italic";
                                } else if (sel.isEsNegrita()) {
                                    styleClass = "bold-text";
                                } else if (sel.isEsCursiva()) {
                                    styleClass = "italic-text";
                                }

                                if (!styleClass.isEmpty()) {
                                    textArea.setStyleClass(start, end, styleClass);
                                }
                            }
                        }
                    } else {
                        // Si no, es texto plano
                        textArea.replaceText(textoFinal);
                        listaSelecciones.clear();
                    }
                });

                return null;
            }
        };

        /*
         *  task.progressProperty() → propiedad observable que contiene el progreso (0.0 a 1.0)
         *  bind(...) → hace que la ProgressBar siga automáticamente el valor de task.progressProperty()
         */
        pgssLabel.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(e -> {
            pgssLabel.setVisible(false);
            lblImportarExportar.setVisible(true);
            lblImportarExportar.setText("Archivo abierto con éxito");
            // Crear y configurar la transición de pausa
            PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

            // Cuando la pausa acabe, se quita el mensaje
            pausa.setOnFinished(_ -> lblImportarExportar.setVisible(false));

            pausa.play();
        });
        task.setOnFailed(e -> {
            pgssLabel.setVisible(false);
            new Alert(Alert.AlertType.ERROR, "Error al abrir").show();
        });

        // Empieza la tarea
        new Thread(task).start();
    }


    public Map<String, Object> convertirHTMLATexto(String contenidoHtml) {
        StringBuilder textoPlano = new StringBuilder();
        ArrayList<Selection> selecciones = new ArrayList<>();

        // El stack es una estructura Last in, First Out. Cuando detecta que un elemento empieza con <b> guarda la posición del caracter. Posteriormente, cuando encuentra un </b> entiende
        // que es el final del anterior y agrega la selección
        Stack<Integer> boldStarts = new Stack<>();
        Stack<Integer> italicStarts = new Stack<>();

        int i = 0;
        while (i < contenidoHtml.length()) {
            if (contenidoHtml.startsWith("<b>", i)) {
                boldStarts.push(textoPlano.length());
                i += 3; // Porque <b> son 3 caracteres
            } else if (contenidoHtml.startsWith("<i>", i)) {
                italicStarts.push(textoPlano.length());
                i += 3;
            } else if (contenidoHtml.startsWith("</b>", i)) {
                if (!boldStarts.isEmpty()) {
                    int start = boldStarts.pop();
                    int end = textoPlano.length();
                    findOrCreateSelection(selecciones, start, end).setEsNegrita(true);
                }
                i += 4; // Porque </b> son 4 caracteres
            } else if (contenidoHtml.startsWith("</i>", i)) {
                if (!italicStarts.isEmpty()) {
                    int start = italicStarts.pop();
                    int end = textoPlano.length();
                    findOrCreateSelection(selecciones, start, end).setEsCursiva(true);
                }
                i += 4;
            } else {
                textoPlano.append(contenidoHtml.charAt(i));
                i++;
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("texto", textoPlano.toString());
        resultado.put("selecciones", selecciones);
        return resultado;
    }


    private Selection findOrCreateSelection(ArrayList<Selection> selecciones, int start, int end) {
        String position = start + ", " + end;

        // Compruebo si existe ya la selección
        for (Selection sel : selecciones) {
            if (sel.getPosition().equals(position)) {
                return sel;
            }
        }

        // Si no, la creo
        Selection nueva = new Selection(position, false, false);
        selecciones.add(nueva);
        return nueva;
    }
}
