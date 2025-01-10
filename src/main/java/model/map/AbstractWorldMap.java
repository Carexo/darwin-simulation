package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;
import model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();

    protected final Map<Vector2D, Plant> plants = new HashMap<>();
    protected int freePlantSpaces;

    protected final int width;
    protected final int height;
    List<MapChangeListener> listeners = new ArrayList<>();
    private final Boundary boundary;
    protected int grassCount;
    protected int grassGrowthPerDay;
    private final Map<Vector2D, List<AbstractAnimal>> animals = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);


    public AbstractWorldMap(Configuration config) {
        width = config.getMapWidth();
        height = config.getMapHeight();
        boundary = new Boundary(new Vector2D(0, 0), new Vector2D(width - 1, height - 1));
        grassCount = config.getStartingGrassCount();
        grassGrowthPerDay = config.getGrassGrowthPerDay();
    }

    private void addAnimal(AbstractAnimal animal) {
        Vector2D position = animal.getPosition();
        List<AbstractAnimal> animalList = animals.computeIfAbsent(position, k -> new ArrayList<>());
        animalList.add(animal);
    }

    private void removeAnimal(AbstractAnimal animal) {
        Vector2D position = animal.getPosition();
        List<AbstractAnimal> animalList = animals.get(position);
        if (animalList != null) {
            animalList.remove(animal);
            if (animalList.isEmpty()) {
                animals.remove(position);
            }
        }
    }
    private void removePlant(Plant plant) {
        plants.remove(plant.getPosition());
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
       return animals.keySet().stream().filter(position::equals).map(animals::get).flatMap(List::stream);
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

        addAnimal(animal);

        notify("Animal" + animal.getAnimalName() + "was placed at " + animal.getPosition());
    }

    @Override
    public List<WorldElement> getElements() {
        return animals.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void move(AbstractAnimal animal) {
        Vector2D currPosition = animal.getPosition();

        removeAnimal(animal);
        animal.move(this);
        addAnimal(animal);

        notify("Animal " + animal.getAnimalName() + " moved from " + currPosition + " to " + animal.getPosition());
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        return position;
    }

    public Boundary getMapBoundary() {
        return boundary;
    }

    public Map<Vector2D, Plant> getPlants() {
        return this.plants;
    }

    public void updateFreePlantSpaces() {
        this.freePlantSpaces = this.width * this.height - this.plants.size();
    }

    public int getFreePlantSpaces() {
        return this.freePlantSpaces;
    }

    public abstract void growPlants();

}
