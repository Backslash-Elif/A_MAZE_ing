package ch.bbcag.gameobjects;

import ch.bbcag.common.SettingsDTO;
import ch.bbcag.simulation.Node;
import ch.bbcag.simulation.NodeController;
import ch.bbcag.simulation.NodeStatus;
import ch.bbcag.simulation.NodeWall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeRenderer extends GameObject {
    private NodeController nodeController;
    private final SettingsDTO sDTO;
    private final int nodeSize;
    private double tickSpeed;

    private double delta = 0;

    public MazeRenderer(SettingsDTO sDTO) {
        this.nodeController = new NodeController(sDTO.getX(), sDTO.getY(), sDTO.getAlgorithmType());
        this.sDTO = sDTO;
        this.nodeSize = sDTO.getNodeSize();
        this.tickSpeed = sDTO.getGenerationTickSpeed(); // initially
    }

    @Override
    public void update(double deltaInSec) {
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

    @Override
    public void draw(GraphicsContext gc) {
        // Get the grid of nodes from NodeController
        Node[][] grid = nodeController.getNodeGrid();

        // Loop through the grid and draw each cell
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                Node node = grid[y][x];

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
            }
        }
    }

    @Override
    public GameObjectType getType() {
        return GameObjectType.MAZE_RENDERER;
    }
}