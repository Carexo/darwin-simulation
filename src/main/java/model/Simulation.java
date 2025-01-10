package model;

import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.animal.Animal;
import model.map.AbstractWorldMap;
import model.util.AnimalUtils;
import model.util.ConsoleMapDisplay;
import model.util.RandomPositionGenerator;

import java.util.*;

public class Simulation {
    private int stepCount = 10;
    private int dayNumber = 1;
    private final AbstractWorldMap map;
    private final Configuration configuration;

    public Simulation(AbstractWorldMap map, Configuration configuration) {
        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        this.map = map;
        this.map.subscribe(consoleMapDisplay);
        this.configuration = configuration;


        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getStartingAnimalsCount());

        for (Vector2D position: randomPositionGenerator) {
            Animal animal = new Animal(position, configuration.getAnimalStartingEnergy(), configuration);
            map.place(animal);
        }
    }


    public void run() {
        while (SimulationCanRun()) {
            dayNumber++;

            removeDeathAnimal();
            moveAnimals();
            animalEats();
            breadAnimals();
            // TODO: growPlants();
        }
    }

    public boolean SimulationCanRun() {
        return !(map.getAnimalsList().isEmpty() || dayNumber > configuration.getTotalSimulationDays());
    }

    private void removeDeathAnimal() {
        List<Animal> deadAnimals = AnimalUtils.getDeathAnimals(map.getAnimalsList());

        deadAnimals.forEach(map::remove);
        deadAnimals.forEach(animal -> animal.setDiedAt(dayNumber));
    }

    private void animalEats() {
        Map<Vector2D, List<Animal>> animals = map.getAnimals();
        Map<Vector2D, Plant> plants = map.getPlants();

        for (Vector2D animalPosition: animals.keySet()) {
            if (plants.get(animalPosition) == null) {
                continue;
            }

            Animal strongestAnimal = AnimalUtils.getStrongestAnimal(animals.get(animalPosition));
            strongestAnimal.eat();
            map.remove(plants.get(animalPosition));
        }

    }

    private void moveAnimals() {
        map.getAnimalsList().forEach(map::move);
    }

    public void breadAnimals() {
        Map<Vector2D, List<Animal>> animals = map.getAnimals();


        for (Vector2D animalPosition: animals.keySet()) {
            List<Animal> canBreadAnimals = AnimalUtils.getBreedingAnimals(animals.get(animalPosition));

            for (int i = 1; i < canBreadAnimals.size(); i += 2) {
                Animal parent1 = canBreadAnimals.get(i);
                Animal parent2 = canBreadAnimals.get(i - 1);

                Animal child = parent1.reproduce(parent2);
                map.place(child);
            }
        }
    }
}
