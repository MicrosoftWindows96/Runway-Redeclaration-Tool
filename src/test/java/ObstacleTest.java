import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.universityofsouthampton.runwayredeclarationtool.airport.Obstacle;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}