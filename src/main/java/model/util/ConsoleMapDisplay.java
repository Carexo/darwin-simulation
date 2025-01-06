package model.util;

import model.map.MapChangeListener;
import model.map.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        updateCount += 1;
        System.out.printf("Update #%d: %s%n \n", updateCount, message);
        System.out.println(worldMap);
    }
}
