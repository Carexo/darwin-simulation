package model.elements;

public class Grass implements WorldElement {
    private final int x;
    private final int y;

    public Grass(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public Vector2D getPosition() {
        return new Vector2D(x, y);
    }

    @Override
    public boolean isAt(Vector2D position) {
        return (this.getPosition() == position);
    }
}
