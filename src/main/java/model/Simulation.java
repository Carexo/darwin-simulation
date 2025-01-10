package model;

import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.animal.AbstractAnimal;
import model.elements.animal.Animal;
import model.map.AbstractWorldMap;
import model.util.ConsoleMapDisplay;
import model.util.RandomPositionGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation {
    private int stepCount = 10;
    private final Set<AbstractAnimal> animals;
    private final AbstractWorldMap map;

    public Simulation(AbstractWorldMap map, Configuration configuration) {
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        this.map = map;
        this.map.subscribe(consoleMapDisplay);

        this.animals = new HashSet<>();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getStartingAnimalsCount());

        for (Vector2D position: randomPositionGenerator) {
            AbstractAnimal animal = new Animal(position, configuration.getAnimalStartingEnergy(), configuration);
            animals.add(animal);
            map.place(animal);
        }
    }

    public void run() {
        while (SimulationCanRun()) {
            moveAnimals();
            breadAnimals();
            this.map.growPlants();
        }
    }

    public boolean SimulationCanRun() {
        stepCount--;
        return stepCount > 0;
    }


    private void moveAnimals() {
        animals.forEach(map::move);
    }

    public void breadAnimals() {
        List<Vector2D> animalsPosition = animals.stream().map(AbstractAnimal::getPosition).toList();

        for (Vector2D animalPosition: animalsPosition) {
            List<AbstractAnimal> canBreadAnimals = map.objectsAt(animalPosition).map(AbstractAnimal.class::cast).filter(AbstractAnimal::canBread).toList();
            for (int i = 1; i < canBreadAnimals.size(); i += 2) {
                AbstractAnimal parent1 = canBreadAnimals.get(i);
                AbstractAnimal parent2 = canBreadAnimals.get(i - 1);

                AbstractAnimal child = parent1.reproduce(parent2);
                animals.add(child);
                map.place(child);
            }
        }

    }


}
