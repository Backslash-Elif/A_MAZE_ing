package ch.bbcag.common;

import ch.bbcag.gui.MainScene;
import ch.bbcag.gui.SceneExtension;
import ch.bbcag.gui.TitleScene;
import javafx.stage.Stage;

public class SceneNavigator {
    private final Stage primaryStage;
    private SceneExtension currentScene;
    private final DataObject dataObject;
    private final SceneController sceneController;

    public enum Scenes {
        TITLE(TitleScene.class),
        SIMULATION(MainScene.class);

        private final Class<? extends SceneExtension> sceneClass;

        Scenes(Class<? extends SceneExtension> sceneClass) {
            this.sceneClass = sceneClass;
        }

        public Class<? extends SceneExtension> getSceneClass() {
            return sceneClass;
        }
    }

    public SceneNavigator(Stage primaryStage, Scenes startScene, DataObject dataObject) {
        this.dataObject = dataObject;
        this.sceneController = new SceneController(this.dataObject.getGraphicsContext());
        this.primaryStage = primaryStage;
        switchScene(startScene);
        this.sceneController.start();
    }

    public void switchScene(Scenes scene) {
        //cleanup
        sceneController.getGameObjects().clear();
        dataObject.getKeysPressed().clear();

        currentScene = createSceneInstance(scene);
        primaryStage.setScene(currentScene);
        sceneController.setCurrentScene(currentScene);
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    public void setStageHeight(int height) {
        primaryStage.setHeight(height);
    }

    public void setStageWidth(int width) {
        primaryStage.setWidth(width);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public SceneController getSceneController() {
        return sceneController;
    }

    private SceneExtension createSceneInstance(Scenes scene) {
        try {
            // Create a new instance of the scene class
            return scene.getSceneClass().getDeclaredConstructor(SceneNavigator.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }

    }
}
