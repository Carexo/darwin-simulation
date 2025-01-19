package model.simulation;

import model.Configuration;
import model.elements.Plant;
import model.elements.Vector2D;
import model.elements.animal.AbstractAnimal;
import model.elements.animal.AgingAnimal;
import model.elements.animal.Animal;
import model.map.AbstractWorldMap;
import model.map.TidesMap;
import util.AnimalUtils;
import util.RandomPositionGenerator;

import java.util.*;

public class Simulation implements Runnable {
    private final UUID simulationId = UUID.randomUUID();
    private int dayNumber = 0;
    private boolean isPaused = false;
    private final AbstractWorldMap map;
    private final StatisticSimulation statisticSimulation;
    private final Configuration configuration;
    private final Map<SimulationEventType, List<SimulationChangeListener>> listeners = new HashMap<>();

    public enum SimulationEventType {
        CHANGE,
        END,
        RESUME,
        PAUSED
    }

    public Simulation(AbstractWorldMap map, Configuration configuration) {
        this.map = map;
        this.configuration = configuration;
        RandomPositionGenerator randomPositionGenerator = null;
        this.statisticSimulation = new StatisticSimulation(this);

        if (map instanceof TidesMap) {
            List<Vector2D> keyList = new ArrayList<Vector2D>(((TidesMap) map).getWaterMap().keySet());
            randomPositionGenerator = new RandomPositionGenerator(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getStartingAnimalsCount(), keyList);

        } else {
            randomPositionGenerator = new RandomPositionGenerator(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getStartingAnimalsCount());
        }
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

    public void subscribe(SimulationChangeListener listener, SimulationEventType eventType) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new ArrayList<>());
        }

        listeners.get(eventType).add(listener);
    }

    private void notifyListeners(SimulationEventType type) {
        if (!listeners.containsKey(type)) {
            return;
        }

        for (SimulationChangeListener listener : listeners.get(type)) {
            listener.onSimulationChange(this);
        }
    }

    public synchronized void run() {
        try {
            while (SimulationCanRun()) {
                if (isPaused) {
                    continue;
                }

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

                notifyListeners(SimulationEventType.CHANGE);

                Thread.sleep(configuration.getSimulationSpeed());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notifyListeners(SimulationEventType.END);
    }

    public boolean SimulationCanRun() {
        return !(map.getAnimalsList().isEmpty() || dayNumber > configuration.getTotalSimulationDays());
    }

    protected void removeDeathAnimal() {
        List<AbstractAnimal> deadAnimals = AnimalUtils.getDeathAnimals(map.getAnimalsList());

        deadAnimals.forEach((animal) -> {
            map.remove(animal);
            animal.setDiedAt(dayNumber);
            statisticSimulation.addDiedAnimal(animal);
        });
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

    public void pause() {
        isPaused = true;
        notifyListeners(SimulationEventType.PAUSED);
    }

    public void resume() {
        isPaused = false;
        notifyListeners(SimulationEventType.RESUME);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public UUID getSimulationId() {
        return simulationId;
    }

    public AbstractWorldMap getMap() {
        return map;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public StatisticSimulation getStatisticSimulation() {
        return statisticSimulation;
    }
}
