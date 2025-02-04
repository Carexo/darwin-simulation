package model.elements;

import javafx.scene.paint.Color;

public class Water implements WorldElement{
    Vector2D position;

    public Water(Vector2D pos) {
        position = pos;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public String getImageSource() {
        return "images/water.png";
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }
}