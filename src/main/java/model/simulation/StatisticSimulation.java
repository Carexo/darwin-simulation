package model.simulation;

import model.elements.animal.AbstractAnimal;
import model.elements.animal.Genome;
import model.map.AbstractWorldMap;

import java.util.*;

public class StatisticSimulation {
    private final AbstractWorldMap map;
    private List<AbstractAnimal> diedAnimals = new ArrayList<>();

    public StatisticSimulation(AbstractWorldMap map) {
        this.map = map;
    }

    public int getAnimalsCount() {
        return map.getAnimalsList().size();
    }

    public int getPlantsCount() {
        return map.getPlants().size();
    }

    public int getFreePositionsCount() {
        int TotalPositions = map.getWidth() * map.getHeight();
        return TotalPositions - getAnimalsCount() - getPlantsCount();
    }

    public double getAverageAnimalEnergy() {
        int animalCount = getAnimalsCount();

        if (animalCount == 0) {
            return 0;
        }

        int sum = 0;

        for (AbstractAnimal animal : map.getAnimalsList()) {
            sum += animal.getEnergyLevel();
        }

        return (double) sum / animalCount;
    }

    public double getAverageDiedAnimalLifespan() {
        int diedAnimalsCount = diedAnimals.size();

        if (diedAnimalsCount == 0) {
            return 0;
        }

        int sum = 0;

        for (AbstractAnimal animal : diedAnimals) {
            sum += animal.getAge();
        }

        return (double) sum / diedAnimalsCount;
    }

    public double getAverageChildCount() {
        int animalCount = getAnimalsCount();

        if (animalCount == 0) {
            return 0;
        }

        int sum = 0;

        for (AbstractAnimal animal : map.getAnimalsList()) {
            sum += animal.getChildrenCount();
        }

        return (double) sum / animalCount;
    }

    public Optional<Genome> getMostPopularGenome() {
        Map<Genome, Integer> genomeCount = new HashMap<>();

        map.getAnimalsList().stream()
                .map(AbstractAnimal::getGenome)
                .forEach(genome -> genomeCount.put(genome, genomeCount.getOrDefault(genome, 0) + 1));

        return genomeCount.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey);
    }


    public void addDiedAnimal(AbstractAnimal animal) {
        diedAnimals.add(animal);
    }
}
