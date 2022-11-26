package net.diegoqueres.opennlptagger.component.dialog;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Properties;

public class AboutDialog {

    private static Properties projectProperties;
    static {
        readProjectProperties();
    }

    private static void readProjectProperties() {
        try {
            projectProperties = new Properties();
            projectProperties.load(AboutDialog.class.getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException ex) {
            ExceptionDialog.show("Error", "Fail to read project properties", ex.getMessage(), ex);
        }
    }

    public static void show() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("About");
        alert.setHeaderText(projectProperties.getProperty("project.name"));
        alert.setContentText(String.format("%s\n\nVersion: %s\nDeveloper: %s\nRepository: %s\nLicense: %s",
                projectProperties.getProperty("project.description"),
                projectProperties.getProperty("project.version"),
                projectProperties.getProperty("project.developer"),
                projectProperties.getProperty("project.url"),
                projectProperties.getProperty("project.license"))
        );

        alert.showAndWait();
    }

}
