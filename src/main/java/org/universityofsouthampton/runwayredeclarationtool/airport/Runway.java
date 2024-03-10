package org.universityofsouthampton.runwayredeclarationtool.airport;

import java.util.ArrayList;

/**
 * This class stores the individual information of a runway to be retrieved by UI classes to visualise the info.
 */
public class Runway {

    private String name; // Runway name
    private final String direction; // Runway direction
    String logicalRunway1, logicalRunway2;
    private String logicalRunway; //runway name if used from the other side. 09L -> 27R
    private final int TORA; // Take-Off Run Available (same as length of runway)
    private final int TODA; // Take-Off Distance Available
    private final int ASDA; // Accelerate-Stop Distance Available
    private final int LDA; // Landing Distance Available

    private int newTORA; // Calculated Take-Off Run Available
    private int newTODA; // Calculated Take-Off Distance Available
    private int newASDA; // Calculated Accelerate-Stop Distance Available
    private int newLDA; // Calculated Landing Distance Available
    private final int displacedThreshold; // Displaced Threshold
    private final ArrayList<Obstacle> obstacles; // ArrayList of Obstacles
    private final int RESA = 240; // Runway End Safety Area
    private int TOCS = 50; // Take-Off Climb Surface
    private final int ALS = 50; // Approach Landing Surface
    private final int stripEnd = 60;
    private int blastProtectionValue;
    private boolean landingOver;
    private boolean landingToward;
    private boolean takeoffAway;
    private boolean takeoffToward;
    private int stopway;
    private int clearway;

    public Runway(String name,String direction, int stopway, int clearway, int TORA, int displacedThreshold){
        this.name = name;
        this.direction = direction;
        this.stopway = stopway;
        this.clearway = clearway;
        this.TORA = TORA;
        this.displacedThreshold = displacedThreshold;
        this.obstacles = new ArrayList<>();
        this.TODA = TORA + clearway;
        this.ASDA = TORA + stopway;
        this.LDA = TORA - displacedThreshold;
        if(!isValidName(name) && checkValidParameters()){
            throw new IllegalArgumentException("Invalid runway parameters");
        }
    }

    // Validators for the runway object
    public boolean isValidName(String name){  // Checks name credentials
        String regex = "^(0[1-9]|1[0-9]|2[0-9]|3[0-6])(L|R|C)?$";
        return name.matches(regex);
    }
    public boolean checkValidParameters(){ // Checks parameters
        boolean greaterThanZero = (TORA<0 || TODA <0 || ASDA <0 || LDA<0 || displacedThreshold <0 || stopway <0 || clearway<0);
        boolean checkTODA = (TODA == TORA + clearway && TODA > TORA);
        boolean checkASDA = (ASDA == TORA + stopway || ASDA == LDA + stopway);
        return (greaterThanZero && checkTODA && checkASDA);
    }

