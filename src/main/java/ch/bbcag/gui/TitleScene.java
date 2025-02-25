package ch.bbcag.gui;

import ch.bbcag.common.SceneNavigator;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static ch.bbcag.gui.ItemCreator.createButton;
import static ch.bbcag.gui.ItemCreator.createLabel;

public class TitleScene extends SceneExtension {
    private final SceneNavigator sceneNavigator;
    private final SimulationSettings settings;
    public TitleScene(SceneNavigator sceneNavigator) {
        super(new BorderPane());

        this.sceneNavigator = sceneNavigator;

        if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
            this.sceneNavigator.setStageHeight(350); //350
            this.sceneNavigator.setStageWidth(500);
        }

        //Title
        Label title = createLabel("A-MAZE-ing", ItemCreator.LabelType.HEADING1);
        Button start = createButton("Run", ItemCreator.ButtonType.NORMAL);
        Label info = new Label("While running press ESC to return.");
        Button configure = createButton("Configure...", ItemCreator.ButtonType.MEDIUM);
        Button exit = createButton("Exit", ItemCreator.ButtonType.SMALL);

        settings = new SimulationSettings(sceneNavigator);

        VBox menuVBox = new VBox(16);

        menuVBox.getChildren().addAll(title, start, info, configure, settings.getSettingsRoot(), exit);
        menuVBox.setAlignment(Pos.CENTER);

        start.setOnAction(e-> sceneNavigator.switchScene(SceneNavigator.Scenes.SIMULATION));
        configure.setOnAction(e-> toggleSettings());
        exit.setOnAction(e-> Platform.exit());

        BorderPane root = (BorderPane) getRoot();
        root.setCenter(menuVBox);
    }

    private void toggleSettings() {
        VBox settingsRoot = settings.getSettingsRoot();
        if (settingsRoot.isVisible()) {
            settingsRoot.setVisible(false);
            settingsRoot.setManaged(false);
            if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
                this.sceneNavigator.setStageHeight(360);
            }
        } else {
            settingsRoot.setVisible(true);
            settingsRoot.setManaged(true);
            if (!this.sceneNavigator.getPrimaryStage().isMaximized()) {
                this.sceneNavigator.setStageHeight(820);
            }
        }
        this.sceneNavigator.setStageWidth(500);
    }
}
