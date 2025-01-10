package model;

import model.elements.Vector2D;

public interface MoveValidator {

    Vector2D getNewPosition(Vector2D position);

    boolean canMoveTo(Vector2D position);
}
