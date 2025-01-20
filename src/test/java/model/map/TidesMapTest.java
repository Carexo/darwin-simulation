package model.map;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.*;

class TidesMapTest {
    private final int width = 10;
    private final int height = 10;

    private Configuration createConfig(int startingOceanCount, int waterSegments) {
        Configuration config = new Configuration();
        config.setStartingOceanCount(startingOceanCount);
        config.setWaterSegments(waterSegments);
        return config;
    }

    @Test
    void testOceanGeneration() {
        Configuration config = createConfig(5, 3);
        TidesMap tidesMap = new TidesMap(config);

        // Ensure water map has the correct initial count
        int startingOceanCount = config.getStartingOceanCount();
        assertEquals(startingOceanCount, tidesMap.getWaterMap().size());
    }

    @Test
    void testTideMapCreation() {
        Configuration config = createConfig(5, 3);
        TidesMap tidesMap = new TidesMap(config);

        tidesMap.getWaterMap().forEach((key, water) -> tidesMap.checkTides(key));
        assertTrue(tidesMap.getWaterMap().size() > 0);
    }

    @Test
    void testSwitchOceanState() {
        Configuration config = createConfig(5, 3);
        TidesMap tidesMap = new TidesMap(config);


        assertFalse(tidesMap.getOceanState());
        tidesMap.switchOceanState();
        assertTrue(tidesMap.getOceanState());
        assertTrue(tidesMap.getWaterMap().size() > config.getStartingOceanCount());
        tidesMap.switchOceanState();
        assertFalse(tidesMap.getOceanState());
        assertEquals(config.getStartingOceanCount(), tidesMap.getWaterMap().size());
    }

    @Test
    void testGenPlantSpaces() {
        Configuration config = createConfig(5, 3);
        TidesMap tidesMap = new TidesMap(config);

        tidesMap.genPlantSpaces();
        for (Map.Entry<Vector2D, Water> entry : tidesMap.getWaterMap().entrySet()) {
            assertFalse(tidesMap.plantSpaces.contains(entry.getKey()));
        }
    }
}