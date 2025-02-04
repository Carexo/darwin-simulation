package model.elements.animal;

import model.Configuration;
import model.elements.Vector2D;
import model.map.MoveValidator;

import java.util.concurrent.ThreadLocalRandom;

public class AgingAnimal extends AbstractAnimal {
    public AgingAnimal(Vector2D position, int energyLevel, Configuration configuration) {
        super(position, energyLevel, configuration);
    }

    public AgingAnimal(Vector2D position, int energyLevel, Genome genome, Configuration configuration) {
        super(position, energyLevel, genome, configuration);
    }

    @Override
    public void move(MoveValidator moveValidator) {
        double chanceOfSkipMove = configuration.getChanceOfAnimalSkipMove() / 100;
        double ageFactor = Math.min(0.8, (double) getAge() / 100);
        double skipProbability = chanceOfSkipMove * ageFactor;

        if (ThreadLocalRandom.current().nextDouble() < skipProbability) {
            return;
        }

        super.move(moveValidator);
    }
}
