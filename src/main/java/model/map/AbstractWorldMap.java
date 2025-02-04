package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.WorldElement;
import model.elements.animal.AbstractAnimal;
import util.MapVisualizer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements WorldMap {
    protected final UUID id = UUID.randomUUID();

    protected final Map<Vector2D, Plant> plants = new ConcurrentHashMap<>();
    protected final int width;
    protected final int height;
    private final Boundary boundary;
    protected int grassCount;
    private final Map<Vector2D, List<AbstractAnimal>> animals = new ConcurrentHashMap<>();
    private final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected int freePlantSpaces;
    protected int grassGrowthPerDay;
    protected List<Vector2D> plantSpaces = new ArrayList<>();


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


    public void remove(WorldElement element) {
        if (element instanceof AbstractAnimal) {
            removeAnimal((AbstractAnimal) element);
        } else if (element instanceof Plant) {
            removePlant((Plant) element);
        }
    }

    private void removePlant(Plant plant) {
        plantSpaces.add(plant.getPosition());
        plants.remove(plant.getPosition());
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
    public void place(AbstractAnimal animal) {
        if(!canMoveTo(animal.getPosition())){
            throw new IllegalArgumentException("Cannot place animal at " + animal.getPosition());
        };

        addAnimal(animal);

    }


    public Map<Vector2D, List<AbstractAnimal>> getAnimals() {
        return animals;
    }

    public List<AbstractAnimal> getAnimalsList() {
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
    public void move(AbstractAnimal animal) {
        removeAnimal(animal);

        animal.move(this);

        addAnimal(animal);
    }

    public void updateFreePlantSpaces() {
        this.freePlantSpaces = this.width * this.height - this.plants.size();
    }

    public int getFreePlantSpaces() {
        return this.freePlantSpaces;
    }

    protected void initialPlantGenerator(Configuration config) {
        Collections.shuffle(plantSpaces);
        for(int i = 0; i< config.getStartingGrassCount(); i++) {
            Vector2D v = plantSpaces.getFirst();
            plantSpaces.removeFirst();
            plants.put(v, new Plant(v));
        }

    }

    public abstract void growPlants();

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        return position;
    }

    public Boundary getMapBoundary() {
        return boundary;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }



}
