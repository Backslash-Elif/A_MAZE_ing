package ch.bbcag.gameobjects;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;

public class GameObject {
    public GameObject() {
    }

    public void update(double deltaInSec) {
    }

    public void draw(GraphicsContext gc) {
    }

    public GameObjectType getType() {
        return null;
    }

    public BoundingBox getBoundingBox() {
        return null;
    }
}
