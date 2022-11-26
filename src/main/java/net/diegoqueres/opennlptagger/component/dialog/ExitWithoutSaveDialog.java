package net.diegoqueres.opennlptagger.component.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ExitWithoutSaveDialog {

    public static ButtonType show(String title, String content) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                content,
                ButtonType.NO,
                ButtonType.CANCEL,
                ButtonType.YES
        );

        alert.setTitle(title);
        alert.setHeaderText("Hey.. attention!");
        alert.showAndWait();
        return alert.getResult();
    }

}
