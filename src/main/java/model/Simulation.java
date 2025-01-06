package model;

import model.elements.animal.AbstractAnimal;
import model.map.AbstractWorldMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation {
    private final Set<AbstractAnimal> animals;
    private final AbstractWorldMap map;

    public Simulation(AbstractWorldMap map, Configuration configuration) {
        this.map = map;
        this.animals = new HashSet<>();
    }

    public void run() {

    }


    private void animalsMove() {

    }
}
