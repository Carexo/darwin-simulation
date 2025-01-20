package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.Water;
import model.elements.WorldElement;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.lang.Math.min;

public class TidesMap extends AbstractWorldMap {
    private final int waterSegments;
    private boolean oceanState;

    private final int startingOceanCount;

    Map<Vector2D, Water> waterMap = new HashMap<Vector2D, Water>();
    Map<Vector2D, Water> tideMap = new HashMap<Vector2D, Water>();

    public TidesMap(Configuration config) {
        super(config);
        this.startingOceanCount = config.getStartingOceanCount();
        this.waterSegments = config.getWaterSegments();
        {
            try {
                generateOcean();
                waterMap.forEach((key, water) -> checkTides(key));

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ocean generation failed");
            }
        }
        this.oceanState = false;
        try {
            genPlantSpaces();
            super.initialPlantGenerator(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateOcean() {
        List<Vector2D> tempList = new ArrayList<Vector2D>();
        List<Vector2D> freeSlots = new ArrayList<>();

        for (int i = 0; i<width;i++){
            for(int j=0;j<height;j++){
                tempList.add(new Vector2D(i,j));
            }
        }

        Collections.shuffle(tempList);

        for (int i = 0; i < min(waterSegments, startingOceanCount); i++) {
            waterMap.put(tempList.get(i), new Water(tempList.get(i)));
            newFreeSlots(freeSlots, tempList.get(i));
        }

        for (int i = 0; i < this.startingOceanCount - waterSegments; i++) {
            int index;
            if (freeSlots.isEmpty()) {
                index = 0;
            }else {
                index = ThreadLocalRandom.current().nextInt(freeSlots.size());
            }
            Vector2D v = freeSlots.get(index);
            waterMap.put(v, new Water(v));
            freeSlots.remove(index);
            newFreeSlots(freeSlots, v);

        }

    }

    private void newFreeSlots(List<Vector2D> freeSlots, Vector2D pos) {
        int[] x_change = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] y_change = {0, -1, -1, -1, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            Vector2D newPos = new Vector2D(pos.x() + x_change[i], pos.y() + y_change[i]);
            if (this.getMapBoundary().inBounds(newPos) && !freeSlots.contains(newPos) && !waterMap.containsKey(newPos)) {
                freeSlots.add(newPos);
            }
        }
    }
    void checkTides(Vector2D pos) {
        int[] x_change = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] y_change = {0, -1, -1, -1, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            Vector2D newPos = new Vector2D(pos.x() + x_change[i], pos.y() + y_change[i]);
            if (this.getMapBoundary().inBounds(newPos) && !tideMap.containsKey(newPos) && !waterMap.containsKey(newPos)) {
                tideMap.put(newPos, new Water(newPos));
            }
        }
    }

    public void switchOceanState() {
        if (this.oceanState == false) {
            waterMap.putAll(tideMap);
            this.oceanState = true;
        } else {
            tideMap.forEach((key, value) -> waterMap.remove(key));
            this.oceanState = false;
        }

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
        return !waterMap.containsKey(position) && getMapBoundary().inBounds(position);
    }

    public boolean getOceanState() {
        return oceanState;
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
                        waterMap.keySet().stream()
                                .filter(position::equals)
                                .map(waterMap::get)
                )
        );
    }

    public Map<Vector2D, Water> getWaterMap() {
        return waterMap;
    }

    void genPlantSpaces() {
        for(int i = 0; i<width; i++){
            for(int j = 0; j<height; j++){
                if(!waterMap.containsKey(new Vector2D(i,j)) && !tideMap.containsKey(new Vector2D(i,j))) {
                    super.plantSpaces.add(new Vector2D(i,j));
                }
            }
        }


    }


}