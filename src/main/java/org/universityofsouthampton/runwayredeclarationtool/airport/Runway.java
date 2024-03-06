package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private String degrees;
    private String direction;
    private String name; // Runway name
    private int runwayLength;
    String logicalRunway1, logicalRunway2;
    private String logicalRunway; //runway name if used from the other side. 09L -> 27R
    private int TORA; // Take-Off Run Available (same as length of runway)
    private int TODA; // Take-Off Distance Available
    private int ASDA; // Accelerate-Stop Distance Available
    private int LDA; // Landing Distance Available

    private int newTORA; // Calculated Take-Off Run Available
    private int newTODA; // Calculated Take-Off Distance Available
    private int newASDA; // Calculated Accelerate-Stop Distance Available
    private int newLDA; // Calculated Landing Distance Available
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
    //private String direction;
    private int stopway;
    private int clearway;

    public Runway(String degrees, String direction, int TORA, int TODA, int ASDA, int LDA, int displacedThreshold) {
        this.degrees = degrees;
        this.direction = direction;
        this.name = degrees + direction;

        if(!isValidName(name)|| TORA < 0 || TODA < 0 || ASDA < 0 || LDA < 0 || displacedThreshold < 0){
            throw new IllegalArgumentException("Invalid runway parameters");
        }

        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
        this.displacedThreshold = displacedThreshold;

        this.obstacles = new ArrayList<>();
    }
    public Runway(String name, int stopway, int clearway, int TORA, int displacedThreshold){
        this.name = name;
        this.stopway = stopway;
        this.clearway = clearway;
        this.TORA = TORA;
        this.displacedThreshold = displacedThreshold;
        this.obstacles = new ArrayList<>();
        this.TODA = TORA + clearway;
        this.ASDA = TORA + stopway;
        if(!isValidName(name) && checkValidParameters()){
            throw new IllegalArgumentException("Invalid runway parameters");
        }
    }
    public boolean checkValidParameters(){
        boolean greaterThanZero = (TORA<0 || TODA <0 || ASDA <0 || LDA<0 || displacedThreshold <=0 || stopway <0 || clearway<0);
        boolean checkTODA = (TODA == TORA + clearway && TODA > TORA);
        boolean checkASDA = (ASDA == TORA + stopway || ASDA == LDA + stopway);
        return (greaterThanZero && checkTODA && checkASDA);
    }

    public ArrayList<Obstacle> getObstacles(){
        return this.obstacles;
    }

    public void addObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        this.obstacles.add(obstacle);
        if (obstacle.getDistanceFromThreshold() <= 1000) {
            this.takeoffAway = true;
            this.landingOver = true;
            this.landingToward = false;
            this.takeoffToward = false;
        } else {
            this.landingToward = true;
            this.takeoffToward = true;
            this.takeoffAway = false;
            this.landingOver = false;
        }
        runCalculations();
    }

    public void runCalculations() {
        this.calculateLDA();
        this.calculateTORA_ASDA_TODA();
    }

    public void removeObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        this.obstacles.remove(obstacle);
        this.newASDA = this.ASDA;
        this.newTODA = this.TODA;
        this.newTORA = this.TORA;
        this.newLDA = this.LDA;

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

    public void setRESA(int RESA) {
        this.RESA = RESA;
    }

    public void setTOCS(int TOCS) {
        this.TOCS = TOCS;
    }

    public void setALS(int ALS) {
        this.ALS = ALS;
    }

    public void setBlastProtectionValue(int BPV) {
        blastProtectionValue = BPV;
    }

    public void setLDA(int LDA) {
        this.LDA = LDA;
    }

    //getter and setter functions for stopway and clearway
    public void setStopway(int stopway){
        this.stopway = stopway;
    }
    public void setClearway(int clearway){
        this.clearway = clearway;
    }
    public int getStopway(){
        return stopway;
    }
    public int getClearway(){
        return clearway;
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

    public int getNewTORA() {
        return newTORA;
    }

    public int getNewTODA() {
        return newTODA;
    }

    public int getNewASDA() {
        return newASDA;
    }

    public int getNewLDA() {
        return newLDA;
    }

    public boolean isValidName(String name){
        String regex = "^(0[1-9]|1[0-9]|2[0-9]|3[0-6])(L|R|C)?$";
        return name.matches(regex);
    }
    public void setName(String name) throws Exception{
        if(!isValidName(name)){
            throw new Exception("Invalid name");
        }
        else this.name = name;
        setLogicalRunways(name);
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

    private void calculateLDA() {

        Obstacle obstacle = obstacles.getFirst();

        if (landingOver) {
            int temporaryThreshold;
            int obstacleHeight = obstacle.getHeight();


            if (obstacleHeight * ALS >= RESA) {
                temporaryThreshold = obstacleHeight * ALS;
            } else {
                temporaryThreshold = RESA;
            }

            if ((temporaryThreshold + stripEnd) >= blastProtectionValue) {
                this.newLDA = LDA - obstacle.getDistanceFromThreshold() - temporaryThreshold - stripEnd;
            } else {
                this.newLDA =  LDA - obstacle.getDistanceFromThreshold() - blastProtectionValue;
            }


        }
        else if (landingToward) {

            this.newLDA = obstacle.getDistanceFromThreshold() - RESA - stripEnd;

        }
    }


    private void calculateTORA_ASDA_TODA() {

        Obstacle obstacle = obstacles.getFirst();

        if (takeoffToward) {
            int temporaryThreshold;
            int obstacleHeight = obstacle.getHeight();

            if (obstacleHeight * TOCS >= RESA) {
                temporaryThreshold = obstacle.getHeight() * TOCS;
            } else {
                temporaryThreshold = RESA;
            }

            this.newTORA = obstacle.getDistanceFromThreshold() + displacedThreshold - temporaryThreshold - stripEnd;
            this.newASDA = newTORA;
            this.newTODA = newTORA;
        }

        else if (takeoffAway) {

            this.newTORA = TORA - blastProtectionValue - obstacle.getDistanceFromThreshold() - displacedThreshold ;
            this.newASDA = newTORA;
            this.newTODA = newTORA;
        }

    }


    private void setDirection () {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public String getDegrees() {
        return this.degrees;
    }
}