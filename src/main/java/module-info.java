module net.diegoqueres.opennlptagger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.prefs;
    requires org.apache.commons.lang3;

    opens net.diegoqueres.opennlptagger to javafx.fxml;
    opens net.diegoqueres.opennlptagger.controller to javafx.fxml;

    exports net.diegoqueres.opennlptagger;
}
