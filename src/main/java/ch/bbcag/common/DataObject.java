package ch.bbcag.common;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.*;

public class DataObject {
    public DataObject() {
    }

    private GraphicsContext graphicsContext = null;
    private Canvas canvas = null;
    private final Set<KeyCode> keysPressed = new HashSet<>();
    private KeyCode keyTyped = null;

    private SettingsDTO settingsDTO = new SettingsDTO();

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Set<KeyCode> getKeysPressed() {
        return keysPressed;
    }

    public void addKey(KeyCode key) {
        keysPressed.add(key);
        keyTyped = key;
    }

    public void  removeKey(KeyCode key) {
        keysPressed.remove(key);
        if (keyTyped == key) {
            keyTyped = null;
        }
    }

    public SettingsDTO getSettingsDTO() {
        return settingsDTO;
    }

    public void setSettingsDTO(SettingsDTO settingsDTO) {
        this.settingsDTO = settingsDTO;
    }

    public KeyCode getKeyTyped() {
        return keyTyped;
    }

    public void resetKeyTyped() {
        this.keyTyped = null;
    }
}
