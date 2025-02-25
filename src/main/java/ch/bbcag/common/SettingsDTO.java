package ch.bbcag.common;

import ch.bbcag.simulation.AlgorithmType;
import javafx.scene.paint.Color;

public class SettingsDTO {
    //Default values ARE set
    private int X = 50;
    private int Y = 50;
    private int nodeSize = 16;
    private double generationTickSpeed = 0;
    private double solveTickSpeed = 0;
    private AlgorithmType algorithmType = AlgorithmType.NEAREST;
    private boolean autoRestart = false;
    private double autoRestartTimer = 10;
    private String generationPathColor = "#00ff00";
    private String generationBacktrackColor = "#ff0000";
    private String solvePathColor = "#00ff00";
    private String solveBacktrackColor = "#ff0000";
    private String solveStartColor = "#f7f700";
    private String solveEndColor = "#0000ff";
    private String bgColor = "#808080";
    private String fgColor = "#ffffff";

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    public double getGenerationTickSpeed() {
        return generationTickSpeed;
    }

    public void setGenerationTickSpeed(double generationTickSpeed) {
        this.generationTickSpeed = generationTickSpeed;
    }

    public double getSolveTickSpeed() {
        return solveTickSpeed;
    }

    public void setSolveTickSpeed(double solveTickSpeed) {
        this.solveTickSpeed = solveTickSpeed;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }

    public boolean isAutoRestart() {
        return autoRestart;
    }

    public void setAutoRestart(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    public double getAutoRestartTimer() {
        return autoRestartTimer;
    }

    public void setAutoRestartTimer(double autoRestartTimer) {
        this.autoRestartTimer = autoRestartTimer;
    }

    public String getGenerationPathColor() {
        return generationPathColor;
    }

    public void setGenerationPathColor(String generationPathColor) {
        this.generationPathColor = generationPathColor;
    }

    public String getGenerationBacktrackColor() {
        return generationBacktrackColor;
    }

    public void setGenerationBacktrackColor(String generationBacktrackColor) {
        this.generationBacktrackColor = generationBacktrackColor;
    }

    public String getSolvePathColor() {
        return solvePathColor;
    }

    public void setSolvePathColor(String solvePathColor) {
        this.solvePathColor = solvePathColor;
    }

    public String getSolveBacktrackColor() {
        return solveBacktrackColor;
    }

    public void setSolveBacktrackColor(String solveBacktrackColor) {
        this.solveBacktrackColor = solveBacktrackColor;
    }

    public String getSolveStartColor() {
        return solveStartColor;
    }

    public void setSolveStartColor(String solveStartColor) {
        this.solveStartColor = solveStartColor;
    }

    public String getSolveEndColor() {
        return solveEndColor;
    }

    public void setSolveEndColor(String solveEndColor) {
        this.solveEndColor = solveEndColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getFgColor() {
        return fgColor;
    }

    public void setFgColor(String fgColor) {
        this.fgColor = fgColor;
    }
}
