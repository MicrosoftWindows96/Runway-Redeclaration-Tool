package org.universityofsouthampton.runwayredeclarationtool.airport;

public class Runway {

    private final String name;
    private final int length;
    private final int width;

    public Runway(String name, int length, int width) {
        this.name = name;
        this.length = length;
        this.width = width;
    }

    @Override
    public String toString() {
        return "Runway{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                '}';
    }
}
