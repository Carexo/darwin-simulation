package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.Animal;
import model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();

    protected final Map<Vector2D, Plant> plants = new HashMap<>();
    protected final int width;
    protected final int height;
    List<MapChangeListener> listeners = new ArrayList<>();
    private final Boundary boundary;
    protected int grassCount;
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);


    public AbstractWorldMap(Configuration config) {
        width = config.getMapWidth();
        height = config.getMapHeight();
        boundary = new Boundary(new Vector2D(0, 0), new Vector2D(width - 1, height - 1));
        grassCount = config.getStartingGrassCount();
    }

    private void addAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        List<Animal> animalList = animals.computeIfAbsent(position, k -> new ArrayList<>());
        animalList.add(animal);
    }

    private void removeAnimal(Animal animal) {
        Vector2D position = animal.getPosition();
        List<Animal> animalList = animals.get(position);
        if (animalList != null) {
            animalList.remove(animal);
            if (animalList.isEmpty()) {
                animals.remove(position);
            }
        }
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


    public void remove(WorldElement element) {
        if (element instanceof Animal) {
            removeAnimal((Animal) element);
            notify("Animal " + element + " was removed from the map");
        } else if (element instanceof Plant) {
            plants.remove(element.getPosition());
            notify("Plant " + element + " was removed from the map");
        }
    }

    public String toString() {
        return mapVisualizer.draw(boundary.lowerLeft(), boundary.upperRight());
    }

    @Override
    public Stream<WorldElement> objectsAt(Vector2D position) {
        return Stream.concat(
                animals.keySet().stream().filter(position::equals).map(animals::get).flatMap(List::stream),
                plants.keySet().stream().filter(position::equals).map(plants::get)
        );

    }

    @Override
    public boolean isOccupied(Vector2D position) {
        return objectsAt(position).findAny().isPresent();
    }

    @Override
    public void place(Animal animal) {
        if(!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("Cannot place animal at " + animal.getPosition());
        };

        addAnimal(animal);

        notify("Animal" + animal.getAnimalName() + "was placed at " + animal.getPosition());
    }


    public Map<Vector2D, List<Animal>> getAnimals() {
        return animals;
    }

    public List<Animal> getAnimalsList() {
        return animals.values().stream().flatMap(List::stream).toList();
    }

    public Map<Vector2D, Plant> getPlants() {
        return plants;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void move(Animal animal) {
        Vector2D currPosition = animal.getPosition();

        removeAnimal(animal);

        animal.move(this);

        addAnimal(animal);

        notify("Animal " + animal.getAnimalName() + " " + animal.getEnergyLevel() + " moved from " + currPosition + " to " + animal.getPosition());
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        return position;
    }

    public Boundary getMapBoundary() {
        return boundary;
    }

}
