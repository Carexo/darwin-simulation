package model.map;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.Water;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TidesMapTest {
    private final int width = 10;
    private final int height = 10;

    private Configuration createConfig(int startingOceanCount) {
        Configuration config = new Configuration();
        config.setStartingOceanCount(startingOceanCount);
        return config;
    }

    @Test
    void testOceanGeneration() {
        Configuration config = createConfig(5);
        TidesMap tidesMap = new TidesMap(config);

        // Ensure water map has the correct initial count
        int startingOceanCount = config.getStartingOceanCount();
        assertEquals(startingOceanCount, tidesMap.getWaterPositionList().size());
    }

    @Test
    void testSwitchOceanState() {
        Configuration config = createConfig(5);
        TidesMap tidesMap = new TidesMap(config);


        assertFalse(tidesMap.getInflow());
        tidesMap.switchOceanState();
        assertTrue(tidesMap.getInflow());
        assertTrue(tidesMap.getWaterPositionList().size() > config.getStartingOceanCount());
        tidesMap.switchOceanState();
        assertFalse(tidesMap.getInflow());
        assertEquals(config.getStartingOceanCount(), tidesMap.getWaterPositionList().size());
    }

    @Test
    void testGenPlantSpaces() {
        Configuration config = createConfig(5);
        TidesMap tidesMap = new TidesMap(config);

        tidesMap.genPlantSpaces();
        for (Vector2D position : tidesMap.getWaterPositionList()) {
            assertFalse(tidesMap.plantSpaces.contains(position));
        }
    }
}