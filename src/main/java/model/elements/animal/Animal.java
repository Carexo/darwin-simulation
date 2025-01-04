package model.elements.animal;

import model.Configuration;
import model.elements.Vector2D;

public class Animal extends AbstractAnimal{
    public Animal(Vector2D position, int energyLevel, Configuration configuration) {
        super(position, energyLevel, configuration);
    }

    public Animal(Vector2D position, int energyLevel, Genom genome, Configuration configuration) {
        super(position, energyLevel, genome, configuration);
    }
}
