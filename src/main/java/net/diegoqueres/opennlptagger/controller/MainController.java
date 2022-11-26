package net.diegoqueres.opennlptagger.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.diegoqueres.opennlptagger.component.dialog.*;
import net.diegoqueres.opennlptagger.domain.exception.InvalidInputException;
import net.diegoqueres.opennlptagger.domain.model.Tag;
import net.diegoqueres.opennlptagger.domain.service.FileService;
import net.diegoqueres.opennlptagger.domain.service.TagService;
import net.diegoqueres.opennlptagger.domain.service.impl.FileServiceImpl;
import net.diegoqueres.opennlptagger.domain.service.impl.TagServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static java.util.Objects.nonNull;


public class MainController implements Initializable, Controller {

    @FXML
    private TextField txtOpenTag;
    @FXML
    private TextField txtCloseTag;
    @FXML
    private TextArea txtaText;
    
    private Stage stage;
    
    private FileChooser fileChooser;
    
    private FileService fileService;
    
    private TagService tagService;
    
    private Optional<Path> openedFile;
    
    private Preferences preferences;

    private Properties projectProperties;

    public MainController() {
        openedFile = Optional.empty();
        fileService = new FileServiceImpl();
        tagService = new TagServiceImpl();
    }

    @Override
    public void init(Stage myStage) {
        this.stage = myStage;
        this.readProjectProperties();

        var windowTitle = String.format("%s - v%s",
                projectProperties.getProperty("project.name"), projectProperties.getProperty("project.version"));
        this.stage.setTitle(windowTitle);

        this.stage.setOnCloseRequest(evt -> quit(evt));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initFileChooser();
        loadPreferences();
    }
    
    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(
                        new FileChooser.ExtensionFilter("Text", "*.txt"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
    }

    // Call after all components loading
    private void loadPreferences() {
        preferences = Preferences.userNodeForPackage(MainController.class);

        this.txtOpenTag.setText(preferences.get("tags.open", ""));
        this.txtCloseTag.setText(preferences.get("tags.close", ""));

        changeFont(Integer.parseInt(preferences.get("font.size", "25")));

        var lastDirectory = preferences.get("directory.last", System.getProperty("user.home"));
        fileChooser.setInitialDirectory(Path.of(lastDirectory).toFile());
    }
    
    @FXML
    private void newFile(ActionEvent event) {
        openedFile = Optional.empty();
        txtaText.clear();
    }

    @FXML
    private void openFile(ActionEvent event) {
        fileChooser.setTitle("Open File");
        
        var path = Path.of(preferences.get("directory.last", System.getProperty("user.home")));
        fileChooser.setInitialDirectory(path.toFile());
        
        var file = fileChooser.showOpenDialog(stage);
        if (nonNull(file)) {
            try {
                var content = fileService.open(file);
                txtaText.setText(content.toString());
                openedFile = Optional.of(file.toPath());
            } catch (InvalidInputException e) {
                WarningDialog.show("Warning", "Something went wrong...", e.getMessage(), e.getUserAdvice());
            } catch (IOException e) {
                ExceptionDialog.show("Error", "Something went wrong...", e.getMessage(), e);
            } finally {
                preferences.put("directory.last", file.getParent());
            }
        }
    }

    @FXML
    private void closeFile(ActionEvent event) {
        if (openedFile.isEmpty()) {
            txtaText.setText("");
            return;
        }

        try {
            var result = ExitWithoutSaveDialog.show("Confirm",
                    "Do you want to save this document before close?");
            if (result == ButtonType.YES) {
                this.saveFile();
            } else if (result == ButtonType.CANCEL) {
                return;
            }

            closeFile();
        } catch (IOException e) {
            ExceptionDialog.show("Error", "Something went wrong...", e.getMessage(), e);
        }
    }

    private void closeFile(){
        openedFile = Optional.empty();
        txtaText.setText("");
    }

    @FXML
    private void saveFile(ActionEvent event) {
        try {
            this.saveFile();
        } catch (IOException e) {
            ExceptionDialog.show("Error", "Something went wrong...", e.getMessage(), e);
        }
    }

    private void saveFile() throws IOException {
        if (openedFile.isEmpty()) {
            saveFileAs();
            return;
        }

        try {
            var file = openedFile.get().toFile();
            fileService.save(file, txtaText.getText());
            saveTagPreferences();
        } finally {
            preferences.put("directory.last", openedFile.get().toFile().getParent());
        }
    }

    @FXML
    private void saveFileAs(ActionEvent event) {
        try {
            saveFileAs();
        } catch (IOException e) {
            ExceptionDialog.show("Error", "Something went wrong...", e.getMessage(), e);
        }
    }

    private void saveFileAs() throws IOException {
        fileChooser.setTitle("Save As");
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                fileService.save(file, txtaText.getText());
                openedFile = Optional.of(file.toPath());
            } finally {
                preferences.put("directory.last", file.getParent());
            }
        }
    }

    @FXML
    private void quit(ActionEvent event) {
        quit((WindowEvent)null);
    }

    private void quit(WindowEvent event) {
        saveTagPreferences();

        if (txtaText.getText().isEmpty()) {
            Platform.exit();
            return;
        }

        var result = ExitWithoutSaveDialog.show("Confirm",
                "Exit without saving?");
        if (result == ButtonType.YES) {
            Platform.exit();
        } else if (result == ButtonType.NO) {
            try {
                saveFile();
                Platform.exit();
            } catch (IOException e) {
                ExceptionDialog.show("Error", "Something went wrong...", e.getMessage(), e);
            }
        }
    }

    private void saveTagPreferences() {
        preferences.put("tags.open", this.txtOpenTag.getText().trim());
        preferences.put("tags.close", this.txtCloseTag.getText().trim());
    }

    @FXML
    private void taggIt(ActionEvent event) {
        var openTag = txtOpenTag.getText();
        var closeTag = txtCloseTag.getText();
        Tag tag = new Tag(openTag, closeTag);
        
        var content = new StringBuffer(txtaText.getText());
        var initialPos = txtaText.getSelection().getStart();
        var endPos = txtaText.getSelection().getEnd();
        
        tagService.tagIt(tag, content, initialPos, endPos);
         
        updateContentWithoutScroll(content.toString());
    }
    
    private void updateContentWithoutScroll(String result) {
        double pos = txtaText.getScrollTop();
        int anchor = txtaText.getAnchor();
        int caret = txtaText.getCaretPosition();
        txtaText.setText(result);
        txtaText.setScrollTop(pos);
        txtaText.selectRange(anchor, caret);       
    }

    @FXML
    private void about(ActionEvent event) {
        AboutDialog.show();
    }

    private void readProjectProperties() {
        try {
            projectProperties = new Properties();
            projectProperties.load(AboutDialog.class.getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException ex) {
            ExceptionDialog.show("Error", "Fail to read project properties", ex.getMessage(), ex);
        }
    }

    public void changeFont(ActionEvent actionEvent) {
        var result = FontSizeDialog.show();
        if (result.isPresent()) {
            var fontSize = result.get().size();
            changeFont(fontSize);
            preferences.put("font.size", String.valueOf(fontSize));
        }
    }

    private void changeFont(int fontSize) {
        txtaText.setStyle(String.format("-fx-font-size: %dpx", fontSize));
    }

}
