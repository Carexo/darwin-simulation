package model.map;

import com.sun.javafx.geom.Vec2d;
import model.Configuration;
import model.elements.EarthPlant;
import model.elements.Vector2D;
import model.elements.animal.Animal;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class EarthMapTest {

//    @Test
//    void removePlant() {
//        Configuration config = new Configuration();
//
//        EarthMap map = new EarthMap(config);
//        map.plants.forEach((key, value) -> {map.remove(value);});
//        Vector2D v = new Vector2D(0,0);
//        EarthPlant plant = new EarthPlant(v, false);
//
//
//        assertTrue(map.plants.isEmpty());
//    }

    @Test
    void updateFreePlantSpaces() {
    }

    @Test
    void growPlants() {
    }

    @Test
    void remove() {
    }

    @Test
    void getNewPositionLeftBoundary() {
        Vector2D v = new Vector2D(-1,0);
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        assertEquals(map.getNewPosition(v), new Vector2D(map.width-1, 0));
    }

    @Test
    void getNewPositionRightBoundary() {
        Vector2D v = new Vector2D(-1,0);
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        assertEquals(map.getNewPosition(v), new Vector2D(map.width-1, 0));
    }

    @Test
    void getNewPositionTopBoundary() {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        Vector2D v = new Vector2D(0,map.height);
        assertEquals(map.getNewPosition(v), new Vector2D(0, map.height-1));
    }

    @Test
    void getNewPositionBottomBoundary() {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        Vector2D v = new Vector2D(0,-1);
        assertEquals(map.getNewPosition(v), new Vector2D(0, 0));
    }


    @Test
    void canMoveToFalse() {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        Vector2D v = new Vector2D(0,0);
        Vector2D v2 = new Vector2D(1,0);
        Animal A = new Animal(v, 10, config);
        map.place(A);
        Animal B = new Animal(v, 10, config);
        assertFalse(map.canMoveTo(new Vector2D(0,-1)));
    }
    @Test
    void canMoveToTrue() {
        Configuration config = new Configuration();
        EarthMap map = new EarthMap(config);
        Vector2D v = new Vector2D(0,0);
        Vector2D v2 = new Vector2D(1,0);
        Animal A = new Animal(v, 10, config);
        map.place(A);
        Animal B = new Animal(v, 10, config);
        assertTrue(map.canMoveTo(new Vector2D(0,0)));
    }
}