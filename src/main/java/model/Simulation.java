package model;

import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.animal.AbstractAnimal;
import model.elements.animal.AgingAnimal;
import model.elements.animal.Animal;
import model.map.AbstractWorldMap;
import model.map.TidesMap;
import model.util.AnimalUtils;
import model.util.RandomPositionGenerator;

import java.util.*;

public class Simulation implements Runnable {
    protected final UUID simulationId = UUID.randomUUID();
    protected int dayNumber = 0;
    protected final AbstractWorldMap map;
    protected Configuration configuration;

    public Simulation(AbstractWorldMap map, Configuration configuration) {
        this.map = map;
        this.configuration = configuration;


        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getStartingAnimalsCount());

        for (Vector2D position: randomPositionGenerator) {
            if (configuration.getAnimalType() == Configuration.AnimalType.AGING) {
                AbstractAnimal animal = new AgingAnimal(position, configuration.getAnimalStartingEnergy(), configuration);
                map.place(animal);
            } else if (configuration.getAnimalType() == Configuration.AnimalType.NORMAL) {
                AbstractAnimal animal = new Animal(position, configuration.getAnimalStartingEnergy(), configuration);
                map.place(animal);
            }
        }
    }


    public void run() {
        try {
            while (SimulationCanRun()) {
                dayNumber++;

                if(map instanceof TidesMap) {
                    drownAnimals();
                    if (dayNumber % configuration.getOceanChangeRate() == 0) {
                        ((TidesMap) map).switchOceanState();
                    }
                }
                removeDeathAnimal();
                moveAnimals();
                animalEats();
                breadAnimals();
                map.growPlants();
                agingAnimals();

                Thread.sleep(configuration.getSimulationSpeed());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean SimulationCanRun() {
        return !(map.getAnimalsList().isEmpty() || dayNumber > configuration.getTotalSimulationDays());
    }

    protected void removeDeathAnimal() {
        List<AbstractAnimal> deadAnimals = AnimalUtils.getDeathAnimals(map.getAnimalsList());

        deadAnimals.forEach(map::remove);
        deadAnimals.forEach(animal -> animal.setDiedAt(dayNumber));
    }

    protected void animalEats() {
        Map<Vector2D, List<AbstractAnimal>> animals = map.getAnimals();
        Map<Vector2D, Plant> plants = map.getPlants();

        for (Vector2D animalPosition: animals.keySet()) {
            if (plants.get(animalPosition) == null) {
                continue;
            }

            AbstractAnimal strongestAnimal = AnimalUtils.getStrongestAnimal(animals.get(animalPosition));
            strongestAnimal.eat();
            map.remove(plants.get(animalPosition));
        }

    }

    protected void moveAnimals() {
        map.getAnimalsList().forEach(map::move);
    }

    protected void agingAnimals() {
        map.getAnimalsList().forEach(AbstractAnimal::aging);
    }

    public void breadAnimals() {
        Map<Vector2D, List<AbstractAnimal>> animals = map.getAnimals();


        for (Vector2D animalPosition: animals.keySet()) {
            List<AbstractAnimal> canBreadAnimals = AnimalUtils.getBreedingAnimals(animals.get(animalPosition));

            for (int i = 1; i < canBreadAnimals.size(); i += 2) {
                AbstractAnimal parent1 = canBreadAnimals.get(i);
                AbstractAnimal parent2 = canBreadAnimals.get(i - 1);

                AbstractAnimal child = parent1.reproduce(parent2);
                map.place(child);
            }
        }
    }
    public void drownAnimals() {
        map.getAnimals().forEach((v, animalList) -> animalList.forEach(animal -> {if(((TidesMap)map).getWaterMap().containsKey(v)){animal.die(0);}}));
    }

    public UUID getSimulationId() {
        return simulationId;
    }
}
