package net.diegoqueres.opennlptagger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.diegoqueres.opennlptagger.controller.Controller;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URL;

public class Launcher extends Application {
    private static final float INITIAL_WINDOW_WIDTH_PERCENT = 0.85f;
    private static final float INITIAL_WINDOW_HEIGHT_PERCENT = 0.75f;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Pair<Controller, Parent> loadedData = loadFXML("Main");
        Parent root = loadedData.getRight();

        scene = new Scene(root, 
                screenBounds.getWidth() * INITIAL_WINDOW_WIDTH_PERCENT,
                screenBounds.getHeight() * INITIAL_WINDOW_HEIGHT_PERCENT);

        Controller controller = loadedData.getLeft();
        controller.init(stage);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml).getRight();
        scene.setRoot(root);
    }

    private static Pair<Controller, Parent> loadFXML(String fxml) throws IOException {
        URL resource = Launcher.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent parent = fxmlLoader.load();
        Controller controller = (Controller) fxmlLoader.getController();

        return Pair.of(controller, parent);
    }

    public static void main(String[] args) {
        launch();
    }

}