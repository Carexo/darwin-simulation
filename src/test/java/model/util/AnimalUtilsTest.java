package model.util;

import model.Configuration;
import model.elements.Vector2D;
import model.elements.animal.Animal;
import org.junit.jupiter.api.Test;

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

        List<Animal> animals = List.of(
                animal1,
                animal2,
                animal3
        );

        List<Animal> sortedAnimals = AnimalUtils.getBreedingAnimals(animals);

        System.out.println(animals);
        System.out.println(sortedAnimals);

        assertEquals(2, sortedAnimals.size());

        assertArrayEquals(new Animal[]{animal2, animal1}, sortedAnimals.toArray());
    }
}