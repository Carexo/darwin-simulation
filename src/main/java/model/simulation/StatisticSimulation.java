package model.simulation;

import model.elements.animal.AbstractAnimal;
import model.elements.animal.Genome;
import model.map.AbstractWorldMap;

import java.util.*;

public class StatisticSimulation {
    private final AbstractWorldMap map;
    private final Simulation simulation;
    private int diedAnimalCount = 0;
    private double diedAnimalLifeSpan = 0;

    public StatisticSimulation(Simulation simulation) {
        this.map = simulation.getMap();
        this.simulation = simulation;
    }

    public int getDayNumber() {
        return simulation.getDayNumber();
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
        return diedAnimalLifeSpan;
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

    public HashSet<AbstractAnimal> getAnimalsWithMostPopularGenome() {
        Optional<Genome> mostPopularGenome = getMostPopularGenome();
        HashSet<AbstractAnimal> animalsWithMostPopularGenome = new HashSet<>();

        if (mostPopularGenome.isEmpty()) {
            return animalsWithMostPopularGenome;
        }

        for (AbstractAnimal animal : map.getAnimalsList()) {
            if (animal.getGenome().equals(mostPopularGenome.get())) {
                animalsWithMostPopularGenome.add(animal);
            }
        }

        return animalsWithMostPopularGenome;
    }


    public void addDiedAnimal(AbstractAnimal animal) {
       diedAnimalLifeSpan = (diedAnimalCount * diedAnimalLifeSpan + animal.getAge()) / (diedAnimalCount + 1);
       diedAnimalCount++;
    }

    public String getCSVHeaders() {
        return "Day number;Animals count;Plants count;Free positions count;Average animal energy;Average died animal lifespan;Average child count";
    }

    public String getCSVData() {
        return getDayNumber() + ";" + getAnimalsCount() + ";" + getPlantsCount() + ";" + getFreePositionsCount() + ";" + getAverageAnimalEnergy() + ";" + getAverageDiedAnimalLifespan() + ";" + getAverageChildCount();
    }
}
