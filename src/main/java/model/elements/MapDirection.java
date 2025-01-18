package model.elements;

import model.elements.animal.Gene;

import java.util.concurrent.ThreadLocalRandom;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public Vector2D toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2D(0, 1);
            case NORTH_EAST -> new Vector2D(1, 1);
            case EAST -> new Vector2D(1, 0);
            case SOUTH_EAST -> new Vector2D(1, -1);
            case SOUTH -> new Vector2D(0, -1);
            case SOUTH_WEST -> new Vector2D(-1, -1);
            case WEST -> new Vector2D(-1, 0);
            case NORTH_WEST -> new Vector2D(-1, 1);
        };
    }

    public MapDirection shift(Gene value) {
        return values()[(this.ordinal() + value.ordinal()) % values().length];
    }

    public static MapDirection getRandom() {
        return values()[ThreadLocalRandom.current().nextInt(0, values().length)];
    }


}
