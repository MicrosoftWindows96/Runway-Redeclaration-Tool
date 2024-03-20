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
        runway = new Runway("18R", 200, 300, 3929, 0);
        obstacle = new Obstacle("Tree", 20,50, 2);
    }

    @Test
    void testGetAttributes(){
        runway = new Runway("10L",320,213,3321,200);
        assertEquals("10L", runway.getName()); //tests getter for name
        assertEquals(3321, runway.getTORA()); //gets TORA
        assertEquals(200, runway.getDisplacedThreshold()); //gets Displaced Threshold
        assertEquals(320, runway.getStopway()); //gets Stopway
        assertEquals(213, runway.getClearway()); //gets Clearway
        assertEquals(3534, runway.getTODA());
        assertEquals(3641, runway.getASDA());
    }

    @Test
    void testValidName(){
        assertFalse(runway.isNameInvalid("18"));
        assertFalse(runway.isNameInvalid("30L"));
        assertFalse(runway.isNameInvalid("20R"));
        assertFalse(runway.isNameInvalid("10C"));
        assertTrue(runway.isNameInvalid("00"));
        assertTrue(runway.isNameInvalid("50"));
        assertTrue(runway.isNameInvalid("sjdjsjd"));
    }

    @Test
    void testValidParameters(){

    }

    @Test
    void testAddObstacle(){

    }

    @Test
    void testCalculations(){

    }
}
