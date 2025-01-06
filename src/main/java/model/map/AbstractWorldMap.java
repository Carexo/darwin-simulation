package model.map;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.Animal;
import model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();
    protected final int width;
    protected final int height;
    List<MapChangeListener> listeners = new ArrayList<>();
    private final Boundary boundary;
    protected int grassCount;
    private final Map<Vector2D, Animal> animalList = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);


    public AbstractWorldMap(Configuration config) {
        width = config.getMapWidth();
        height = config.getMapHeight();
        boundary = new Boundary(new Vector2D(0, 0), new Vector2D(width - 1, height - 1));
        grassCount = config.getStartingGrassCount();
    }


    public void subscribe(MapChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    protected void notify(String message) {
        for (MapChangeListener listener : listeners) {
            listener.mapChanged(this, message);
        }
    }

    public String toString() {
        return mapVisualizer.draw(boundary.lowerLeft(), boundary.upperRight());
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
        return objectAt(position) != null;
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
        return new ArrayList<>(animalList.values());
    }

    @Override
    public UUID getId() {
        return id;
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
        return position;
    }

    public Boundary getMapBoundary() {
        return boundary;
    }

}
