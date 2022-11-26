package net.diegoqueres.opennlptagger.component.dialog;

import javafx.scene.control.Alert;

public class WarningDialog {

    public static void show(String title, String header, String content, String userAdvice) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);

        var contentText = String.format("%s\n\n%s", content, userAdvice);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

}
