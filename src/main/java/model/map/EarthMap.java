package model.map;

import model.Configuration;
import model.elements.Vector2D;

import java.util.*;

public class EarthMap extends AbstractWorldMap {

    private List<Vector2D> plants = new ArrayList<>();

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
                plants.add(nonPreferable.get(np_ind));
                np_ind++;
            }
            else {
                plants.add(preferable.get(p_ind));
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
