package org.universityofsouthampton.runwayredeclarationtool.airport;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private String type; //single or parallel runway
    private String name;
    private int TORA; // Take-Off Run Available
    private int TODA; // Take-Off Distance Available
    private int ASDA; // Accelerate-Stop Distance Available
    private int LDA; // Landing Distance Available

    public Runway() {
     this.type = "Single";
     this.name = "00";
    }

    //@Override
    /*public String toString() {
        return "Runway{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                '}';
    }*/

    // Setters and Getters for the runway distances available to update
    public void setTORA(int TORA) {
        this.TORA = TORA;
    }

    public void setTODA(int TODA) {
        this.TODA = TODA;
    }

    public void setASDA(int ASDA) {
        this.ASDA = ASDA;
    }

    public void setLDA(int LDA) {
        this.LDA = LDA;
    }

    public int getTORA() {
        return TORA;
    }

    public int getTODA() {
        return TODA;
    }

    public int getASDA() {
        return ASDA;
    }

    public int getLDA() {
        return LDA;
    }
}
