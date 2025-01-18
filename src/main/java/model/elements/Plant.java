package model.elements;

import javafx.scene.paint.Color;

public class Plant implements WorldElement {
    private final Vector2D position;

    public Plant(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean isAt(Vector2D positionCheck) {
        return (position.equals(positionCheck));
    }

    @Override
    public String getImageSource() {
        return "images/plant.png";
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}
