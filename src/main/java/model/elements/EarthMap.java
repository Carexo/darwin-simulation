package model.elements;

import model.Configuration;

import java.util.AbstractMap;

public class EarthMap extends AbstractWorldMap {

    public EarthMap(Configuration config) {
        super(config);
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() < bottomLeft.x()) {
            return new Vector2D(topRight.x(),position.y());
        }
        else if (position.x() > topRight.x()) {
            return new Vector2D(bottomLeft.x(),topRight.y());
        }
        else if (position.y() < bottomLeft.y()) {
            return new Vector2D(position.x(),bottomLeft.y());
        }
        else if (position.y() > topRight.y()) {
            return new Vector2D(position.x(),topRight.y());
        }
        else{return position;}
    }
}
