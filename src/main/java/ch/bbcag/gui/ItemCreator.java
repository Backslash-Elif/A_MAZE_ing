package ch.bbcag.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ItemCreator {
    public enum LabelType {
        NORMAL, HEADING1, HEADING2, TEXT
    }

    public static Label createLabel(String text, LabelType labelType) {
        Label label = new Label(text);
        switch (labelType) {
            case HEADING1: {
                label.setStyle("-fx-font-size: 4em;");
                break;
            }
            case HEADING2: {
                label.setStyle("-fx-font-size: 2em;");
                break;
            }
            case TEXT: {
                label.setStyle("-fx-font-size: 1.3em;");
                break;
            }
        }
        return label;
    }

    public enum ButtonType{
        NORMAL, MEDIUM, SMALL, BOX
    }

    public static Button createButton(String text, ButtonType buttonType) {
        Button button = new Button(text);

        switch (buttonType){
            case NORMAL: {
                button.setPadding(new Insets(10, 10, 10, 10));
                button.setPrefWidth(200);
                button.setStyle("-fx-font-size: 2em;");
                break;
            }
            case MEDIUM: {
                button.setPadding(new Insets(7, 7, 7, 7));
                button.setPrefWidth(150);
                button.setStyle("-fx-font-size: 1.7em;");
                break;
            }
            case SMALL: {
                button.setPadding(new Insets(5, 5, 5, 5));
                button.setPrefWidth(100);
                button.setStyle("-fx-font-size: 1.5em;");
                break;
            }
            case BOX:{
                button.setPadding(new Insets(1, 1, 1, 1));
                button.setPrefWidth(40);
                button.setPrefHeight(40);
                break;
            }
        }
        return button;
    }
}
