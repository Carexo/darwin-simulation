package model.elements;

import javafx.scene.paint.Color;

public interface WorldElement {
    Vector2D getPosition();

    String getImageSource();

    Color getColor();
}
