package ch.bbcag.simulation;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Node {
    private final NodeController controller;

    private final int ID;
    private final int xPos;
    private final int yPos;

    private  Node neighbourN;
    private  Node neighbourE;
    private  Node neighbourS;
    private  Node neighbourW;

    private NodeWall wallN = NodeWall.UNSET;
    private NodeWall wallE = NodeWall.UNSET;
    private NodeWall wallS = NodeWall.UNSET;
    private NodeWall wallW = NodeWall.UNSET;

    private final Set<NodeDirection> edges = new HashSet<NodeDirection>();

    private NodeStatus status = NodeStatus.EMPTY;

    public Node(NodeController controller, int id, int xPos, int yPos) {
        this.controller = controller;

        this.ID = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public Node generate(int lastNode) {
        if (isBlocked()) {
            status = NodeStatus.BACKTRACKED;

            for (NodeDirection direction : NodeDirection.values()) {
                if (getWallFromDir(direction) != NodeWall.TRUE && getNeighbourFromDir(direction).status == NodeStatus.TOUCHED) {
                    return getNeighbourFromDir(direction);
                }
            }
        } else {
            status = NodeStatus.TOUCHED;

            //set walls
            for (NodeDirection direction : NodeDirection.values()) {
                if (getNeighbourFromDir(direction).ID == lastNode || getWallFromDir(direction) != NodeWall.UNSET) {
                    continue;
                } else {
                    updateWallFromDir(direction, NodeWall.TRUE);
                }
            }

            //choose random next node
            do {
                NodeDirection direction = getRandomDirection();
                if (getNeighbourFromDir(direction).status == NodeStatus.EMPTY) {
                    updateWallFromDir(direction, NodeWall.FALSE);
                    return getNeighbourFromDir(direction);
                }
            } while (true);
        }
        System.out.println("Error while generating!");
        return null;
    }

    public Node solve(int lastNodeId) {
        if (isDeadEnd()) {
            status = NodeStatus.BACKTRACKED;

            for (NodeDirection direction : NodeDirection.values()) {
                if (getWallFromDir(direction) != NodeWall.TRUE && getNeighbourFromDir(direction).status == NodeStatus.TOUCHED) {
                    return getNeighbourFromDir(direction);
                }
            }
        } else { //new direction
            status = NodeStatus.TOUCHED;

            switch (controller.getAlgorithmType()) {
                case NEAREST: {
                    List<NodeDirection> tryDirections = controller.getDirectionsInOrder(this);

                    for (NodeDirection direction : tryDirections) {
                        if (getWallFromDir(direction) != NodeWall.TRUE && getNeighbourFromDir(direction).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(direction);
                        }
                    }
                }
                case FARTHEST: {
                    List<NodeDirection> tryDirections = controller.getDirectionsInOrder(this);

                    for (NodeDirection direction : tryDirections.reversed()) {
                        if (getWallFromDir(direction) != NodeWall.TRUE && getNeighbourFromDir(direction).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(direction);
                        }
                    }
                }
                case LEFT: {
                    NodeDirection tempDir = NodeDirection.NORTH;

                    for (NodeDirection direction : NodeDirection.values()) {
                        if (getNeighbourFromDir(direction).getID() == lastNodeId) {
                            tempDir = direction;
                        }
                    }

                    while (true) {
                        if (getWallFromDir(tempDir) != NodeWall.TRUE && getNeighbourFromDir(tempDir).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(tempDir);
                        }

                        tempDir = rotate90Clockwise(tempDir);
                    }
                }
                case RIGHT: {
                    NodeDirection tempDir = NodeDirection.NORTH;

                    for (NodeDirection direction : NodeDirection.values()) {
                        if (getNeighbourFromDir(direction).getID() == lastNodeId) {
                            tempDir = direction;
                        }
                    }

                    while (true) {
                        if (getWallFromDir(tempDir) != NodeWall.TRUE && getNeighbourFromDir(tempDir).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(tempDir);
                        }

                        tempDir = rotate90AntiClockwise(tempDir);
                    }
                }
                case RANDOM: {
                    NodeDirection tempDir = getRandomDirection();
                    while (true) {
                        if (getWallFromDir(tempDir) != NodeWall.TRUE && getNeighbourFromDir(tempDir).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(tempDir);
                        }

                        tempDir = getRandomDirection();
                    }
                }
                case NESW: {
                    for (NodeDirection direction : NodeDirection.values()) {
                        if (getWallFromDir(direction) != NodeWall.TRUE && getNeighbourFromDir(direction).getStatus() == NodeStatus.EMPTY) {
                            return getNeighbourFromDir(direction);
                        }
                    }
                }
            }
        }
        System.out.println("Error while solving!");
        return null;
    }

    public NodeWall getWallFromDir(NodeDirection direction) {
        return switch (direction) {
            case NORTH -> wallN;
            case EAST -> wallE;
            case SOUTH -> wallS;
            case WEST -> wallW;
        };
    }

    public void updateWallFromDir(NodeDirection direction, NodeWall value) {
        if (direction == NodeDirection.NORTH) {
            updateWallN(value);
        }
        if (direction == NodeDirection.EAST) {
            updateWallE(value);
        }
        if (direction == NodeDirection.SOUTH) {
            updateWallS(value);
        }
        if (direction == NodeDirection.WEST) {
            updateWallW(value);
        }
    }

    public Node getNeighbourFromDir(NodeDirection direction) {
        return switch (direction) {
            case NORTH -> neighbourN;
            case EAST -> neighbourE;
            case SOUTH -> neighbourS;
            case WEST -> neighbourW;
        };
    }

    public static NodeDirection getRandomDirection() {
        NodeDirection[] directions = NodeDirection.values();
        return directions[new Random().nextInt(directions.length)];
    }

    private static NodeDirection rotate90Clockwise(NodeDirection direction) {
        return switch (direction) {
            case NORTH -> NodeDirection.EAST;
            case EAST -> NodeDirection.SOUTH;
            case SOUTH -> NodeDirection.WEST;
            case WEST -> NodeDirection.NORTH;
        };
    }

    private static NodeDirection rotate90AntiClockwise(NodeDirection direction) {
        return switch (direction) {
            case NORTH -> NodeDirection.WEST;
            case EAST -> NodeDirection.NORTH;
            case SOUTH -> NodeDirection.EAST;
            case WEST -> NodeDirection.SOUTH;
        };
    }

    private boolean isBlocked() {
        return !(neighbourN.status == NodeStatus.EMPTY || neighbourE.status == NodeStatus.EMPTY || neighbourS.status == NodeStatus.EMPTY || neighbourW.status == NodeStatus.EMPTY);
    }

    private boolean isDeadEnd() {
        return (wallN == NodeWall.TRUE || neighbourN.getStatus() != NodeStatus.EMPTY) && (wallE == NodeWall.TRUE || neighbourE.getStatus() != NodeStatus.EMPTY) && (wallS == NodeWall.TRUE || neighbourS.getStatus() != NodeStatus.EMPTY) && (wallW == NodeWall.TRUE || neighbourW.getStatus() != NodeStatus.EMPTY);
    }

    // updateWallx is recomended to use cuz it updates the neighbouring node too
    public void updateWallN(NodeWall wall){
        wallN = wall;
        neighbourN.setWallS(wall);
    }

    public void updateWallE(NodeWall wall){
        wallE = wall;
        neighbourE.setWallW(wall);
    }

    public void updateWallS(NodeWall wall){
        wallS = wall;
        neighbourS.setWallN(wall);
    }

    public void updateWallW(NodeWall wall){
        wallW = wall;
        neighbourW.setWallE(wall);
    }

    public NodeWall getWallN() {
        return wallN;
    }

    public void setWallN(NodeWall wallN) {
        this.wallN = wallN;
    }

    public NodeWall getWallE() {
        return wallE;
    }

    public void setWallE(NodeWall wallE) {
        this.wallE = wallE;
    }

    public NodeWall getWallS() {
        return wallS;
    }

    public void setWallS(NodeWall wallS) {
        this.wallS = wallS;
    }

    public NodeWall getWallW() {
        return wallW;
    }

    public void setWallW(NodeWall wallW) {
        this.wallW = wallW;
    }

    public int getID() {
        return ID;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public Node getNeighbourN() {
        return neighbourN;
    }

    public void setNeighbourN(Node neighbourN) {
        this.neighbourN = neighbourN;
    }

    public Node getNeighbourE() {
        return neighbourE;
    }

    public void setNeighbourE(Node neighbourE) {
        this.neighbourE = neighbourE;
    }

    public Node getNeighbourS() {
        return neighbourS;
    }

    public void setNeighbourS(Node neighbourS) {
        this.neighbourS = neighbourS;
    }

    public Node getNeighbourW() {
        return neighbourW;
    }

    public void setNeighbourW(Node neighbourW) {
        this.neighbourW = neighbourW;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public Set<NodeDirection> getEdges() {
        return edges;
    }
}
