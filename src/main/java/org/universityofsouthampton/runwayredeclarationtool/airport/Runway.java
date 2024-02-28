package org.universityofsouthampton.runwayredeclarationtool.airport;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private final String name;
    private final int length;
    private final int width;
    private int tora; // Take-Off Run Available
    private int toda; // Take-Off Distance Available
    private int asda; // Accelerate-Stop Distance Available
    private int lda; // Landing Distance Available

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

    // Setters and Getters for the runway distances available to update
    public void setTora(int tora) {
        this.tora = tora;
    }

    public void setToda(int toda) {
        this.toda = toda;
    }

    public void setAsda(int asda) {
        this.asda = asda;
    }

    public void setLda(int lda) {
        this.lda = lda;
    }

    public int getTora() {
        return tora;
    }

    public int getToda() {
        return toda;
    }

    public int getAsda() {
        return asda;
    }

    public int getLda() {
        return lda;
    }
}
