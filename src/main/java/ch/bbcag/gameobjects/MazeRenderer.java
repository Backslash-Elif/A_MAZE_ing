package ch.bbcag.gameobjects;

import ch.bbcag.common.DataObject;
import ch.bbcag.common.SceneNavigator;
import ch.bbcag.common.SettingsDTO;
import ch.bbcag.simulation.Node;
import ch.bbcag.simulation.NodeController;
import ch.bbcag.simulation.NodeStatus;
import ch.bbcag.simulation.NodeWall;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class MazeRenderer extends GameObject {
    private NodeController nodeController;
    private final SceneNavigator sceneNavigator;
    private final SettingsDTO sDTO;
    private final DataObject dataObject;
    private final int nodeSize;
    private double tickSpeed;

    private double delta = 0;

    private boolean paused = false;
    private boolean showinfo = false;

    private String infoText;
    private final Canvas infoCanvas;
    private final GraphicsContext igc;

    public MazeRenderer(SceneNavigator sceneNavigator, Canvas infoCanvas) {
        this.sceneNavigator = sceneNavigator;
        this.sDTO = sceneNavigator.getDataObject().getSettingsDTO();
        this.dataObject = sceneNavigator.getDataObject();
        this.nodeSize = sDTO.getNodeSize();
        this.tickSpeed = sDTO.getGenerationTickSpeed(); // initially

        this.nodeController = new NodeController(sDTO.getX(), sDTO.getY(), sDTO.getAlgorithmType());

        this.infoCanvas = infoCanvas;
        igc = infoCanvas.getGraphicsContext2D();

        igc.setFont(Font.font("Ariala", FontWeight.BOLD, 16));
    }

    @Override
    public void update(double deltaInSec) {
        if (dataObject.getKeyTyped() == KeyCode.P) {
            dataObject.resetKeyTyped();
            paused = !paused;
        }
        if (dataObject.getKeyTyped() == KeyCode.I) {
            dataObject.resetKeyTyped();
            showinfo = !showinfo;
            igc.clearRect(0, 0, infoCanvas.getWidth(), infoCanvas.getHeight());
        }

        if (!paused) {
            delta+= deltaInSec;
            if (delta >= tickSpeed) {
                int iterations = 1;
                if (tickSpeed <= -1) {
                    iterations = (int) Math.abs(tickSpeed);
                }
                for (int i = 0; i < iterations; i++) {
                    nodeController.tick();
                }
                if (nodeController.isGenerationCompleted()) {
                    tickSpeed = sDTO.getSolveTickSpeed();
                }
                if (nodeController.isSolveCompleted() && sDTO.isAutoRestart()) {
                    tickSpeed = sDTO.getAutoRestartTimer();
                    if (delta >= tickSpeed) {
                        nodeController = new NodeController(sDTO.getX(), sDTO.getY(), sDTO.getAlgorithmType()); //restart after delay
                        tickSpeed = sDTO.getGenerationTickSpeed();
                    }
                }
                delta = 0;
            }
        }

        if (showinfo) {
            String text = "Iteration: " + nodeController.getIteration();
            text += "\nEmpty: " + nodeController.getEmptyCount();
            text += "\nPath: " + nodeController.getTouchCount();
            text += "\nBacktracked: " + nodeController.getBackTrackCount();
            if (nodeController.isGenerationCompleted()) {
                text = "Task: Solve\n" + text;
                float ratio = nodeController.getTouchCount() > 0
                        ? Math.round((1 - ((float) nodeController.getBackTrackCount() / (nodeController.getTouchCount() + nodeController.getBackTrackCount()))) * 10000) / 100.0f
                        : 100.0f;
                text += "\nEfficiency: " + ratio + "%"; // to round to 2 digits
            } else {
                text = "Task: Generate\n" + text;
                text += "\nProgress: " + (Math.round(((float) (nodeController.getTouchCount() + nodeController.getBackTrackCount()) / (nodeController.getxSize() * nodeController.getySize())) * 10000) / 100.0) + "%"; // to round to 2 digits
            }
            infoText = text;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Get the grid of nodes from NodeController
        Node[][] grid = nodeController.getNodeGrid();

        for (Node node : nodeController.getUpdateNodes()) {
            drawNode(node, gc);
        }
        nodeController.getUpdateNodes().clear();
    }

    private void drawNode(Node node, GraphicsContext gc) {
        int x = node.getxPos();
        int y = node.getyPos();
        // Determine the background color based on the node's status
        if (node.getStatus() == NodeStatus.EMPTY) {
            gc.setFill(Color.web(sDTO.getBgColor()));
        } else if (node.getStatus() == NodeStatus.TOUCHED) {
            if (nodeController.isGenerationCompleted()) {
                gc.setFill(Color.web(sDTO.getSolvePathColor()));
            } else {
                gc.setFill(Color.web(sDTO.getGenerationPathColor()));
            }
        } else if (node.getStatus() == NodeStatus.BACKTRACKED) {
            if (nodeController.isGenerationCompleted()) {
                gc.setFill(Color.web(sDTO.getSolveBacktrackColor()));
            } else {
                gc.setFill(Color.web(sDTO.getGenerationBacktrackColor()));
            }
        }

        if (nodeController.isGenerationCompleted()) {
            if (nodeController.getStartPoint() == node) {
                gc.setFill(Color.web(sDTO.getSolveStartColor()));
            }
            if (nodeController.getEndPoint() == node) {
                gc.setFill(Color.web(sDTO.getSolveEndColor()));
            }
        }

        // Draw the cell background
        gc.fillRect(x * nodeSize, y * nodeSize, nodeSize, nodeSize);

        // Draw the walls of the cell
        gc.setStroke(Color.web(sDTO.getFgColor()));
        gc.setLineWidth(2);

        if (node.getWallN() == NodeWall.TRUE) {
            gc.strokeLine(x * nodeSize, y * nodeSize, (x + 1) * nodeSize, y * nodeSize); // Top wall
        }
        if (node.getWallE() == NodeWall.TRUE) {
            gc.strokeLine((x + 1) * nodeSize, y * nodeSize, (x + 1) * nodeSize, (y + 1) * nodeSize); // Right wall
        }
        if (node.getWallS() == NodeWall.TRUE) {
            gc.strokeLine(x * nodeSize, (y + 1) * nodeSize, (x + 1) * nodeSize, (y + 1) * nodeSize); // Bottom wall
        }
        if (node.getWallW() == NodeWall.TRUE) {
            gc.strokeLine(x * nodeSize, y * nodeSize, x * nodeSize, (y + 1) * nodeSize); // Left wall
        }
        if (showinfo) {
            igc.clearRect(0, 0, infoCanvas.getWidth(), infoCanvas.getHeight());
            igc.setStroke(Color.WHITE);
            igc.setLineWidth(5);
            igc.strokeText(infoText, 5, 20);
            igc.setFill(Color.BLACK);
            igc.fillText(infoText, 5, 20);
        }
    }

    @Override
    public GameObjectType getType() {
        return GameObjectType.MAZE_RENDERER;
    }
}