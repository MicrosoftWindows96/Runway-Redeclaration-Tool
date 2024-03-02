package org.universityofsouthampton.runwayredeclarationtool.airport;

public class Obstacle {
    private String name;
    private int height;
    private int distanceFromThreshold;
    private int distanceFromCentreline;

    public Obstacle(String name, int height, int distanceFromThreshold, int distanceFromCentreline) {
        this.name = name;
        this.height = height;
        this.distanceFromThreshold = distanceFromThreshold;
        this.distanceFromCentreline = distanceFromCentreline;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() {
        return this.height;
    }


    public void setDistanceFromThreshold(int distanceFromThreshold) {
        this.distanceFromThreshold = distanceFromThreshold;
    }
    public int getDistanceFromThreshold() {
        return this.distanceFromThreshold;
    }

    public void setDistanceFromCentreline(int distanceFromCentreline) {
        this.distanceFromCentreline = distanceFromCentreline;
    }

    public int getDistanceFromCentreline() {
        return this.distanceFromCentreline;
    }


}
