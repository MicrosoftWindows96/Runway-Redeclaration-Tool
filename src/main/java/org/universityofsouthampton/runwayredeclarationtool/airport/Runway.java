package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

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
    private final int displacedThreshold; // Displaced Threshold

    private final ArrayList<Obstacle> obstacles;
//    private List<Obstacle> obstacles; // List of obstacles
    private int RESA; // Runway End Safety Area
    private int TOCS; // Take-Off Climb Surface
    private int ALS; // Approach Landing Surface
    private final int stripEnd = 60;
    private int blastProtectionValue;
    private boolean landingOver;
    private boolean landingToward;
    private boolean takeoffAway;
    private boolean takeoffToward;
    private final String direction;
    private final String degrees;

    public Runway(String degrees, String direction, int TORA, int TODA, int ASDA, int LDA, int displacedThreshold) {
        this.degrees = degrees;
        this.direction = direction;
        this.name = degrees + direction;

        if (isValidName(name)) {
            throw new IllegalArgumentException("Invalid degrees/direction");
        } else if ((TORA < 0) || (TODA < 0) || (ASDA < 0) || (LDA < 0) || (displacedThreshold < 0)) {
            throw new IllegalArgumentException("Invalid runway parameters");
        }

        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;

        this.displacedThreshold = displacedThreshold;
        this.obstacles = new ArrayList<>();
    }

    public ArrayList<Obstacle> getObstacles() {
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
        String regex = "^(0[1-9]|1[0-9]|2[0-9]|3[0-6])([LRC])?$";
        return !name.matches(regex);
    }
    public void setName(String name) throws Exception{
        if(isValidName(name)){
            throw new Exception("Invalid name");
        }

        else this.name = name;
        setLogicalRunways(name);
    }

    public void setLogicalRunways(String runwayName) {
        int runwayNumber = Integer.parseInt(runwayName);

        int oppositeRunwayNumber = runwayNumber + 18;
        if (oppositeRunwayNumber > 36) {
            oppositeRunwayNumber -= 36;
        }

        logicalRunway1 = String.format("%02d", runwayNumber);
        logicalRunway2 = String.format("%02d", oppositeRunwayNumber);
    }

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


            temporaryThreshold = Math.max(obstacleHeight * ALS, RESA);

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

    public String getDirection() {
        return this.direction;
    }

    public String getDegrees() {
        return this.degrees;
    }
}