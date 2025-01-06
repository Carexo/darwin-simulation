package model.map;

import model.elements.Vector2D;

public record Boundary(Vector2D lowerLeft, Vector2D upperRight) {
    public boolean inBounds(Vector2D position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }
}
