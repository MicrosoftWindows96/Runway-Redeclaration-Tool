package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private String name; // Runway name
    String logicalRunway1, logicalRunway2;
    private String logicalRunway; //runway name if used from the other side. 09L -> 27R
    private int TORA; // Take-Off Run Available
    private int TODA; // Take-Off Distance Available
    private int ASDA; // Accelerate-Stop Distance Available
    private int LDA; // Landing Distance Available
    private int displacedThreshold; // Displaced Threshold

    private ArrayList<Obstacle> obstacles;
//    private List<Obstacle> obstacles; // List of obstacles
    private int RESA; // Runway End Safety Area
    private int TOCS; // Take-Off Climb Surface
    private int ALS; // Approach Landing Surface
    private int stripEnd = 60;
    private int blastProtectionValue;
    private boolean landingOver;
    private boolean landingToward;
    private boolean takeoffAway;
    private boolean takeoffToward;

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

        this.obstacles = new ArrayList<>();

    }

    public ArrayList<Obstacle> getObstacles(){
        return this.obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        this.obstacles.add(obstacle);
    }

    public void removeObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        this.obstacles.remove(obstacle);
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

    //get logical runways out of one runway name.
    public void setLogicalRunways(String runwayName) {
        int runwayNumber = Integer.parseInt(runwayName);

        int oppositeRunwayNumber = runwayNumber + 18;
        if (oppositeRunwayNumber > 36) {
            oppositeRunwayNumber -= 36;
        }

        // Convert back to strings, ensuring they are formatted with leading zeros if necessary
        logicalRunway1 = String.format("%02d", runwayNumber);
        logicalRunway2 = String.format("%02d", oppositeRunwayNumber);
    }

    // Getters for the logical runways
    public String getLogicalRunway1() {
        return logicalRunway1;
    }

    public String getLogicalRunway2() {
        return logicalRunway2;
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

    private void calculateLDA(Obstacle obstacle) {

        if (landingOver) {
            int temporaryThreshold;
            int obstacleHeight = obstacle.getHeight();


            if (obstacleHeight * ALS >= RESA) {
                temporaryThreshold = obstacleHeight * ALS;
            } else {
                temporaryThreshold = RESA;
            }

            if ((temporaryThreshold + stripEnd) >= blastProtectionValue) {
                this.LDA = LDA - obstacle.getDistanceFromThreshold() - temporaryThreshold - stripEnd;
            } else {
                this.LDA =  LDA - obstacle.getDistanceFromThreshold() - blastProtectionValue;
            }


        }
        else if (landingToward) {

            this.LDA = obstacle.getDistanceFromThreshold() - RESA - stripEnd;

        }
    }


    private void calculateTORA_ASDA_TODA(Obstacle obstacle) {
        if (takeoffToward) {
            int temporaryThreshold;
            int obstacleHeight = obstacle.getHeight();

            if (obstacleHeight * TOCS >= RESA) {
                temporaryThreshold = obstacle.getHeight() * TOCS;
            } else {
                temporaryThreshold = RESA;
            }

            this.TORA = obstacle.getDistanceFromThreshold() + displacedThreshold - temporaryThreshold - stripEnd;
            this.ASDA = TORA;
            this.TODA = TORA;
        }

        else if (takeoffAway) {

            this.TORA = TORA - blastProtectionValue - obstacle.getDistanceFromThreshold() - displacedThreshold ;

        }

    }
}