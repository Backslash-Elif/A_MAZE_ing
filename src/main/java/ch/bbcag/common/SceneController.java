package ch.bbcag.common;

import ch.bbcag.gameobjects.GameObject;
import ch.bbcag.gui.SceneExtension;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SceneController extends AnimationTimer {
    private long lastTimeInNanoSec = System.nanoTime();
    private final GraphicsContext gc;
    private final List<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    private SceneExtension currentScene = null;

    public SceneController(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void handle(long currentTimeInNanoSec) {
        long deltaInNanoSec = currentTimeInNanoSec - lastTimeInNanoSec;
        double deltaInSec = Math.min(0.1, deltaInNanoSec / 1e9);
        lastTimeInNanoSec = currentTimeInNanoSec;
        update(deltaInSec);
        draw();
    }

    private void update(double deltaInSec) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaInSec);
        }
        if (currentScene != null) {
            currentScene.update(deltaInSec);
        }
    }

    private void draw() {
        if (currentScene != null) {
            currentScene.draw(gc);
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(gc);
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setCurrentScene(SceneExtension currentScene) {
        this.currentScene = currentScene;
    }
}
