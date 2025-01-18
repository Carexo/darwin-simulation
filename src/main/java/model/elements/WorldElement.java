package model.elements;

import javafx.scene.paint.Color;

public interface WorldElement {
    Vector2D getPosition();

    boolean isAt(Vector2D position);

    String getImageSource();

    Color getColor();
}