    // Removing obstacle AND adding obstacle changes calculation conditions
    public void addObstacle(Obstacle obstacle) {
        if(obstacle == null){
            throw new IllegalArgumentException("Invalid obstacle");
        }
        this.obstacles.add(obstacle);
        if (obstacle.getDistanceFromThreshold() <= 500) {
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
        System.out.println("Added Obstacle! Setting calculation conditions");
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
        System.out.println("Removed Obstacle, reverted to original Values!");
    }

    public void runCalculations() { // Only call when blast protection value is set
        if (obstacles.size() > 0) {
            System.out.println("Running calculations...");
            this.calculateLDA(obstacles.get(0));
            this.calculateTORA_ASDA_TODA(obstacles.get(0));
        } else {
            System.out.println("No obstacle present!");
        }
    }

    private void calculateLDA(Obstacle obstacle) {
        if (landingOver) {
            int obstacleHeight = obstacle.getHeight();
            int temporaryThreshold = Math.max(obstacleHeight * ALS, RESA);

            if ((temporaryThreshold + stripEnd) >= blastProtectionValue) {
                this.newLDA = LDA - obstacle.getDistanceFromThreshold() - temporaryThreshold - stripEnd;
            } else {
                this.newLDA =  LDA - obstacle.getDistanceFromThreshold() - blastProtectionValue;
            }
            this.newTODA = this.TORA + stopway;
            this.newASDA = this.TORA + clearway;
        }
        else if (landingToward) {
            this.newLDA = obstacle.getDistanceFromThreshold() - RESA - stripEnd;
            this.newTODA = TORA;
        }
        System.out.println("Successfully calculated LDA");
    }

    private void calculateTORA_ASDA_TODA(Obstacle obstacle) {
        if (takeoffToward) {
            int obstacleHeight = obstacle.getHeight();
            int temporaryThreshold = Math.max(obstacleHeight * TOCS, RESA);

            this.newTORA = obstacle.getDistanceFromThreshold() + displacedThreshold - temporaryThreshold - stripEnd;
            this.newASDA = newTORA;
            this.newTODA = newTORA;
        }
        else if (takeoffAway) {
            this.newTORA = TORA - blastProtectionValue - obstacle.getDistanceFromThreshold() - displacedThreshold ;
            this.newASDA = newTORA + stopway;
            this.newTODA = newTORA + clearway;
        }
        System.out.println("Successfully calculated TORA, TODA and ASDA");
    }

    public String getCalculationBreakdown() {
        StringBuilder breakdown = new StringBuilder();

        if (!obstacles.isEmpty()) {
            Obstacle obstacle = obstacles.get(0);
            breakdown.append("Calculation Breakdown:\n");

            if (landingOver || landingToward) {
                breakdown.append("Landing Distance Available (LDA) calculation:\n");
                if (landingOver) {
                    breakdown.append("Obstacle is overflown. New LDA = Original LDA - (Distance from Threshold to Obstacle + Temporary Threshold + Strip End)\n");
                    breakdown.append("New LDA = ").append(LDA).append(" - (").append(obstacle.getDistanceFromThreshold()).append(" + ").append(obstacle.getHeight() * ALS).append(" + ").append(stripEnd).append(")\n");
                } else {
                    breakdown.append("Landing toward obstacle. New LDA = Distance from Threshold to Obstacle - RESA - Strip End\n");
                    breakdown.append("New LDA = ").append(obstacle.getDistanceFromThreshold()).append(" - ").append(RESA).append(" - ").append(stripEnd).append("\n");
                }
                breakdown.append(String.format("Resulting LDA: %d meters\n", newLDA));
            }

            if (takeoffAway || takeoffToward) {
                breakdown.append("\nTake-Off Run Available (TORA), Take-Off Distance Available (TODA), Accelerate-Stop Distance Available (ASDA) calculation:\n");
                if (takeoffToward) {
                    breakdown.append("Taking off toward the obstacle.\n");
                    breakdown.append("New TORA = Distance from Threshold to Obstacle + Displaced Threshold - Displaced Threshold - Strip End\n");
                    breakdown.append("New TORA = ").append(obstacle.getDistanceFromThreshold()).append(" + ").append(displacedThreshold).append(" - ").append(RESA).append(" - ").append(stripEnd).append("\n");
                } else {
                    breakdown.append("Taking off away from the obstacle.\n");
                    breakdown.append("New TORA = Original TORA - Blast Protection - Distance from Threshold to Obstacle - Displaced Threshold\n");
                    breakdown.append("New TORA = ").append(TORA).append(" - ").append(blastProtectionValue).append(" - ").append(obstacle.getDistanceFromThreshold()).append(" - ").append(displacedThreshold).append("\n");
                }
                breakdown.append(String.format("Resulting TORA: %d meters, TODA: %d meters, ASDA: %d meters\n", newTORA, newTODA, newASDA));
            }
        } else {
            breakdown.append("No obstacles present. Original runway parameters apply.");
        }

        return breakdown.toString();
    }

    // Setter + getter methods
    public String getName() {
        return name;
    }
    public void setName(String name) throws Exception{
        if(!isValidName(name)){
            throw new Exception("Invalid name");
        }
        else this.name = name;
        setLogicalRunways(name);
    }
    public String getDirection() {
        return this.direction;
    }
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
    public String getLogicalRunway1() {
        return logicalRunway1;
    }

    public String getLogicalRunway2() {
        return logicalRunway2;
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
    public ArrayList<Obstacle> getObstacles(){
        return this.obstacles;
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
    public int getBlastProtectionValue() {
        return blastProtectionValue;
    }
    public void setBlastProtectionValue(int BPV) {
        blastProtectionValue = BPV;
    }
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

}