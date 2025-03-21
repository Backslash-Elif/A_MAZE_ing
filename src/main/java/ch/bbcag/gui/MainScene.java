package ch.bbcag.gui;

import ch.bbcag.common.SceneNavigator;
import ch.bbcag.common.SettingsDTO;
import ch.bbcag.gameobjects.MazeRenderer;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainScene extends SceneExtension{
    private final SceneNavigator sceneNavigator;
    private final MazeRenderer renderer;

    public MainScene(SceneNavigator sceneNavigator) {
        super(new BorderPane());
        this.sceneNavigator = sceneNavigator;
        SettingsDTO sDTO = sceneNavigator.getDataObject().getSettingsDTO();

        sceneNavigator.getDataObject().getCanvas().setWidth(sDTO.getX() * sDTO.getNodeSize());
        sceneNavigator.getDataObject().getCanvas().setHeight(sDTO.getY() * sDTO.getNodeSize());

        BorderPane root = (BorderPane) getRoot();
        Canvas infoCanvas = new Canvas(sceneNavigator.getDataObject().getCanvas().getWidth(), sceneNavigator.getDataObject().getCanvas().getHeight());
        StackPane stackPane = new StackPane(sceneNavigator.getDataObject().getCanvas(), infoCanvas);
        root.setCenter(stackPane);

        sceneNavigator.getPrimaryStage().sizeToScene();

        renderer = new MazeRenderer(sceneNavigator, infoCanvas);
        sceneNavigator.getSceneController().getGameObjects().add(renderer);

        this.setOnKeyPressed((e) -> {
            sceneNavigator.getDataObject().addKey(e.getCode());
        });
        this.setOnKeyReleased((e) -> {
            sceneNavigator.getDataObject().removeKey(e.getCode());
        });
    }

    @Override
    public void update(double deltaInSec) {
        if (sceneNavigator.getDataObject().getKeyTyped() == KeyCode.ESCAPE) {
            sceneNavigator.getDataObject().resetKeyTyped();
            sceneNavigator.switchScene(SceneNavigator.Scenes.TITLE);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
