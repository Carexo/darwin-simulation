package model.elements;

public interface WorldElement {
    Vector2D getPosition();

    boolean isAt(Vector2D position);
}
