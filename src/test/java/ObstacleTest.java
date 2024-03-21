import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;
import org.universityofsouthampton.runwayredeclarationtool.airport.Runway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstacleTest {
    Obstacle obstacle;
    @BeforeEach
    void setUp() {
        obstacle = new Obstacle("Tree",32,21,21);
    }

    @Test
    void testGetAttributes() {
        obstacle = new Obstacle("Tree",32,21,21);
        assertEquals("Tree", obstacle.getName());
        assertEquals(32, obstacle.getHeight());
        assertEquals(21, obstacle.getDistanceFromThreshold());
        assertEquals(21, obstacle.getDistanceFromCentreline());
    }

    @Test
    void testSetAttributes() {
        obstacle = new Obstacle("Tree",32,21,21);
        obstacle.setName("Tower");
        obstacle.setHeight(21);
        obstacle.setDistanceFromThreshold(100);
        obstacle.setDistanceFromCentreline(123);
        assertEquals("Tower", obstacle.getName());
        assertEquals(21, obstacle.getHeight());
        assertEquals(100, obstacle.getDistanceFromThreshold());
        assertEquals(123, obstacle.getDistanceFromCentreline());
    }

    @Test
    void testValidObstacle(){ // TESTING RANGES PARTITIONS
        Runway runway = new Runway("09",0,0,3660,307);
        assertTrue(obstacle.validObstacle(runway));

        obstacle.setDistanceFromCentreline(50);
        assertTrue(obstacle.validObstacle(runway)); // valid distance from centre line

        obstacle.setDistanceFromCentreline(150);
        assertFalse(obstacle.validObstacle(runway)); // invalid distance from centre line

        obstacle.setDistanceFromCentreline(50);
        obstacle.setHeight(20);
        assertTrue(obstacle.validObstacle(runway));  // valid height

        obstacle.setHeight(-10);
        assertFalse(obstacle.validObstacle(runway)); // invalid height (HEIGHT NEGATIVE)

        obstacle.setHeight(40);
        assertFalse(obstacle.validObstacle(runway)); // invalid height (HEIGHT TOO LARGE)

        obstacle.setHeight(20);
        obstacle.setDistanceFromThreshold(200);
        assertTrue(obstacle.validObstacle(runway)); // valid distance from threshold

        obstacle.setDistanceFromThreshold(4000);
        assertFalse(obstacle.validObstacle(runway)); // invalid distance from threshold (DISTANCE BEYOND TORA)

        obstacle.setDistanceFromThreshold(-400);
        assertFalse(obstacle.validObstacle(runway)); // invalid distance from threshold (DISTANCE BEYOND DISPLACED THRESHOLD)
    }

    @Test
    void testObstacleBoundaries() {
        Runway runway = new Runway("09",0,0,3660,307);
        assertTrue(obstacle.validObstacle(runway));

        obstacle.setDistanceFromCentreline(99);
        assertTrue(obstacle.validObstacle(runway)); // Distance from centre < 100

        obstacle.setDistanceFromCentreline(100);
        assertFalse(obstacle.validObstacle(runway)); // Distance from centre > 100

        obstacle.setDistanceFromCentreline(50);
        obstacle.setHeight(34);
        assertTrue(obstacle.validObstacle(runway));  // Height < 35

        obstacle.setHeight(35);
        assertFalse(obstacle.validObstacle(runway)); // Height > 35

        obstacle.setHeight(10);
        assertTrue(obstacle.validObstacle(runway));  // Height < 10

        obstacle.setHeight(9);
        assertFalse(obstacle.validObstacle(runway)); // Height > 10

        obstacle.setHeight(20);
        obstacle.setDistanceFromThreshold(3659);
        assertTrue(obstacle.validObstacle(runway)); // Distance from threshold < TORA

        obstacle.setDistanceFromThreshold(3660);
        assertFalse(obstacle.validObstacle(runway)); // Distance from threshold > TORA

        obstacle.setDistanceFromThreshold(-306);
        assertTrue(obstacle.validObstacle(runway)); // Distance from threshold < - displaced threshold

        obstacle.setDistanceFromThreshold(-307);
        assertFalse(obstacle.validObstacle(runway)); // Distance from threshold > - displaced threshold
    }

    @Test
    public void testConstructor() {
        assertEquals("Tree", obstacle.getName());
        assertEquals(32, obstacle.getHeight());
        assertEquals(21, obstacle.getDistanceFromThreshold());
        assertEquals(21, obstacle.getDistanceFromCentreline());
    }

    @Test
    public void testSetName() {
        obstacle.setName("New Name");
        assertEquals("New Name", obstacle.getName());
    }

    @Test
    public void testSetHeight() {
        obstacle.setHeight(20);
        assertEquals(20, obstacle.getHeight());
    }

    @Test
    public void testSetDistanceFromThreshold() {
        obstacle.setDistanceFromThreshold(200);
        assertEquals(200, obstacle.getDistanceFromThreshold());
    }

    @Test
    public void testSetDistanceFromCentreline() {
        obstacle.setDistanceFromCentreline(100);
        assertEquals(100, obstacle.getDistanceFromCentreline());
    }
}


