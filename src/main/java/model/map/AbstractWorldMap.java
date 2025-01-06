package model.map;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;
import model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();
    protected final int width;
    protected final int height;
    List<MapChangeListener> listeners = new ArrayList<>();
    private final Boundary boundary;
    protected int grassCount;
    private final Map<Vector2D, AbstractAnimal> animalList = new HashMap<>();
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
    public Stream<WorldElement> objectsAt(Vector2D position) {
       return animalList.keySet().stream().filter(animalPosition -> animalPosition.equals(position)).map(animalList::get);
    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return objectsAt(position).findAny().isPresent();
    }

    @Override
    public void place(AbstractAnimal animal) {
        if(!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("Cannot place animal at " + animal.getPosition());
        };

        animalList.put(animal.getPosition(), animal);

        notify("Animal" + animal.getAnimalName() + "was placed at " + animal.getPosition());
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
    public void move(AbstractAnimal animal) {
        Vector2D currPosition = animal.getPosition();
        animal.move( this);
        animalList.remove(currPosition);
        Vector2D position = animal.getPosition();

        position = getNewPosition(position);
        animal.setPosition(position);
        animalList.put(animal.getPosition(), animal);

        notify("Animal " + animal.getAnimalName() + " moved from " + currPosition + " to " + animal.getPosition());
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        return position;
    }

    public Boundary getMapBoundary() {
        return boundary;
    }

}
