module org.group.practica_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;
    requires vosk;
    requires java.desktop;


    opens org.group.practica_3 to javafx.fxml;
    exports org.group.practica_3;
    exports org.group.practica_3.component;
    opens org.group.practica_3.component to javafx.fxml;
}