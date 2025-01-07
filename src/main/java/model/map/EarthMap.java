package model.map;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;

import java.util.*;

public class EarthMap extends AbstractWorldMap {

    public EarthMap(Configuration config) {
        super(config);
        int equatorHeight = this.height/6;
        int equator = this.height/2;
        List<Vector2D> nonPreferable = new ArrayList<Vector2D>();
        List<Vector2D> preferable = new ArrayList<>();
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
        Random random = new Random();
        for(int i = 0; i < super.grassCount; i++) {
            int chance = random.nextInt(5);
            if(chance == 0) {
                Vector2D position = nonPreferable.get(np_ind);
                plants.put(position, new Plant(position));
                np_ind++;
            }
            else {
                Vector2D position = preferable.get(p_ind);
                plants.put(position, new Plant(position));
                p_ind++;
            }
        }
    }

    @Override
    public Vector2D getNewPosition(Vector2D position) {
        if (position.x() >= width)
            return new Vector2D(position.x() % width, position.y());
        else if(position.x() < 0)
            return new Vector2D(width - 1, position.y());

        return position;
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return position.y() < height && position.y() >= 0;
    }
}
