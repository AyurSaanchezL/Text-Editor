package org.group.practica_3.nui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import org.group.practica_3.MainController;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

public class NuiSpeechService {
    private NuiListener listener;
    private boolean running = false;
    private MainController controller;

    public NuiSpeechService(NuiListener listener, MainController c) {
        this.listener = listener;
        this.controller = c;
    }

    public void start() {
        if (running) return; // Si ya está escuchando
        running = true;

        // Creo un hilo para no interferir en la ejecución de la aplicación en sí (el hilo principal)
        Thread thread = new Thread(() -> {

            // El model es el modelo de Vosk, en este caso es el spanish_small
            // El recognizer usa el modelo y la frecuencia de muestreo para reconocer audio

            try (Model model = new Model("src/main/resources/org/group/practica_3/model/vosk-model-small-es-0.42");
                 Recognizer recognizer = new Recognizer(model, 16000)) { // 16kHz

                // AudioFormat define el formato de audio que vamos a capturar: 16kHz, 16 bits, mono, signed, little-endian
                AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

                // TargetDataLine es la línea de audio de la que vamos a leer el micrófono
                TargetDataLine mic = AudioSystem.getTargetDataLine(format);

                mic.open(format);
                mic.start();

                byte[] buffer = new byte[4096]; // Buffer para almacenar los datos de audio temporalmente

                while (running) {
                    // Lee del micrófono y lo guarda en el buffer, n indica cuántos bytes ha leído
                    int n = mic.read(buffer, 0, buffer.length);

                    // acceptWaveForm procesa el audio y devuelve true si hay un resultado final
                    if (recognizer.acceptWaveForm(buffer, n)) {
                        String json = recognizer.getResult();   // Obtiene el resultado en formato JSON
                        String text = extraerTexto(json);       // Extrae el texto del JSON

                        if (controller.estaDictando){
                            Platform.runLater(() -> {
                                controller.dictar(getNormalText(text));
                            });
                        }else{
                            NuiCommand cmd = NuiSpeechParser.parse(text);

                            if (cmd != null) {
                                Platform.runLater(() -> {
                                    controller.setCommand(cmd);
                                    listener.onCommand(cmd, null);
                                    PauseTransition pausa = new PauseTransition(Duration.seconds(2));   // Para que al usuario le de tiempo a leer el comando, se hace una pausa
                                    pausa.setOnFinished(_ -> controller.resetCommand());
                                    pausa.play();
                                });
                            }
                        }

                    }
                }

                mic.stop();
                mic.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true); // El hilo es daemon para que no impida cerrar la aplicación
        thread.start();
    }

    public void stop() {
        running = false;
    }

    private String getNormalText(String text) {
        String result;
        result = text.trim();
        result = result.replaceAll("\\{", "");
        result = result.replaceAll("\\}", "");
        return result.trim();
    }

    private String extraerTexto(String json) {
        return json.replaceAll(".*\"text\"\\s*:\\s*\"(.*?)\".*", "$1");
    }
}
