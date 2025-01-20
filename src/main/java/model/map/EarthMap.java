package model.map;

import model.Configuration;
import model.elements.EarthPlant;
import model.elements.Vector2D;
import model.elements.WorldElement;

import static java.lang.Math.min;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EarthMap extends AbstractWorldMap {
    List<Vector2D> nonPreferable = new ArrayList<Vector2D>();
    List<Vector2D> preferable = new ArrayList<>();

    public EarthMap(Configuration config) {
        super(config);
        initialPlantGenerator(config);
    }

    @Override
    protected void initialPlantGenerator(Configuration config) {
        int equatorHeight = this.height/6;
        int equator = this.height/2;

        for(int i = 0; i<this.height; i++){
            for(int j = 0; j<this.width; j++){
                if(i<=equator+equatorHeight && i>=equator-equatorHeight){
                    preferable.add(new Vector2D(j,i));
                }
                else{
                    nonPreferable.add(new Vector2D(j,i));
                }
            }
        }

        Collections.shuffle(preferable);
        Collections.shuffle(nonPreferable);
        int p_ind = 0;
        int np_ind = 0;

        for(int i = 0; i < super.grassCount; i++) {
            if (!preferable.isEmpty() && !nonPreferable.isEmpty()) {
                int chance = ThreadLocalRandom.current().nextInt(5);
                if (chance == 0) {
                    plants.put(nonPreferable.get(np_ind), new EarthPlant(nonPreferable.get(np_ind), false));
                    np_ind++;
                } else {
                    plants.put(preferable.get(p_ind), new EarthPlant(preferable.get(p_ind), true));
                    p_ind++;
                }
            } else if (!preferable.isEmpty()) {
                plants.put(preferable.get(p_ind), new EarthPlant(preferable.get(p_ind), true));
                p_ind++;
            } else if (!nonPreferable.isEmpty()) {
                plants.put(nonPreferable.get(np_ind), new EarthPlant(nonPreferable.get(np_ind), false));
                np_ind++;
            }
        }

        preferable = preferable.subList(p_ind, preferable.size());
        nonPreferable = nonPreferable.subList(p_ind, nonPreferable.size());
    }

    public void removePlant(EarthPlant plant) {
        if (plants.containsValue(plant)) {
            plants.remove(plant.getPosition());
            if (plant.isPreferredPosition()) {
                int index = ThreadLocalRandom.current().nextInt(preferable.size()+1);
                preferable.add(index, plant.getPosition());
            } else {
                int index = ThreadLocalRandom.current().nextInt(nonPreferable.size()+1);
                nonPreferable.add(index, plant.getPosition());
            }
            updateFreePlantSpaces();
        } else throw new IllegalArgumentException("Plant " + plant + " does not exist");

    }

    public Set<Vector2D> getPreferredPlantPositions() {
        return new HashSet<>(preferable);
    }


    @Override
    public void updateFreePlantSpaces() {
        this.freePlantSpaces = preferable.size() + nonPreferable.size();
    }

    @Override
    public void growPlants() {

        for(int i = 0; i < min(super.grassGrowthPerDay, this.getFreePlantSpaces()); i++) {
            if (!preferable.isEmpty() && !nonPreferable.isEmpty()) {
                int chance = ThreadLocalRandom.current().nextInt(5);
                if (chance == 0) {
                    plants.put(nonPreferable.getFirst(), new EarthPlant(nonPreferable.getFirst(), false));
                    nonPreferable.removeFirst();
                } else {
                    plants.put(preferable.getFirst(), new EarthPlant(preferable.getFirst(), true));
                    preferable.removeFirst();
                }
            } else if (!preferable.isEmpty()) {
                plants.put(preferable.getFirst(), new EarthPlant(preferable.getFirst(), true));
                preferable.removeFirst();
            } else if (!nonPreferable.isEmpty()) {
                plants.put(nonPreferable.getFirst(), new EarthPlant(nonPreferable.getFirst(), false));
                nonPreferable.removeFirst();
            }
        }

        updateFreePlantSpaces();
    }

    @Override
    public void remove(WorldElement element) {
        if (element instanceof EarthPlant) {
            removePlant((EarthPlant) element);
        } else {
            super.remove(element);
        }
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() >= width)
            return new Vector2D(position.x() % width, position.y());
        else if (position.x() < 0)
            return new Vector2D(width - 1, position.y());

        return position;
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return position.y() < height && position.y() >= 0;
    }
}
