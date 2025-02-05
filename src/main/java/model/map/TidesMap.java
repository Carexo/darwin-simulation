package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.Water;
import model.elements.WorldElement;
import util.RandomPositionGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.lang.Math.min;

public class TidesMap extends AbstractWorldMap {
    private boolean inflow;

    private final int startingOceanCount;

    private final ConcurrentHashMap<Vector2D, Water> water = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Vector2D, Water> tides = new ConcurrentHashMap<>();

    public TidesMap(Configuration config) {
        super(config);
        this.startingOceanCount = config.getStartingOceanCount();

        {
            try {
                generateOcean();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ocean generation failed");
            }
        }

        this.inflow = false;

        try {
            genPlantSpaces();
            super.initialPlantGenerator(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateOcean() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, startingOceanCount);

        for (Vector2D position : randomPositionGenerator) {
            water.put(position, new Water(position));

            List<Vector2D> neighbours = position.getNeighbors();

            for (Vector2D neighbour : neighbours) {
                if (this.getMapBoundary().inBounds(neighbour) && !water.containsKey(neighbour)) {
                    tides.put(neighbour, new Water(neighbour));
                }
            }

        }
    }

    public void switchOceanState() {
        this.inflow = !this.inflow;
    }

    @Override
    public void growPlants() {
        for(int i = 0; i< min(plantSpaces.size(), super.grassGrowthPerDay); i++) {
            Vector2D v = plantSpaces.getFirst();
            plants.put(v, new Plant(v));
            plantSpaces.removeFirst();
        }
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return !water.containsKey(position) && getMapBoundary().inBounds(position);
    }

    public boolean getInflow() {
        return inflow;
    }

    @Override
    public Stream<WorldElement> objectsAt(Vector2D position) {

        return Stream.concat(
                super.getAnimals().keySet().stream()
                        .filter(position::equals)
                        .map(super.getAnimals()::get)
                        .flatMap(List::stream),
                Stream.concat(
                        plants.keySet().stream()
                                .filter(position::equals)
                                .map(plants::get),
                        getWaters().keySet().stream().filter(position::equals).map(getWaters()::get)
                )
        );
    }

    void genPlantSpaces() {
        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                if(!water.containsKey(new Vector2D(i,j)) && !tides.containsKey(new Vector2D(i,j))) {
                    super.plantSpaces.add(new Vector2D(i,j));
                }
            }
        }
    }

    public List<Vector2D> getWaterPositionList() {
        return getWaters().keySet().stream().toList();
    }

    private HashMap<Vector2D, Water> getWaters() {
        HashMap<Vector2D, Water> waters = new HashMap<>(water);

        if (inflow) {
            waters.putAll(tides);
        }

        return waters;
    }

}