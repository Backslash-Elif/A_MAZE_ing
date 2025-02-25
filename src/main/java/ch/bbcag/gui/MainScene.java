package ch.bbcag.gui;

import ch.bbcag.common.SceneNavigator;
import ch.bbcag.common.SettingsDTO;
import ch.bbcag.gameobjects.MazeRenderer;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

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
        root.setCenter(sceneNavigator.getDataObject().getCanvas());

        sceneNavigator.getPrimaryStage().sizeToScene();

        renderer = new MazeRenderer(sDTO);
        sceneNavigator.getSceneController().getGameObjects().add(renderer);

        this.setOnKeyPressed((e) -> {
            sceneNavigator.getDataObject().getKeysPressed().add(e.getCode());
        });
        this.setOnKeyReleased((e) -> {
            sceneNavigator.getDataObject().getKeysPressed().remove(e.getCode());
        });
    }

    @Override
    public void update(double deltaInSec) {
        if (sceneNavigator.getDataObject().getKeysPressed().contains(KeyCode.ESCAPE)) {
            sceneNavigator.switchScene(SceneNavigator.Scenes.TITLE);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
