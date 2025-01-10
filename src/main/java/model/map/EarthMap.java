package model.map;

import model.Configuration;
import model.elements.EarthPlant;
import model.elements.Plant;
import model.elements.Vector2D;

import java.util.*;

import static java.lang.Math.min;


public class EarthMap extends AbstractWorldMap {
    List<Vector2D> nonPreferable = new ArrayList<Vector2D>();
    List<Vector2D> preferable = new ArrayList<>();
    private final Random random = new Random();

    public EarthMap(Configuration config) {
        super(config);
        initialPlantGenerator(config);
    }

    private void initialPlantGenerator(Configuration config) {
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
            int chance = this.random.nextInt(5);
            if(chance == 0) {
                plants.put(nonPreferable.get(np_ind), new EarthPlant(nonPreferable.get(np_ind), false));
                np_ind++;
            }
            else {
                plants.put(preferable.get(p_ind), new EarthPlant(preferable.get(p_ind), true));
                p_ind++;
            }
        }
        preferable = preferable.subList(p_ind, preferable.size());
        nonPreferable = nonPreferable.subList(p_ind, nonPreferable.size());


    }
    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() >= width)
            return new Vector2D(position.x() % width, position.y());
        else if(position.x() < 0)
            return new Vector2D(width - 1, position.y());

        return position;
    }

    public void removePlant(EarthPlant plant) {
        if (plants.containsValue(plant)) {
            plants.remove(plant.getPosition());
            if (plant.isPreferredPosition()) {
                int index = random.nextInt(preferable.size());
                preferable.add(index, plant.getPosition());
            } else {
                int index = random.nextInt(nonPreferable.size());
                nonPreferable.add(index, plant.getPosition());
            }
            updateFreePlantSpaces();
        } else throw new IllegalArgumentException("Plant " + plant + " does not exist");

    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return position.y() < height && position.y() >= 0;
    }

    @Override
    public void updateFreePlantSpaces() {
        this.freePlantSpaces = preferable.size() + nonPreferable.size();
    }

    @Override
    public void growPlants() {
        for(int i = 0; i < min(super.grassGrowthPerDay, this.getFreePlantSpaces()); i++) {
            int chance = random.nextInt(5);
            if (chance == 0) {
                plants.put(nonPreferable.getFirst(), new EarthPlant(nonPreferable.getFirst(), false));
                nonPreferable.remove(nonPreferable.getFirst());
            } else {
                plants.put(preferable.getFirst(), new EarthPlant(preferable.getFirst(), true));
                preferable.remove(preferable.getFirst());
            }
        }
        updateFreePlantSpaces();
    }
}
