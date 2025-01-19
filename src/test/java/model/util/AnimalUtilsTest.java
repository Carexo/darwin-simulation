package model.util;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.animal.AbstractAnimal;
import model.elements.animal.Animal;
import org.junit.jupiter.api.Test;
import util.AnimalUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalUtilsTest {


    @Test
    void animalReadyToBreed() {
        Configuration configuration = new Configuration();
        configuration.setAnimalReadyToBreedEnergyLevel(6);

        Animal animal1 = new Animal(new Vector2D(0, 0), 10, configuration);
        Animal animal2 = new Animal(new Vector2D(0, 0), 20, configuration);
        Animal animal3 = new Animal(new Vector2D(0, 0), 5, configuration);

        List<AbstractAnimal> animals = List.of(
                animal1,
                animal2,
                animal3
        );

        List<AbstractAnimal> sortedAnimals = AnimalUtils.getBreedingAnimals(animals);

        System.out.println(animals);
        System.out.println(sortedAnimals);

        assertEquals(2, sortedAnimals.size());

        assertArrayEquals(new Animal[]{animal2, animal1}, sortedAnimals.toArray());
    }

    @Test
    void animalDeath() {
        Configuration configuration = new Configuration();

        Animal animal1 = new Animal(new Vector2D(0, 0), 10, configuration);
        Animal animal2 = new Animal(new Vector2D(0, 0), 20, configuration);
        Animal animal3 = new Animal(new Vector2D(0, 0), 5, configuration);

        List<AbstractAnimal> animals = List.of(
                animal1,
                animal2,
                animal3
        );

        List<AbstractAnimal> deadAnimals = AnimalUtils.getDeathAnimals(animals);

        System.out.println(animals);
        System.out.println(deadAnimals);

        assertEquals(0, deadAnimals.size());

        animal1.die(2);

        deadAnimals = AnimalUtils.getDeathAnimals(animals);

        System.out.println(animals);
        System.out.println(deadAnimals);

        assertEquals(1, deadAnimals.size());
        assertEquals(animal1, deadAnimals.getFirst());
    }

    @Test
    void strongestAnimal() {
        Configuration configuration = new Configuration();
        configuration.setAnimalReadyToBreedEnergyLevel(6);

        Animal animal1 = new Animal(new Vector2D(0, 0), 10, configuration);
        Animal animal2 = new Animal(new Vector2D(0, 0), 20, configuration);
        Animal animal3 = new Animal(new Vector2D(0, 0), 5, configuration);

        List<AbstractAnimal> animals = List.of(
                animal1,
                animal2,
                animal3
        );

        AbstractAnimal strongestAnimal = AnimalUtils.getStrongestAnimal(animals);

        System.out.println(animals);
        System.out.println(strongestAnimal);

        assertEquals(animal2, strongestAnimal);
    }

}