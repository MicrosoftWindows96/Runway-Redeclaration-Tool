package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.List;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private String name; // Runway name
    String logicalRunway1, getLogicalRunway2;
    private String logicalRunway; //runway name if used from the other side. 09L -> 27R
    private int TORA; // Take-Off Run Available
    private int TODA; // Take-Off Distance Available
    private int ASDA; // Accelerate-Stop Distance Available
    private int LDA; // Landing Distance Available
    private int displacedThreshold; // Displaced Threshold
    private List<Obstacle> obstacles; // List of obstacles
    private int RESA; // Runway End Safety Area
    private int TOCS; // Take-Off Climb Surface
    private int ALS; // Approach Landing Surface

    public Runway(String name, int TORA, int TODA, int ASDA, int LDA, int displacedThreshold) {
        if(!isValidName(name) || name == null || TORA < 0 || TODA < 0 || ASDA < 0 || LDA < 0 || displacedThreshold < 0){
            throw new IllegalArgumentException("Invalid runway parameters");
        }
        this.name = name;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
        this.displacedThreshold = displacedThreshold;
    }

    public void addObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        obstacles.add(obstacle);
    }

    public void removeObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        obstacles.remove(obstacle);
    }

    @Override
    public String toString() {
        return "Runway{" +
                "type='" + '\'' +
                ", name='" + name + '\'' +
                ", TORA=" + TORA +
                ", TODA=" + TODA +
                ", ASDA=" + ASDA +
                ", LDA=" + LDA +
                ", displacedThreshold=" + displacedThreshold +
                //
                '}';
    }

    public String getName() {
        return name;
    }

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

    public int getDisplacedThreshold() {
        return displacedThreshold;
    }

    public boolean isValidName(String name){
        int value = Integer.parseInt(name);
        if (value >= 1 && value <= 9 && name.matches("0[1-9]")) {
            return true;
        }
        return (value >= 10 && value <= 36);
    }
    public void getName(String name) throws Exception{
        if(!isValidName(name)){
            throw new Exception("Invalid name");
        }
        else this.name = name;
    }

    public String getVerticalOpposite(String number) {
        try {
            int num = Integer.parseInt(number); // Convert string to integer
            int opposite = num + 18; // Add 18 to get the opposite
            if (opposite > 36) opposite -= 36; // Wrap around if necessary
            return String.format("%02d", opposite);
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    public void recalculateLDA(Obstacle obstacle) {
        int obstacleHeight = obstacle.getHeight();
        int distanceFromThreshold = obstacle.getDistanceFromThreshold();

        // ALS (Approach Landing Surface) ratio
        final int ALS_RATIO = 50;

        // Calculate distance needed to clear obstacle, includes height of obstacle times ALS ratio
        int distanceToClearObstacle = obstacleHeight * ALS_RATIO;

        // Minimum RESA (Runway End Safety Area) distance
        final int MINIMUM_RESA = 240;

        final int STRIP_END = 60;

        // Calculate total distance to be subtracted from LDA
        int totalDistanceToSubtract = distanceFromThreshold + distanceToClearObstacle + MINIMUM_RESA + STRIP_END;

        // Check if obstacle within RESA distance from threshold
        if (distanceFromThreshold < MINIMUM_RESA) {
            // If obstacle within RESA distance from threshold, entire runway is unavailable
            this.LDA = 0;
        } else {
            // Subtract total distance to clear the obstacle from LDA
            this.LDA = Math.max(this.LDA - totalDistanceToSubtract, 0);
        }
    }
}