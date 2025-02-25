package ch.bbcag.gui;

import ch.bbcag.common.SceneNavigator;
import ch.bbcag.common.SettingsDTO;
import ch.bbcag.simulation.AlgorithmType;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static ch.bbcag.gui.ItemCreator.createButton;
import static ch.bbcag.gui.ItemCreator.createLabel;

//settings: X, Y, nodeSize, generation tickspeed, solve tickspeed, auto restart, TODO add bgcolor and edgecolor

public class SimulationSettings {
    private final SceneNavigator sceneNavigator;

    private final TextField xSize;
    private final TextField ySize;
    private final TextField nodeSize;
    private final TextField generationTickSpeed;
    private final TextField solveTickSpeed;
    private final ComboBox<AlgorithmType> algorithmType;
    private final CheckBox autoRestart;
    private final TextField autoRestartTimer;
    private final TextField generationPathColor;
    private final TextField generationBacktrackColor;
    private final TextField solvePathColor;
    private final TextField solveBacktrackColor;
    private final TextField solveStartColor;
    private final TextField solveEndColor;
    private final TextField bgColor;
    private final TextField fgColor;

    private final Label settingsInfoLabel;

    private final VBox settingsVBox;

    public SimulationSettings(SceneNavigator sceneNavigator) {
        this.sceneNavigator = sceneNavigator;

        Label xSizeLabel = createSetting("X size", "Width of the simulation");
        xSize = new TextField();

        Label ySizeLabel = createSetting("Y Size", "Height of the simulation");
        ySize = new TextField();

        Label nodeSizeLabel = createSetting("Node Size", "Scale of nodes and the entire simulation");
        nodeSize = new TextField();

        Label generationTickSpeedLabel = createSetting("Generation Tick Speed", "Duration of each tick in seconds. 0 limits it to the framerate. For negative values, abs(value) = ticks per frame");
        generationTickSpeed = new TextField();

        Label solveTickSpeedLabel = createSetting("Solve Tick Speed", "Duration of each tick in seconds. 0 limits it to the framerate. For negative values, abs(value) = ticks per frame");
        solveTickSpeed = new TextField();

        Label algorithmTypeLabel = createSetting("Algorythm Type", "Algorythm type that's used to solve the maze");
        algorithmType = new ComboBox<AlgorithmType>();
        algorithmType.getItems().addAll(AlgorithmType.values());

        Label autoRestartLabel = createSetting("Auto Restart", "Automatically generates a new maze and restarts the process after solving");
        autoRestart = new CheckBox();

        Label autoRestartTimerLabel = createSetting("Auto Restart timer", "Duration of waiting time between solve and restart");
        autoRestartTimer = new TextField();
        
        Label generationPathColorLabel = createSetting("Generation Path Color", "Color of the current path being tracked by the algorithm during maze generation");
        generationPathColor = new TextField();

        Label generationBacktrackColorLabel = createSetting("Generation Backtrack Color", "Color of the path backtracked by the algorithm during maze generation");
        generationBacktrackColor = new TextField();

        Label solvePathColorLabel = createSetting("Solve Path Color", "Color of the current path being tracked by the algorithm during maze solving");
        solvePathColor = new TextField();

        Label solveBacktrackColorLabel = createSetting("Solve Backtrack Color", "Color of the path backtracked by the algorithm during maze solving");
        solveBacktrackColor = new TextField();

        Label solveStartColorLabel = createSetting("Solve Start Color", "Color of the maze's starting point");
        solveStartColor = new TextField();

        Label solveEndColorLabel = createSetting("Solve End Color", "Color of the maze's ending point");
        solveEndColor = new TextField();

        Label bgColorLabel = createSetting("Background Color", "Color of the maze background");
        bgColor = new TextField();

        Label fgColorLabel = createSetting("Foreground Color", "Color of the maze foreground");
        fgColor = new TextField();

        TilePane settingsTilePane = new TilePane(xSizeLabel, xSize, ySizeLabel, ySize, nodeSizeLabel, nodeSize, generationTickSpeedLabel, generationTickSpeed, solveTickSpeedLabel, solveTickSpeed, algorithmTypeLabel, algorithmType, autoRestartLabel, autoRestart, autoRestartTimerLabel, autoRestartTimer, generationPathColorLabel, generationPathColor, generationBacktrackColorLabel, generationBacktrackColor, solvePathColorLabel, solvePathColor, solveBacktrackColorLabel, solveBacktrackColor, solveStartColorLabel, solveStartColor, solveEndColorLabel, solveEndColor, bgColorLabel, bgColor, fgColorLabel, fgColor);
        settingsTilePane.setPrefTileWidth(200);
        settingsTilePane.setPrefColumns(2);
        settingsTilePane.setPrefWidth(400);
        settingsTilePane.setAlignment(Pos.CENTER);

        Button loadSettings = createButton("Load", ItemCreator.ButtonType.MEDIUM);
        HBox loadHBox = new HBox(loadSettings);
        loadHBox.setAlignment(Pos.CENTER);


        settingsInfoLabel = new Label("Ready.");
        settingsInfoLabel.setAlignment(Pos.CENTER);

        settingsVBox = new VBox(settingsTilePane, loadHBox, settingsInfoLabel);
        settingsVBox.setVisible(false);
        settingsVBox.setManaged(false);

        loadSettings.setOnAction(e->{
            validateSave();
        });

        loadValues();
    }

