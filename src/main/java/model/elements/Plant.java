package model.elements;

import java.util.Vector;

public class Plant implements WorldElement {
    private final Vector2D position;


    public Plant(Vector2D pos) {
        this.position = pos;

    }


    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public boolean isAt(Vector2D position) {
        return (this.getPosition() == position);
    }



}
