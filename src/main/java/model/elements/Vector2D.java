package model.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Vector2D(int x, int y) {

    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    public boolean precedes(Vector2D other) {
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2D other) {
        return x >= other.x && y >= other.y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    public Vector2D upperRight(Vector2D other) {
        return new Vector2D(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2D opposite() {
        return new Vector2D(-x, -y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public List<Vector2D> getNeighbors() {
        List<Vector2D> neighbors = new ArrayList<>();
        neighbors.add(new Vector2D(x - 1, y));
        neighbors.add(new Vector2D(x + 1, y));
        neighbors.add(new Vector2D(x, y - 1));
        neighbors.add(new Vector2D(x, y + 1));
        neighbors.add(new Vector2D(x - 1, y - 1));
        neighbors.add(new Vector2D(x + 1, y + 1));
        neighbors.add(new Vector2D(x - 1, y + 1));
        neighbors.add(new Vector2D(x + 1, y - 1));
        return neighbors;
    }
}