package ch.bbcag.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeController {
    private boolean generationCompleted = false;
    private boolean solveCompleted = false;

    private final int xSize;
    private final int ySize;

    private final AlgorithmType algorithmType;

    private final Node[][] nodeGrid;
    private final Node guardNode;

    private Node currentNode;
    private int lastNodeId;

    private Node startPoint;
    private Node endPoint;

    // counting info
    private int emptyCount;
    private int touchCount;
    private int backTrackCount;

    private NodeStatus subtractCount;
    private NodeStatus addCount;

    private int iteration = 0;

    public NodeController(int xSize, int ySize, AlgorithmType algorithmType) {
        this.xSize = xSize;
        this.ySize = ySize;

        this.algorithmType = algorithmType;

        nodeGrid = new Node[ySize][xSize];
        guardNode = new Node(this, -1);

        emptyCount = xSize * ySize;
        touchCount = 0;
        backTrackCount = 0;

        this.init();
    }

    private void init() {
        guardNode.setWallN(NodeWall.TRUE);
        guardNode.setWallE(NodeWall.TRUE);
        guardNode.setWallS(NodeWall.TRUE);
        guardNode.setWallW(NodeWall.TRUE);
        guardNode.setStatus(NodeStatus.BACKTRACKED);

        List<Node> edgeNodes = new ArrayList<>();

        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                nodeGrid[i][j] = new Node(this, i * ySize + j);
            }
        }
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                Node node = nodeGrid[i][j];
                if (i == 0) { // Top row
                    node.setNeighbourN(guardNode);
                    node.setWallN(NodeWall.TRUE);
                    edgeNodes.add(node);
                } else {
                    node.setNeighbourN(nodeGrid[i-1][j]);
                }

                if (i == ySize - 1) { // Bottom row
                    node.setNeighbourS(guardNode);
                    node.setWallS(NodeWall.TRUE);
                    edgeNodes.add(node);
                } else {
                    node.setNeighbourS(nodeGrid[i+1][j]);
                }

                if (j == 0) { // Leftmost column
                    node.setNeighbourW(guardNode);
                    node.setWallW(NodeWall.TRUE);
                    edgeNodes.add(node);
                } else {
                    node.setNeighbourW(nodeGrid[i][j-1]);
                }

                if (j == xSize - 1) { // Rightmost column
                    node.setNeighbourE(guardNode);
                    node.setWallE(NodeWall.TRUE);
                    edgeNodes.add(node);
                } else {
                    node.setNeighbourE(nodeGrid[i][j+1]);
                }
            }
        }

        Random r = new Random();
        currentNode = nodeGrid[Math.abs(r.nextInt(ySize))][Math.abs(r.nextInt(xSize))];
        for (NodeDirection direction : NodeDirection.values()) {
            currentNode.updateWallFromDir(direction, NodeWall.TRUE);
        }
        lastNodeId = 0;

        startPoint = edgeNodes.get(Math.abs(r.nextInt(edgeNodes.size())));
        edgeNodes.remove(startPoint);
        endPoint = edgeNodes.get(Math.abs(r.nextInt(edgeNodes.size())));
    }

    public void tick() {
        if (solveCompleted) {
            return;
        }
        iteration += 1;
        if (!generationCompleted) {
            subtractCount = currentNode.getStatus();
            Node node = currentNode.generate(lastNodeId);
            addCount = currentNode.getStatus();
            lastNodeId = currentNode.getID();
            currentNode = node;
        } else {
            subtractCount = currentNode.getStatus();
            Node node = currentNode.solve(lastNodeId);
            addCount = currentNode.getStatus();
            lastNodeId = currentNode.getID();
            currentNode = node;
            if (currentNode == endPoint) {
                solveCompleted = true;
            }
        }

        switch (subtractCount) {
            case EMPTY -> emptyCount--;
            case TOUCHED -> touchCount--;
            case BACKTRACKED -> backTrackCount--;
        }
        switch (addCount) {
            case EMPTY -> emptyCount++;
            case TOUCHED -> touchCount++;
            case BACKTRACKED -> backTrackCount++;
        }

        int bTCounter = 0;
        for (Node[] nodes : nodeGrid) {
            for (Node node : nodes) {
                if (node.getStatus() == NodeStatus.BACKTRACKED) {
                    bTCounter ++;
                }
            }
        }
        if (bTCounter == xSize * ySize -1) {
            generationCompleted = true;
            for (Node[] nodes : nodeGrid) {
                for (Node node : nodes) {
                    node.setStatus(NodeStatus.EMPTY);
                }
                lastNodeId = startPoint.getNeighbourN().getID();
                currentNode = startPoint;
            }

            emptyCount = xSize * ySize;
            touchCount = 0;
            backTrackCount = 0;

            iteration = 0;
        }
    }

    public List<NodeDirection> getDirectionsInOrder(Node source) {
        Node destination = endPoint;
        // Get the positions of the source and destination nodes in the grid
        int[] sourcePosition = findNodePosition(source);
        int[] destinationPosition = findNodePosition(destination);

        if (sourcePosition == null || destinationPosition == null) {
            throw new IllegalArgumentException("Source or destination node not found in the grid.");
        }

        int sourceX = sourcePosition[1];
        int sourceY = sourcePosition[0];
        int destinationX = destinationPosition[1];
        int destinationY = destinationPosition[0];

        // Calculate the differences in x and y coordinates
        int dx = destinationX - sourceX;
        int dy = destinationY - sourceY;

        // Create a list to store directions in order of preference
        List<NodeDirection> directions = new ArrayList<>();

        // Determine the primary and secondary directions
        if (Math.abs(dx) > Math.abs(dy)) {
            // Horizontal movement is prioritized
            if (dx > 0) {
                directions.add(NodeDirection.EAST);
            } else {
                directions.add(NodeDirection.WEST);
            }

            if (dy > 0) {
                directions.add(NodeDirection.SOUTH);
            } else if (dy < 0) {
                directions.add(NodeDirection.NORTH);
            }
        } else {
            // Vertical movement is prioritized
            if (dy > 0) {
                directions.add(NodeDirection.SOUTH);
            } else {
                directions.add(NodeDirection.NORTH);
            }

            if (dx > 0) {
                directions.add(NodeDirection.EAST);
            } else if (dx < 0) {
                directions.add(NodeDirection.WEST);
            }
        }

        // Add the remaining directions (if not already added)
        for (NodeDirection direction : NodeDirection.values()) {
            if (!directions.contains(direction)) {
                directions.add(direction);
            }
        }

        return directions;
    }

    // Helper method to find the position of a node in the grid
    private int[] findNodePosition(Node node) {
        for (int y = 0; y < nodeGrid.length; y++) {
            for (int x = 0; x < nodeGrid[y].length; x++) {
                if (nodeGrid[y][x] == node) {
                    return new int[]{y, x}; // Return the position as [y, x]
                }
            }
        }
        return null; // Node not found
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public Node[][] getNodeGrid() {
        return nodeGrid;
    }

    public Node getStartPoint() {
        return startPoint;
    }

    public Node getEndPoint() {
        return endPoint;
    }

    public boolean isGenerationCompleted() {
        return generationCompleted;
    }

    public boolean isSolveCompleted() {
        return solveCompleted;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public int getEmptyCount() {
        return emptyCount;
    }

    public int getTouchCount() {
        return touchCount;
    }

    public int getBackTrackCount() {
        return backTrackCount;
    }

    public int getIteration() {
        return iteration;
    }
}
