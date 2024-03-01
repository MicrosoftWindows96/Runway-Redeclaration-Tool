package org.universityofsouthampton.runwayredeclarationtool.airport;

public class Obstacle {
    private int height;
    private int distanceFromThreshold;
    private int distanceFromCenterline;

    public Obstacle(int height, int distanceFromThreshold, int distanceFromCenterline) {
        this.height = height;
        this.distanceFromThreshold = distanceFromThreshold;
        this.distanceFromCenterline = distanceFromCenterline;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDistanceFromThreshold() {
        return distanceFromThreshold;
    }

    public void setDistanceFromThreshold(int distanceFromThreshold) {
        this.distanceFromThreshold = distanceFromThreshold;
    }

    public int getDistanceFromCenterline() {
        return distanceFromCenterline;
    }

    public void setDistanceFromCenterline(int distanceFromCenterline) {
        this.distanceFromCenterline = distanceFromCenterline;
    }
}
