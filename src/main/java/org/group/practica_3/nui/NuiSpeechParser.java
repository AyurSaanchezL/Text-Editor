package org.group.practica_3.nui;

public class NuiSpeechParser {

    public static NuiCommand parse(String speech){
        if (speech == null)
            return null;

        speech = speech.toLowerCase();

        if (speech.contains("abrir")) return NuiCommand.ABRIR;
        if (speech.contains("guardar")) return NuiCommand.GUARDAR;
        if (speech.contains("exportar")) return NuiCommand.EXPORTAR;

        if (speech.contains("negrita")) return NuiCommand.NEGRITA;
        if (speech.contains("cursiva")) return NuiCommand.CURSIVA;

        if (speech.contains("invertir") || speech.contains("al revés")) return NuiCommand.INVERTIR;
        if (speech.contains("espacios")) return NuiCommand.ESPACIOS;

        if (speech.contains("mayúscula")) return NuiCommand.MAYUSCULAS;
        if (speech.contains("minúscula")) return NuiCommand.MINUSCULAS;

        if (speech.contains("limpiar")) return NuiCommand.LIMPIAR;
        if (speech.contains("copiar")) return NuiCommand.COPIAR;
        if (speech.contains("reiniciar")) return NuiCommand.REINICIAR;

        return null;

    }
}
