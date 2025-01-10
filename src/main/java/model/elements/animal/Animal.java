package model.elements.animal;

import model.Configuration;
import model.map.MoveValidator;
import model.elements.MapDirection;
import model.elements.Vector2D;
import model.elements.WorldElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;

public class Animal implements WorldElement {
    private final String animalName = String.format("%s",(char) ThreadLocalRandom.current().nextInt(65, 91));
    private Vector2D position;
    private MapDirection direction;
    private int energyLevel;
    private int eatenGrass = 0;
    private final Genome genome;
    private final Configuration configuration;
    private final List<Animal> children;
    private int diedAt = -1;
    private int age = 1;

    public Animal(Vector2D position, int energyLevel, Configuration configuration) {
        this.children = new ArrayList<>();
        this.configuration = configuration;
        this.genome = Genome.RandomGenome(configuration.getGenomeLength());

        placeAnimal(position, energyLevel);

    }

    public Animal(Vector2D position, int energyLevel, Genome genome, Configuration configuration) {
        this.children = new ArrayList<>();
        this.configuration = configuration;
        this.genome = genome;

        placeAnimal(position, energyLevel);
    }

    public String toString() {
        return animalName;
    }

    public void placeAnimal(Vector2D position, int energyLevel) {
        this.position = position;
        this.energyLevel = energyLevel;
        this.direction = MapDirection.getRandom();
    }

    public void move(MoveValidator moveValidator) {
        this.direction = direction.shift(genome.getActiveGene());
        Vector2D newPosition = moveValidator.getNewPosition(position.add(direction.toUnitVector()));


        if (moveValidator.canMoveTo(newPosition)) {
            position = newPosition;
        } else {
            direction = direction.shift(Gene.ROTATION_180);
        }

        genome.nextGene();
        energyLevel -= configuration.getAnimalEnergyLossPerMove();
    }

    public Animal reproduce(Animal other) {
        int side = ThreadLocalRandom.current().nextInt(0, 2);

        float genomePercent = max(energyLevel, other.energyLevel)/(float)(energyLevel + other.energyLevel);

        Genome childGenome = generateChildGenome(other, side, genomePercent);

        energyLevel -= configuration.getAnimalEnergyGivenToChild();
        other.energyLevel -= configuration.getAnimalEnergyGivenToChild();

        Animal child = new Animal(position, configuration.getAnimalEnergyGivenToChild(), childGenome, configuration);
        children.add(child);

        return child;
    }

    private Genome generateChildGenome(Animal other, int side, float genomePercent) {
        Genome strongGenome = energyLevel > other.energyLevel ? genome : other.genome;
        Genome weakGenome = energyLevel < other.energyLevel ? genome: other.genome;

        Genome childGenome;

        if (side == 0) {
            childGenome = strongGenome.combineGenomes(weakGenome, genomePercent);
        } else {
            childGenome = weakGenome.combineGenomes(strongGenome, genomePercent);
        }

        childGenome.mutate(configuration.getMinimalMutationsCount(), configuration.getMaximalMutationsCount());

        return childGenome;
    }

    public void die(int day) {
        energyLevel = 0;
        diedAt = day;
    }

    public void setDiedAt(int diedAt) {
        this.diedAt = diedAt;
    }

    public void eat() {
        energyLevel += configuration.getGrassEnergyLevel();
        eatenGrass++;
    }
    public boolean isAlive() {
        return energyLevel > 0;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public boolean canBread() {
        return energyLevel > configuration.getAnimalReadyToBreedEnergyLevel();
    }

    public void aging() {
        this.age++;
    }

    @Override
    public boolean isAt(Vector2D position) {
        return this.position.equals(position);
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D newPosition) {
        this.position = newPosition;
    }

    public String getAnimalName() {
        return animalName;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public int getEatenGrass() {
        return eatenGrass;
    }

    public MapDirection getDirection() {
        return direction;
    }

}
