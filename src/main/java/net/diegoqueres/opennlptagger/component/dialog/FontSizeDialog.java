package net.diegoqueres.opennlptagger.component.dialog;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.Optional;

public class FontSizeDialog {

    public record Choice(
       String description,
       int size
    ) {
        @Override
        public String toString() {
            return description;
        }
    }

    public static Optional<Choice> show() {
        var choices = generateChoices();
        var defaultChoice =
                choices.stream().filter(c -> c.description().equals("medium")).findFirst();

        ChoiceDialog<Choice> dialog = new ChoiceDialog<>(defaultChoice.get(), choices);
        dialog.setTitle("Font Dialog");
        dialog.setHeaderText("Font size");
        dialog.setContentText("Choose a comfortable font size:");

        return dialog.showAndWait();
    }

    private static ArrayList<Choice> generateChoices() {
        var choices = new ArrayList<Choice>();

        choices.add(new Choice("xx-small", 10));
        choices.add(new Choice("x-small", 15));
        choices.add(new Choice("small", 20));
        choices.add(new Choice("medium", 25));
        choices.add(new Choice("large", 30));
        choices.add(new Choice("x-large", 35));
        choices.add(new Choice("xx-large", 40));

        return choices;
    }

}
