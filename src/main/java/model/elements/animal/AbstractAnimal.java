package model.elements.animal;

import javafx.scene.paint.Color;
import model.Configuration;
import model.map.MoveValidator;
import model.elements.MapDirection;
import model.elements.Vector2D;
import model.elements.WorldElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;

public abstract class AbstractAnimal implements WorldElement {
    private final String animalName = String.format("%s",(char) ThreadLocalRandom.current().nextInt(65, 91));
    private Vector2D position;
    private MapDirection direction;
    private int energyLevel;
    private int eatenGrass = 0;
    private final Genome genome;
    protected final Configuration configuration;
    private final List<AbstractAnimal> children;
    private int age = 1;
    private int diedAt = -1;

    public AbstractAnimal(Vector2D position, int energyLevel, Configuration configuration) {
        this.children = new ArrayList<>();
        this.configuration = configuration;
        this.genome = Genome.RandomGenome(configuration.getGenomeLength());

        placeAnimal(position, energyLevel);

    }

    public AbstractAnimal(Vector2D position, int energyLevel, Genome genome, Configuration configuration) {
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

    public AbstractAnimal reproduce(AbstractAnimal other) {
        int side = ThreadLocalRandom.current().nextInt(0, 2);

        float genomePercent = max(energyLevel, other.energyLevel)/(float)(energyLevel + other.energyLevel);

        Genome childGenome = generateChildGenome(other, side, genomePercent);

        energyLevel -= configuration.getAnimalEnergyGivenToChild();
        other.energyLevel -= configuration.getAnimalEnergyGivenToChild();

        AbstractAnimal child = new Animal(position, configuration.getAnimalEnergyGivenToChild(), childGenome, configuration);
        children.add(child);

        return child;
    }

    private Genome generateChildGenome(AbstractAnimal other, int side, float genomePercent) {
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

    public void aging() {
        age++;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public boolean isAlive() {
        return energyLevel > 0;
    }

    public boolean canBread() {
        return energyLevel > configuration.getAnimalReadyToBreedEnergyLevel();
    }

    @Override
    public boolean isAt(Vector2D position) {
        return this.position.equals(position);
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public int getDescendantCount() {
        return children.stream().mapToInt(AbstractAnimal::getDescendantCount).sum() + children.size();
    }

    public int getEatenGrass() {
    return eatenGrass;
}

    public int getEnergyLevel() {
        return energyLevel;
    }

    public String getAnimalName() {
        return animalName;
    }

    public int getAge() {
        return age;
    }

    public int getDiedAt() {
        return diedAt;
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public String getImageSource() {
        return "images/animal.png";
    }

    @Override
    public Color getColor() {
        double maxEnergy = configuration.getAnimalStartingEnergy();
        double energyRatio = Math.min(energyLevel / maxEnergy, 1);

        double red = 1.0;
        double green = 1.0 - energyRatio;
        double blue = 1.0 - energyRatio * 0.5;

        return Color.color(red, green, blue);
    }
}
