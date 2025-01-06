package model.map;

import model.Configuration;
import model.elements.Vector2D;

public class EarthMap extends AbstractWorldMap {

    public EarthMap(Configuration config) {
        super(config);
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() >= width)
            return new Vector2D(position.x() % width, position.y());
        else if(position.x() < 0)
            return new Vector2D(width - 1, position.y());

        return position;
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return position.y() < height && position.y() >= 0;
    }
}
