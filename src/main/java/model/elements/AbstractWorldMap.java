package model.elements;

import model.Configuration;
import model.MoveValidator;
import model.elements.animal.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
    protected Vector2D bottomLeft = new Vector2D(0,0);
    protected Vector2D topRight;
    protected int grassCount;
    protected Map<Vector2D, Animal> animalList = new HashMap<>();
    protected int Id = 0;


    public AbstractWorldMap(Configuration config) {
        int x = config.getMapWidth() - bottomLeft.x();
        int y = config.getMapHeight() - bottomLeft.y();
        topRight = new Vector2D(x,y);
        grassCount = config.getStartingGrassCount();
    }

    @Override
    public WorldElement objectAt(Vector2D position) {
        if (animalList.containsKey(position)) {
            return animalList.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        if (objectAt(position) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return (!isOccupied(position) || !(objectAt(position) instanceof Animal));
    }

    @Override
    public boolean place(Animal animal) {
        Vector2D position = animal.getPosition();
        if(canMoveTo(position)){
            animalList.put(position, animal);
            return true;
        };
        return false;
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>(animalList.values());
        return elements;
    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void move(Animal animal) {
        Vector2D currPosition = animal.getPosition();
        animal.move( this);
        animalList.remove(currPosition);
        Vector2D position = animal.getPosition();
        position = getNewPosition(position);
        animal.setPosition(position);
        animalList.put(animal.getPosition(), animal);
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() < bottomLeft.x()) {
            return new Vector2D(bottomLeft.x(),position.y());
        }
        else if (position.x() > topRight.x()) {
            return new Vector2D(topRight.x(),topRight.y());
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
