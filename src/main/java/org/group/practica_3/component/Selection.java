package org.group.practica_3.component;

public class Selection {
    private String position;
    private boolean esNegrita;
    private boolean esCursiva;

    public Selection() {
    }

    public Selection(String position, boolean esNegrita, boolean esCursiva) {
        this.position = position;
        this.esNegrita = esNegrita;
        this.esCursiva = esCursiva;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isEsNegrita() {
        return esNegrita;
    }

    public void setEsNegrita(boolean esNegrita) {
        this.esNegrita = esNegrita;
    }

    public boolean isEsCursiva() {
        return esCursiva;
    }

    public void setEsCursiva(boolean esCursiva) {
        this.esCursiva = esCursiva;
    }


    @Override
    public String toString() {
        return "Selection{" +
                "texto='" + position + '\'' +
                ", esNegrita=" + esNegrita +
                ", esCursiva=" + esCursiva +
                '}';
    }
}