    private Label createSetting(String settingName, String settingTip) {
        Label label = new Label(settingName + " (?)");
        Tooltip.install(label, new Tooltip(settingTip));
        return label;
    }

    private void loadValues() {
        SettingsDTO settingsDTO = sceneNavigator.getDataObject().getSettingsDTO();

        xSize.setText(String.valueOf(settingsDTO.getX()));
        ySize.setText(String.valueOf(settingsDTO.getY()));
        nodeSize.setText(String.valueOf(settingsDTO.getNodeSize()));
        generationTickSpeed.setText(String.valueOf(settingsDTO.getGenerationTickSpeed()));
        solveTickSpeed.setText(String.valueOf(settingsDTO.getSolveTickSpeed()));
        algorithmType.setValue(settingsDTO.getAlgorithmType());
        autoRestart.setSelected(settingsDTO.isAutoRestart());
        autoRestartTimer.setText(String.valueOf(settingsDTO.getAutoRestartTimer()));
        generationPathColor.setText(settingsDTO.getGenerationPathColor());
        generationBacktrackColor.setText(settingsDTO.getGenerationBacktrackColor());
        solvePathColor.setText(settingsDTO.getSolvePathColor());
        solveBacktrackColor.setText(settingsDTO.getSolveBacktrackColor());
        solveStartColor.setText(settingsDTO.getSolveStartColor());
        solveEndColor.setText(settingsDTO.getSolveEndColor());
        bgColor.setText(settingsDTO.getBgColor());
        fgColor.setText(settingsDTO.getFgColor());
    }
    
    private void validateSave() {
        try {
            SettingsDTO settingsDTO = new SettingsDTO();
            
            settingsDTO.setX(Integer.parseInt(xSize.getText()));
            settingsDTO.setY(Integer.parseInt(ySize.getText()));
            settingsDTO.setNodeSize(Integer.parseInt(nodeSize.getText()));
            settingsDTO.setGenerationTickSpeed(Double.parseDouble(generationTickSpeed.getText()));
            settingsDTO.setSolveTickSpeed(Double.parseDouble(solveTickSpeed.getText()));
            settingsDTO.setAlgorithmType(algorithmType.getValue());
            settingsDTO.setAutoRestart(autoRestart.isSelected());
            settingsDTO.setAutoRestartTimer(Double.parseDouble(autoRestartTimer.getText()));
            settingsDTO.setGenerationPathColor(validateAndReturn(generationPathColor.getText()));
            settingsDTO.setGenerationBacktrackColor(validateAndReturn(generationBacktrackColor.getText()));
            settingsDTO.setSolvePathColor(validateAndReturn(solvePathColor.getText()));
            settingsDTO.setSolveBacktrackColor(validateAndReturn(solveBacktrackColor.getText()));
            settingsDTO.setSolveStartColor(validateAndReturn(solveStartColor.getText()));
            settingsDTO.setSolveEndColor(validateAndReturn(solveEndColor.getText()));
            settingsDTO.setBgColor(validateAndReturn(bgColor.getText()));
            settingsDTO.setFgColor(validateAndReturn(fgColor.getText()));
            
            sceneNavigator.getDataObject().setSettingsDTO(settingsDTO);
            
            settingsInfoLabel.setText("Settings applied.");
        } catch (Exception e) {
            settingsInfoLabel.setText("ERROR: Settings value error. Check values.");
        }
    }

    private void validateHexColor(String color) {
        if (!color.matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Invalid hex color code: " + color);
        }
    }

    private String validateAndReturn(String color) {
        validateHexColor(color);
        return color;
    }

    public VBox getSettingsRoot() {
        return settingsVBox;
    }
}
