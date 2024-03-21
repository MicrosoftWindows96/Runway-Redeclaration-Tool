import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

public class RunwayTest {

    Runway runway;
    Obstacle obstacle;
    @BeforeEach
    void setUp(){
        runway = new Runway("27", 0, 78, 3884, 0);
        obstacle = new Obstacle("Tree", 20,50, 2);
    }

    @Test
    void testGetAttributes(){
        assertEquals("27", runway.getName()); //tests getter for name
        assertEquals(3884, runway.getTORA()); //gets TORA
        assertEquals(0, runway.getDisplacedThreshold()); //gets Displaced Threshold
        assertEquals(0, runway.getStopway()); //gets Stopway
        assertEquals(78, runway.getClearway()); //gets Clearway
        assertEquals(3962, runway.getTODA()); // Tests if TODA is calculated properly
        assertEquals(3884, runway.getASDA()); // Tests if ASDA is calculated properly
        assertEquals(3884, runway.getLDA()); // Tests if LDA is calculated properly
    }

    @Test
    void testValidName(){ // This method checks if the given runway name does not match the naming credentials
        assertFalse(runway.isNameInvalid("18"));
        assertTrue(runway.isNameInvalid("30L"));
        assertTrue(runway.isNameInvalid("20R"));
        assertTrue(runway.isNameInvalid("10C"));
        assertTrue(runway.isNameInvalid("00"));
        assertFalse(runway.isNameInvalid("27"));
        assertTrue(runway.isNameInvalid("sjdjsjd"));
    }

    @Test
    void testValidParameters(){
        assertTrue(runway.checkValidParameters());
        assertTrue(new Runway("09",0,0,3660,307).checkValidParameters());
        assertThrows(IllegalArgumentException.class, () -> new Runway("09",-10,0,3660,307).checkValidParameters()); // Negative stopway
        assertThrows(IllegalArgumentException.class, () -> new Runway("09",0,-10,3660,307).checkValidParameters()); // Negative clearway
        assertThrows(IllegalArgumentException.class, () -> new Runway("09",0,0,-3660,307).checkValidParameters()); // negative TORA
        assertThrows(IllegalArgumentException.class, () -> new Runway("09",0,0,3660,-307).checkValidParameters()); // Negative displaced threshold
    }

    @Test
    void testAddObstacle(){
        assertThrows(IllegalArgumentException.class, () -> runway.addObstacle(null)); // Adding a null parameter

        runway = new Runway("09",0,0,3660,307);
        runway.addObstacle(obstacle);
        assertThrows(IllegalArgumentException.class, () -> runway.addObstacle(obstacle)); // Adding an obstacle when there is already one on the runway.
    }

    @Test
    void testCalculations(){
        runway.addObstacle(new Obstacle("Test",20, 50,20));
        runway.setBlastProtectionValue(300);
        runway.runCalculations();
        assertEquals(3534,runway.getNewTORA());
        assertEquals(3612,runway.getNewTODA());
        assertEquals(3534,runway.getNewASDA());
        assertEquals(2774,runway.getNewLDA());
    }

    @Test
    void shouldThrowExceptionWhenInvalidRunwayName() {
        assertThrows(IllegalArgumentException.class, () -> new Runway("50", 200, 300, 3929, 0));
    }

    @Test
    void shouldThrowExceptionWhenInvalidDistances() {
        assertThrows(IllegalArgumentException.class, () -> new Runway("18R", -200, 300, 3929, 0));
    }

    @Test
    void shouldAddObstacleSuccessfully() {
        runway.addObstacle(obstacle);
        assertEquals(1, runway.getObstacles().size());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullObstacle() {
        assertThrows(IllegalArgumentException.class, () -> runway.addObstacle(null));
    }

    @Test
    void shouldRemoveObstacleSuccessfully() {
        runway.addObstacle(obstacle);
        runway.removeObstacle(obstacle);
        assertEquals(0, runway.getObstacles().size());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNullObstacle() {
        assertThrows(IllegalArgumentException.class, () -> runway.removeObstacle(null));
    }

    @Test
    void shouldCalculateDistancesCorrectly() {
        runway.addObstacle(obstacle);
        runway.setBlastProtectionValue(100);
        runway.runCalculations();
        assertTrue(runway.getNewTORA() > 0);
        assertTrue(runway.getNewTODA() > 0);
        assertTrue(runway.getNewASDA() > 0);
        assertTrue(runway.getNewLDA() > 0);
    }
}
