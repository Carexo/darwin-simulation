package model.elements;

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
    public boolean isAt(Vector2D position) {
        return position.equals(this.position);
    }

    @Override
    public String toString() {

        return "~";
    }
}