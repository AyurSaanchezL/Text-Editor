package org.group.practica_3.nui;

import org.group.practica_3.MainController;

public class NuiController implements NuiListener{
    MainController controller;

    public NuiController(MainController controller){
        this.controller = controller;
    }
   @Override
    public void onCommand(NuiCommand cmd, String payload) {
        switch (cmd) {
            case ABRIR -> controller.doAbrir(null);
            case GUARDAR ->  controller.doGuardar(null);
            case EXPORTAR -> controller.doExportar(null);
            case NEGRITA -> controller.doNegrita(null);
            case CURSIVA -> controller.doCursiva(null);
            case INVERTIR -> controller.doAlReves(null);
            case ESPACIOS -> controller.doQuitarEspacios(null);
            case MAYUSCULAS -> controller.doMayusculas(null);
            case MINUSCULAS -> controller.doMinusculas(null);
            case LIMPIAR -> controller.doLimpiar(null);
            case COPIAR -> controller.doCopiar(null);
            case REINICIAR -> controller.doReiniciar(null);
        }
    }
}
