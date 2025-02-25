package ch.bbcag.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

public class SceneExtension extends Scene {
    public SceneExtension(Parent parent) {
        super(parent);
    }

    public void update(double deltaInSec) {
    }

    public void draw(GraphicsContext gc) {
    }
}
